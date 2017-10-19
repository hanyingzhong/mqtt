package com.bbd.cloudmqtt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class AssociationMessage implements ExchangeMqttMessage{
	ArrayList<UpstreamBoxMessage> msgList;

	@Override
	public void handling() {
		System.out.println("start handlle AssociationMessage");
		showBoxInfos();
	}

	void decode(String topic, MqttMessage message) {
		msgList = new ArrayList<UpstreamBoxMessage>();

		byte[] upbytes = message.getPayload();
		int pos = 0;
		int msgLength = upbytes.length;
		
		while (pos < msgLength) {
			int length;
			UpstreamBoxMessage upmsg = new UpstreamBoxMessage();
			
			UpstreamBoxMessage.decodeTopic(upmsg, topic);
			upmsg.id = NumberUtil.byte2ToUnsignedShort(upbytes , pos) - 0xa000;
			pos += 2;
			length = NumberUtil.byte2ToUnsignedShort(upbytes, pos);
			pos += 2;
			if (length == 0) {
				upmsg.batteryExist = false;
				msgList.add(upmsg);
				continue;
			}
			try {
				UpstreamBoxMessage.decodeBoxInfo(upmsg, new String(upbytes, pos, length, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			pos += length;
			msgList.add(upmsg);
		}
	}
	
	void showBoxInfos() {
		for(UpstreamBoxMessage ele : msgList) {
			System.out.println(ele.toString());
		}
	}
}
