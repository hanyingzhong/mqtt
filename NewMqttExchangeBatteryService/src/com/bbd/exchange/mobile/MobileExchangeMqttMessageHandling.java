package com.bbd.exchange.mobile;

import com.bbd.exchange.mqtt.ExchangeMqttMessage;

public class MobileExchangeMqttMessageHandling implements Runnable {
	ExchangeMqttMessage message;
	
	public MobileExchangeMqttMessageHandling(ExchangeMqttMessage message) {
		this.message = message;
	}

	@Override
	public void run() {
		message.handling();
	}

}
