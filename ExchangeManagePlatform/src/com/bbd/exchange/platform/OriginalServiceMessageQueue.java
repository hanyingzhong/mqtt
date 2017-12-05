package com.bbd.exchange.platform;

import com.bbd.exchange.message.ExchangeRequest;

public interface OriginalServiceMessageQueue {
	void add(ExchangeRequest message);
	ExchangeRequest pull();
}
