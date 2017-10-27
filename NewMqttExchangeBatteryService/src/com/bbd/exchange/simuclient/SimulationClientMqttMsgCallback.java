package com.bbd.exchange.simuclient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.bbd.exchange.util.NumberUtil;

public class SimulationClientMqttMsgCallback implements MqttCallback{

	@Override
	public void connectionLost(Throwable arg0) {
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		//System.out.println(topic + "-->" + NumberUtil.bytesToHexString(message.getPayload()));
		System.out.println(topic + "-->" + message.toString());
	}

}
