package com.bbd.exchange.table;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.platform.RedisIntf;

public class CabinetObject {
	private static final Logger logger = LoggerFactory.getLogger(CabinetObject.class);

	final static String NUMOFBOX = "boxnum";
	final static String DEFAULT_NUMOFBOX = "12";

	String cabinetID;
	int boxnum;

	RedisStoreObj redisObj;
	CabinetBoxManager[] boxObjs;

	int getArrayIdx(int ID) {
		return ID - 1;
	}

	String getBoxID(int ID) {
		StringBuilder boxStrID = new StringBuilder(cabinetID);

		boxStrID.append("/");
		boxStrID.append(ID);
		return boxStrID.toString();
	}

	public CabinetBoxManager createCabinetBox(String boxID) {
		Map<String, String> map = RedisIntf.getMap(CabinetBoxManager.redisArea, boxID);
		RedisStoreObj redisObj = new RedisStoreObj(map);
		CabinetBoxManager boxMgr = new CabinetBoxManager();

		boxMgr.setBox(redisObj);
		return boxMgr;
	}

	public static void updateCabinetBox(CabinetBoxManager boxMgr) {
		Map<String, String> map = RedisIntf.getMap(CabinetBoxManager.redisArea, boxMgr.getBoxID());

		boxMgr.getBox().setMap(map);
	}

	public CabinetBoxManager getBoxObject(int ID) {
		int idx = getArrayIdx(ID);
		if (boxObjs[idx] != null) {
			return boxObjs[idx];
		}

		String boxID = getBoxID(ID);
		boxObjs[idx] = createCabinetBox(boxID);
		return boxObjs[idx];
	}

	void loadBoxFromRedis() {
		String numOfStr = redisObj.getMap().get(NUMOFBOX);

		if (numOfStr == null) {
			numOfStr = DEFAULT_NUMOFBOX;
			RedisIntf.setAttr(CabinetManager.redisArea, cabinetID, NUMOFBOX, DEFAULT_NUMOFBOX);
		}

		int numOfBox = Integer.parseInt(numOfStr);
		setBoxnum(numOfBox);

		if (boxObjs == null) {
			boxObjs = new CabinetBoxManager[numOfBox];
		}

		for (int boxID = 1; boxID <= numOfBox; boxID++) {
			int idx = getArrayIdx(boxID);
			String boxStrID = getBoxID(boxID);

			boxObjs[idx] = createCabinetBox(boxStrID);
			boxObjs[idx].setBoxID(boxStrID);
		}
	}

	public CabinetBoxManager getEmptyBox() {
		for (int boxID = 1; boxID <= getBoxnum(); boxID++) {
			int idx = getArrayIdx(boxID);
			CabinetBoxManager box = boxObjs[idx];

			if ((box != null) && box.isEmptyExchangable()) {
				return box;
			}
		}

		return null;
	}

	public CabinetBoxManager getFullBox(String batteryType) {
		for (int boxID = 1; boxID <= getBoxnum(); boxID++) {
			int idx = getArrayIdx(boxID);
			CabinetBoxManager box = boxObjs[idx];

			if ((box != null) && box.isFullExchangable(batteryType)) {
				return box;
			}
		}

		return null;
	}

	public void showAllCabinetBox() {
		for (int boxID = 1; boxID <= getBoxnum(); boxID++) {
			CabinetBoxManager box = getBoxObject(boxID);

			logger.info(box.toString());
		}
		System.out.println("");
	}

	public RedisStoreObj getRedisObj() {
		return redisObj;
	}

	public void setRedisObj(RedisStoreObj redisObj) {
		this.redisObj = redisObj;
	}

	@Override
	public String toString() {
		return "++[cabinetID=" + cabinetID + ", redisObj=" + redisObj.toString() + "]";
	}

	public String getCabinetID() {
		return cabinetID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	public int getBoxnum() {
		return boxnum;
	}

	public void setBoxnum(int boxnum) {
		this.boxnum = boxnum;
	}
}
