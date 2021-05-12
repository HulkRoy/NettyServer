package com.mijia.netty.bean;

public class MessageBean {
	private String Action;
	private int MsgId;
	private String Imei;
	private String Timer;
	private DataBean Data;
	public MessageBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MessageBean(String action, int msgId, String imei, String timer, DataBean data) {
		super();
		Action = action;
		MsgId = msgId;
		Imei = imei;
		Timer = timer;
		Data = data;
	}
	public String getAction() {
		return Action;
	}
	public void setAction(String action) {
		Action = action;
	}
	public int getMsgId() {
		return MsgId;
	}
	public void setMsgId(int msgId) {
		MsgId = msgId;
	}
	public String getImei() {
		return Imei;
	}
	public void setImei(String imei) {
		Imei = imei;
	}
	public String getTimer() {
		return Timer;
	}
	public void setTimer(String timer) {
		Timer = timer;
	}
	public DataBean getData() {
		return Data;
	}
	public void setData(DataBean data) {
		Data = data;
	}
	@Override
	public String toString() {
		return "MessageBean [Action=" + Action + ", MsgId=" + MsgId + ", Imei=" + Imei + ", Timer=" + Timer + ", Data="
				+ Data + "]";
	}
}
