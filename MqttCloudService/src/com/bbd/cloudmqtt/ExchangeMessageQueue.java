package com.bbd.cloudmqtt;

public interface ExchangeMessageQueue {
	void add(MqttOriginalMessage message);
	MqttOriginalMessage pull();
}
