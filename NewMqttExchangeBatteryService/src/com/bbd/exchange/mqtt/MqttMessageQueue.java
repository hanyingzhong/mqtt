package com.bbd.exchange.mqtt;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MqttMessageQueue implements ExchangeMessageQueue{
	//private static Queue<MqttOriginalMessage> queue = new ConcurrentLinkedQueue<MqttOriginalMessage>();
	private static BlockingQueue<MqttOriginalMessage> queue = new LinkedBlockingQueue<MqttOriginalMessage>(1024);

	private static final MqttMessageQueue INSTANCE = new MqttMessageQueue();  
	
	public static final MqttMessageQueue getInstance() { 
		return MqttMessageQueue.INSTANCE;
	}
	
	@Override
	public void add(MqttOriginalMessage message) {
		if(false == queue.offer(message)) {
			System.out.println(this.getClass().getName() + "queue is full.");
		}	
	}

	@Override
	public MqttOriginalMessage pull() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
