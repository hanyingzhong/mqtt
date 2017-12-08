package com.bbd.exchange.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.message.ExchangeMessage;
import com.bbd.exchange.message.InternalExchangeResponseMessage;
import com.bbd.exchange.message.MobileExchangeRequestMessage;

public class CommonExchangeRequest implements ExchangeMessage {
	private static final Logger logger = LoggerFactory.getLogger(MobileExchangeRequestHandler.class);

	String topic;
	String message;

	public CommonExchangeRequest(String topic, String message) {
		this.topic = topic;
		this.message = message;

		System.out.println(topic + ":" + message);
	}

	@Override
	public void handling() {
		if (topic.startsWith("exchangeResponse")) {
			InternalExchangeResponseMessage response = InternalExchangeResponseMessage.encode2Obj(message);
			if (null != response) {
				response.handling();
			} else {
				logger.warn("{} can't be convert into InternalExchangeResponseMessage", this);
			}
			/*
			 * InternalExchangeRequestMessage requestmMessasge =
			 * MobileExchangeRequestHandler.encode2Obj(message); if (null !=
			 * requestmMessasge) {
			 * MobileExchangeRequestHandler.messageHandler(requestmMessasge); } else {
			 * logger.warn("{} can't be convert into MobileExchangeRequestMessage", this); }
			 */

			// MobileExchangeRequestHandler.handling(message);
			return;
		}

		if (topic.startsWith("exbattery")) {
			MobileExchangeRequestMessage requestmMessasge = MobileExchangeRequestMessage.encode2Obj(message);
			if (null != requestmMessasge) {
				MobileExchangeRequestHandler.messageHandler(requestmMessasge);
			} else {
				logger.warn("{} can't be convert into MobileExchangeRequestMessage", this);
			}
			return;
		}
	}

	/*
	 * protected void finalize() throws Throwable { super.finalize();
	 * System.out.println("obj [Date: " + this.getClass().getName() + "] is gc"); }
	 */
}
