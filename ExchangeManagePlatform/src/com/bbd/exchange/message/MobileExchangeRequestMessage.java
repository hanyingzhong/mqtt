package com.bbd.exchange.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class MobileExchangeRequestMessage {
	private static final Logger logger = LoggerFactory.getLogger(MobileExchangeRequestMessage.class);

	// userID+serialnumer
	String requestID;
	String deviceID;
	// indicate is cabinet or IoT-device
	String deviceType;
	String batteryType;
	String notifyTopic;

	public MobileExchangeRequestMessage() {

	}

	public static MobileExchangeRequestMessage encode2Obj(String message) {
		JSONObject jsonObject = JSONObject.fromObject(message);
		MobileExchangeRequestMessage exchange = (MobileExchangeRequestMessage) JSONObject
				.toBean(jsonObject.getJSONObject("mobileExchange"), MobileExchangeRequestMessage.class);
		return exchange;
	}

	public String encode2Json() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mobileExchange", this);
		return jsonObject.toString();
	}

	public String getRequestID() {
		return requestID;
	}
	
	public String destinationTopic() {
		return "exbattery/" + requestID;
	}
	
	private void setNotifyTopic() {
		notifyTopic = "exbatteryResponse/" + requestID;
	}
	
	public void setRequestID(String requestID) {
		this.requestID = requestID;
		setNotifyTopic();
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
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

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
