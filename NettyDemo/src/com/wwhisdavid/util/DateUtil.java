package com.wwhisdavid.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	/*
	 * 时间戳字符串转SimpleDateFormat
	 */
	public static String unixTime2String(String timestamp){
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd-HH:mm:ss");
		String date = sdf.format(new Date(Integer.valueOf(timestamp) * 1000L));
		return date;
	}
}

