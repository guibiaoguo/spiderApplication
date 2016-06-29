package com.spider.collection.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggerUtils {
	
	private LoggerUtils() {
		
	}

	private static Map<String, Logger> loggerMap = new HashMap<String, Logger>();
	private static String isclassify = "off";

	public static Logger getLogger(Class<?> clz) {
		Logger logger = loggerMap.get(clz.getName());
		if (logger == null) {
			if (isclassify.contains("off")) {
				logger = LoggerFactory.getLogger(clz);
			} else {
				logger = CoreLogger.getCoreLogger(clz);
			}
			loggerMap.put(clz.getName(), logger);
		}
		return logger;
	}

}
