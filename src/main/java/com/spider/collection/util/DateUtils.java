package com.spider.collection.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	public static String F19 = "yyyy-MM-dd HH:mm:ss";

	public static String F14 = "yyyyMMddHHmmss";

	public static String F10 = "yyyy-MM-dd";

	public static final String ISO_DATE_FORMAT = "yyyyMMdd";

	public static String[] dataStringFormats = {
			"yyyyMMddHHmmss",
			"yyyyMMdd",
			"yyyy-MM-dd",
			"yyyy-MM-dd HH:mm:ss",
			"yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss"
	};

	public static Date stringToDate(String dateString) {
		Date date = null;
		try {
			date = parseDate(dateString, dataStringFormats);
		} catch (ParseException e) {
			System.err.println("string to Date format failed:" + dateString);
		}
		return date;
	}

	/**
	 * ������ת��Ϊָ���ĸ�ʽ��ʾ
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}

	/**
	 * �õ���ǰʱ�䣬ָ����ʽ���ַ�����ʾyyyyMMddHHmmss
	 * @param f
	 * @return
	 */
	public static String getCurrTime(String f){
		SimpleDateFormat sf = new SimpleDateFormat(f);
		return sf.format(new Date());
	}


	/**
	 * �õ���ǰ���ڣ�ָ����ʽ���ַ�����ʾyyyyMMdd
	 * @return
	 */
	public static String getCurrDay(){
		return dateToString(new Date(), "yyyyMMdd");
	}



	/**
	 * ���ڽ���
	 * @param source
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String source, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format) ;
		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			return null;
		}
	}


	public static String parseDateStr(Object source,String f) {
		if(source == null){
			return "";
		}
		Date d = stringToDate(source.toString());
		if(d != null){
			return dateToString(d, f);
		}else{
			return source.toString();
		}
	}

	/**
	 * �õ���ǰʱ�伸����ǰ�򼸷��Ӻ��ʱ��
	 * @param minutes
	 * @return
	 */
	public static Date getAddMinutesTime(int minutes) {
		Calendar dalendar = Calendar.getInstance();
		dalendar.add(Calendar.MINUTE, minutes);
		return dalendar.getTime();
	}

	/**
	 * �õ���ǰʱ�伸Сʱǰ��Сʱ���ʱ��
	 * @param hour
	 * @return
	 */
	public static Date getAddHourTime(int hour) {
		Calendar dalendar = Calendar.getInstance();
		dalendar.add(Calendar.HOUR,hour);
		return dalendar.getTime();
	}

	/**
	 * �õ���ǰʱ�伸��ǰ������ʱ��
	 * @param day
	 * @return
	 */
	public static Date getAddDateTime(int day) {
		Calendar dalendar = Calendar.getInstance();
		dalendar.add(Calendar.DATE,day);
		return dalendar.getTime();
	}

	/**
	 * ĳ����������������ȡ��������ַ���
	 *
	 * @param date ����
	 * @param days ����
	 * @return java.lang.String (yyyyMMdd)
	 */
	public static String dateIncreaseByDay(String date, int days) {
		return dateIncreaseByDay(date, ISO_DATE_FORMAT, days);
	}

	/**
	 * ĳ����������������ȡ��������ַ���
	 *
	 * @param date ����
	 * @param fmt ��ʽ�ַ���
	 * @param days ���ӵ�����
	 * @return
	 */
	public static String dateIncreaseByDay(String date, String fmt, int days) {
		return dateIncrease(date, fmt, Calendar.DATE, days);
	}

	/**
	 * ĳ��������������/����/������ȡ�������
	 *
	 * @param isoString �����ַ���
	 * @param fmt �����ַ����ĸ�ʽ
	 * @param field ��������������/����/���� Calendar.YEAR/Calendar.MONTH/Calendar.DATE
	 * @param amount ����
	 * @return
	 * @throws ParseException
	 */
	public static final String dateIncrease(String isoString, String fmt, int field, int amount) {

		try {
			Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTime(stringToDate(isoString, fmt, true));
			cal.add(field, amount);

			return dateToString(cal.getTime(), fmt);

		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * �����ڵ��ַ���ת��ΪDate��
	 *
	 * @param dateText �����ַ���
	 * @param format ��ʽ���ַ���
	 * @param lenient ָ�������ڣ�ʱ��ķ����Ƿ��ǿ��ɵ�
	 * @return
	 */
	public static Date stringToDate(String dateText, String format, boolean lenient) {

		if (dateText == null) {

			return null;
		}
		DateFormat df = null;
		try {

			if (format == null) {
				df = new SimpleDateFormat();
			} else {
				df = new SimpleDateFormat(format);
			}

			// setLenient avoids allowing dates like 9/32/2001
			// which would otherwise parse to 10/2/2001
			df.setLenient(false);

			return df.parse(dateText);
		} catch (ParseException e) {

			return null;
		}
	}

	/**
	 * �õ���ǰʱ��,��ʽ:yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static String currtimeToString14() {
		return dateToString(new Date(), "yyyyMMddHHmmss");
	}

	/**
	 * �õ���ǰʱ��,��ʽ:yyyyMMddHHmmssSSS
	 *
	 * @return
	 */
	public static String currtimeToString17() {
		return dateToString(new Date(), "yyyyMMddHHmmssSSS");
	}

	/**
	 * �õ���ǰʱ��,��ʽ:yyyyMMdd
	 *
	 * @return
	 */
	public static String currtimeToString8() {
		return dateToString(new Date(), "yyyyMMdd");
	}

	/**
	 * �õ���ǰʱ��,��ʽ:yyyyMMdd
	 *
	 * @return
	 */
	public static String currtimeToString12() {
		String time = dateToString(new Date(),"yyyyMMddHHmm");
		if(StringUtils.endsWithAny(time,new String[] {"0","5"})) {
			return time;
		} else if(StringUtils.endsWithAny(time,new String[] {"1","2","3","4"})){
			return StringUtils.substring(time,0,time.length()-1)+"0";
		} else if(StringUtils.endsWithAny(time,new String[] {"6","7","8","9"})){
			return StringUtils.substring(time,0,time.length()-1)+"5";
		}
		return dateToString(new Date(), "yyyyMMddHHmm");
	}
	/**
	 * ���ʱ����λ�Ƿ�
	 * ��ʽ:yyyyMMddHHmmss
	 * @param endTime
	 * * @return
	 */
	public static Integer getDateTimeLag(String endTime){

		try {
			Date end = stringToDate(endTime);
			Date now = new Date();
			long aa = now.getTime()-end.getTime();
			long min=aa/(1000*60);
			return Integer.parseInt((min+""));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * �ַ���ת����ָ���������ַ�����ʽ
	 * @param dateString
	 * @param format
	 * @return
	 */
	public static String stringToDateStr(String dateString,String format){
		String dateStr = "";
		if(dateString != "" && dateString != null){
			Date date =stringToDate(dateString);
			DateFormat t = new SimpleDateFormat(format);
			dateStr = t.format(date);
		}
		return dateStr;
	}
//	public static void main(String[] args) {
////		System.out.println(DateUtils.dateIncrease("20131212","yyyyMMdd",Calendar.MONTH,-1));
////		System.out.println(DateUtils.getDateTimeLag("20131031164923"));
////		System.out.println(DateUtils.dateIncrease(DateUtils.getCurrTime(DateUtils.F10),DateUtils.F10,Calendar.MONTH,-1)+" 00:00:00");
////		System.out.println(DateUtils.getCurrTime(DateUtils.F19));
//		System.out.println(stringToDateStr("20131212121212","yyyyMMdd HH:mm"));
//
//	}

}
