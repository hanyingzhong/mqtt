package com.bbd.exchange.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.platform.MobileExchangeRequestMgr;

import net.sf.json.JSONObject;

public class InternalExchangeResponseMessage implements ExchangeMessage {
	private static final Logger logger = LoggerFactory.getLogger(InternalExchangeResponseMessage.class);

	String requestID;
	String result;
	int errcodeID;

	public final static int EC_EXCHANGE_SUCCEEDED = 0x00;
	public final static int EC_EMPTY_BOX_CANNOT_OPENED = 0x01;
	public final static int EC_FULL_BOX_CANNOT_OPENED = 0x02;
	public final static int EC_W4_EMPTY_BOX_CLOSED_EXPIRE = 0x03;
	public final static int EC_W4_FULL_BOX_CLOSED_EXPIRE = 0x04;
	public final static int EC_EMPTY_BOX_RTN_WITHOUT_BATTERY = 0x05;
	public final static int EC_FULL_BOX_RTN_WITH_BATTERY = 0x06;
	public final static int EC_MAX_CODE = 0x07;

	public InternalExchangeResponseMessage() {

	}

	public InternalExchangeResponseMessage(String requestID, String cabinetID, String notifyTopic, String result) {
		super();
		this.requestID = requestID;
		this.result = result;
	}

	@Override
	public void handling() {
		InternalExchangeRequestMessage request = MobileExchangeRequestMgr.getInstance().getRequestMessage(requestID);
		if (request == null) {
			return;
		}
		if (errcodeID == EC_EXCHANGE_SUCCEEDED) {
			MobileExchangeRequestMgr.exchangeSucceeded(request);
			return;
		}
		if (errcodeID == EC_EMPTY_BOX_CANNOT_OPENED) {
			MobileExchangeRequestMgr.exchangeW4EmptyBoxOpenAckFailed(request);
			return;
		}
		if (errcodeID == EC_FULL_BOX_CANNOT_OPENED) {
			MobileExchangeRequestMgr.exchangeW4FullBoxOpenAckFailed(request);
			return;
		}
		if (errcodeID == EC_W4_EMPTY_BOX_CLOSED_EXPIRE) {
			MobileExchangeRequestMgr.exchangeW4EmptyBoxClosedExpire(request);
			return;
		}
		if (errcodeID == EC_W4_FULL_BOX_CLOSED_EXPIRE) {
			MobileExchangeRequestMgr.exchangeW4FullBoxClosedExpire(request);
			return;
		}
	}

	public String encode2Json(InternalExchangeResponseMessage messasge) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("exchangeResponse", messasge);
		return jsonObject.toString();
	}

	public static InternalExchangeResponseMessage encode2Obj(String message) {
		JSONObject jsonObject = JSONObject.fromObject(message);
		InternalExchangeResponseMessage response = (InternalExchangeResponseMessage) JSONObject
				.toBean(jsonObject.getJSONObject("exchangeResponse"), InternalExchangeResponseMessage.class);
		return response;
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

	public int getErrcodeID() {
		return errcodeID;
	}

	public void setErrcodeID(int errcodeID) {
		this.errcodeID = errcodeID;
	}
	
}
