package com.bbd.exchange.service;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.bbd.exchange.mobile.MobileMqttClientSimnulation;
import com.bbd.exchange.simuclient.ExchangeMqttClient;
import com.bbd.exchange.util.MqttCfgUtil;

public class ServiceMqttClient {
	private static final ServiceMqttClient INSTANCE = new ServiceMqttClient();

	public static final ServiceMqttClient getInstance() {
		return ServiceMqttClient.INSTANCE;
	}
	
	static MobileMqttClientSimnulation mqttClient = MobileMqttClientSimnulation.getInstance();

	static {
		//ExchangeMqttClient exchangeMqttClient = new ExchangeMqttClient("tcp://121.40.109.91", "parry","parry123", "SIMU-DDER");
		ExchangeMqttClient exchangeMqttClient = new ExchangeMqttClient(MqttCfgUtil.getServerUri(), "parry","parry123", "EXCHANGE-DDER");
		mqttClient.setMqttClient(exchangeMqttClient);
	}
	
	public boolean publish(String topic, String  message) {
		if(false == mqttClient.getMqttClient().getClient().isConnected()) {
			mqttClient.connect();
		}
		
		try {
			mqttClient.getMqttClient().getClient().publish(topic, message.getBytes(), 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		return true;	
	}
}
