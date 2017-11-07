package com.bbd.exchange.control;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.bbd.exchange.util.RedisUtils;

import redis.clients.jedis.Jedis;

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
	public static final int redisStoreAera = 1;

	public static final DeviceMgrContainer getInstance() {
		return DeviceMgrContainer.INSTANCE;
	}

	public void redisStoreDeviceSet(String deviceID, String cabinetID) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(DeviceMgrContainer.redisStoreAera);
		jedis.sadd(deviceID, cabinetID);
		RedisUtils.returnResource(jedis);
	}

	public void redisRemoveDeviceSet(String deviceID, String cabinetID) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(DeviceMgrContainer.redisStoreAera);
		jedis.srem(deviceID, cabinetID);
		RedisUtils.returnResource(jedis);
	}

	public Set<String> redisSMEMBERS(String deviceID) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(DeviceMgrContainer.redisStoreAera);
		Set<String> smembers = jedis.smembers(deviceID);
		RedisUtils.returnResource(jedis);
		
		for (String str : smembers) {
		      System.out.println(str);
		}
		return smembers;
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
		redisStoreDeviceSet(deviceID, cabinetID);
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

		/* if there is no cabinet in deviceMgr, deviceMgr remove the IoT-device */
		if (set.isEmpty()) {
			deviceMgr.remove(deviceID);
		}
		redisRemoveDeviceSet(deviceID, cabinetID);
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
