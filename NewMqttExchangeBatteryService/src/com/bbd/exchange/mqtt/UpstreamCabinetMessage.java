package com.bbd.exchange.mqtt;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.control.AssociatedCabinetMessageHandling;
import com.bbd.exchange.control.NotifyCabinetMessageHandling;
import com.bbd.exchange.util.NumberUtil;

public class UpstreamCabinetMessage implements ExchangeMqttMessage {
    private static final Logger logger = LoggerFactory.getLogger(UpstreamCabinetMessage.class);

	String deviceID;
	String cabinetID;
	String verb;

	List<CabinetBoxContainer> boxList = new LinkedList<CabinetBoxContainer>();

	public List<CabinetBoxContainer> getBoxList() {
		return boxList;
	}

	public UpstreamCabinetMessage(String verb) {
		this.verb = verb;
	}

	@Override
	public void handling() {
		showMessage();
		if (verb.equals(InteractionCommand.UP_ASSOCIATE)) {
			AssociatedCabinetMessageHandling.getInstance().handling(this);
			return;
		}

		if (verb.equals(InteractionCommand.UP_NOTIFY)) {
			NotifyCabinetMessageHandling.getInstance().handling(this);
			return;
		}
	}

	public void addBox(CabinetBoxContainer box) {
		boxList.add(box);
	}

	@Override
	public boolean decode(BaseExchangeMqttMessage msg) {
		deviceID = msg.getTopic().getDeviceID();
		cabinetID = msg.getCabinet();
		verb = msg.getCmd();

		for (CabinetBox ele : msg.msgList) {
			CabinetBoxContainer box = new CabinetBoxContainer(msg.cabinet);

			box.id = ele.boxID;
			if (false == box.decodeUpstream(ele.msg)) {
				System.out.println("decode msg error : " + ele.msg);
				return false;
			}

			boxList.add(box);
		}

		return true;
	}

	public String encodeTopic() {
		return "u/" + deviceID;
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

	int encodeCommand(byte[] buffer, int pos) {
		int posCurr = pos;
		int id = 0x0501;
		String command = InteractionCommand.getCommandValue(verb);

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, buffer, posCurr, 2);
		posCurr += 2;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(command.length()), 0, buffer, posCurr, 2);
		posCurr += 2;
		bytesArrayCopy(command.getBytes(), 0, buffer, posCurr, command.length());
		posCurr += command.length();

		return posCurr;
	}

	public byte[] encode() {
		int pos = 0;
		byte[] msg = new byte[512];

		pos = encodeCabinetID(msg, pos);
		pos = encodeCommand(msg, pos);

		for (CabinetBoxContainer ele : boxList) {
			byte[] content = ele.encode();
			bytesArrayCopy(content, 0, msg, pos, content.length);
			pos += content.length;
		}

		byte[] retMsg = new byte[pos];
		bytesArrayCopy(msg, 0, retMsg, 0, pos);
		return retMsg;
	}

	static void bytesArrayCopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
		System.arraycopy(src, srcPos, dest, destPos, length);
	}

	void showMessage() {
		logger.info("device " + deviceID + ":" + InteractionCommand.getCommandString(verb));
		for (CabinetBoxContainer ele : boxList) {
			logger.info("box:" + ele.id + "===>" + verb + "," + ele.cabinetID + "," + ele.encodeBoxInfo());
		}
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
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
}
