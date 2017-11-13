package com.bbd.exchange.control;

import net.sf.json.JSONObject;

public class BoxTimerMessage {
	String cabinetID;
	String boxId; /* "1"...."12" */
	String timerID;

	public BoxTimerMessage() {

	}

	public BoxTimerMessage(String cabinetID, String boxId, String timerID) {
		super();
		this.cabinetID = cabinetID;
		this.boxId = boxId;
		this.timerID = timerID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	public void setTimerID(String timerID) {
		this.timerID = timerID;
	}

	public String getCabinetID() {
		return cabinetID;
	}

	public String getBoxId() {
		return boxId;
	}

	public String getTimerID() {
		return timerID;
	}

	@Override
	public String toString() {
		return "BoxTimerMessage [cabinetID=" + cabinetID + ", boxId=" + boxId + ", timerID=" + timerID + "]";
	}

	public static String destinationTopic(BoxTimerMessage message) {
		return "timer/box/" + message.getBoxId();
	}

	public static BoxTimerMessage encode2Obj(String message) {
		JSONObject jsonObject = JSONObject.fromObject(message);
		BoxTimerMessage exchange = (BoxTimerMessage) JSONObject.toBean(jsonObject.getJSONObject("expire"),
				BoxTimerMessage.class);
		return exchange;
	}

	public static String encode2Json(BoxTimerMessage messasge) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("expire", messasge);
		return jsonObject.toString();
	}
}
