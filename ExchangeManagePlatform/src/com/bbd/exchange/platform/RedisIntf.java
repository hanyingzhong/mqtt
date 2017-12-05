package com.bbd.exchange.platform;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bbd.exchange.util.RedisUtils;

import redis.clients.jedis.Jedis;

public class RedisIntf {
	public static void setAttr(int redisArea, String key, String attr, String value) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(redisArea);
		jedis.hset(key, attr, value);
		RedisUtils.returnResource(jedis);
	}

	public static Set<String> getKeys(int redisArea) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(redisArea);
		Set<String> objs = jedis.keys("*");
		RedisUtils.returnResource(jedis);
		return objs;
	}

	public static Map<String, String> getMap(int redisArea, String id) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(redisArea);
		Map<String, String> param = jedis.hgetAll(id);
		RedisUtils.returnResource(jedis);
		return param;
	}

	public static Set<String> getSet(int redisArea, String id) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(redisArea);
		Set<String> param = (HashSet<String>) jedis.smembers(id);
		RedisUtils.returnResource(jedis);
		return param;
	}
}
