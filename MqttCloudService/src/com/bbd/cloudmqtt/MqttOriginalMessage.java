package com.bbd.cloudmqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttOriginalMessage {
	String topic;
	MqttMessage message;
	public MqttOriginalMessage(String topic, MqttMessage message) {
		super();
		this.topic = topic;
		this.message = message;
	}
}
