package com.bbd.exchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class ExchangeRequestMessage implements ServiceMessage {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRequestMessage.class);

	String requestID;
	String cabinetID;
	String emptyBoxID;
	String fullEnergyBoxID;
	String batteryType;
	String notifyTopic;

	public ExchangeRequestMessage() {
		
	}

	@Override
	public void handling() {
		logger.info("receive exchange message : ");
	}

	public String destinationTopic() {
		return "exchange/" + requestID;
	}
	
	public String getNotifyTopic() {
		return notifyTopic;
	}

	public void setNotifyTopic(String notifyTopic) {
		this.notifyTopic = notifyTopic;
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

	public String getEmptyBoxID() {
		return emptyBoxID;
	}

	public void setEmptyBoxID(String emptyBoxID) {
		this.emptyBoxID = emptyBoxID;
	}

	public String getFullEnergyBoxID() {
		return fullEnergyBoxID;
	}

	public void setFullEnergyBoxID(String fullEnergyBoxID) {
		this.fullEnergyBoxID = fullEnergyBoxID;
	}

	public String getBatteryType() {
		return batteryType;
	}

	public void setBatteryType(String batteryType) {
		this.batteryType = batteryType;
	}
	
	public static ExchangeRequestMessage encode2Obj(String message)
	{
		JSONObject jsonObject = JSONObject.fromObject(message);
		ExchangeRequestMessage exchange = (ExchangeRequestMessage) JSONObject.toBean(jsonObject.getJSONObject("exchange"),
				ExchangeRequestMessage.class);
		return exchange;
	}
	
	public static String encode2Json(ExchangeRequestMessage messasge) {
        JSONObject jsonObject = new JSONObject(); 
        jsonObject.put("exchange", messasge);
        return jsonObject.toString();	
	}
}
