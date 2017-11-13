package com.bbd.exchange.service;

public interface OriginalServiceMessageQueue {
	void add(ServiceMessage message);
	ServiceMessage pull();
}
