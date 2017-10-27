package com.bbd.exchange.mobile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bbd.exchange.mqtt.BaseExchangeMqttMessage;
import com.bbd.exchange.mqtt.DownstreamCabinetMessage;
import com.bbd.exchange.mqtt.ExchangeMqttMessage;
import com.bbd.exchange.mqtt.MqttOriginalMessage;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;

public class MobileMqttMessageHandling extends Thread {
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

	@Override
	public void run() {
		MqttOriginalMessage message;

		while (true) {
			while ((message = MobileMqttMessageQueue.getInstance().pull()) != null) {
				System.out.println("ready to decode..."+message.getMessage());
/*				ExchangeMqttMessage exchangeMsg = messageDecode(message);
				if (exchangeMsg != null) {
					fixedThreadPool.execute(new MobileExchangeMqttMessageHandling(exchangeMsg));
				} else {
					System.out.println(message.getTopic() + " exception.");
				}*/
			}
		}
	}

	ExchangeMqttMessage messageDecode(MqttOriginalMessage message) {
		BaseExchangeMqttMessage baseMsg = new BaseExchangeMqttMessage();

		if (false == baseMsg.decode(message.getTopic(), message.getMessage().getPayload())) {
			return null;
		}

		if (baseMsg.getTopic().getDirection().equals("u")) {

			UpstreamCabinetMessage upmsg = new UpstreamCabinetMessage(baseMsg.getCmd());

			if (false == upmsg.decode(baseMsg)) {
				return null;
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
