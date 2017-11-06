package com.bbd.exchange.mqtt;

import java.util.HashMap;
import java.util.Map;

public class InteractionCommand {
	public static final String UP_ASSOCIATE = "1";
	public static final String UP_DISASSOCIATE = "2";
	public static final String UP_NOTIFY = "3";
	public static final String UP_ALARM = "4";

	public static final String DOWN_MODIFY = "5";
	public static final String DOWN_PING = "6";
	public static final String UP_PONG = "7";
	public static final String UP_ASSOCIATEACK = "8";
	public static final String DOWN_REBOOT = "9";
	
	public static final String DOWN_SUB_OPEN = "1";
	public static final String DOWN_SUB_SYNCBOXSTATUS = "2";
	public static final String DOWN_SUB_SYNCTIME = "3";
	public static final String DOWN_SUB_ACK = "4";
	
	public static final int RESCODE_CLOSED = 0;
	public static final int RESCODE_OPENED = 1;
	public static final int RESCODE_OPENSUCC = 2;
	public static final int RESCODE_OPENFAIL = 3;
	public static final int RESCODE_W4CLOSEDEXPIRE = 4;

	static Map<String, String> cmdMap = new HashMap<String, String>();

	static {
		cmdMap.put(UP_ASSOCIATE, "associate");
		cmdMap.put(UP_DISASSOCIATE, "disassociate");
		cmdMap.put(UP_NOTIFY, "notify");
		cmdMap.put(UP_ALARM, "alarm");
		cmdMap.put(DOWN_MODIFY, "modify");
		cmdMap.put(DOWN_PING, "ping");
		cmdMap.put(UP_PONG, "pong");
	}

	static String getCommandString(String verb) {
		return cmdMap.get(verb);
	}

	static String getCommandValue(String verb) {
		if (verb.equals("asso")) {
			return UP_ASSOCIATE;
		}

		if (verb.equals("disa")) {
			return UP_DISASSOCIATE;
		}

		if (verb.equals("noti")) {
			return UP_NOTIFY;
		}

		if (verb.equals("alam")) {
			return UP_ALARM;
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
