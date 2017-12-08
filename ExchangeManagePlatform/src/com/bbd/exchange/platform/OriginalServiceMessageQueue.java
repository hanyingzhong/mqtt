package com.bbd.exchange.platform;

import com.bbd.exchange.message.ExchangeMessage;

public interface OriginalServiceMessageQueue {
	void add(ExchangeMessage message);
	ExchangeMessage pull();
}
