package com.bbd.exchange.mqtt;

public class InteractionCommand {
	static String getCommandValue(String verb) {
		if (verb.equals("asso")) {
			return "1";
		}

		if (verb.equals("disa")) {
			return "2";
		}

		if (verb.equals("noti")) {
			return "3";
		}

		if (verb.equals("alam")) {
			return "4";
		}

		if (verb.equals("modi")) {
			return "5";
		}

		if (verb.equals("ping")) {
			return "6";
		}

		if (verb.equals("pong")) {
			return "7";
		}

		return "0";
	}
	
	static String getDownSubCommandValue(String verb) {
		if (verb.equals("open")) {
			return "1";
		}

		if (verb.equals("sync")) {
			return "2";
		}

		if (verb.equals("synctime")) {
			return "3";
		}
		
		return "0";
	}
}
