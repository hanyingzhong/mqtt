package com.bbd.exchange.service;

import java.util.concurrent.ConcurrentHashMap;

import com.bbd.exchange.control.CabinetBoxObject;

public class ExchangeTimerLengthMgr {
	static ConcurrentHashMap<String, Integer> timerLengthTbl = new ConcurrentHashMap<String, Integer>();
	
	static {
		timerLengthTbl.put(CabinetBoxObject.WAIT4EDOOROPENED, 30);
		timerLengthTbl.put(CabinetBoxObject.WAIT4EDOORCLOSED, 30);
		timerLengthTbl.put(CabinetBoxObject.WAIT4FDOOROPENED, 30);
		timerLengthTbl.put(CabinetBoxObject.WAIT4FDOORCLOSED, 30);
	}

	public static int getTimerLength(String timer) {
		return timerLengthTbl.get(timer);
	}
	
	public static void setTimerLength(String timer, int length) {
		timerLengthTbl.put(timer, length);
	}
	
}
