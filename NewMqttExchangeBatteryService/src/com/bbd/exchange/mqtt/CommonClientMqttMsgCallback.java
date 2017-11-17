package com.bbd.exchange.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class CommonClientMqttMsgCallback implements MqttCallback{
	CommonExchangeMqttClient client;
	
	public CommonClientMqttMsgCallback(CommonExchangeMqttClient client) {
		super();
		this.client = client;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		client.connect();
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		System.out.println(arg0 + ":" + arg1.toString());
	}

}
