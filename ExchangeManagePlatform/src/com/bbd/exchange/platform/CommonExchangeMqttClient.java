package com.bbd.exchange.platform;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class CommonExchangeMqttClient {
	private static final MqttDefaultFilePersistence DATA_STORE = new MqttDefaultFilePersistence("/tmp");
	MqttConnectOptions options;
	MqttClient client;
	CommonClientMqttMsgCallback callback;
	String server;
	String username;
	String password;
	String clientId;
	Set<String> subscribeTopic = new HashSet<String>();
	int keepAlive = 60;

	public void initClient(CommonClientMqttMsgCallback callbackFunction) throws Exception {
		client = new MqttClient(server, clientId, DATA_STORE);
		callback = callbackFunction;
		client.setCallback(callback);
	}

	public boolean connect() {
		try {
			client.connect(options);
			return true;
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		return false;
	}

	void sendSubscribe() {
		try {
			for (String topic : subscribeTopic) {
				client.subscribe(topic);
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void sendSubscribe(String topic) {
		if (client.isConnected() == false) {
			connect();
		}
		try {
			client.subscribe(topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	public void sendPublish(String topic, String message) {
		if (client.isConnected() == false) {
			connect();
		}

		try {
			client.publish(topic, message.getBytes(), 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void sendPublish(String topic, byte[] message, int qos) {
		if (client.isConnected() == false) {
			connect();
		}

		try {
			client.publish(topic, message, qos, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public CommonExchangeMqttClient(String server, String username, String string, String clientId,
			Set<String> subscribeTopic) {
		super();
		this.server = server;
		this.username = username;
		this.password = string;
		this.clientId = clientId;

		options = new MqttConnectOptions();
		options.setCleanSession(true);
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		options.setKeepAliveInterval(keepAlive);
		options.setConnectionTimeout(5);
	}

	public MqttClient getClient() {
		return client;
	}
}
