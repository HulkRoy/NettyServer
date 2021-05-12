package com.mijia.netty.bean;

import java.util.Arrays;

public class DataBean {
	private int ChannelIndex;
	private String SaleId;
	private int Result;
	private int AlarmCode;
	private String ChannelStatus;
	private int T1;
	private int CsqLevel;
	private String Location;
	
	public DataBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public DataBean(int channelIndex, String saleId, int result, int alarmCode, String channelStatus, int t1,
			int csqLevel, String location) {
		super();
		ChannelIndex = channelIndex;
		SaleId = saleId;
		Result = result;
		AlarmCode = alarmCode;
		ChannelStatus = channelStatus;
		T1 = t1;
		CsqLevel = csqLevel;
		Location = location;
	}

	public int getChannelIndex() {
		return ChannelIndex;
	}

	public void setChannelIndex(int channelIndex) {
		ChannelIndex = channelIndex;
	}

	public String getSaleId() {
		return SaleId;
	}

	public void setSaleId(String saleId) {
		SaleId = saleId;
	}

	public int getResult() {
		return Result;
	}

	public void setResult(int result) {
		Result = result;
	}

	public int getAlarmCode() {
		return AlarmCode;
	}

	public void setAlarmCode(int alarmCode) {
		AlarmCode = alarmCode;
	}

	public String getChannelStatus() {
		return ChannelStatus;
	}

	public void setChannelStatus(String channelStatus) {
		ChannelStatus = channelStatus;
	}

	public int getT1() {
		return T1;
	}

	public void setT1(int t1) {
		T1 = t1;
	}

	public int getCsqLevel() {
		return CsqLevel;
	}

	public void setCsqLevel(int csqLevel) {
		CsqLevel = csqLevel;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	@Override
	public String toString() {
		return "DataBean [ChannelIndex=" + ChannelIndex + ", SaleId=" + SaleId + ", Result=" + Result + ", AlarmCode="
				+ AlarmCode + ", ChannelStatus=" + ChannelStatus + ", T1=" + T1 + ", CsqLevel="
				+ CsqLevel + ", Location=" + Location + "]";
	}
}
