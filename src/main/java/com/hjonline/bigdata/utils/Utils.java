package com.hjonline.bigdata.utils;

public class Utils{
	public static void main(String[] args) {
		System.out.println(isEmpty("NUll", true));
	}

	public static boolean isEmpty(String s, boolean isNullStr) {
		if (!isNullStr) {
			isEmpty(s);
		} else {
			if (null == s || "".equals(s) || "null".equals(s.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEmpty(String s) {
		if(null == s || "".equals(s)) {
			return true;
		}
		return false;
	}

}
