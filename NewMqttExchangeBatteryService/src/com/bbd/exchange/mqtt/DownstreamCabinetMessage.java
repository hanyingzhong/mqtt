package com.bbd.exchange.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.bbd.exchange.control.DeviceMgrContainer;
import com.bbd.exchange.mobile.MobileMqttClientSimnulation;
import com.bbd.exchange.simuclient.ExchangeMqttClient;
import com.bbd.exchange.util.NumberUtil;

public class DownstreamCabinetMessage implements ExchangeMqttMessage {
	static MobileMqttClientSimnulation mqttClient = MobileMqttClientSimnulation.getInstance();

	static {
		ExchangeMqttClient exchangeMqttClient = new ExchangeMqttClient("tcp://121.40.109.91", "parry","parry123", "SIMU-DDER");
		mqttClient.setMqttClient(exchangeMqttClient);
	}
	
	String deviceID;
	String cabinetID;
	String verb;
	String subCmd;
	int ID;
	
	public DownstreamCabinetMessage(String cabinetID, String verb, String subCmd, int iD) {
		this.cabinetID = cabinetID;
		this.verb = verb;
		this.subCmd = subCmd;
		/*
		 * if ID is zero, then operate all box....
		 * */
		ID = iD;
	}

	public DownstreamCabinetMessage() {
	}

	public boolean publish(DownstreamCabinetMessage message) {
		byte[] pubilshMsg = message.encode();
		
		if(false == mqttClient.getMqttClient().getClient().isConnected()) {
			mqttClient.connect();
		}
		
		try {
			mqttClient.getMqttClient().getClient().publish(message.encodeTopic(), pubilshMsg, 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean checkAndSetDeviceID() {
		deviceID = DeviceMgrContainer.getInstance().getDeviceByCabinet(cabinetID);

		if (deviceID == null) {
			return false;
		}

		return true;
	}

	@Override
	public void handling() {
		showMessage();
	}
	
	void showMessage() {
		System.out.println("d/" + deviceID + "/" + cabinetID + "/" + verb + "/" + subCmd + "/" + ID);
	}
	
	boolean decodeDownstreamBoxInfo(String content) {
		String[] sub = content.split(",");
		
		if(sub.length < 1) {
			return false;
		}
		
		subCmd = sub[0];
		return true;	
	}
	
	@Override
	public boolean decode(BaseExchangeMqttMessage msg) {
		deviceID = msg.getTopic().getDeviceID();
		cabinetID = msg.getCabinet();
		verb = msg.getCmd();
		
		for (CabinetBox ele : msg.msgList) {

			ID = ele.boxID;
			if(false == decodeDownstreamBoxInfo(ele.msg)) {
				return false;
			}
			
			/*now there is only one box carried*/
			return true;
		}
		return false;
	}

	int encodeCabinetID(byte[] buffer, int pos) {
		int posCurr = pos;
		int id = 0x0100;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, buffer, posCurr, 2);
		posCurr += 2;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(cabinetID.length()), 0, buffer, posCurr, 2);
		posCurr += 2;
		bytesArrayCopy(cabinetID.getBytes(), 0, buffer, posCurr, cabinetID.length());
		posCurr += cabinetID.length();

		return posCurr;
	}

	public int encodeCommand(byte[] buffer, int pos) {
		int posCurr = pos;
		int id = 0x0501;
		//String command = InteractionCommand.getCommandValue(verb);
		String command = verb;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, buffer, posCurr, 2);
		posCurr += 2;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(command.length()), 0, buffer, posCurr, 2);
		posCurr += 2;
		bytesArrayCopy(command.getBytes(), 0, buffer, posCurr, command.length());
		posCurr += command.length();

		return posCurr;
	}

	int encodeBoxInfo(byte[] buffer, int pos) {
		int posCurr = pos;
		int id = 0xA000 + getID();
		//String command = InteractionCommand.getDownSubCommandValue(verb);
		String command = subCmd;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, buffer, posCurr, 2);
		posCurr += 2;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(command.length()), 0, buffer, posCurr, 2);
		posCurr += 2;
		bytesArrayCopy(command.getBytes(), 0, buffer, posCurr, command.length());
		posCurr += command.length();

		return posCurr;
	}
	
	private int getID() {
		return ID;
	}

	static void bytesArrayCopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
	}

	public String encodeTopic() {
		return "d/" + deviceID;
	}
	
	public byte[] encode() {
		int pos = 0;
		byte[] msg = new byte[512];

		pos = encodeCabinetID(msg, pos);
		pos = encodeCommand(msg, pos);
		pos = encodeBoxInfo(msg, pos);

		byte[] retMsg = new byte[pos];
		bytesArrayCopy(msg, 0, retMsg, 0, pos);
		return retMsg;
	}
	
	public String getCabinetID() {
		return cabinetID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

}
