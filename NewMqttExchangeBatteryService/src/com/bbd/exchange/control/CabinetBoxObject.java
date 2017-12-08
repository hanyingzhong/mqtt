package com.bbd.exchange.control;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.mqtt.CabinetBoxContainer;
import com.bbd.exchange.service.ExchangeTimerLengthMgr;
import com.bbd.exchange.util.RedisUtils;

import redis.clients.jedis.Jedis;

public class CabinetBoxObject implements ExchangeControlObject {
	private static final Logger logger = LoggerFactory.getLogger(NotifyCabinetMessageHandling.class);

	public static final String IDLE = "idle";
	public static final String SELCTED = "selected";
	public static final String EMPTY_W4OPENED = "empty_w4opened";
	public static final String EMPTY_W4CLOSED = "empty_w4closed";
	public static final String INEXCHANGE = "inexchange";
	public static final String FULL_W4OPENED = "full_w4oepn";
	public static final String FULL_W4CLOSED = "full_w4close";
	public static final String OPENED = "opened";
	public static final String CLOSED = "closed";
	public static final String OPENFAULT = "open-fault";

	public static final String STATUS = "status";
	public static final String BATERYID = "batteryID";
	public static final String BATERYTYPE = "batteryType";
	public static final String CAPACITY = "capacity";
	public static final String TEMPERATURE = "temperature";
	public static final String STATE = "state";
	public static final String DOORSTATUS = "doorStatus";

	public static final String BATTEY_EXIST = "exist";
	public static final String BATTEY_EMPTY = "empty";
	public static final String DOOR_CLOSED = "closed";
	public static final String DOOR_OPENED = "opened";

	public static final String BATERYTYPE_48 = "4";
	public static final String BATERYTYPE_60 = "6";
	public static final String BATERYTYPE_72 = "7";

	public static final String WAIT4EDOOROPENED = "wait4emptydoorOpened";
	public static final String WAIT4EDOORCLOSED = "wait4emptydoorClosed";
	public static final String WAIT4FDOOROPENED = "wait4fulledoorOpened";
	public static final String WAIT4FDOORCLOSED = "wait4fulledoorClosed";

	public static final int redisStoreAera = 3;

	String cabinetID;
	int ID;
	String boxID; /* cabinetID/1 */

	Map<String, String> map = new HashMap<String, String>();
	Map<String, ScheduledFuture<?>> timerMap = new ConcurrentHashMap<String, ScheduledFuture<?>>();

	public Map<String, ScheduledFuture<?>> getTimerMap() {
		return timerMap;
	}

	public CabinetBoxObject(String cabinetID, int iD) {
		this.cabinetID = cabinetID;
		ID = iD;
		boxID = cabinetID + "/" + (ID + 1);

		map.put(CabinetBoxObject.STATUS, CabinetBoxObject.BATTEY_EMPTY);
		map.put(CabinetBoxObject.BATERYID, "null");
		map.put(CabinetBoxObject.BATERYTYPE, "null");
		map.put(CabinetBoxObject.CAPACITY, "null");
		map.put(CabinetBoxObject.STATE, CabinetBoxObject.IDLE);
		map.put(CabinetBoxObject.DOORSTATUS, CabinetBoxObject.DOOR_CLOSED);
	}

	public void timerExipre(String timerID) {
		logger.info(boxID + " receives timer : " + timerID);
	}

	public void setBoxAttr(CabinetBoxContainer ele) {
		if (ele.isBatteryExist()) {
			map.put(CabinetBoxObject.STATUS, BATTEY_EXIST);
			map.put(CabinetBoxObject.BATERYID, ele.getBatteryID());
			map.put(CabinetBoxObject.BATERYTYPE, ele.getBatteryID().substring(0, 1));
			map.put(CabinetBoxObject.CAPACITY, ele.getCapacity());
		} else {
			map.put(CabinetBoxObject.STATUS, BATTEY_EMPTY);
			map.put(CabinetBoxObject.BATERYID, "null");
			map.put(CabinetBoxObject.CAPACITY, "null");
		}

		if (ele.isDoorOpened()) {
			map.put(CabinetBoxObject.DOORSTATUS, CabinetBoxObject.DOOR_OPENED);
		} else {
			map.put(CabinetBoxObject.DOORSTATUS, CabinetBoxObject.DOOR_CLOSED);
		}
	}

	public void store2Redis() {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(CabinetBoxObject.redisStoreAera);
		jedis.hmset(getBoxID(), getMap());
		RedisUtils.returnResource(jedis);
	}

	public void storeSingleAttr2Redis(String attr) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(CabinetBoxObject.redisStoreAera);
		jedis.hset(getBoxID(), attr, getMap().get(attr));
		RedisUtils.returnResource(jedis);
	}

	public String toString() {
		return boxID + ":" + map.toString();
	}

	public void newState(String state) {
		map.put(CabinetBoxObject.STATE, state);
		logger.info("{}, new state : {}", boxID, state);
	}

	public void setTimer(String timerID) {
		BoxTimerMessage boxTimer = new BoxTimerMessage(getCabinetID(), Integer.toString(getID() + 1), timerID);
		BoxTimerTask task = new BoxTimerTask(boxTimer);
		int timerLength = ExchangeTimerLengthMgr.getTimerLength(timerID);

		ScheduledFuture<?> result = TimerMgr.setTimer(task, timerLength);
		if (result != null) {
			getTimerMap().put(timerID, result);
			logger.info("{}, Set timer : {}, " + timerLength, getBoxID(), timerID);
		}
	}

	public void setTimer(String timerID, int timerLength) {
		BoxTimerMessage boxTimer = new BoxTimerMessage(getCabinetID(), Integer.toString(getID() + 1), timerID);
		BoxTimerTask task = new BoxTimerTask(boxTimer);

		ScheduledFuture<?> result = TimerMgr.setTimer(task, timerLength);
		if (result != null) {
			getTimerMap().put(timerID, result);
			logger.info("{}, Set timer : {}", getBoxID(), timerID);
		}
	}

	public void killTimer(String timerID) {
		ScheduledFuture<?> schedule = getTimerMap().get(timerID);

		if (schedule != null) {
			logger.info("{}, Kill timer : {}", getBoxID(), timerID);
			schedule.cancel(false);
			getTimerMap().remove(timerID);
		}
	}

	public String getBoxID() {
		return boxID;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public int getID() {
		return ID;
	}

	public String getBatteryType() {
		return getMap().get(BATERYTYPE);
	}

	public int getCapacity() {
		return Integer.parseInt(getMap().get(CAPACITY));
	}

	public String getBoxState() {
		return getMap().get(STATE);
	}

	public String getCabinetID() {
		return cabinetID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}
}
