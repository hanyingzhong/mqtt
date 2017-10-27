package com.bbd.exchange.control;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ExchangeCabinetGroupManager {
	ConcurrentHashMap<String, CabinetControlObject> cabinetsMap = new ConcurrentHashMap<String, CabinetControlObject>();

	void insertCabinetControlObject(String cabinetID, CabinetControlObject obj) {
		cabinetsMap.put(cabinetID, obj);

	}
	
	void show() {
		 for (Entry<String, CabinetControlObject> entry : this.cabinetsMap.entrySet()) {
			 CabinetControlObject obj = entry.getValue();
			 System.out.println(obj.cabinetID);
		 }
	}
	
	public static void main(String[] args) {
		ExchangeCabinetGroupManager manager = new ExchangeCabinetGroupManager();
		
		manager.insertCabinetControlObject("XB-00000012", new CabinetControlObject("XB-00000012", 12)); 
		
		manager.show();
	}
}
