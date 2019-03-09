package com.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者 ：continue
 * 时间 ：2017/3/7 0007.
 * 描述 ：TODO
 */

public class StringUtils {
    /**
     * 判断一个字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty( String str ){
        if( str == null || str.length() == 0 ){
            return true ;
        }
        return false ;
    }

    /**
     * 字符串为空处理
     * @param str
     * @return
     */
    public static String formatStr( String str ){
        if(isEmpty( str )){
            return "" ;
        }
        return str ;
    }

    /**
     * 字符串为空处理
     * @param str
     * @return
     */
    public static String formatStr2No( String str ){
        if(isEmpty( str )){
            return "\"\"" ;
        }
        return str ;
    }

    /**
     * 字符串去除空格
     * @param str
     * @return
     */
    public static String tirm( String str ){
        if(isEmpty( str )){
            return "" ;
        }
        return str.replace(" " ,"") ;
    }

    /**
     *
     * @param str
     * @return
     */
    public static boolean isTrue( String str ){
        if( str.equals("true") || str.equals("TRUE")){
            return true ;
        }
        return false ;
    }

    public static boolean numberMatch(String str){
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(str);
        if ( m.matches() ){
           return true;
        }
        return false;
    }
}
