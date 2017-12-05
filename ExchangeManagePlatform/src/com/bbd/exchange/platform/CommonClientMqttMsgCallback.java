package com.bbd.exchange.platform;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class CommonClientMqttMsgCallback implements MqttCallback{
	CommonExchangeMqttClient client;
	
	enum CommandType {
		FROM_NONE, FROM_DEVICE, FROM_MOBILE, FROM_TIMERMGR, FROM_EXCHANGER;
	}

	CommandType msgFrom;

	CommandType getMsgSource(String topic) {
		if(topic.startsWith("exchange")) {
			return CommandType.FROM_EXCHANGER;
		}
	
		if(topic.startsWith("exbattery")) {
			return CommandType.FROM_EXCHANGER;
		}
	
		return CommandType.FROM_NONE;
	}

	public CommonClientMqttMsgCallback(CommonExchangeMqttClient client) {
		super();
		this.client = client;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		//client.connect();
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {	
		if(CommandType.FROM_EXCHANGER == getMsgSource(topic)) {
			ServiceMessageQueue.getInstance().add(new CommonExchangeRequest(topic, message.toString()));
		}
	}

}
