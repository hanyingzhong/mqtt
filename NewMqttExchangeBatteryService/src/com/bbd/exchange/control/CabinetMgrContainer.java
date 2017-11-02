package com.bbd.exchange.control;

import java.util.concurrent.ConcurrentHashMap;

public class CabinetMgrContainer {
	static ConcurrentHashMap<String, CabinetControlObject> deviceMgr = new ConcurrentHashMap<String, CabinetControlObject>();

	private static final CabinetMgrContainer INSTANCE = new CabinetMgrContainer();

	public static final CabinetMgrContainer getInstance() {
		return CabinetMgrContainer.INSTANCE;
	}

	public boolean createCabinet(String cabinetID, CabinetControlObject objMgr) {
		CabinetControlObject mgr = deviceMgr.get(cabinetID);
		if(mgr == null) {
			mgr = deviceMgr.put(cabinetID, objMgr);
			return mgr == null;
		}
		
		return true;
	}
	
	public void removeCabinet(String cabinetID) {
		deviceMgr.remove(cabinetID);
	}
	
	public CabinetControlObject getCabinetControlObject(String cabinetID) {
		return deviceMgr.get(cabinetID);
	}
	
	public CabinetBoxObject getEmptyCabinetBox(String cabinetID) {
		CabinetControlObject mgrObj = getCabinetControlObject(cabinetID);
		CabinetBoxObject boxObj = mgrObj != null ? mgrObj.getEmptyCabinetBox() : null;
		
		if(boxObj != null) {
			System.out.println("emtpy box selected : " + boxObj.toString());
		}
		return boxObj;		
	}
	
	public CabinetBoxObject getCabinetBoxByBatteryType(String cabinetID, String type) {
		CabinetControlObject mgrObj = getCabinetControlObject(cabinetID);
		CabinetBoxObject boxObj = mgrObj != null ? mgrObj.getFullEnergyCabinetBox(type) : null;
		
		if(boxObj != null) {
			System.out.println("full energy box selected : " + boxObj.toString());
		}
		return boxObj;		
	}
	
	public CabinetBoxObject getCabinetBox(String cabinetID, int id) {
		CabinetControlObject mgrObj = getCabinetControlObject(cabinetID);
		CabinetBoxObject boxObj = mgrObj != null ? mgrObj.getCabinetBox(id) : null;
		
		if(boxObj != null) {
			return boxObj;
		}
		return null;		
	}
}
