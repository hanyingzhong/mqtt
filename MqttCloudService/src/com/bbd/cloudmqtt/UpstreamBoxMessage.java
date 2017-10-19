package com.bbd.cloudmqtt;

import java.io.UnsupportedEncodingException;

public class UpstreamBoxMessage implements ExchangeMqttMessage {
	String cabinetID;
	int id;
	boolean doorOpened;
	boolean batteryExist;
	String batteryID;
	int capacity;

	@Override
	public void handling() {
		System.out.println("start handlle AssociationMessage");
		showBoxInfos();
	}

	public boolean isDoorOpened() {
		return doorOpened;
	}

	public void setDoorOpened(boolean doorOpened) {
		this.doorOpened = doorOpened;
	}

	public String getCabinetID() {
		return cabinetID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	byte[] msg = new byte[128];
	int msgLength;

	/**
	 * foramt : tag+length+content BoxID : A001-A00B length
	 * Content:1,batteryID,capacity if no battery, length is Zero
	 */
	public byte[] encode() {
		String boxInfo;
		int pos = 0;
		
		id = 0xA000 + id;
		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, msg, 0, 2);
		pos += 2;
		
		/*
		 * encode door status.opened or closed...
		 * 
		 * */
		boxInfo = doorOpened ? "1" : "0";
		boxInfo += ",";
		/*
		 * encode battery status...exist..id..capacity
		 * */
		if (batteryExist) {
			boxInfo += "1," + batteryID + "," + capacity;
		} else {
			boxInfo += "0";
		}
		
		bytesArrayCopy(NumberUtil.unsignedShortToByte2(boxInfo.length()), 0, msg, pos, 2);
		pos += 2;
		bytesArrayCopy(boxInfo.getBytes(), 0, msg, pos, boxInfo.length());
		pos += boxInfo.length();
		
		msgLength = pos;
		System.out.println(NumberUtil.bytesToHexString(msg));

		byte[] retMsg = new byte[msgLength];
		bytesArrayCopy(msg, 0, retMsg, 0, msgLength);
		return retMsg;
	}

	public ExchangeMqttMessage decode(String topic, byte[] upbytes) {
		int pos = 0;
		int length;

		decodeTopic(this, topic);
		id = NumberUtil.byte2ToUnsignedShort(upbytes) - 0xa000;
		length = NumberUtil.byte2ToUnsignedShort(upbytes, 2);
		pos += 4;
		if (length > 0) {
			String boxInfo;
			try {
				boxInfo = new String(upbytes, pos, length, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}

			if (false == decodeBoxInfo(this, boxInfo)) {
				return null;
			}

			pos += length;
		} else if (length == 0) {
			batteryExist = false;
		} else {
			System.out.println("received a message with length < 0");
		}

		return this;
	}

	static boolean decodeBoxInfo(UpstreamBoxMessage upmsg, String msg) {
		String[] sub = msg.split(",");
		int paramNum = sub.length;

		if(sub[0] != null)
		{
			upmsg.doorOpened = Integer.parseInt(sub[0]) == 1 ? true : false;
		}

		if(sub[1] != null)
		{
			upmsg.batteryExist = Integer.parseInt(sub[1]) == 1 ? true : false;
		}

		if (paramNum < 4) {
			return false;
		}

		upmsg.batteryID = sub[2];
		upmsg.capacity = Integer.parseInt(sub[3]);
		return true;
	}

	static boolean decodeTopic(UpstreamBoxMessage upmsg, String topic) {
		String[] sub = topic.split("/");

		if (sub.length < 3) {
			return false;
		}

		upmsg.cabinetID = sub[1];
		return true;
	}

	static void bytesArrayCopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "UpstreamBoxMessage [cabinetID=" + cabinetID + ", id=" + id + ", doorOpened=" + doorOpened
				+ ", batteryExist=" + batteryExist + ", batteryID=" + batteryID + ", capacity=" + capacity + "]";
	}

	void showBoxInfos() {
			System.out.println(this.toString());
	}
}
