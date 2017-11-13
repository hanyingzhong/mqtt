package com.bbd.exchange.test;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import com.bbd.exchange.dbintf.CabinetLoadInterface;
import com.bbd.exchange.mobile.MobileMqttMessageHandling;
import com.bbd.exchange.mqtt.CabinetBoxContainer;
import com.bbd.exchange.mqtt.DownstreamCabinetMessage;
import com.bbd.exchange.mqtt.HandlingOriginalMqttMessage;
import com.bbd.exchange.mqtt.MqttMessageCallback;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;
import com.bbd.exchange.service.ServiceMessageHandling;
import com.bbd.exchange.util.MqttCfgUtil;

public class HuandianguiService {
	private static final MqttDefaultFilePersistence DATA_STORE = new MqttDefaultFilePersistence("/tmp");
	static MqttConnectOptions options;
	static MqttClient client;

	//static String server = "tcp://121.40.109.91";
	//static String server = "tcp://localhost";
	static String server = MqttCfgUtil.getServerUri();
	static String username = "parry";
	static char[] password = "parry123".toCharArray();
	static String clientId = "Device-1545662222222";
	static int keepAlive = 60;

	static Timer timer = new Timer();
	static TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			if ((client != null) && (!client.isConnected())) {
			}
		}
	};

	static void monitorMqttConnStatus() {
		timer.schedule(timerTask, 0, 2000);
	}

	static void reconect() {
		while (true) {
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
		if ((client != null) && (client.isConnected())) {
			try {
				client.subscribe("u/#", 0);
				//client.subscribe("u/HDG-000011", 0);
				//client.subscribe("u/DEVICE-000011", 0);
				//client.subscribe("d/DEVICE-000011", 0);
				client.subscribe("a/#", 0);
				// client.subscribe("ActiveMQ/Advisory/Connection");
				client.subscribe("b/DEVICE-000011/will");
				client.subscribe("timer/#", 0);
				client.subscribe("exchange/#", 0);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	static void sendPublish(String topic, String message) {
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
		UpstreamCabinetMessage msg = new UpstreamCabinetMessage("asso");
		CabinetBoxContainer box = new CabinetBoxContainer("HDG-00001238");
		CabinetBoxContainer box2 = new CabinetBoxContainer("HDG-00001238");
		CabinetBoxContainer box3 = new CabinetBoxContainer("HDG-00001238");

		msg.setDeviceID("DEVICE-000011");
		msg.setCabinetID("HDG-00001238");
		msg.setVerb("asso");

		box.setId(1);
		box.setBatteryExist(true);
		box.setBatteryID("6AAA121104000111");
		box.setCapacity("90");
		box.setDoorOpened(false);
		msg.addBox(box);

		box2.setId(2);
		box2.setBatteryExist(true);
		box2.setBatteryID("4AAA121104000111");
		box2.setCapacity("90");
		box2.setDoorOpened(false);
		msg.addBox(box2);

		box3.setId(3);
		box3.setBatteryExist(false);
		box3.setDoorOpened(false);
		msg.addBox(box3);

		String topic = msg.encodeTopic();
		byte[] buffer = msg.encode();

		try {
			client.publish(topic, buffer, 0, false);
			// client.publish(topic, buffer, 0, false);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void sendDownPublish() {
		DownstreamCabinetMessage down = new DownstreamCabinetMessage("HDG-00001238", "modi", "open", 1);

		if (down.checkAndSetDeviceID() == false) {
			return;
		}

		byte[] buffer = down.encode();
		try {
			client.publish(down.encodeTopic(), buffer, 0, false);
			// client.publish(topic, buffer, 0, false);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void init() throws Exception {

		// MqttConnectOptions options = new MqttConnectOptions();
		options = new MqttConnectOptions();
		options.setCleanSession(true);
		options.setUserName(username);
		options.setPassword(password);
		options.setKeepAliveInterval(keepAlive);
		options.setConnectionTimeout(5);
		options.setWill("b/HDG-000011/will", "disconnect".getBytes(), 2, false);
		// options.setAutomaticReconnect(true);

		// MqttClient client = new MqttClient(server, clientId, DATA_STORE);
		client = new MqttClient(server, clientId, DATA_STORE);
		client.setCallback(new MqttMessageCallback());
		client.connect(options);

		System.out.println("connect status is " + client.isConnected());

		sendSubsrcibe();
		/*
		 * if(client.isConnected()) { client.subscribe("u/#", 0); }
		 */

		monitorMqttConnStatus();

		sendPublish();

/*		new Timer().schedule(new TimerTask() {
			public void run() {
				if ((client != null) && (client.isConnected())) {
					sendDownPublish();
				}
			}
		}, 5000, 5000);*/
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		//MqttCfgUtil.loadProps();
		CabinetLoadInterface.getInstance().loadCabinetTable(1);
		
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			reconect();
		}

		System.out.println("HelloWorld.");
		new HandlingOriginalMqttMessage().start();
		new MobileMqttMessageHandling().start();
		new ServiceMessageHandling().start();

		while (true) {
			try {
				Thread.currentThread().sleep(1000);
				if (!client.isConnected()) {
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
