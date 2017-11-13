package com.bbd.exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.control.BoxTimerMessage;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;

public class CommonServiceMessage implements ServiceMessage {
	private static final Logger logger = LoggerFactory.getLogger(UpstreamCabinetMessage.class);

	final static String EXCHANFE_REQUEST = "exchange";
	final static String TIMER_EXPIRE = "timer";
	final static String CABINET_MAINTAIN = "maintain";

	String topic;
	String message;

	public CommonServiceMessage(String topic2, String message2) {
		topic = topic2;
		message = message2;
	}

	void timerExpireEventHandling() {
		BoxTimerMessage timer = BoxTimerMessage.encode2Obj(message);
		if (timer != null) {
			ExchangeServiceRequestMgr.getInstance().timerExpireHandling(timer);
		}
	}

	void exchangeEventHandling() {
		ExchangeRequestMessage exchange = ExchangeRequestMessage.encode2Obj(message);
		if (exchange != null) {
			ExchangeServiceRequestMgr.getInstance().exchangeRequestMessageHandling(exchange);
			//exchange.handling();
		} else {
			logger.error("json {} to ExchangeRequestMessage failed.", message);
		}
	}

	@Override
	public void handling() {
		String service = getServiceType();

		if (service != null && service.equals(TIMER_EXPIRE)) {
			timerExpireEventHandling();
			return;
		}

		if (service != null && service.equals(EXCHANFE_REQUEST)) {
			exchangeEventHandling();
			return;
		}
	}

	String getServiceType() {
		char[] array = topic.toCharArray();

		if (array[0] == 't' && array[1] == 'i') {
			return TIMER_EXPIRE;
		}

		if (array[0] == 'e' && array[1] == 'x') {
			return EXCHANFE_REQUEST;
		}

		if (array[0] == 'm' && array[1] == 'a') {
			return CABINET_MAINTAIN;
		}

		return null;
	}
}
