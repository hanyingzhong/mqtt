package com.bbd.exchange.service;

public class ServiceMessageHandlingTask implements Runnable {
	ServiceMessage message;

	public ServiceMessageHandlingTask(ServiceMessage message) {
		this.message = message;
	}

	@Override
	public void run() {
		handling();
	}

	void handling() {
		message.handling();
	}
}
