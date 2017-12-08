package com.bbd.exchange.test;

import com.bbd.exchange.mobile.ClientRequestMessage;
import com.bbd.exchange.service.ExchangeServiceResponseMessage;

import net.sf.json.JSONObject;

public class JsonTest {
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		ClientRequestMessage message = new ClientRequestMessage("exchange", "12345678", "HDG-000011", "6020");
		String json = message.toString();

		ClientRequestMessage message2 = new ClientRequestMessage(json);

		System.out.println(json);
		System.out.println(message2.toString());

		ExchangeServiceResponseMessage response = new ExchangeServiceResponseMessage("18616996973", 0);
		System.out.println(response.encode2Json(response));

		TestA a = new TestA();
		TestB b = new TestB();
		a.setA1("1");
		a.setA2("2");
		b.setB1("b1");
		b.setB2("b2");
		a.setB(b);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("exchangeResponse", a);
		jsonObject.getJSONObject("exchangeResponse").put("a/0", b);
		System.out.println(jsonObject.toString());
	}
}
