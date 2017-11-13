package com.bbd.exchange.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceMessageHandling extends Thread {
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

	@Override
	public void run() {
		ServiceMessage message;

		while (true) {
			while ((message = ServiceMessageQueue.getInstance().pull()) != null) {
					fixedThreadPool.execute(new ServiceMessageHandlingTask(message));
			}
		}
	}
}
