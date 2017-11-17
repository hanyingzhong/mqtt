package com.bbd.exchange.test;

import com.bbd.exchange.mobile.ClientRequestMessage;
import com.bbd.exchange.service.ExchangeServiceResponseMessage;

public class JsonTest {
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		ClientRequestMessage message = new ClientRequestMessage("exchange", "12345678", "HDG-000011", "6020");
		String json = message.toString();

		ClientRequestMessage message2 =  new ClientRequestMessage(json);
		
		System.out.println(json);
		System.out.println(message2.toString());
		
		ExchangeServiceResponseMessage response = new ExchangeServiceResponseMessage("18616996973", "HDG-00001238", "dddd", "wait for empty box open-ack expire.");
		System.out.println(response.encode2Json(response));
	}
}
