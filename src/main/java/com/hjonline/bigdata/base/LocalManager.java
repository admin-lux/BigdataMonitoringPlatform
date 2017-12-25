package com.hjonline.bigdata.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地变量
 */
public class LocalManager {

	private static ThreadLocal<Map<LocalManagerType, Object>> tl = null;

	static {
		tl = new ThreadLocal<Map<LocalManagerType, Object>>() {
			@Override
			protected Map<LocalManagerType, Object> initialValue() {
				return new HashMap<LocalManagerType, Object>();
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static <T> T getVal(LocalManagerType key) {
		return (T) getVal(key, key.get().getClass());
	}

	@SuppressWarnings("unchecked")
	private static <T> T getVal(LocalManagerType key, Class<T> c) {
		return (T) tl.get().get(key);
	}

	public static void setVal(LocalManagerType key, Object val) {
		tl.get().put(key, val);
	}

	public static Object removeVal(String key) {
		return tl.get().remove(key);
	}

	public static void remove() {
		tl.remove();
	}

	public static Map<LocalManagerType, Object> getData() {
		return tl.get();
	}
}