package com.bbd.exchange.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.bbd.exchange.mqtt.ExchangeMessageQueue;
import com.bbd.exchange.mqtt.MqttOriginalMessage;

public class ServiceMessageQueue implements OriginalServiceMessageQueue{
	//private static Queue<MqttOriginalMessage> queue = new ConcurrentLinkedQueue<MqttOriginalMessage>();
	private static BlockingQueue<ServiceMessage> queue = new LinkedBlockingQueue<ServiceMessage>(1024);

	private static final ServiceMessageQueue INSTANCE = new ServiceMessageQueue();  
	
	public static final ServiceMessageQueue getInstance() { 
		return ServiceMessageQueue.INSTANCE;
	}
	
	@Override
	public void add(ServiceMessage message) {
		if(false == queue.offer(message)) {
			System.out.println(this.getClass().getName() + "queue is full.");
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
