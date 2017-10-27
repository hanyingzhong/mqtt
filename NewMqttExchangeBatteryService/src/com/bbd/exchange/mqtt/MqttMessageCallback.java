package com.bbd.exchange.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.bbd.exchange.mobile.MobileMqttMessageQueue;
import com.bbd.exchange.util.NumberUtil;

public class MqttMessageCallback implements MqttCallback {
	enum CloudCommand {
		FROM_NONE, FROM_DEVICE, FROM_MOBILE;
	}

	CloudCommand msgFrom;

	CloudCommand getMsgSource(String topic) {
		char[] charTopic = topic.toCharArray();

		if (charTopic[0] == 'd' || charTopic[0] == 'u') {
			return CloudCommand.FROM_DEVICE;
		}

		if (charTopic[0] == 'a' || charTopic[0] == 'b') {
			return CloudCommand.FROM_MOBILE;
		}

		return CloudCommand.FROM_NONE;
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("connection lost : " + cause.toString());
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (CloudCommand.FROM_DEVICE == getMsgSource(topic)) {
			System.out.println(topic + "-->" + NumberUtil.bytesToHexString(message.getPayload()));
			MqttMessageQueue.getInstance().add(new MqttOriginalMessage(topic, message));
			return;
		}
		if (CloudCommand.FROM_MOBILE == getMsgSource(topic)) {
			System.out.println(topic + "-->" + message);
			MobileMqttMessageQueue.getInstance().add(new MqttOriginalMessage(topic, message));
			return;
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {

	}

}
