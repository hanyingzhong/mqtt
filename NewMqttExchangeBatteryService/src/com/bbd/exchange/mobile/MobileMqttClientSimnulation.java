package com.bbd.exchange.mobile;

import com.bbd.exchange.simuclient.ExchangeMqttClient;

public class MobileMqttClientSimnulation {
	ExchangeMqttClient mqttClient = null;

	private static final MobileMqttClientSimnulation INSTANCE = new MobileMqttClientSimnulation();

	public static final MobileMqttClientSimnulation getInstance() {
		return MobileMqttClientSimnulation.INSTANCE;
	}
	
	//mqttClient = new ExchangeMqttClient("tcp://121.40.109.91", "parry","parry123", "DEV-1545662ER");
	public MobileMqttClientSimnulation() {
	}

	public ExchangeMqttClient getMqttClient() {
		return mqttClient;
	}

	public void setMqttClient(ExchangeMqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}

	public boolean connect(){	
		return mqttClient.connect();	
	}	
}
