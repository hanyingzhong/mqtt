package com.bbd.exchange.platform;

import java.util.Date;

import com.bbd.exchange.message.ExchangeRequest;

public class ServiceMessageHandlingTask implements Runnable {
	ExchangeRequest message;

	public ServiceMessageHandlingTask(ExchangeRequest message) {
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
