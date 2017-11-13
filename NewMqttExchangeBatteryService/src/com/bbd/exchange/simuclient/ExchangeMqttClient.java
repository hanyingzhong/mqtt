package com.bbd.exchange.simuclient;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class ExchangeMqttClient {
	private static final MqttDefaultFilePersistence DATA_STORE = new MqttDefaultFilePersistence("/tmp");
	MqttConnectOptions options;
	MqttClient client;
	SimulationClientMqttMsgCallback callback;
	String server;
	String username;
	String password;
	String clientId;
	String subscribeTopic;
	int keepAlive = 60;

	void initClient() throws Exception {
		subscribeTopic = "usr/" + clientId;
		client = new MqttClient(server, clientId, DATA_STORE);
		callback = new SimulationClientMqttMsgCallback();
		client.setCallback(callback);
	}

	public boolean connect() {
		try {
			client.connect(options);
			sendSubscribe();
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
			client.subscribe("a/" + clientId);
			client.subscribe("a/" + clientId);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void sendPublish(String message) {
		if (client.isConnected() == false) {
			try {
				client.connect();
			} catch (MqttSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			client.publish("a/" + clientId, message.getBytes(), 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
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

	public ExchangeMqttClient(String server, String username, String string, String clientId) {
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

		try {
			initClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSubscribeTopic() {
		return subscribeTopic;
	}

	public MqttClient getClient() {
		return client;
	}
}
