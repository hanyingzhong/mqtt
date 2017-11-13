package com.bbd.exchange.simucabinet;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class NewExchangeMqttClient {
	private static final MqttDefaultFilePersistence DATA_STORE = new MqttDefaultFilePersistence("/tmp");
	MqttConnectOptions options;
	MqttClient client;
	SimulationCabinetMqttMsgCallback callback;
	String server;
	String username;
	String password;
	String clientId;
	int keepAlive = 60;
	
	void initClient() throws Exception {
		client = new MqttClient(server, clientId, DATA_STORE);
		//callback = new SimulationClientMqttMsgCallback();
		client.setCallback(callback);		
	}
	
	public boolean connect() {
		try {
			client.connect(options);
			sendSubscribe();
			return true;
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	void sendSubscribe() {
		try {
			//client.subscribe("a/"+clientId);
			client.subscribe("a/#");
			client.subscribe("d/#");
			client.subscribe("u/#");
			client.subscribe("timer/#");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendPublish(String message) {
		if(client.isConnected() == false) {
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
			client.publish("a/"+clientId, message.getBytes(), 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPublish(String topic, String message) {
		if(client.isConnected() == false) {
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
			client.publish(topic, message.getBytes(), 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPublish(String topic, byte[] message) {
		if(client.isConnected() == false) {
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
			client.publish(topic, message, 0, false);
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public NewExchangeMqttClient(String server, String username, String password, String clientId, SimulationCabinetMqttMsgCallback callback) {
		super();
		this.server = server;
		this.username = username;
		this.password = password;
		this.clientId = clientId;
		
		options = new MqttConnectOptions();
		options.setCleanSession(true);
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		options.setKeepAliveInterval(keepAlive);
		options.setConnectionTimeout(5);
		
		this.callback = callback;
		
		try {
			initClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MqttClient getClient() {
		return client;
	}

}
