package com.bbd.cloudmqtt;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class HuandianguiService {
	private static final MqttDefaultFilePersistence DATA_STORE = new MqttDefaultFilePersistence("/tmp");
	static MqttConnectOptions options;
	static MqttClient client;
	
	static String server = "tcp://121.40.109.91";
	static String  username = "parry";
	static char[]  password = "parry123".toCharArray();
	static String clientId = "Device-1545662222222";
	static int    keepAlive = 60;
	
	static Timer timer = new Timer();
	static TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			if((client != null) && (!client.isConnected())) {
			}
		}    		
	};
	
	static void monitorMqttConnStatus() {
    	timer.schedule(timerTask, 0, 2000);
	}
	
	static void reconect() {
		while(true) {
			try {
				client.connect(options);
				sendSubsrcibe();
				break;
			} catch (MqttException e) {
				try {
					Thread.currentThread();
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}
	
	static void sendSubsrcibe() {
		if((client != null) && (client.isConnected())) {
    		try {
				client.subscribe("d/HDG-000011/#", 0);
				//client.subscribe("ActiveMQ/Advisory/Connection");
				client.subscribe("b/HDG-000011/will");
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static void sendPublish(String topic, String message) 
	{
		MqttMessage msg = new MqttMessage();
		
		msg.setPayload(message.getBytes());
		try {
			client.publish(topic, msg);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void sendPublish() {
		UpstreamBoxMessage box = new UpstreamBoxMessage();
		
		box.setId(1);
		box.setBatteryExist(true);
		box.setBatteryID("XB-72000001");
		box.setCapacity(99);
		
		byte[] msg = box.encode();
		
		try {
			client.publish("d/HDG-000011/asso", msg, 0, false);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void sendPublish2() {
		UpstreamBoxMessage box = new UpstreamBoxMessage();
		
		box.setId(1);
		box.setBatteryExist(true);
		box.setBatteryID("XB-72000001");
		box.setCapacity(99);
		
		byte[] msg = box.encode();

		UpstreamBoxMessage box2 = new UpstreamBoxMessage();
		box2.setId(2);
		box2.setBatteryExist(false);
		
		byte[] msg2 = box2.encode();
		byte[] msgTotal = new byte[msg.length + msg2.length];
		
		System.arraycopy(msg,  0, msgTotal, 0, msg.length);
		System.arraycopy(msg2, 0, msgTotal, msg.length, msg2.length);
		
		try {
			client.publish("d/HDG-000011/asso", msgTotal, 0, false);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void init() throws Exception{
		
//    	MqttConnectOptions options = new MqttConnectOptions();
		options = new MqttConnectOptions();
    	options.setCleanSession(true);
    	options.setUserName(username);
    	options.setPassword(password);
    	options.setKeepAliveInterval(keepAlive);
    	options.setConnectionTimeout(5);
    	options.setWill("b/HDG-000011/will", "disconnect".getBytes(), 2, false);
    	//options.setAutomaticReconnect(true);
    	
//    	MqttClient client = new MqttClient(server, clientId, DATA_STORE);
    	client = new MqttClient(server, clientId, DATA_STORE);
    	client.setCallback(new MqttMessageCallback());
    	client.connect(options);
    	
    	System.out.println("connect status is "+ client.isConnected());

    	sendSubsrcibe();
/*    	if(client.isConnected()) {
    		client.subscribe("u/#", 0);
    	}*/
    	sendPublish("d/HDG-000011/modi", "hahahhhhhhhhhhhhhhhhhhh");
    	monitorMqttConnStatus();
    	
    	//sendPublish();
    	sendPublish2();
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args){
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			reconect();
		}
		
		System.out.println("HelloWorld.");
		
		while(true) {
			try {
				Thread.currentThread().sleep(1000);
				if(!client.isConnected()) {
					System.out.println("connection lost.....reconnect");
					reconect();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

