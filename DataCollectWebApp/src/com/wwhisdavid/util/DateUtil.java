package com.wwhisdavid.util;

import java.text.ParseException;
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
	
	public static String unixTime2StringSecond(String timestamp){
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd-HH:mm:ss");
		String date = sdf.format(new Date(Long.valueOf(timestamp)));
		return date;
	}
	
	public static long formateTime2unix(String formate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd-HH:mm:ss");
		Date date = sdf.parse(formate);
		return date.getTime()/1000;
	}
}

