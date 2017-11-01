package com.bbd.exchange.control;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 * A IoT device may associate at least one cabinet. two or more cabinets also exist.
 * 
 * for HashSet, thers is no same cabinet exist in the structure.
 * 
 * Device Mgr only store real time state information
 * 
 * 
 * */

public class DeviceMgrContainer implements ContainerManagment {
	static ConcurrentHashMap<String, Set<String>> deviceMgr = new ConcurrentHashMap<String, Set<String>>();
	static ConcurrentHashMap<String, String> cabinetMgr = new ConcurrentHashMap<String, String>();

	private static final DeviceMgrContainer INSTANCE = new DeviceMgrContainer();

	public static final DeviceMgrContainer getInstance() {
		return DeviceMgrContainer.INSTANCE;
	}

	@Override
	public boolean insertCabinet(String deviceID, String cabinetID) {
		Set<String> set = deviceMgr.get(deviceID);

		if (set == null) {
			set = new HashSet<String>();
			set.add(cabinetID);
		}

		set.add(cabinetID);
		deviceMgr.put(deviceID, set);
		cabinetMgr.put(cabinetID, deviceID);
		return true;
	}

	@Override
	public boolean removeCabinet(String deviceID, String cabinetID) {
		Set<String> set = deviceMgr.get(deviceID);

		if (set == null) {
			return true;
		}

		set.remove(cabinetID);
		cabinetMgr.remove(cabinetID);
		return true;
	}

	/*
	 * when disa received, the function must be called....
	 */
	@Override
	public boolean removeDevice(String deviceID) {
		Set<String> set = deviceMgr.get(deviceID);

		if (set == null) {
			deviceMgr.remove(deviceID);
			return true;
		}

		for (String ele : set) {
			set.remove(ele);
			cabinetMgr.remove(ele);
		}

		deviceMgr.remove(deviceID);
		return false;
	}

	@Override
	public boolean removeCabinet(String cabinetID) {
		String deviceID = cabinetMgr.get(cabinetID);
		Set<String> set = deviceMgr.get(deviceID);

		cabinetMgr.remove(cabinetID);
		if (set != null) {
			set.remove(cabinetID);
		}

		return false;
	}

	@Override
	public String getDeviceByCabinet(String cabinetID) {
		return cabinetMgr.get(cabinetID);
	}

}
