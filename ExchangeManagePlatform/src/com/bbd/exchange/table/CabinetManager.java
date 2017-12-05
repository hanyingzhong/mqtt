package com.bbd.exchange.table;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.platform.RedisIntf;

public class CabinetManager {
	private static final Logger logger = LoggerFactory.getLogger(CabinetManager.class);

	private static final CabinetManager INSTANCE = new CabinetManager();

	public static final CabinetManager getInstance() {
		return CabinetManager.INSTANCE;
	}

	static Map<String, CabinetObject> manager = new ConcurrentHashMap<String, CabinetObject>();

	public final static int redisArea = 2;

	public static void loadFromRedis() {
		Set<String> devices = RedisIntf.getKeys(redisArea);
		for (String key : devices) {
			Map<String, String> map = RedisIntf.getMap(redisArea, key);
			RedisStoreObj redisObj = new RedisStoreObj(map);
			CabinetObject cabinet = new CabinetObject();
			
			cabinet.setCabinetID(key);
			cabinet.setRedisObj(redisObj);
			manager.put(key, cabinet);
			
			cabinet.loadBoxFromRedis();
			
			logger.info(manager.get(key).toString());
			cabinet.showAllCabinetBox();
		}

/*		for (String key : devices) {
			logger.info(manager.get(key).toString());
		}*/
	}
	
	public CabinetObject getCabinetObj(String cabinetID) {
		return manager.get(cabinetID);
	}
	
	
	
}
