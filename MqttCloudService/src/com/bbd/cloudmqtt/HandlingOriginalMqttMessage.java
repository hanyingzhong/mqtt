package com.bbd.cloudmqtt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlingOriginalMqttMessage extends Thread {
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

	@Override
	public void run() {
		MqttOriginalMessage message;

		while (true) {
			while ((message = MqttMessageQueue.getInstance().pull()) != null) {
				ExchangeMqttMessage exchangeMsg = messageDecode(message);
				if (exchangeMsg != null) {
					fixedThreadPool.execute(new ExchangeMqttMessageHandling(exchangeMsg));
				}
			}
		}
	}

	ExchangeMqttMessage messageDecode(MqttOriginalMessage message) {
		if (message.topic.indexOf("asso") != -1) {
			AssociationMessage asso = new AssociationMessage();

			asso.decode(message.topic, message.message);
			//asso.showBoxInfos();
			return asso;
		}

		if (message.topic.indexOf("noti") != -1) {
			UpstreamBoxMessage modify = new UpstreamBoxMessage();

			modify.decode(message.topic, message.message.getPayload());
			//modify.showBoxInfos();
			return modify;
		}

		return null;
	}
}
