package com.bbd.exchange.mqtt;

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
				} else {
					System.out.println(message.topic + " exception.");
				}

			}
		}
	}

	ExchangeMqttMessage messageDecode(MqttOriginalMessage message) {
		BaseExchangeMqttMessage baseMsg = new BaseExchangeMqttMessage();

		if (false == baseMsg.decode(message.topic, message.message.getPayload())) {
			return null;
		}

		if (baseMsg.getTopic().getDirection().equals("u")) {

			UpstreamCabinetMessage upmsg = new UpstreamCabinetMessage(baseMsg.cmd);

			if (false == upmsg.decode(baseMsg)) {
				return null;
			}

			if (baseMsg.getCabinetVoltage() != null) {
				upmsg.setVoltage(baseMsg.getCabinetVoltage());
			}
			return upmsg;
		}

		if (baseMsg.getTopic().getDirection().equals("d")) {

			DownstreamCabinetMessage dowmmsg = new DownstreamCabinetMessage();

			if (false == dowmmsg.decode(baseMsg)) {
				return null;
			}
			return dowmmsg;
		}

		return null;
	}
}
