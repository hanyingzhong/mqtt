package com.bbd.exchange.table;

import java.util.Map;

public class RedisStoreObj {
	Map<String, String> map = null;

	public RedisStoreObj(Map<String, String> map2) {
		setMap(map2);
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return map.toString();
	}

}
