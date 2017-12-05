package com.bbd.exchange.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class InternalExchangeRequestMessage {
    private static final Logger logger = LoggerFactory.getLogger(InternalExchangeRequestMessage.class);

	String requestID;
	//String cabinetID;
	String emptyBoxID;
	String fullEnergyBoxID;
	String batteryType;
	String notifyTopic;

	public InternalExchangeRequestMessage(){
		
	}

	public String encode2Json() {
        JSONObject jsonObject = new JSONObject(); 
        jsonObject.put("exchange", this);
        return jsonObject.toString();	
	}

	public String destinationTopic() {
		return "exchange/" + requestID;
	}
	
	private void setNotifyTopic() {
		notifyTopic = "exchangeResponse/" + requestID;
	}
	
	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
		setNotifyTopic();
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

	public String getNotifyTopic() {
		return notifyTopic;
	}

	public void setNotifyTopic(String notifyTopic) {
		this.notifyTopic = notifyTopic;
	}
}
