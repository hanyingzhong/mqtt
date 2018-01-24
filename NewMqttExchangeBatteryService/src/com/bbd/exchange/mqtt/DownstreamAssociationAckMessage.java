package com.bbd.exchange.mqtt;

import com.bbd.exchange.util.MqttDateTime;
import com.bbd.exchange.util.NumberUtil;

public class DownstreamAssociationAckMessage extends DownstreamCabinetMessage {

	public DownstreamAssociationAckMessage() {
		super();
	}

	public DownstreamAssociationAckMessage(String cabinetID) {
		super(cabinetID, InteractionCommand.UP_ASSOCIATEACK, InteractionCommand.DOWN_SUB_ACK, 0);
	}

	int encodeSyncTime(byte[] buffer, int pos) {
		int posCurr = pos;
		int id = 0x9001;
		// String command = InteractionCommand.getDownSubCommandValue(verb);
		String command = MqttDateTime.getCurrentTime();

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, buffer, posCurr, 2);
		posCurr += 2;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(command.length()), 0, buffer, posCurr, 2);
		posCurr += 2;
		bytesArrayCopy(command.getBytes(), 0, buffer, posCurr, command.length());
		posCurr += command.length();

		return posCurr;
	}

	int encodeLocalBatteryMonitorParam(byte[] buffer, int pos) {
		StringBuffer content = new StringBuffer(); 
		
		content.append(Integer.toString(50));
		content.append(",");
		content.append(Integer.toString(15));
		return encodeString(buffer, pos, 0x9002, content.toString());
	}
	
	int encodeW4BoxClosedTimer(byte[] buffer, int pos){
		StringBuffer content = new StringBuffer(); 
		
		content.append(Integer.toString(15));
		return encodeString(buffer, pos, 0x9004, content.toString());		
	}
	
	int encodeString(byte[] buffer, int pos, int paramId, String value) {
		int posCurr = pos;
		int id = paramId;

		String param = value;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, buffer, posCurr, 2);
		posCurr += 2;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(param.length()), 0, buffer, posCurr, 2);
		posCurr += 2;
		bytesArrayCopy(param.getBytes(), 0, buffer, posCurr, param.length());
		posCurr += param.length();

		return posCurr;
	}
	
	@Override
	public byte[] encode() {
		int pos = 0;
		byte[] msg = new byte[512];

		pos = encodeCabinetID(msg, pos);
		pos = encodeCommand(msg, pos);
		pos = encodeSyncTime(msg, pos);
		pos = encodeRfidConfig(msg, pos);
		pos = encodeLocalBatteryMonitorParam(msg, pos);
		pos = encodeW4BoxClosedTimer(msg, pos);
		
		byte[] retMsg = new byte[pos];
		bytesArrayCopy(msg, 0, retMsg, 0, pos);
		return retMsg;
	}
}
