package com.hjonline.bigdata.utils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hjonline.bigdata.controller.em.SqlType;

public class MatchDate {

	public static void main(String[] args) {
		System.out.println(new Date(1510656309077L).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
				.plusDays(Integer.parseInt(getNum("$sysday(-1)"))).format(formatter) + " 00:00:00");

		System.out.println(getNum("$sysday(-1)"));

		SqlType type = SqlType.ORACLE;
		System.out.println(getWhereSql(
				"(CREAT_TIME >= $sysday(-1) and CREAT_TIME < $sysday(0)) or (MODIFY_TIME >= $sysday(-1) and MODIFY_TIME < $sysday(0))",
				type, 1510656309077L + ""));

		System.out.println(getTime("$sysday(-1)", "1510656309077"));
	}

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static Date date = new Date();
	private static ZoneId zoneId = ZoneId.systemDefault();

	public static String getWhereSql(String whereSql, SqlType type, String timeMillis) {
		Pattern pattern = Pattern.compile("\\$sysday\\([-]?[0-9]+\\)");
		Matcher matcher = pattern.matcher(whereSql);
		while (matcher.find()) {
			String group = matcher.group();
			whereSql = whereSql.replace(group, getSqlDate(getTime(group, timeMillis), type));
		}
		return whereSql;
	}

	private static String getTime(String group, String timeMillis) {
		date.setTime(Long.parseLong(timeMillis));
		return date.toInstant().atZone(zoneId).toLocalDate().plusDays(Integer.parseInt(getNum(group))).format(formatter)
				+ " 00:00:00";
	}

	private static String getSqlDate(String time, SqlType type) {
		if (SqlType.ORACLE == type) {
			return String.format("TO_DATE('%s', 'yyyy-mm-dd hh24:mi:ss')", time);
		} else if (SqlType.MYSQL == type) {
			return String.format("STR_TO_DATE('%s', '%Y-%m-%d %T')", time);
		}
		return null;
	}

	private static String getNum(String str) {
		Matcher matcher = Pattern.compile("[-]?[0-9]+").matcher(str);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}
}
