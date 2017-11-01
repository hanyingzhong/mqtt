package com.bbd.exchange.mobile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bbd.exchange.mqtt.MqttOriginalMessage;

public class MobileMqttMessageHandling extends Thread {
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

	@Override
	public void run() {
		MqttOriginalMessage message;

		while (true) {
			while ((message = MobileMqttMessageQueue.getInstance().pull()) != null) {
				System.out.println("ready to decode..." + message.getMessage());
				ClientRequestMessage requestMsg = messageDecode(message);

				if (requestMsg != null) {
					fixedThreadPool.execute(new MobileExchangeMqttMessageHandling(requestMsg));
				} else {
					System.out.println(message.getTopic() + " exception.");
				}

			}
		}
	}

	ClientRequestMessage messageDecode(MqttOriginalMessage message) {
		String jsonMsg = message.getMessage().toString();

		ClientRequestMessage clientRequestMsg = new ClientRequestMessage(jsonMsg);

		System.out.println("mobile request message:" + clientRequestMsg.toString());
		return clientRequestMsg;
	}
}
