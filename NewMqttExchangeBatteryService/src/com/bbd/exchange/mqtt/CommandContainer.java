package com.bbd.exchange.mqtt;

import com.bbd.exchange.util.NumberUtil;

public class CommandContainer {
	String cabinetID;
	int id;
	CloudCommand command;
	
	/*
	 * if ID is zero, then query all the boxes.
	 * 
	 * */
	public CommandContainer(String cabinetID, int id, CloudCommand command) {
		this.cabinetID = cabinetID;
		this.id = id;
		this.command = command;
	}
	
	String encodeTopic() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("d/");
		stringBuilder.append(cabinetID);
		stringBuilder.append("/");
		stringBuilder.append("modi");
		if(command == CloudCommand.CMD_OPEN) {
			stringBuilder.append("/open");
		}
		return stringBuilder.toString();		
	}
	
	public String encodeBoxInfo() {
		StringBuilder stringBuilder = new StringBuilder();
		
		if(command == CloudCommand.CMD_OPEN) {
			stringBuilder.append("1");
		}
		if(command == CloudCommand.CMD_QUERY) {
			stringBuilder.append("2");
		}
		
		return stringBuilder.toString();
	}

	public byte[] encode() {
		byte[] msg = new byte[128]; 
		String boxInfo;
		int pos = 0;
		
		id = BaseExchangeMqttMessage.boxIdBaseValue + id;
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

	static void bytesArrayCopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
	}

}
