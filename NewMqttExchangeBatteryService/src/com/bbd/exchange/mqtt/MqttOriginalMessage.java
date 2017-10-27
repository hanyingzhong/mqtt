package com.bbd.exchange.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttOriginalMessage {
	String topic;
	MqttMessage message;
	public MqttOriginalMessage(String topic, MqttMessage message) {
		super();
		this.topic = topic;
		this.message = message;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public MqttMessage getMessage() {
		return message;
	}
	
	public void setMessage(MqttMessage message) {
		this.message = message;
	}
}
