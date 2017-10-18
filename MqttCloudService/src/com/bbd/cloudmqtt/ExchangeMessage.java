package com.bbd.cloudmqtt;

public interface ExchangeMessage {
    String ASSO = "asso";
	
	byte[] encode();
	ExchangeMessage  decode(String topic, byte[] upbytes);
}
