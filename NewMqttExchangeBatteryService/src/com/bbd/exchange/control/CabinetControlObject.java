package com.bbd.exchange.control;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.util.ConcurrentHashSet;

import com.bbd.exchange.mqtt.CabinetBoxContainer;
import com.bbd.exchange.util.RedisUtils;

import redis.clients.jedis.Jedis;

public class CabinetControlObject implements ExchangeControlObject {
	final static int defaultBoxNumberOfCabinet = 12;

	public static final String OFFLINE = "offline";
	public static final String ONLINE = "online";
	public static final String WORKSTATE_IDLE = "idle";
	public static final String WORKSTATE_EXCHANGE = "exchanging";
	public static final String WORKSTATE_MAINAIN = "maintain";

	public static final String STATE = "state";
	public static final String WORKSTATE = "workstate";
	public static final String DEVICEID = "deviceid";
	public static final String VOLAGE = "voltage";
	
/*	public String state = OFFLINE;
	public String workstate = WORKSTATE_IDLE;
*/
	public static final int redisStoreAera = 2;
	
	String cabinetID;
	CabinetBoxObject[] boxArray;
	private int boxNumberOfCabinet;

	Map<String, String> map = new ConcurrentHashMap<String, String>();
	
	Set<CabinetBoxObject> emptyBoxHashSet = new ConcurrentHashSet<CabinetBoxObject>();
	Set<CabinetBoxObject> nonEmptyBoxHashSet = new ConcurrentHashSet<CabinetBoxObject>();
	Set<CabinetBoxObject> exceptionBoxHashSet = new ConcurrentHashSet<CabinetBoxObject>();

	public CabinetControlObject(String cabinetID, int boxNumberOfCabinet) {
		this.cabinetID = cabinetID;
		this.boxNumberOfCabinet = boxNumberOfCabinet;

		boxArray = new CabinetBoxObject[boxNumberOfCabinet];

		for (int idx = 0; idx < boxNumberOfCabinet; idx++) {
			CabinetBoxObject e = new CabinetBoxObject(cabinetID, idx);
			set(idx, e);
			emptyBoxHashSet.add(e);
		}
	}

	public void store2Redis() {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(CabinetControlObject.redisStoreAera);
		jedis.hmset(cabinetID, map);
		RedisUtils.returnResource(jedis);
	}
	
	public void storeSingleAttr2Redis(String attr) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(CabinetControlObject.redisStoreAera);
		jedis.hset(cabinetID, attr, map.get(attr));
		RedisUtils.returnResource(jedis);
	}
	
	public CabinetControlObject(String cabinetID) {
		this.cabinetID = cabinetID;
		this.boxNumberOfCabinet = defaultBoxNumberOfCabinet;
	}

	void set(int id, CabinetBoxObject obj) {
		boxArray[id] = obj;
	}

	public int getBoxNumberOfCabinet() {
		return boxNumberOfCabinet;
	}

	public CabinetBoxObject getEmptyCabinetBox() {
		CabinetBoxObject boxObj = null;
		for (CabinetBoxObject s : emptyBoxHashSet) {
			boxObj = s;
			break;
		}

		emptyBoxHashSet.remove(boxObj);
		return boxObj;
	}

	public CabinetBoxObject getFullEnergyCabinetBox(String batteryType) {
		CabinetBoxObject boxObj = null;
		for (CabinetBoxObject s : nonEmptyBoxHashSet) {
			if (s.getCapacity() > 80) {
				boxObj = s;
				nonEmptyBoxHashSet.remove(boxObj);
				return boxObj;
			}
		}

		return null;
	}

	public void move2EmptyBoxHashSet(CabinetBoxObject e) {
		emptyBoxHashSet.add(e);
		nonEmptyBoxHashSet.remove(e);
		exceptionBoxHashSet.remove(e);
	}

	public void move2NonEmptyBoxHashSet(CabinetBoxObject e) {
		nonEmptyBoxHashSet.add(e);
		emptyBoxHashSet.remove(e);
		exceptionBoxHashSet.remove(e);
	}

	public void move2ExceptionBoxHashSet(CabinetBoxObject e) {
		exceptionBoxHashSet.add(e);
		emptyBoxHashSet.remove(e);
		nonEmptyBoxHashSet.remove(e);
	}

	public void moveBox2HashSet(CabinetBoxObject e, CabinetBoxContainer ele) {
		if (ele.isDoorClosed() && (ele.isBatteryExist() == false)) {
			move2EmptyBoxHashSet(e);
			return;
		}

		if (ele.isDoorClosed() && (ele.isBatteryExist() == true)) {
			move2NonEmptyBoxHashSet(e);
			return;
		}

	}

	public CabinetBoxObject getCabinetBox(int id) {
		if (id >= 0 && id < boxNumberOfCabinet) {
			return boxArray[id];
		}

		return null;
	}

	public String getState() {
		return map.get(CabinetControlObject.STATE);
	}

	public void setState(String state) {
		map.put(CabinetControlObject.STATE, state);
		storeSingleAttr2Redis(CabinetControlObject.STATE);
	}

	public String getWorkstate() {
		return map.get(CabinetControlObject.WORKSTATE);
	}

	public void setWorkstate(String workstate) {
		map.put(CabinetControlObject.WORKSTATE, workstate);
		storeSingleAttr2Redis(CabinetControlObject.WORKSTATE);
	}

	public void setVoltage(String voltage) {
		map.put(CabinetControlObject.VOLAGE, voltage);
		storeSingleAttr2Redis(CabinetControlObject.VOLAGE);
	}
	
	public void setDeviceId(String deviceid) {
		map.put(CabinetControlObject.DEVICEID, deviceid);
		storeSingleAttr2Redis(CabinetControlObject.DEVICEID);
	}
	
	public void show() {
		for (int idx = 0; idx < boxNumberOfCabinet; idx++) {
			CabinetBoxObject e = boxArray[idx];
			System.out.println(e.toString());
		}
	}

}
