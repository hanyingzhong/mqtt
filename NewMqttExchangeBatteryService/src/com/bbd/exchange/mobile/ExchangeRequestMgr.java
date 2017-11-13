package com.bbd.exchange.mobile;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.control.BoxTimerMessage;
import com.bbd.exchange.control.CabinetBoxObject;
import com.bbd.exchange.control.CabinetMgrContainer;
import com.bbd.exchange.dbintf.RemoteTerminalRequest;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;
import com.bbd.exchange.service.ExchangeRequestMessage;

/*
 * manage exchange request 
 * store the request information...
 * generate request record : succeed or failed...exchange time....etc..
 * 
 * */

public class ExchangeRequestMgr {
	private static final Logger logger = LoggerFactory.getLogger(UpstreamCabinetMessage.class);

	private static final ExchangeRequestMgr INSTANCE = new ExchangeRequestMgr();

	public static final ExchangeRequestMgr getInstance() {
		return ExchangeRequestMgr.INSTANCE;
	}

	static ConcurrentHashMap<String, RemoteTerminalRequest> requestMgr = new ConcurrentHashMap<String, RemoteTerminalRequest>();

	public void addRequestInstance(CabinetBoxObject empty, CabinetBoxObject full, ClientRequestMessage request) {	
		requestMgr.put(empty.getBoxID(), new RemoteTerminalRequest(full, request));
		requestMgr.put(full.getBoxID(), new RemoteTerminalRequest(empty, request));
	}
	
	/*only open the box...for....*/
	public void addRequestInstance(CabinetBoxObject box, ClientRequestMessage request) {	
		requestMgr.put(box.getBoxID(), new RemoteTerminalRequest(null, request));
	}
	
	public CabinetBoxObject getAssociatedBox(CabinetBoxObject closed) {
		return requestMgr.get(closed.getBoxID()).getAssociatedBox();
	}
	
	public void removeRequestInstance(CabinetBoxObject closed) {
		requestMgr.remove(closed.getBoxID());
	}
	
	public void exchangeRequestMessageHandling(ExchangeRequestMessage message) {
		CabinetBoxObject empty = CabinetMgrContainer.getInstance().getCabinetBox(message.getCabinetID(), Integer.parseInt(message.getEmptyBoxID()) - 1); 
		CabinetBoxObject full = CabinetMgrContainer.getInstance().getCabinetBox(message.getCabinetID(), Integer.parseInt(message.getFullEnergyBoxID()) - 1); 

		if(empty == null) {
			logger.info(message.getEmptyBoxID() + " is not configured.");
			return;
		}
		
		if(full == null) {
			logger.info(message.getFullEnergyBoxID() + " is not configured.");
			return;
		}	
		
		logger.info("receive a valid exchange request. empty box is {}, full box is {}", empty.getBoxID(), full.getBoxID());
	}

	void boxTimerExpire(CabinetBoxObject boxObj, String timer) {
		
	}
	
	public void timerExpireHandling(BoxTimerMessage timer) {
		logger.info("Timer expire : " + timer.toString());
		CabinetBoxObject boxObj = CabinetMgrContainer.getInstance().getCabinetBox(timer.getCabinetID(),
				Integer.parseInt(timer.getBoxId()) - 1);
		if (boxObj != null) {
			//boxObj.timerExipre(timer.getTimerID());
			boxTimerExpire(boxObj, timer.getTimerID());
		}		
	}
}
