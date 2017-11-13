package com.bbd.exchange.dbintf;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.control.CabinetControlObject;
import com.bbd.exchange.control.CabinetMgrContainer;
import com.bbd.exchange.control.NotifyCabinetMessageHandling;
import com.bbd.exchange.mqtt.DownRebootCabinetMessage;
import com.bbd.exchange.util.RedisUtils;

import redis.clients.jedis.Jedis;

public class CabinetLoadInterface {
	private static final Logger logger = LoggerFactory.getLogger(NotifyCabinetMessageHandling.class);

	public static final String LOADFROMREDIS = "redis";
	public static final String LOADFROMDB = "db";
	public static final String LOADFROMMEMORY = "memory";

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

	public static void loadCabinetTable(String loadMode) {
		if (loadMode.equals(LOADFROMREDIS)) {
			loadFromRedis();
			return;
		}

		if (loadMode.equals(LOADFROMDB)) {
			loadFromDatabase();
			return;
		}

		if (loadMode.equals(LOADFROMMEMORY)) {
			loadFromMemory();
			return;
		}
	}

	public static Set<String> getCabinetKeysFromRedis() {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(CabinetControlObject.redisStoreAera);
		Set<String> cabinets = jedis.keys("*");
		RedisUtils.returnResource(jedis);
		return cabinets;
	}

	public static Map<String, String> getCabinetInfoFromRedis(String cabinetID) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(CabinetControlObject.redisStoreAera);
		Map<String, String> cabinet = jedis.hgetAll(cabinetID);
		RedisUtils.returnResource(jedis);
		return cabinet;
	}

	static void sendDownRebootMessage(String cabinetID, String deviceID) {
		DownRebootCabinetMessage reboot = new DownRebootCabinetMessage(cabinetID, 0);
		
		reboot.setDeviceID(deviceID);
		reboot.publish(reboot);
		logger.info("send down Reboot message to {}/{}", deviceID, cabinetID);
	}
	
	/*if the record number is huge...the function will delay......*/
	static void loadFromRedis() {
		Set<String> cabinets = getCabinetKeysFromRedis();
		
		logger.info("====Loading cabinet:===============================");
		for (String cabinet : cabinets) {
			Map<String, String> attr = getCabinetInfoFromRedis(cabinet);
			if (attr != null) {
				int defaultBoxNum = 12;
				String boxNum = attr.get("boxnum");
				String deviceID = attr.get("deviceid");

				if (boxNum != null) {
					defaultBoxNum = Integer.parseInt(boxNum);
				}

				createCabinet(cabinet, defaultBoxNum);
				if(deviceID != null) {
					sendDownRebootMessage(cabinet, deviceID);
				}
					
				logger.info(cabinet + " : " + attr);
			} else {
				System.out.println(cabinet + ":" + "invalid");
			}
		}
		logger.info("====================================================");
	}

	static void loadFromDatabase() {

	}

	static void loadFromMemory() {
		createCabinet("HDG-00001238", 12);
		createCabinet("HDG-SZ000001", 12);
		createCabinet("EB000001", 12);
	}
}
