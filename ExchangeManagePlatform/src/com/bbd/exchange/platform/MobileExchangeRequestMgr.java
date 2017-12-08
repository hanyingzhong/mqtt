package com.bbd.exchange.platform;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.message.CabinetBoxID;
import com.bbd.exchange.message.InternalExchangeRequestMessage;
import com.bbd.exchange.table.CabinetBoxManager;
import com.bbd.exchange.table.CabinetManager;
import com.bbd.exchange.table.CabinetObject;

public class MobileExchangeRequestMgr {
	private static final Logger logger = LoggerFactory.getLogger(MobileExchangeRequestMgr.class);

	private static final MobileExchangeRequestMgr INSTANCE = new MobileExchangeRequestMgr();

	public static final MobileExchangeRequestMgr getInstance() {
		return MobileExchangeRequestMgr.INSTANCE;
	}

	static ConcurrentHashMap<String, InternalExchangeRequestMessage> requestMgr = new ConcurrentHashMap<String, InternalExchangeRequestMessage>();

	public static void addRequest(InternalExchangeRequestMessage message) {
		requestMgr.put(message.getRequestID(), message);
	}

	public static void removeRequest(String request) {
		requestMgr.remove(request);
	}

	public InternalExchangeRequestMessage getRequestMessage(String request) {
		return requestMgr.get(request);
	}

	private static CabinetBoxID decodeCabinetBoxID(String boxID) {
		String[] sub = boxID.split("/");

		return new CabinetBoxID(sub[0], Integer.parseInt(sub[1]));
	}

	static CabinetBoxManager getBoxManager(String boxID) {
		CabinetBoxID boxIDObj = decodeCabinetBoxID(boxID);
		CabinetObject cabinet = CabinetManager.getInstance().getCabinetObj(boxIDObj.getCabinetID());

		return cabinet.getBoxObject(boxIDObj.getID());
	}

	public static void exchangeSucceeded(InternalExchangeRequestMessage request) {
		CabinetBoxManager emptyBox = getBoxManager(request.getEmptyBoxID());
		CabinetBoxManager fullBox = getBoxManager(request.getFullEnergyBoxID());

		emptyBox.setWorkState(CabinetBoxManager.WK_NORMAL);
		fullBox.setWorkState(CabinetBoxManager.WK_NORMAL);

		removeRequest(request.getRequestID());
		
		CabinetObject.updateCabinetBox(emptyBox);
		CabinetObject.updateCabinetBox(fullBox);
	}

	public static void exchangeW4EmptyBoxOpenAckFailed(InternalExchangeRequestMessage request) {
		CabinetBoxManager emptyBox = getBoxManager(request.getEmptyBoxID());
		CabinetBoxManager fullBox = getBoxManager(request.getFullEnergyBoxID());

		emptyBox.setWorkState(CabinetBoxManager.WK_FAULT);
		fullBox.setWorkState(CabinetBoxManager.WK_NORMAL);

		removeRequest(request.getRequestID());
	}

	public static void exchangeW4FullBoxOpenAckFailed(InternalExchangeRequestMessage request) {
		CabinetBoxManager emptyBox = getBoxManager(request.getEmptyBoxID());
		CabinetBoxManager fullBox = getBoxManager(request.getFullEnergyBoxID());

		emptyBox.setWorkState(CabinetBoxManager.WK_NORMAL);
		fullBox.setWorkState(CabinetBoxManager.WK_FAULT);

		removeRequest(request.getRequestID());
		CabinetObject.updateCabinetBox(emptyBox);
	}

	public static void exchangeW4EmptyBoxClosedExpire(InternalExchangeRequestMessage request) {
		CabinetBoxManager emptyBox = getBoxManager(request.getEmptyBoxID());
		CabinetBoxManager fullBox = getBoxManager(request.getFullEnergyBoxID());

		emptyBox.setWorkState(CabinetBoxManager.WK_FAULT);
		fullBox.setWorkState(CabinetBoxManager.WK_NORMAL);

		removeRequest(request.getRequestID());
		/*notify maintainer.....*/
		CabinetObject.updateCabinetBox(emptyBox);
	}

	public static void exchangeW4FullBoxClosedExpire(InternalExchangeRequestMessage request) {
		CabinetBoxManager emptyBox = getBoxManager(request.getEmptyBoxID());
		CabinetBoxManager fullBox = getBoxManager(request.getFullEnergyBoxID());

		emptyBox.setWorkState(CabinetBoxManager.WK_NORMAL);
		fullBox.setWorkState(CabinetBoxManager.WK_NORMAL);

		removeRequest(request.getRequestID());
		/*notify maintainer.....*/
		CabinetObject.updateCabinetBox(emptyBox);
		CabinetObject.updateCabinetBox(fullBox);
	}

}
