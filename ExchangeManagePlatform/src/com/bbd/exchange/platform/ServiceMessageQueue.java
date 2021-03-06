package com.bbd.exchange.platform;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.message.ExchangeMessage;

public class ServiceMessageQueue implements OriginalServiceMessageQueue{
	private static final Logger logger = LoggerFactory.getLogger(ServiceMessageQueue.class);
	//private static Queue<MqttOriginalMessage> queue = new ConcurrentLinkedQueue<MqttOriginalMessage>();
	private static BlockingQueue<ExchangeMessage> queue = new LinkedBlockingQueue<ExchangeMessage>(1024);

	private static final ServiceMessageQueue INSTANCE = new ServiceMessageQueue();  
	
	public static final ServiceMessageQueue getInstance() { 
		return ServiceMessageQueue.INSTANCE;
	}
	
	@Override
	public void add(ExchangeMessage message) {
		if(false == queue.offer(message)) {
			logger.error(this.getClass().getName() + "queue is full.");
		}	
	}

	@Override
	public ExchangeMessage pull() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
