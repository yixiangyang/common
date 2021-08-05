package com.xiangyang.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * LocalDate工具类
 *
 */
@Slf4j
public class DateUtil {
	private static final String YYYYMMDD = "yyyy-MM-dd";
	private static final String YYYYMMDD2 = "YYYY.MM.dd";
	private static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	private static final String YYYYMMDDHHMMSS0 = "yyyyMMddHHmmss";

	private static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

	private static final String YYYYMMDDHH0 = "yyyyMMdd";

	private static final String HHMM = "HH:mm";

	private static final String HHMMssSSS = "HH:mm:ss:SSS";



	public static Timestamp getCurrentTimestamp(){
		Timestamp timestamp = new Timestamp(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
		return timestamp;
	}

	public static String getCurrentYYYYMMDD(){
		DateTimeFormatter df= DateTimeFormatter.ofPattern(YYYYMMDDHH0);
		String time = df.format(LocalDateTime.ofInstant(
				Instant.ofEpochMilli(getCurrentTimestamp().getTime()),ZoneId.of("Asia/Shanghai")));
		return time;
	}

	public static String getCurrentYYYYMMDDHHmmss(){
		DateTimeFormatter df= DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS0);
		String time = df.format(LocalDateTime.ofInstant(
				Instant.ofEpochMilli(getCurrentTimestamp().getTime()),ZoneId.of("Asia/Shanghai")));
		return time;
	}

	public static String getCurrentYYYYMMDDHHmmssSSS(){
		DateTimeFormatter df= DateTimeFormatter.ofPattern(YYYYMMDDHHMMSSSSS);
		String time = df.format(LocalDateTime.ofInstant(
				Instant.ofEpochMilli(getCurrentTimestamp().getTime()),ZoneId.of("Asia/Shanghai")));
		return time;
	}

