package com.bbd.exchange.mqtt;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.util.NumberUtil;

public class BaseExchangeMqttMessage {
    private static final Logger logger = LoggerFactory.getLogger(BaseExchangeMqttMessage.class);

	final static int cainetTagValue = 0x0100;
	final static int boxIdBaseValue = 0xa000;
	final static int cmdTagValue = 0x0501;
	final static int timeTagValue = 0x9000;
	final static int voltageTagValue = 0x8000;
	final static int versionTagValue = 0x8001;

	public Topic getTopic() {
		return topic;
	}

	public class Topic {
		String direction;
		String deviceID;

		public Topic(String direction, String deviceID) {
			this.direction = direction;
			this.deviceID = deviceID;
		}

		public String getDeviceID() {
			return deviceID;
		}

		public String getDirection() {
			return direction;
		}
	};

	Topic topic;
	List<CabinetBox> msgList;
	String cabinet = new String("");
	String cmd = new String("");
	String time = null;
	String cabinetVoltage = null;
	String cabinetVersion = null;

	public List<CabinetBox> getMsg() {
		return msgList;
	}

	Topic decodeTopic(String msg) {
		String[] sub = msg.split("/");

		if (sub.length != 2) {
			return null;
		}

		if ((sub[0].equals("d") != true) && (sub[0].equals("u") != true)) {
			return null;
		}

		/*
		 * check the cabinet encode validity....
		 * 
		 */

		/*
		 * 
		 * check the verb validity
		 * 
		 */

		return new Topic(sub[0], sub[1]);
	}

	public boolean decode(String topicString, byte[] buffer) {
		int pos = 0;
		int msgLength = buffer.length;
		msgList = new LinkedList<CabinetBox>();

		topic = decodeTopic(topicString);

		pos = decodeCabinetID(buffer, pos);
		if (-1 == pos) {
			return false;
		}

		pos = decodeCommand(buffer, pos);
		if (-1 == pos) {
			return false;
		}

		while (pos < msgLength) {
			int boxID;
			String param;
			int length;

			/*******************************************************************
			 * 
			********************************************************************/
			int paramID = NumberUtil.byte2ToUnsignedShort(buffer, pos);
			if (paramID == timeTagValue) {
				pos = decodeTime(buffer, pos);
				logger.info("@time:"+time);
				continue;
			}
			
			if (paramID == voltageTagValue) {
				pos = decodeCabinetVoltage(buffer, pos);
				continue;
			}
			
			if (paramID == versionTagValue) {
				pos = decodeCabinetVersion(buffer, pos);
				continue;
			}
			
			/*******************************************************************/
			if ((paramID < boxIdBaseValue) || paramID > (boxIdBaseValue + 12)) {
				return false;
			}

			boxID = NumberUtil.byte2ToUnsignedShort(buffer, pos) - boxIdBaseValue;
			pos += 2;
			length = NumberUtil.byte2ToUnsignedShort(buffer, pos);
			pos += 2;

			if ((length <= 0) || (length > (buffer.length - pos))) {
				return false;
			}

			try {
				param = new String(buffer, pos, length, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}

			pos += length;
			msgList.add(new CabinetBox(boxID, param));
		}

		return true;
	}

	public int decodeTime(byte[] buffer, int pos) {
		int tag;
		String param;
		int length;

		tag = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		if (tag != timeTagValue) {
			return -1;
		}
		pos += 2;
		length = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		pos += 2;

		try {
			param = new String(buffer, pos, length, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		}

		pos += length;
		time = param;
		return pos;
	}

	public int decodeCabinetVoltage(byte[] buffer, int pos) {
		int tag;
		String param;
		int length;

		tag = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		if (tag != voltageTagValue) {
			return -1;
		}
		pos += 2;
		length = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		pos += 2;

		try {
			param = new String(buffer, pos, length, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		}

		pos += length;
		cabinetVoltage = param;
		return pos;
	}
	
	public int decodeCabinetVersion(byte[] buffer, int pos) {
		int tag;
		String param;
		int length;

		tag = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		if (tag != versionTagValue) {
			return -1;
		}
		pos += 2;
		length = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		pos += 2;

		try {
			param = new String(buffer, pos, length, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		}

		pos += length;
		cabinetVersion = param;
		logger.info("version £º {}", cabinetVersion);
		return pos;
	}
	
	public int decodeCabinetID(byte[] buffer, int pos) {
		int tag;
		String param;
		int length;

		tag = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		if (tag != cainetTagValue) {
			return -1;
		}
		pos += 2;
		length = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		pos += 2;

		try {
			param = new String(buffer, pos, length, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		}

		pos += length;
		cabinet = param;
		return pos;
	}

	public int decodeCommand(byte[] buffer, int pos) {
		int tag;
		String param;
		int length;

		tag = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		if (tag != cmdTagValue) {
			return -1;
		}
		pos += 2;
		length = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		pos += 2;

		try {
			param = new String(buffer, pos, length, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		}

		pos += length;
		cmd = param;
		return pos;
	}

	void setTopic(String direction, String deviceID) {
		topic = new Topic(direction, deviceID);
	}

	void addBox(int boxID, String param) {
		msgList.add(new CabinetBox(boxID, param));
	}

	public String getCabinet() {
		return cabinet;
	}

	public String getCmd() {
		return cmd;
	}

	public String getCabinetVoltage() {
		return cabinetVoltage;
	}

	public void setCabinetVoltage(String cabinetVoltage) {
		this.cabinetVoltage = cabinetVoltage;
	}

	byte[] encode() {

		return null;
	}
}
