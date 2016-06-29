package com.spider.collection.util;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.*;
import java.util.Properties;

public final class PropertyUtils {
	
	public static  String DEFAULT_PROPERTIES = "application.properties";

	private static boolean type = false;

	public static String getDefaultProperties() {
		return DEFAULT_PROPERTIES;
	}

	public static void setDefaultProperties(String defaultProperties) {
		DEFAULT_PROPERTIES = defaultProperties;
	}

	private PropertyUtils() {
		
	}

	public static boolean isType() {
		return type;
	}

	public static void setType(boolean type) {
		PropertyUtils.type = type;
	}

	public static String get(Object key, String file) {
		Properties props = new Properties();
		PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(file);
		InputStream is;
		try {
			if(isType()) {
				System.out.println(PropertyUtils.class.getResource("/"));
				is = new FileInputStream(new File(System.getProperty("user.dir")+File.separator+DEFAULT_PROPERTIES));
			} else
				is = resource.getInputStream();
			propertiesPersister.load(props, new InputStreamReader(is, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object result = props.get(key);
		return result == null ? "" : result.toString();
	}
	
	public static String get(Object key) {
		return get(key,DEFAULT_PROPERTIES);
	}

}
