package com.bbd.exchange.table;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.platform.RedisIntf;

public class IoTDeviceManager {
	private static final Logger logger = LoggerFactory.getLogger(IoTDeviceManager.class);

	private static final IoTDeviceManager INSTANCE = new IoTDeviceManager();

	public static final IoTDeviceManager getInstance() {
		return IoTDeviceManager.INSTANCE;
	}

	public final static int redisArea = 1;

	static ConcurrentHashMap<String, Set<String>> deviceMgr = new ConcurrentHashMap<String, Set<String>>();

	public static void loadFromRedis() {
		Set<String> devices = RedisIntf.getKeys(redisArea);
		for (String key : devices) {
			deviceMgr.put(key, RedisIntf.getSet(redisArea, key));
		}

		logger.info(deviceMgr.toString());
	}

}
