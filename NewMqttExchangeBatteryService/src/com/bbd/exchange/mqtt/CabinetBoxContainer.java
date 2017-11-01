package com.bbd.exchange.mqtt;

import com.bbd.exchange.util.NumberUtil;

public class CabinetBoxContainer {
	String deviceID;
	String cabinetID;
	String command;

	int id;
	boolean doorOpened;
	boolean batteryExist;
	String batteryID;
	String capacity;

	public CabinetBoxContainer(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	/**
	 * foramt : tag+length+content BoxID : A001-A00B length
	 * Content:1,batteryID,capacity if no battery, length is Zero
	 */
	public byte[] encode() {
		byte[] msg = new byte[128];
		String boxInfo;
		int pos = 0;

		id = 0xA000 + id;
		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, msg, 0, 2);
		pos += 2;

		boxInfo = encodeBoxInfo();

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(boxInfo.length()), 0, msg, pos, 2);
		pos += 2;
		bytesArrayCopy(boxInfo.getBytes(), 0, msg, pos, boxInfo.length());
		pos += boxInfo.length();

		System.out.println(NumberUtil.bytesToHexString(msg));

		byte[] retMsg = new byte[pos];
		bytesArrayCopy(msg, 0, retMsg, 0, pos);
		return retMsg;
	}

	boolean decodeUpstream(String content) {
		String[] sub = content.split(",");
		int paramNum = sub.length;

		if (sub.length < 2) {
			return false;
		}

		if (sub[0] != null) {
			doorOpened = Integer.parseInt(sub[0]) == 1 ? true : false;
		}

		if (sub[1] != null) {
			batteryExist = Integer.parseInt(sub[1]) == 1 ? true : false;
		}

		if (paramNum == 2) {
			return true;
		}

		batteryID = sub[2];
		capacity = sub[3];
		return true;
	}

	public String encodeBoxInfo() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(doorOpened ? "1" : "0");
		stringBuilder.append(",");
		/*
		 * encode battery status...exist..id..capacity
		 */
		if (batteryExist) {
			stringBuilder.append("1");
			stringBuilder.append(",");
			stringBuilder.append(batteryID);
			stringBuilder.append(",");
			stringBuilder.append(capacity);
		} else {
			stringBuilder.append("0");
		}
		return stringBuilder.toString();
	}

	static void bytesArrayCopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
	}

	public String getCabinetID() {
		return cabinetID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDoorOpened() {
		return doorOpened;
	}

	public void setDoorOpened(boolean doorOpened) {
		this.doorOpened = doorOpened;
	}

	public boolean isBatteryExist() {
		return batteryExist;
	}

	public void setBatteryExist(boolean batteryExist) {
		this.batteryExist = batteryExist;
	}

	public String getBatteryID() {
		return batteryID;
	}

	public void setBatteryID(String batteryID) {
		this.batteryID = batteryID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

}
