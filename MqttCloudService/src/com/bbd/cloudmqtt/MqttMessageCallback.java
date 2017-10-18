package com.bbd.cloudmqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttMessageCallback implements MqttCallback {

	@Override
	public void connectionLost(Throwable cause) {
		// System.out.println("connection lost : " + cause.toString());
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println(topic + "-->" + NumberUtil.bytesToHexString(message.getPayload()));

		if(topic.indexOf("Connection") != -1) {
			//System.out.println(topic + "received.");
		}
		
		if (topic.indexOf("asso") != -1) {
			AssociationMessage asso = new AssociationMessage();

			asso.decode(topic, message);
			asso.showBoxInfos();
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		
	}

}
