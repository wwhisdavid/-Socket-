package com.wwhisdavid.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

public class WebBeanUtil {
	/*
	 * 请求数据的封装
	 */
	public static <T> T copyToBean(HttpServletRequest request, Class<T> clazz){
		try {
			// 可以注册转换器
			T t = clazz.newInstance();
			Enumeration<String> enumeration = request.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement();
				String value = request.getParameter(name);
				BeanUtils.copyProperty(t, name, value);
			}
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
}
