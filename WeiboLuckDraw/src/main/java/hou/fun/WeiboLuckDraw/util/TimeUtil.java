package hou.fun.WeiboLuckDraw.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author houweitao
 * @date 2016年10月30日下午5:58:06
 */

public class TimeUtil {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
	static SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
	static SimpleDateFormat secondFormat = new SimpleDateFormat("ss");
	static DecimalFormat doubleFormat = new DecimalFormat("######0.000000000000");

	private static final long yearLen = 365 * 24 * 60 * 60;
	private static final long dayLen = 24 * 60 * 60;

	static boolean isRunYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
			return true;
		else
			return false;
	}

	static long lenOfYear(int year) {
		if (isRunYear(year)) {
			return yearLen + dayLen;
		} else
			return yearLen;
	}

	static int year(Date date) {
		String year = yearFormat.format(date);
		return Integer.valueOf(year);
	}

	public static String used(Date date) {
		int year = year(date);
		String str = year + "-01-01 00:00:00";

		Date start;
		try {
			start = sdf.parse(str);
			System.out.println(start.getTime() / 1000);

			System.out.println(date.getTime() / 1000);
			System.out.println(lenOfYear(year));
			long has = (date.getTime() - start.getTime()) / 1000;

			has *= 100;

			double ret = (double) has / (double) lenOfYear(year);
			System.out.println(ret);
			return doubleFormat.format(ret) + "%";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static Date nextHour(Date date) {
		long now = date.getTime();
		int min = Integer.valueOf(minuteFormat.format(date));
		int sec = Integer.valueOf(secondFormat.format(date));

		long begin = now - min * 60 * 1000 - sec * 1000;
		long end = begin + 60 * 60 * 1000;
		return new Date(end);
	}

	public static int getYear(Date date) {
		return Integer.valueOf(yearFormat.format(date));
	}

	public static void main(String[] args) throws ParseException {
		TimeUtil tu = new TimeUtil();
		System.out.println(tu.used(new Date()));

		System.out.println(tu.nextHour(new Date()));
	}

}
