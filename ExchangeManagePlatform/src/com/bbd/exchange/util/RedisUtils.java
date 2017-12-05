package com.bbd.exchange.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

	private static JedisPool jedisPool = null;
	/**
	 * ��ʼ��Redis���ӳ�
	 */
	static {
		JedisPoolConfig config = new JedisPoolConfig();

		config.setMaxTotal(Integer.valueOf(PropertyUtil.getProperty("master.redis.max_active")));
		config.setMaxIdle(Integer.valueOf(PropertyUtil.getProperty("master.redis.max_idle")));
		config.setMaxWaitMillis(Long.valueOf(PropertyUtil.getProperty("master.redis.max_wait")));
		config.setTestOnBorrow(Boolean.valueOf(PropertyUtil.getProperty("master.redis.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(PropertyUtil.getProperty("master.redis.testOnReturn")));
		jedisPool = new JedisPool(config, PropertyUtil.getProperty("master.redis.ip"),
				Integer.valueOf(PropertyUtil.getProperty("master.redis.port")));
	}

	/**
	 * ��ȡJedisʵ��
	 * 
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �ͷ�jedis��Դ
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResourceObject(jedis);
		}
	}

    /** 
     * Jedis������쳣��ʱ�򣬻���Jedis������Դ 
     * @param jedis 
     */  
    public synchronized  void returnBrokenResource(Jedis jedis){  
        if(jedis != null){  
            jedisPool.returnBrokenResource(jedis);  
        } 
    }
    
	public static void main(String[] args) {
		System.out.println("haha");
	}
}
