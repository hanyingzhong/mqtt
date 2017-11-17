package com.bbd.exchange.test;

import com.bbd.exchange.mqtt.CommonClientMqttMsgCallback;
import com.bbd.exchange.mqtt.CommonExchangeMqttClient;
import com.bbd.exchange.util.MqttCfgUtil;

public class CommonMqttClientTest {

	public static void main(String[] args) {
		CommonExchangeMqttClient client = new CommonExchangeMqttClient(MqttCfgUtil.getServerUri(), "parry", "parry123",
				"TEST-3245662ER", null);

		try {
			client.initClient(new CommonClientMqttMsgCallback(client));
			client.connect();
			client.sendSubscribe("aaaaa/#");
			client.sendPublish("aaaaa/11", "aaaaaaaaaaaaa");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
