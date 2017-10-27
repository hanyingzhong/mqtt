package com.bbd.exchange.control;

import java.util.HashMap;
import java.util.Map;

public class CabinetBoxObject implements ExchangeControlObject{
	String cabinetID;
	int ID;
	String boxID; /*cabinetID/1*/
	
	Map<String, String> map = new HashMap<String, String>();
	
	public CabinetBoxObject(String cabinetID, int iD) {
		this.cabinetID = cabinetID;
		ID = iD;
	}

	
}
