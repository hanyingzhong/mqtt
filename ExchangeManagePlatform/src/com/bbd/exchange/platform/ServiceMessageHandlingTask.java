package com.bbd.exchange.platform;

import java.util.Date;

import com.bbd.exchange.message.ExchangeMessage;

public class ServiceMessageHandlingTask implements Runnable {
	ExchangeMessage message;

	public ServiceMessageHandlingTask(ExchangeMessage message) {
		this.message = message;
	}

	@Override
	public void run() {
		handling();
	}

	void handling() {
		message.handling();
	}
	
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("obj [Date: " + this.getClass().getName() + "] is gc");
    } 
}
