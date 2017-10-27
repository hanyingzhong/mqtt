package com.bbd.exchange.mqtt;

public class ExchangeMqttMessageHandling implements Runnable {
	ExchangeMqttMessage message;
	
	public ExchangeMqttMessageHandling(ExchangeMqttMessage message) {
		this.message = message;
	}

	@Override
	public void run() {
		message.handling();
	}

}
