package com.xiangyang.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LocalDate工具类
 * @author 15138
 *
 */
public class LocalDateUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateUtil.class);
	private static final String YYYYMMDD = "yyyy-MM-dd";
	private static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	private static final String HHMM = "HH:mm";
	
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
			LOGGER.error(e.getMessage());
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
			LOGGER.error(e.getMessage());
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
	 * 获取当前时间的 分钟和秒数
	 * @return 16:01
	 */
	public static final String getCurrentHHMM(){
		DateTimeFormatter df = DateTimeFormatter.ofPattern(HHMM);
		String time = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(getCurrentDate().getTime()), ZoneId.of("Asia/Shanghai")));
		return time;
	}
	
	/**
	 * 将一个日期加几天 或者减几天
	 * @param date 日期
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
	public static void main(String[] args) {
//		System.out.println(getCurrentMillisecond());
//		System.out.println("秒:"+getCurrentSecond());
//		System.out.println(parseYYYYMMDDHHMMSS("2019-12-12 10:47:47"));
//		System.out.println(formatYYYY_MM_DDHHMMSS(parseYYYYMMDDHHMMSS("2019-12-12 10:47:47").getTime()));
//		System.out.println(DateFormat.getInstance());
//		System.out.println(addDays(new Date(), -2l));
//		System.out.println(differDays(new Date(), parseYYYYMMDD("2019-12-24")));
//		System.out.println(differWeeks(parseYYYYMMDD("2020-01-06"), parseYYYYMMDD("2020-01-14")));
		System.out.println(getCurrentHHMM());
	}
}
