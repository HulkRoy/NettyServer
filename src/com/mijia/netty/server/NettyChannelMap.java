package com.mijia.netty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class NettyChannelMap {
    private static Map<String,Channel> map=new ConcurrentHashMap<String, Channel>();
    private static Map<String,String> messageMap=new ConcurrentHashMap<String, String>();

    public static void addMap(String clientIP,Channel socketChannel){
        map.put(clientIP, socketChannel);
    }
    public static Channel getMap(String clientIP){
       return map.get(clientIP);
    }
    public static void removeMap(Channel socketChannel){
        for (Map.Entry<String, Channel> entry: map.entrySet()){
            if (entry.getValue()==socketChannel){
                map.remove(entry.getKey());
            }
        }
    }
    
    public static void addMessageMap(String imei,String clientIP){
    	messageMap.put(imei, clientIP);
    }
    public static String getMessageMap(String imei){
       return messageMap.get(imei);
    }
    public static void removeMessageMap(String clientIP){
        for (Map.Entry<String, String> entry: messageMap.entrySet()){
            if (entry.getValue().equals(clientIP)){
            	messageMap.remove(entry.getKey());
            }
        }
    }
    
    public static String getImeiByChannel(Channel socketChannel) {
    	String clientIP = "";
    	String imei = "";
    	for (Map.Entry<String, Channel> entry: map.entrySet()){
            if (entry.getValue()==socketChannel){
                clientIP = entry.getKey();
            }
        }
    	for (Map.Entry<String, String> entry: messageMap.entrySet()){
            if (entry.getValue().equals(clientIP)){
            	imei = entry.getKey();
            }
        }
    	return imei;
    }
    
    public static void showAllMap(){
    	System.out.println(map.toString());
    	System.out.println(messageMap.toString());
    }
}