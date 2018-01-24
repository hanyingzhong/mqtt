package com.bbd.exchange.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.control.DeviceMgrContainer;
import com.bbd.exchange.util.MqttCfgUtil;
import com.bbd.exchange.util.NumberUtil;
import com.bbd.rfid.RfidConfigParam;

public class DownstreamCabinetMessage implements ExchangeMqttMessage {
    private static final Logger logger = LoggerFactory.getLogger(DownstreamCabinetMessage.class);

/*    static MobileMqttClientSimnulation mqttClient = MobileMqttClientSimnulation.getInstance();

	static {
		//ExchangeMqttClient exchangeMqttClient = new ExchangeMqttClient("tcp://121.40.109.91", "parry","parry123", "SIMU-DDER");
		ExchangeMqttClient exchangeMqttClient = new ExchangeMqttClient(MqttCfgUtil.getServerUri(), "parry","parry123", "SIMU-DDER");
		mqttClient.setMqttClient(exchangeMqttClient);
	}*/
	
	static CommonExchangeMqttClient newMqttClient;

	static {
		newMqttClient = new CommonExchangeMqttClient(MqttCfgUtil.getServerUri(), "parry", "parry123",
				"DS-CLIENT002R", null);

		try {
			newMqttClient.initClient(new CommonClientMqttMsgCallback(newMqttClient));
			newMqttClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public boolean publish(String topic, String  message) {
/*		if(false == mqttClient.getMqttClient().getClient().isConnected()) {
			mqttClient.connect();
		}
		
		try {
			mqttClient.getMqttClient().getClient().publish(topic, message.getBytes(), 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}*/
		newMqttClient.sendPublish(topic, message);
		logger.info("publish: {}, {}", topic, message);
		return true;	
	}
	
	public boolean publish(DownstreamCabinetMessage message) {
		byte[] pubilshMsg = message.encode();
		
/*		if(false == mqttClient.getMqttClient().getClient().isConnected()) {
			mqttClient.connect();
		}
		
		try {
			mqttClient.getMqttClient().getClient().publish(message.encodeTopic(), pubilshMsg, 0, false);
			logger.info("publish: {}, {}", message.encodeTopic(), NumberUtil.bytesToHexString(pubilshMsg));
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}*/
		newMqttClient.sendPublish(message.encodeTopic(), pubilshMsg, 0);
		logger.info("==publish: {}, {}", message.encodeTopic(), NumberUtil.bytesToHexString(pubilshMsg));
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

	public int encodeTime(byte[] buffer, int pos) {
		int posCurr = pos;
		int id = 0x9001;
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

	int encodeRfidConfig(byte[] buffer, int pos) {
		int posCurr = pos;
		int id = 0x9003;

		byte[] command = RfidConfigParam.getAllOneRfidConfigParam(); //RfidConfigParam.getRfidConfigParam();

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, buffer, posCurr, 2);
		posCurr += 2;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(command.length), 0, buffer, posCurr, 2);
		posCurr += 2;
		bytesArrayCopy(command, 0, buffer, posCurr, command.length);
		posCurr += command.length;

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
