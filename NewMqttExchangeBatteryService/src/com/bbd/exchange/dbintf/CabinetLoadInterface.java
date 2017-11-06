package com.bbd.exchange.dbintf;

import com.bbd.exchange.control.CabinetControlObject;
import com.bbd.exchange.control.CabinetMgrContainer;

public class CabinetLoadInterface {
	private static final CabinetLoadInterface INSTANCE = new CabinetLoadInterface();

	public static final CabinetLoadInterface getInstance() {
		return CabinetLoadInterface.INSTANCE;
	}

	public static CabinetControlObject createCabinet(String cabinetID, int numOfbox) {
		CabinetControlObject obj = CabinetMgrContainer.getInstance().getCabinetControlObject(cabinetID);

		if (obj == null) {
			obj = new CabinetControlObject(cabinetID, numOfbox);
			CabinetMgrContainer.getInstance().createCabinet(cabinetID, obj);
			return obj;
		}

		/* if cabinet is in associated reboot the cabinet */

		return obj;
	}

	public static void loadCabinetTable(int loadMode) {
		if (loadMode == 1) {
			loadFromRedis();
			return;
		}
		if (loadMode == 2) {
			loadFromDatabase();
			return;
		}
		if (loadMode == 3) {
			loadFromMemory();
			return;
		}
	}

	static void loadFromRedis() {

	}

	static void loadFromDatabase() {

	}

	static void loadFromMemory() {
		createCabinet("HDG-00001238", 12);
		createCabinet("HDG-SZ000001", 12);
		createCabinet("EB000001", 12);
	}
}
