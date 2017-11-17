package com.bbd.exchange.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceMessageQueue implements OriginalServiceMessageQueue{
	private static final Logger logger = LoggerFactory.getLogger(ExchangeServiceResponseMessage.class);
	//private static Queue<MqttOriginalMessage> queue = new ConcurrentLinkedQueue<MqttOriginalMessage>();
	private static BlockingQueue<ServiceMessage> queue = new LinkedBlockingQueue<ServiceMessage>(1024);

	private static final ServiceMessageQueue INSTANCE = new ServiceMessageQueue();  
	
	public static final ServiceMessageQueue getInstance() { 
		return ServiceMessageQueue.INSTANCE;
	}
	
	@Override
	public void add(ServiceMessage message) {
		if(false == queue.offer(message)) {
			logger.error(this.getClass().getName() + "queue is full.");
		}	
	}

	@Override
	public ServiceMessage pull() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
