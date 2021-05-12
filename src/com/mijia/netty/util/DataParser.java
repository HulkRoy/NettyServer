package com.mijia.netty.util;

import com.mijia.netty.bean.DataBean;
import com.mijia.netty.bean.MessageBean;

public class DataParser {
	
	// S=0&Action=GoodsNotice&MsgId=124&Timer=170829183336&Imei=862991528739995&ChannelIndex=0&SaleId=123&E=0
	public static MessageBean stringToObject(String msg) {
		MessageBean msgBean = new MessageBean();
		DataBean dataBean = new DataBean();
		String[] cmdArray = msg.split("&");
		String cmdName;
		String cmdValue;
		for(String cmd : cmdArray) {
			cmdName = cmd.split("=")[0];
			cmdValue = cmd.split("=")[1];
			if (cmdName.equals("Action")) {
				msgBean.setAction(cmdValue);
			} else if (cmdName.equals("MsgId")) {
				msgBean.setMsgId(Integer.parseInt(cmdValue));
			} else if (cmdName.equals("Imei")) {
				msgBean.setImei(cmdValue);
			} else if (cmdName.equals("Timer")) {
				msgBean.setTimer(cmdValue);
			} else {
				if (cmdName.equals("ChannelIndex")) {
					dataBean.setChannelIndex(Integer.parseInt(cmdValue));					
				} else if (cmdName.equals("SaleId")) {
					dataBean.setSaleId(cmdValue);					
				} else if (cmdName.equals("Result")) {
					dataBean.setResult(Integer.parseInt(cmdValue));					
				} else if (cmdName.equals("AlarmCode")) {
					dataBean.setAlarmCode(Integer.parseInt(cmdValue));					
				} else if (cmdName.equals("ChannelStatus")) {
					dataBean.setChannelStatus(cmdValue);					
				} else if (cmdName.equals("T1")) {
					dataBean.setT1(Integer.parseInt(cmdValue));					
				} else if (cmdName.equals("CsqLevel")) {
					dataBean.setCsqLevel(Integer.parseInt(cmdValue));					
				} else if (cmdName.equals("Location")) {
					dataBean.setLocation(cmdValue);					
				}
			}
		}
		msgBean.setData(dataBean);
		return msgBean;
	}
}
