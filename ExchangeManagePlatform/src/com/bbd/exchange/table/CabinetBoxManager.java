package com.bbd.exchange.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.platform.RedisIntf;

public class CabinetBoxManager {
	private static final Logger logger = LoggerFactory.getLogger(CabinetBoxManager.class);

	String boxID;
	RedisStoreObj box;

	final static String STATE = "state";
	final static String STATUS = "status";/* empty/full/fault/maintain */
	final static String BATTERYTYPE = "batteryType";
	final static String CAPACITY = "capacity";
	final static String WKSTATE = "wkstate";
	final static String DOORSTATUS = "doorStatus";

	final static String ST_EMPTY = "empty";
	final static String ST_EXIST = "exist";

	public final static String WK_NORMAL = "normal";
	public final static String WK_FAULT = "fault";
	public final static String WK_MAINTAIN = "maintain";
	public final static String WK_INEXCHAGING = "exchanging";

	public static final String DOOR_CLOSED = "closed";
	public static final String DOOR_OPENED = "opened";

	final static int redisArea = 3;

	@Override
	public String toString() {
		return "Box:" + box.toString();
	}

	public String getStatus() {
		return box.getMap().get(STATUS);
	}

	public String getDoorStatus() {
		return box.getMap().get(DOORSTATUS);
	}

	public boolean isDoorClosed() {
		if (getDoorStatus().equals(DOOR_CLOSED)) {
			return true;
		}

		return false;
	}

	public String getWorkState() {
		return box.getMap().get(WKSTATE);
	}

	public void setWorkState(String state) {
		box.getMap().put(CabinetBoxManager.WKSTATE, state);
		logger.info("{}, set workstate {}", boxID, state);
		//RedisIntf.setAttr(redisArea, boxID, CabinetBoxManager.WKSTATE, state);
	}

	public String getBatteryType() {
		return box.getMap().get(BATTERYTYPE);
	}

	public int getCapacity() {
		String cap = box.getMap().get(CAPACITY);
		if (cap == null) {
			return 0;
		}

		if (cap.equals("null")) {
			return 0;
		}

		int capacity = Integer.parseInt(cap);
		return capacity;
	}

	public RedisStoreObj getBox() {
		return box;
	}

	public void setBox(RedisStoreObj box) {
		this.box = box;
	}

	public boolean isWorkNormal() {
		String workState = getWorkState();

		if (workState == null) {
			return false;
		}

		if (workState.equals(WK_NORMAL)) {
			return true;
		}

		return false;
	}

	public boolean isEmptyExchangable() {
		if (isWorkNormal() && isEmpty() && isDoorClosed()) {
			return true;
		}

		return false;
	}

	public boolean isFullExchangable(String batteryType) {
		if (isWorkNormal() && (getCapacity() > 80) && isDoorClosed() && isFullWithMatchhedBattery(batteryType)) {
			return true;
		}

		return false;
	}

	public boolean isEmpty() {

		if (getStatus().equals(ST_EMPTY)) {
			return true;
		}

		return false;
	}

	public boolean isExist() {
		if (getStatus().equals(ST_EXIST)) {
			return true;
		}

		return false;
	}

	public boolean isFullWithMatchhedBattery(String batteryType) {
		if (isExist() && matchedBattery(batteryType)) {
			return true;
		}
		return false;
	}

	public boolean matchedBattery(String batteryType) {
		if (getBatteryType().charAt(0) == batteryType.charAt(0)) {
			return true;
		}

		return false;
	}

	public String getBoxID() {
		return boxID;
	}

	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}

}
