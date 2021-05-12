package com.mijia.netty.server;

import java.util.Date;

import com.mijia.netty.bean.MessageBean;
import com.mijia.netty.dao.Dao;
import com.mijia.netty.util.DataParser;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> { // (1)
	
	/**
	 * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
	 * A closed Channel is automatically removed from the collection,
	 */
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();
        
        // Broadcast a message to multiple Channels
//        channels.writeAndFlush("[FROM SERVER] - " + incoming.remoteAddress() + " In\n");
        
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
        
        // Broadcast a message to multiple Channels
//        channels.writeAndFlush("[FROM SERVER] - " + incoming.remoteAddress() + " Left\n");
        
        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
    }
    @Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception { // (4)
    	Channel incoming = ctx.channel();
		System.out.println("######### channelRead(" + new Date() + ") #########: " + msg);
		if (msg.startsWith("S=0") && msg.endsWith("E=0")) {
			MessageBean msgBean = DataParser.stringToObject(msg);		
//			System.out.println("convertedMsg: " + msgBean);
			NettyChannelMap.addMessageMap(msgBean.getImei(), incoming.remoteAddress().toString());
			NettyChannelMap.showAllMap();
			System.out.println("dataUploaded: " + Dao.uploadData(msgBean.getMsgId(), msgBean.getAction(), msg));
			System.out.println("heartbeatUploaded: " + Dao.heartbeatInsert(msgBean.getImei(), "online"));
			if (msgBean.getAction().equals("CheckIn")) {
				// get msgid from DB
				int localMsgId = Dao.getLatestMsgId(msgBean.getImei());
//				int latestMsgId = localMsgId == 0 ? msgBean.getMsgId() : localMsgId;
				if (localMsgId >= 0 ) {
					// return @CheckIn
					incoming.writeAndFlush("S=0&Action=@CheckIn&MsgId=" + (localMsgId + 1) + "&Timer=0&Imei=" + msgBean.getImei() + "&E=0");
					// update latest msgid and heartbeat to DB
					Dao.updateMsgId(localMsgId + 1, msgBean.getImei());
					// insert channel info to netty_channel (initialize 16 channels)
					Dao.initializeChannels(msgBean.getImei());   //wrong
				}
			} else if (msgBean.getAction().equals("GoodsNotice")) {
				//send to imei related channel "S=0&Action=GoodsNotice&MsgId=0&Timer=0&Imei=sendTo862991528720001&ChannelIndex=0&SaleId=0&E=0"
				String destIP = NettyChannelMap.getMessageMap(msgBean.getImei().replace("sendTo", ""));
				if (destIP != null) {
					Channel destChannel = NettyChannelMap.getMap(destIP);
					if (destChannel != null) {
						destChannel.writeAndFlush(msg.replace("sendTo", ""));
					}
				}
			} else if (msgBean.getAction().equals("Deliver")) {
				// return result to control channel
				String ctrChannelImei = "sendTo" + msgBean.getImei();
				String destIP = NettyChannelMap.getMessageMap(ctrChannelImei);
				if (destIP != null) {
					Channel destChannel = NettyChannelMap.getMap(destIP);
					if (destChannel != null) {
						destChannel.writeAndFlush(msg);
					}
				}
				if (msgBean.getData().getResult() == 1) {
					// update goods status of the channel if control success
					Dao.updateGoodsByChannelIndex(msgBean.getImei(), msgBean.getData().getChannelIndex(), 0, msgBean.getData().getSaleId(), msgBean.getData().getAlarmCode());
				} else {
					// skip goods status of the channel if control failed
					Dao.updateSkipGoodsByChannelIndex(msgBean.getImei(), msgBean.getData().getChannelIndex(), msgBean.getData().getSaleId(), msgBean.getData().getAlarmCode());
				}
				// update latest msgid to DB
				Dao.updateMsgId(msgBean.getMsgId(), msgBean.getImei());
			} else if (msgBean.getAction().equals("ManSupDefault")) {
				// update goods status of all the channels
				Dao.updateAllGoods(msgBean.getImei(), 1);
				// update latest msgid to DB
				Dao.updateMsgId(msgBean.getMsgId(), msgBean.getImei());
			} else if (msgBean.getAction().equals("Temperature")) {
				// update temperature of the imei together with the msgid
				Dao.updateTemperature(msgBean.getImei(), msgBean.getMsgId(), msgBean.getData().getT1());
			} else if (msgBean.getAction().equals("CSQ")) {
				// update csqlevel of the imei together with the msgid
				Dao.updateCSQ(msgBean.getImei(), msgBean.getMsgId(), msgBean.getData().getCsqLevel());
			} else if (msgBean.getAction().equals("Location")) {
				// update location of the imei together with the msgid
				Dao.updateLocation(msgBean.getImei(), msgBean.getMsgId(), msgBean.getData().getLocation());
			}
		}
//		for (Channel channel : channels) {
//            if (channel != incoming){
//                channel.writeAndFlush("[FROM" + incoming.remoteAddress() + "] - " + msg + "\n");
//                System.out.println("channelWriteAndFlush: " + "[FROM" + incoming.remoteAddress() + "] - " + msg + "\n");
//            } else {
////            	Thread.sleep(3000);
//            	channel.writeAndFlush("[FROM YOURSELF] - " + msg + "\n");
//            	System.out.println("channelWriteAndFlush: " + "[FROM YOURSELF] - " + msg + "\n");
//            }
//        }
	}
  
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
		System.out.println("client: "+incoming.remoteAddress()+" Online");
		NettyChannelMap.addMap(incoming.remoteAddress().toString(), incoming);
		NettyChannelMap.showAllMap();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
		System.out.println("client: "+incoming.remoteAddress()+" Offline");
		String imei = NettyChannelMap.getImeiByChannel(incoming);
		if (!imei.equals("")) {
			Dao.updateOfflineImei(NettyChannelMap.getImeiByChannel(incoming));			
		}
		NettyChannelMap.removeMap(incoming);
		NettyChannelMap.removeMessageMap(incoming.remoteAddress().toString());
		NettyChannelMap.showAllMap();
	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
    	Channel incoming = ctx.channel();
    	System.out.println("client: "+incoming.remoteAddress()+" Exception");
    	String imei = NettyChannelMap.getImeiByChannel(incoming);
		if (!imei.equals("")) {
			Dao.updateOfflineImei(NettyChannelMap.getImeiByChannel(incoming));			
		}
		NettyChannelMap.removeMap(incoming);
		NettyChannelMap.removeMessageMap(incoming.remoteAddress().toString());
		NettyChannelMap.showAllMap();
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
    
    public static String getIPString(ChannelHandlerContext ctx){  
        String ipString = "";  
        String socketString = ctx.channel().remoteAddress().toString();  
        int colonAt = socketString.indexOf(":");  
        ipString = socketString.substring(1, colonAt);  
        return ipString;  
    }
}