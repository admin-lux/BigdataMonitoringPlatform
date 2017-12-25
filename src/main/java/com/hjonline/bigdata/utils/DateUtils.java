package com.hjonline.bigdata.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author rick_lu
 *
 */
public class DateUtils {

	public static String getFormat(String s, String pattern) {
		if (Utils.isEmpty(s)) {
			return "";
		}
		return new SimpleDateFormat(pattern).format(new Date(Long.parseLong(s)));
	}

	/***
	 * yyyy/MM/dd HH:mm:ss
	 * 
	 * @param s
	 * @return
	 */
	public static String getFormat(String s) {

		if (Utils.isEmpty(s)) {
			return "";
		}
		return getFormat(s, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * HH:mm:ss.SSS
	 * 
	 * @param s
	 * @return
	 */
	public static String getFormat2(String s) {

		if (Utils.isEmpty(s)) {
			return "";
		}
		return getFormat(s, "HH:mm:ss.SSS");
	}
}
