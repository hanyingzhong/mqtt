package com.bbd.exchange.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class InternalExchangeResponseMessage{
	private static final Logger logger = LoggerFactory.getLogger(InternalExchangeResponseMessage.class);

	String requestID;
	String result;
	
	public InternalExchangeResponseMessage(){
		
	}
	
	public InternalExchangeResponseMessage(String requestID, String cabinetID, String notifyTopic, String result) {
		super();
		this.requestID = requestID;
		this.result = result;
	}

	public String encode2Json(InternalExchangeResponseMessage messasge) {
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
