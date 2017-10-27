package com.bbd.exchange.mqtt;

public interface ExchangeMqttMessage {
	void handling();
	boolean decode(BaseExchangeMqttMessage msg);
}
