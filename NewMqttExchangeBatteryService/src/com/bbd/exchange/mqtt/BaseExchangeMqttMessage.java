package com.bbd.exchange.mqtt;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import com.bbd.exchange.util.NumberUtil;

public class BaseExchangeMqttMessage {
	final static int cainetTagValue  = 0x0100;
	final static int boxIdBaseValue = 0xa000;
	final static int cmdTagValue    = 0x0501;

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

	public List<CabinetBox> getMsg() {
		return msgList;
	}

	Topic decodeTopic(String msg) {
		String[] sub = msg.split("/");

		if (sub.length != 2) {
			return null;
		}

		if((sub[0].equals("d") != true) && (sub[0].equals("u") != true)) {
			return null;
		}

		/*
		 * check the cabinet encode validity....
		 * 
		 * */
		
		/*
		 * 
		 * check the verb validity
		 * 
		 * */
		
		return new Topic(sub[0], sub[1]);
	}

	public boolean decode(String topicString, byte[] buffer) {
		int pos = 0;
		int msgLength = buffer.length;
		msgList = new LinkedList<CabinetBox>();
		
		topic = decodeTopic(topicString);
		
		pos = decodeCabinetID(buffer, pos);
		pos = decodeCommand(buffer, pos);
		
		while (pos < msgLength) {
			int boxID;
			String param;
			int length;

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

	public int decodeCabinetID(byte[] buffer, int pos) {
		int tag;
		String param;
		int length;
		
		tag = NumberUtil.byte2ToUnsignedShort(buffer, pos);
		if(tag != cainetTagValue) {
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
		if(tag != cmdTagValue) {
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

	byte[] encode() {

		return null;
	}
}
