package com.bbd.exchange.test;

import com.bbd.exchange.util.ClientRequestMessage;

public class JsonTest {
	public static void main(String[] args) {
		ClientRequestMessage message = new ClientRequestMessage("exchange", "12345678", "HDG-000011", "6020");
		String json = message.toString();

		ClientRequestMessage message2 =  new ClientRequestMessage(json);
		
		System.out.println(json);
		System.out.println(message2.toString());
	}
}
