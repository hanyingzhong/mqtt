package com.bbd.exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class ExchangeServiceResponseMessage implements ServiceResponseMessage {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeServiceResponseMessage.class);

	String requestID;
	String cabinetID;
	String notifyTopic;
	String result;
	
	public ExchangeServiceResponseMessage(){
		
	}
	
	public ExchangeServiceResponseMessage(String requestID, String cabinetID, String notifyTopic, String result) {
		super();
		this.requestID = requestID;
		this.cabinetID = cabinetID;
		this.notifyTopic = notifyTopic;
		this.result = result;
	}

	@Override
	public String encode() {
		return null;
	}

	public static String encode2Json(ExchangeServiceResponseMessage messasge) {
        JSONObject jsonObject = new JSONObject(); 
        jsonObject.put("exchangeResponse", messasge);
        return jsonObject.toString();	
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getCabinetID() {
		return cabinetID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	public String getNotifyTopic() {
		return notifyTopic;
	}

	public void setNotifyTopic(String notifyTopic) {
		this.notifyTopic = notifyTopic;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public static Logger getLogger() {
		return logger;
	}
	
}
