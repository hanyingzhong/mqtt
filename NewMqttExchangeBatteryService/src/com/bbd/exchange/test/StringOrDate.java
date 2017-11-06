package com.bbd.exchange.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class StringOrDate {
	public static String dateToString(Date date, String type) {
		String str = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (type.equals("SHORT")) {
			// 07-1-18
			format = DateFormat.getDateInstance(DateFormat.SHORT);
			str = format.format(date);
		} else if (type.equals("MEDIUM")) {
			// 2007-1-18
			format = DateFormat.getDateInstance(DateFormat.MEDIUM);
			str = format.format(date);
		} else if (type.equals("FULL")) {
			// 2007年1月18日 星期四
			format = DateFormat.getDateInstance(DateFormat.FULL);
			str = format.format(date);
		}
		return str;
	}

	public static Date stringToDate(String str) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			// Fri Feb 24 00:00:00 CST 2012
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 2012-02-24
		date = java.sql.Date.valueOf(str);

		return date;
	}

	String CurrentDateTime2String() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = formatter.format(date);
		return time;
	}

	public static void main(String[] args) {
		Date date1 = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = formatter.format(date1);
		System.out.println(time);

		Date date = new Date();
		System.out.println(StringOrDate.dateToString(date, "MEDIUM"));
		String str = "2012-2-24";
		System.out.println(StringOrDate.stringToDate(str));
	}
}