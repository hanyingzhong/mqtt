package com.bbd.exchange.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MqttDateTime {
	static String CurrentDateTime2String() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = formatter.format(date);
		return time;
	}

	static String getCurrentTime() {
		return CurrentDateTime2String();
	}
}
