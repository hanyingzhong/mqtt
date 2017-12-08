package com.bbd.exchange.service;

import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class ExchangeServiceResponseMessage implements ServiceResponseMessage {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeServiceResponseMessage.class);

	String requestID;
	int errcodeID;
	String result;

	public final static int EC_EXCHANGE_SUCCEEDED = 0x00;
	public final static int EC_EMPTY_BOX_CANNOT_OPENED = 0x01;
	public final static int EC_FULL_BOX_CANNOT_OPENED = 0x02;
	public final static int EC_W4_EMPTY_BOX_CLOSED_EXPIRE = 0x03;
	public final static int EC_W4_FULL_BOX_CLOSED_EXPIRE = 0x04;
	public final static int EC_EMPTY_BOX_RTN_WITHOUT_BATTERY = 0x05;
	public final static int EC_FULL_BOX_RTN_WITH_BATTERY = 0x06;
	public final static int EC_MAX_CODE = 0x07;
	static String errcodeDesc[] = new String[EC_MAX_CODE];
	static {
		errcodeDesc[EC_EXCHANGE_SUCCEEDED] = "exchange succeeded";
		errcodeDesc[EC_EMPTY_BOX_CANNOT_OPENED] = "wait empty box open failed";
		errcodeDesc[EC_FULL_BOX_CANNOT_OPENED] = "wait full box open failed";
		errcodeDesc[EC_W4_EMPTY_BOX_CLOSED_EXPIRE] = "wait empty box open failed";
		errcodeDesc[EC_W4_FULL_BOX_CLOSED_EXPIRE] = "wait full box open failed";
		errcodeDesc[EC_EMPTY_BOX_RTN_WITHOUT_BATTERY] = "empty box closed without battery";
		errcodeDesc[EC_FULL_BOX_RTN_WITH_BATTERY] = "full box closed with battery";
	}

	String getErrorCodeString(int errcode) {
		if (errcode >= 0 && errcode < EC_MAX_CODE) {
			return errcodeDesc[errcode];
		}
		return "unknown errcode";
	}

	public ExchangeServiceResponseMessage() {

	}

	public ExchangeServiceResponseMessage(String requestID, int errcodeID) {
		super();
		this.requestID = requestID;
		this.errcodeID = errcodeID;
		result = getErrorCodeString(errcodeID);
	}

	@Override
	public String encode() {
		return null;
	}

	public String encode2Json(ExchangeServiceResponseMessage messasge) {
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

	public int getErrcodeID() {
		return errcodeID;
	}

	public void setErrcodeID(int errcodeID) {
		this.errcodeID = errcodeID;
	}

}
