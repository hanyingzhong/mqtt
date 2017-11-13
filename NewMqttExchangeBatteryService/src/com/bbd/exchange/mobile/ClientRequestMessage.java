package com.bbd.exchange.mobile;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientRequestMessage {
	String topic;
	String requestType;
	String clientID;
	String cabinetID;
	String batteryType;

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public ClientRequestMessage(){
		
	}
	
	public String getTopic() {
		return "a/" + clientID;
	}
	
	public String getRequestType() {
		return requestType;
	}

	public String getClientID() {
		return clientID;
	}

	public String getCabinetID() {
		return cabinetID;
	}

	public String getBatteryType() {
		return batteryType;
	}

	public ClientRequestMessage(String json){		
		JSONObject cmdJSONObject;
		try {
			cmdJSONObject = new JSONObject(json);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return;
		}
		
		try {
			requestType = cmdJSONObject.getString("requestType");
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

		try {
			clientID = cmdJSONObject.getString("clientID");
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}

		try {
			cabinetID = cmdJSONObject.getString("cabinetID");
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			batteryType = cmdJSONObject.getString("batteryType");
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public ClientRequestMessage(String requestType, String clientID, String cabinetID, String batteryType) {
		this.requestType = requestType;
		this.clientID = clientID;
		this.cabinetID = cabinetID;
		this.batteryType = batteryType;
	}

	@Override
	public String toString() {
		JSONObject cmdJSONObject = new JSONObject();
		try {
			cmdJSONObject.put("requestType", requestType);
			cmdJSONObject.put("clientID", clientID);
			cmdJSONObject.put("cabinetID", cabinetID);
			cmdJSONObject.put("batteryType", batteryType);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return cmdJSONObject.toString();
	}
	
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	public void setBatteryType(String batteryType) {
		this.batteryType = batteryType;
	}

	public boolean json2Object(String json) {
		JSONObject cmdJSONObject;
		try {
			cmdJSONObject = new JSONObject(json);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return false;
		}
		
		try {
			requestType = cmdJSONObject.getString("requestType");
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		try {
			clientID = cmdJSONObject.getString("clientID");
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		try {
			cabinetID = cmdJSONObject.getString("cabinetID");
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			batteryType = cmdJSONObject.getString("batteryType");
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
