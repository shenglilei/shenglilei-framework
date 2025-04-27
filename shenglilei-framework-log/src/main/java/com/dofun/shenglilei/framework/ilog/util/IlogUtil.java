package com.dofun.shenglilei.framework.ilog.util;

import java.util.List;

public class IlogUtil {
	
	private static final CopyOnWriteMap<String, String> propertyCache = new CopyOnWriteMap<>();
	/**
	 * 为logger设置属性
	 * @param loggerName
	 * @param key
	 * @param value
	 */
	public static void putLogProperty(String loggerName,String key,String value)
	{
		String ckey = loggerName+"."+key;
		propertyCache.put(ckey, value);
	}
	/**
	 * 取出logger的属性
	 * @param loggerName
	 * @param key
	 * @return
	 */
	public static String getLogProperty(String loggerName,String key)
	{
		String ckey = loggerName+"."+key;
		String value = propertyCache.get(ckey);
		return value;
	}
	public static String getLogPropertyKey(String loggerName,String key)
	{
		String ckey = loggerName+"."+key;
		return ckey;
	}
	public static void removePropertyKeys(List<String> keys) {
		propertyCache.clearKeys(keys);
	}
	public static List<String> getAllPropertyKeys(){
		return propertyCache.getKeys();
	}
	
	
}
