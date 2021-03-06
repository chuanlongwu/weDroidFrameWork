package com.wedroid.framework.v2.common;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import com.wedroid.framework.R;

/**
 * 
 * @author 吴传龙
 * @date 2015年12月14日 上午9:47:08
 * @Description: 时间转换工具类
 */
public class WeDroidTimeUtil {
	
	public static long getCurrentTimeMill(){
		return System.currentTimeMillis();
	}
	
	public static String getCurrentDate(){
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	public static String getCurrentTime(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	@SuppressLint("SimpleDateFormat")
	public static long time2Mills(String time){
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return  sdf2.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}
	
	
	/**
	 * GMT时间转换为北京时间<BR/>Fri, 15 Jan 2016 01:36:17 GMT ----> 2016-01-15 09:36:17
	 */
	@SuppressLint("SimpleDateFormat")
	public static String GMT2BJTime(String gmtTime){
		String time = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date ftime = sdf.parse(gmtTime);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = sdf2.format(ftime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(time==null){
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
		return time;
	}
	
	/**
	 * GMT时间转换为北京时间毫秒值<BR/>Fri, 15 Jan 2016 01:36:17 GMT ----> 1452821777000
	 */
	@SuppressLint("SimpleDateFormat")
	public static long GMT2BJTimeMills(String gmtTime){
		long time = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date ftime = sdf.parse(gmtTime);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = sdf2.parse(sdf2.format(ftime)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(time==0){
			time = System.currentTimeMillis();
		}
		return time;
	}
	
	
	
	
	/**
	 * 将long型的毫秒值转换为指定的格式时间
	 * @param time 要转化的毫秒值
	 * @param format 指定的时间的格式
	 * @return 
	 */
	@Deprecated
	public static String switchTime(long time,String format){
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	

	/**
	 * 获取时间的格式
	 * @param skTimePattern  时间格式
	 * @see  com.xy.sk.bean.SKTimePattern
	 * @return 转换后相对应的时间格式   /
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getSKTimePattern(SKTimePattern skTimePattern,long timeMillis){
		return timeMillis != 0 ? 
				new SimpleDateFormat(skTimePattern.pattern()).format(new Date(timeMillis)) 
				:
				new SimpleDateFormat(skTimePattern.pattern()).format(new Date());
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getSKTimePattern(SKTimePattern skTimePattern){
		return new SimpleDateFormat(skTimePattern.pattern()).format(new Date()) ;
	}
	
	/**
	 * 时间转换类,用于显示界面时间时转化用，保存时间不可用此方法
	 * 	eg：当前时间为2014-07-06 17:59:00
	 * 	如果是当天的消息，直接显示时间格式为   09:55
	 *  如果是昨天的消息，显示时间为     昨天 09:55
	 *  如果是同年的消息 ，显示   07-06 17:59
	 *  否则   2014-07-06 17:59
	 * @param time  格式为 以2014-07-06 形式开头
	 * @see  com.WeDroidTimeUtil.sk.util.SKUtil 
	 * <br/> 中的getSKTimePattern(SKTimePattern skTimePattern,long timeMillis){
	 * @return 
	 */
	@Deprecated
	public static String getSKTimePattern(String time,Context context){
		if (time == null){
			return getSKTimePattern(SKTimePattern.MDHM);
		}
		if (time.length()<10 && !time.startsWith("20")){
			return null;
		}
		String year = time.substring(0, 4);
		String month = time.substring(5, 7);
		String day = time.substring(8, 10);
		String t = year+month+day;
		String dayTime = getweek(t,time, context);
		if (!dayTime.equals("")){
			return dayTime;
		}
		if (year.equals(getExtraTime(0).substring(0, 4))){
			// 同年
			return time.substring(5, time.length());
		}
		// 去年的时间
		return time;
	}
	
	/**
	 * 
	 * @param time
	 * @param context
	 * @param shwoMillos 是否精确到秒
	 * @return
	 */
	public static String getSKTimePattern(String time,Context context,boolean shwoMillos){
		if (time == null){
			return getSKTimePattern(SKTimePattern.MDHM);
		}
		if (time.length()<10 && !time.startsWith("20")){
			return null;
		}
		time = getSKTimePattern(time, context);
		// 去年的时间
		return shwoMillos?time:time.substring(0, time.length()-3);
	}
	
	/**
	 * 获取星期
	 */
	public static String getWeek(Context context){
		return getWeek(null,context);
	}
	
	/**
	 * 获取星期
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getWeek(String time,Context context){
		if(time==null){
			time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = null;
		String week = null;
		try {
			parse = dateFormat.parse(time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(parse);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			w = w<0?0:w;
			switch (w) {
			case 0:
				week = getWeekFromR(context, weekDays[6]);
				break;
			case 1:
				week = getWeekFromR(context, weekDays[0]);
				break;
			case 2:
				week = getWeekFromR(context, weekDays[1]);
				break;
			case 3:
				week = getWeekFromR(context, weekDays[2]);
				break;
			case 4:
				week = getWeekFromR(context, weekDays[3]);
				break;
			case 5:
				week = getWeekFromR(context, weekDays[4]);
				break;
			case 6:
				week = getWeekFromR(context, weekDays[5]);
				break;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return week;
	}
	
	private static int[] weekDays = {  com.wedroid.framework.R.string.sk_time_monday, 
		R.string.sk_time_tud, R.string.sk_time_won, R.string.sk_time_thu, R.string.sk_time_fri,
		R.string.sk_time_sta,R.string.sk_time_sun};
	private static String getweek(String t,String time,Context context){
		// 今天是星期几
		int nowDataOfWeek = getWeekOfDate(new Date());
		if (t.equals(getExtraTime(0))){//今天
			return time.substring(11, time.length());
		}else if (t.equals(getExtraTime(-1))){//昨天
			return context.getResources().getString(R.string.sk_time_yesterday)+time.substring(11, time.length());
		}else if (t.equals(getExtraTime(-2))){//前天
			return context.getResources().getString(R.string.sk_time_before_yesterday)+time.substring(11, time.length());
		}else if (t.equals(getExtraTime(-3))){//相隔3天
			if (nowDataOfWeek!=3 && nowDataOfWeek!=2&&nowDataOfWeek!=1&&nowDataOfWeek!=0){
				return getWeekFromR(context,weekDays[nowDataOfWeek-4])+" "+time.substring(11, time.length());
			}
		}else if (t.equals(getExtraTime(-4))){//相隔4天
			if (nowDataOfWeek!=4 &&nowDataOfWeek!=3 && nowDataOfWeek!=2&&nowDataOfWeek!=1&&nowDataOfWeek!=0){
				return getWeekFromR(context,weekDays[nowDataOfWeek-5])+" "+time.substring(11, time.length());
			}
		}else if (t.equals(getExtraTime(-5))){//相隔5天
			if (nowDataOfWeek!=5 &&nowDataOfWeek!=4 &&nowDataOfWeek!=3 && nowDataOfWeek!=2&&nowDataOfWeek!=1&&nowDataOfWeek!=0){
				return getWeekFromR(context,weekDays[nowDataOfWeek-6])+" "+time.substring(11, time.length());
			}
		}else if (t.equals(getExtraTime(-6))){//相隔6天
			if (nowDataOfWeek!=6 &&nowDataOfWeek!=5 &&nowDataOfWeek!=4 &&nowDataOfWeek!=3 && nowDataOfWeek!=2&&nowDataOfWeek!=1&&nowDataOfWeek!=0){
				return getWeekFromR(context,weekDays[nowDataOfWeek-7])+" "+time.substring(11, time.length());
			}
		}
		return "";
	}

	private static String getWeekFromR(Context context,int stringId){
		return context.getResources().getString(stringId);
	}
	
	/**
	 * 获得当前时间是这个星期的第几天
	 * @param dt
	 * @return
	 */
	public static int getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return w<0?0:w;
	}
	
	
	public static String getExtraTime(int amount) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, amount);
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = new DecimalFormat("00").format(c.get(Calendar.MONTH)+1);;
		String day = new DecimalFormat("00").format(c.get(Calendar.DAY_OF_MONTH));;
		return year+month+day;
	}
}
