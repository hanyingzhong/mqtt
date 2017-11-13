package com.bbd.exchange.service;

import com.bbd.exchange.control.BoxTimerMessage;

public class TimerExpireServiceMessage implements ServiceMessage {
	BoxTimerMessage timer;
	
	public TimerExpireServiceMessage(BoxTimerMessage timer) {
		this.timer = timer;
	}

	@Override
	public void handling() {
		
	}
}
