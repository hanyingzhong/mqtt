package com.bbd.exchange.mqtt;

public interface ExchangeMessageQueue {
	void add(MqttOriginalMessage message);
	MqttOriginalMessage pull();
}
