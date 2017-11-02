package com.bbd.exchange.dbintf;

import java.util.concurrent.ConcurrentHashMap;

import com.bbd.exchange.control.CabinetBoxObject;
import com.bbd.exchange.mobile.ClientRequestMessage;

/*
 * manage exchange request 
 * store the request information...
 * generate request record : succeed or failed...exchange time....etc..
 * 
 * */

public class ExchangeRequestMgr {
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
}