	/**
	 * 解析时间字符串成 yyyyMMddHHmmss
	 * @param dateText
	 * @return
	 */
	public static final Date parseYYYYMMDDHHMMSS0(String dateText) {
		if(dateText == null) {return null;}
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS0);
			LocalDateTime dateTime = LocalDateTime.parse(dateText, df);
			return localDateTimeConvertToDate(dateTime);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}


	/**
	 * 获取当前时间
	 * @return
	 */
	public static Date getCurrentDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneId zone = ZoneId.of("Asia/Shanghai");
		Instant instant = localDateTime.atZone(zone).toInstant();
		Date date = Date.from(instant);
		return date;
	}
	
	/**
	 * 获取当前时间的毫秒
	 * @return
	 */
	public static Long getCurrentMillisecond() {
		return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
	}
	/**
	 * 获取当前时间的秒
	 * @return
	 */
	public static Long getCurrentSecond() {
		return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
	}
	
	//将java.util.Date 转换为java8 的java.time.LocalDateTime,默认时区为东8区
    public static LocalDateTime dateConvertToLocalDateTime(Date date) {
        return date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }
 
    //将java.util.Date 转换为java8 的java.time.LocalDate,默认时区为东8区
    public static LocalDate dateConvertToLocalDate(Date date) {
        return date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDate();
    }
   
    //将java8 的 java.time.LocalDateTime 转换为 java.util.Date，默认时区为东8区
    public static Date localDateTimeConvertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
    }
	
  //将java8 的 java.time.LocalDateTime 转换为 java.util.Date，默认时区为东8区
    public static Date localDateConvertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.of("Asia/Shanghai")).toInstant());
    }
    
    /**
	 * 解析时间字符串成 yyyy-MM-dd HH:mm:ss
	 * @param dateText
	 * @return
	 */
	public static final Date parseYYYYMMDDHHMMSS(String dateText) {
		if(dateText == null) {return null;}
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime dateTime = LocalDateTime.parse(dateText, df);
			return localDateTimeConvertToDate(dateTime);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 解析时间字符串成 yyyy-MM-dd HH:mm:ss
	 * @param dateText
	 * @return
	 */
	public static final Date parseYYYYMMDD(String dateText) {
		if(dateText == null) {return null;}
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYYMMDD);
			LocalDate dateTime = LocalDate.parse(dateText, df);
			return localDateConvertToDate(dateTime);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 将毫秒值 转换为日期
	 * @param time
	 * @return
	 */
	public static final String formatYYYY_MM_DDHHMMSS(long time) {
		DateTimeFormatter df= DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);
		String longToDateTime = df.format(LocalDateTime.ofInstant(
		   Instant.ofEpochMilli(time),ZoneId.of("Asia/Shanghai")));
		return longToDateTime;
	}

	/**
	 * 将毫秒值 转换为日期 格式 yyyy-mm-dd
	 * @param time
	 * @return
	 */
	public static final String formatYYYY_MM_DD(long time) {
		DateTimeFormatter df= DateTimeFormatter.ofPattern(YYYYMMDD);
		String longToDateTime = df.format(LocalDateTime.ofInstant(
				Instant.ofEpochMilli(time),ZoneId.of("Asia/Shanghai")));
		return longToDateTime;
	}

	/**
	 * 获取当前时间的 分钟和秒数
	 * @return 16:01
	 */
	public static final String getCurrentHHMM(){
		DateTimeFormatter df = DateTimeFormatter.ofPattern(HHMM);
		String time = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(getCurrentDate().getTime()), ZoneId.of("Asia/Shanghai")));
		return time;
	}

	public static final String getCurrentYYYYMMDD2(){
		DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYYMMDD2);
		String time = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(getCurrentDate().getTime()), ZoneId.of("Asia/Shanghai")));
		return time;
	}

	/**
	 * 获取当前时间的 分钟和秒数毫秒
	 * @return 16:01
	 */
	public static final String getCurrentHHMMssSSS(){
		DateTimeFormatter df = DateTimeFormatter.ofPattern(HHMMssSSS);
		String time = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(getCurrentDate().getTime()), ZoneId.of("Asia/Shanghai")));
		return time;
	}
	
	/**
	 * 将一个日期加几天 或者减几天
	 * @param date
	 * @param days 添加的数
	 * @return  年-月-日
	 */
	public static final String addDays(Date date,Long days) {
		if(date ==null) {
			date = new Date();
		}
		DateTimeFormatter df= DateTimeFormatter.ofPattern(YYYYMMDD);
		return df.format(dateConvertToLocalDateTime(date).plusDays(days));
	}
	
	public static final String addYears(Date date,Long years) {
		if(date ==null) {
			date = new Date();
		}
		DateTimeFormatter df= DateTimeFormatter.ofPattern(YYYYMMDD);
		return df.format(dateConvertToLocalDateTime(date).plusYears(years));
	}

	/**
	 * 添加多少个月
	 * @param date 需要添加的月份的日期
	 * @param month 多少月
	 * @return
	 */
	public static final String addMonth(Date date,Long month){
		if(date == null){
			date = new Date();
		}
		DateTimeFormatter df= DateTimeFormatter.ofPattern(YYYYMMDD);
		return df.format(dateConvertToLocalDateTime(date).plusMonths(month));
	}

	/**
	 * 对时间添加分钟
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static final String addMinutes(Date date,Long minutes) {
		if(date ==null) {
			date = new Date();
		}
		DateTimeFormatter df= DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);
		return df.format(dateConvertToLocalDateTime(date).plusMinutes(minutes));
	}
	
	/**
	 * 获取两个时间年的差值
	 * @param beforDate
	 * @param afterDate
	 * @return
	 */
	public static final int differYear(Date beforDate,Date afterDate) {
		LocalDate localBeforDate = dateConvertToLocalDate(beforDate);
		LocalDate localAfterDate = dateConvertToLocalDate(afterDate);
		Period period = Period.between(localBeforDate, localAfterDate);
		return period.getYears();
	}
	
	/**
	 * 获取两个时间月的差值
	 * @param beforDate
	 * @param afterDate
	 * @return
	 */
	public static final int differMonth(Date beforDate,Date afterDate) {
		LocalDate localBeforDate = dateConvertToLocalDate(beforDate);
		LocalDate localAfterDate = dateConvertToLocalDate(afterDate);
		Period period = Period.between(localBeforDate, localAfterDate);
		return period.getMonths();
	}
	
	/**
	 * 获取两个时间天的差值
	 * @param beforDate
	 * @param afterDate
	 * @return
	 */
	public static final int differDays(Date beforDate,Date afterDate) {
		LocalDate localBeforDate = dateConvertToLocalDate(beforDate);
		LocalDate localAfterDate = dateConvertToLocalDate(afterDate);
//		Period period = Period.between(localBeforDate, localAfterDate);
		return (int)(localAfterDate.toEpochDay() - localBeforDate.toEpochDay());
	}
    
	/**
	 * 获取两个时间周的差值
	 * @param beforDate
	 * @param afterDate
	 * @return
	 */
	public static final int differWeeks(Date beforDate,Date afterDate) {
		LocalDate localBeforDate = dateConvertToLocalDate(beforDate);
		LocalDate localAfterDate = dateConvertToLocalDate(afterDate);
//		Period period = Period.between(localBeforDate, localAfterDate);
//		System.out.println("aa"+period.getDays());
		return (int)((localAfterDate.toEpochDay() - localBeforDate.toEpochDay())/7);
	}

	/**
	 * 获取日期 某个月有多少天
	 * @param date
	 * @return
	 */
	public static final int getMonthLenth(Date date){
		LocalDate localDate = dateConvertToLocalDate(date);
		return localDate.lengthOfMonth();
	};

	/**
	 * 获取当前是周几 周一 到周日 对应 1-7
	 * @param date
	 * @return
	 */
	public static  final int getDayOfWeek(Date date){
		LocalDate localDate = dateConvertToLocalDate(date);
		return localDate.getDayOfWeek().getValue();
	}
	public static void main(String[] args) {
//		System.out.println(getCurrentMillisecond());
//		System.out.println("秒:"+getCurrentSecond());
//		System.out.println(parseYYYYMMDDHHMMSS("2019-12-12 10:47:47"));
//		System.out.println(formatYYYY_MM_DDHHMMSS(parseYYYYMMDDHHMMSS("2019-12-12 10:47:47").getTime()));
//		System.out.println(DateFormat.getInstance());
//		System.out.println(addDays(new Date(), -2l));
//		System.out.println(differDays(new Date(), parseYYYYMMDD("2019-12-24")));
//		System.out.println(differWeeks(parseYYYYMMDD("2020-01-06"), parseYYYYMMDD("2020-01-14")));
//		System.out.println(getCurrentTimestamp());
//		System.out.println(formatYYYY_MM_DDHHMMSS(getCurrentTimestamp().getTime()));
//		System.out.println(getCurrentYYYYMMDDHHmmssSSS());
//		System.out.println(LocalDate.now().lengthOfMonth());
		System.out.println(getDayOfWeek(DateUtil.parseYYYYMMDD("2020-11-15")));
	}

}
