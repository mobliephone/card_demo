package com.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by continue on 2017/4/10 0010.
 * TODO
 */

public class DateUtils {

    /**
     * 日期转毫秒
     * @param date  yyyy-MM-dd hh:mm:ss.SSS
     * @return
     */
    public static long date2mills(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        long millionSeconds = 0 ;
        try {
            millionSeconds = sdf.parse(date).getTime();
        } catch (ParseException e) {

        }
        return millionSeconds ;
    }

    /**
     * 毫秒转日期
     * @param mills
     * @return
     */
    public static String mills2date( long mills ){
        if( mills == 0 ) return "\"\"" ;
        Date d = new Date(mills);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(d) ;
    }

    /**
     * 毫秒转日期
     * @param mills
     * @return
     */
    public static String mills2date1( long mills ){
        if( mills == 0 ) return "\"\"" ;
        Date d = new Date(mills);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d) ;
    }

    /**
     * String转毫秒
     * @param date
     * @return
     */
    public static long date2mills1(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        long millionSeconds = 0 ;
        try {
            millionSeconds = sdf.parse(date).getTime();
        } catch (ParseException e) {

        }
        return millionSeconds ;
    }


    /**
     * 时间相差秒数
     * @param datebefore
     * @param dateafter
     * @return
     */
    public static int between(String datebefore , String dateafter ){
        long date1 = date2mills1(datebefore);
        long date2 = date2mills1(dateafter);
        int result = (int)(date2 - date1)/(24 * 60 * 60 * 60 ) ;

        return result ;
    }

    /**
     * 以当前日期作为一个
     * @return
     */
    public static String UUID( ){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(d) ;
    }

    /**
     * 当前日期yyyy-MM-dd
     * @return
     */
    public static String currentDate( ){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d) ;
    }

    /**
     * 当前年yyyy
     * @return
     */
    public static String currentYear( ){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(d) ;
    }

    /**
     * 当前日期yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String currentTime( ){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d) ;
    }


    /**
     * 当前时间
     * @return
     */
    public static String currentOnlyTime( ){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(d) ;
    }

    /**
     * 获取前三个月的日期
     * @param date 传入的日期
     * @return
     */
    public static String reduceMonthBefore(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -3);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }

    /**
     * 获取后三个月的日期
     * @param date 传入的日期
     * @return
     */
    public static String reduceMonthAfter(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +3);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }

    /**
     * 时间间隔
     * @param startTime
     * @param endTime
     * @return
     */
    public static long betweenTime(String startTime,String endTime){

        //格式化时间
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nm = (1000 * 60);// 一分钟的毫秒数
        long diff = 0;
        long between = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = CurrentTime.parse(endTime).getTime()
                    - CurrentTime.parse(startTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        between = Math.abs(diff / nm);
        return between;
    }


    /**
     * 比较时间是否是同一天
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate( String date1, String date2) {

        //格式化时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        try {
            cal1.setTime(format.parse(date1));
            cal2.setTime(format.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }


}
