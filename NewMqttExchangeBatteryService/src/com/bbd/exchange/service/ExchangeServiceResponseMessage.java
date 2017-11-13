package com.bbd.exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeServiceResponseMessage implements ServiceResponseMessage {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeServiceResponseMessage.class);

	String requestID;
	String cabinetID;
	String notifyTopic;

	public ExchangeServiceResponseMessage(String requestID, String cabinetID, String notifyTopic) {
		this.requestID = requestID;
		this.cabinetID = cabinetID;
		this.notifyTopic = notifyTopic;
	}

	@Override
	public String encode() {
		return null;
	}

}
