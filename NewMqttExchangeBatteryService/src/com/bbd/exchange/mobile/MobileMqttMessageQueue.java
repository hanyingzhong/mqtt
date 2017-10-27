package com.bbd.exchange.mobile;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.bbd.exchange.mqtt.ExchangeMessageQueue;
import com.bbd.exchange.mqtt.MqttOriginalMessage;

public class MobileMqttMessageQueue implements ExchangeMessageQueue{
	//private static Queue<MqttOriginalMessage> queue = new ConcurrentLinkedQueue<MqttOriginalMessage>();
	private static BlockingQueue<MqttOriginalMessage> queue = new LinkedBlockingQueue<MqttOriginalMessage>(1024);

	private static final MobileMqttMessageQueue INSTANCE = new MobileMqttMessageQueue();  
	
	public static final MobileMqttMessageQueue getInstance() { 
		return MobileMqttMessageQueue.INSTANCE;
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
