package com.st.utils;

/**
 * Created by cgw on 2018-03-05.
 * 类注释：打卡底部提示信息
 */

public class BottomMsgUtils {

    public BottomMsgUtils() {
    }

    public static String bottomMessage(String type){
        if ("1".equals(type)){//正常打卡
            return "正常，打卡成功！";
        } else if ("2".equals(type)){//两次打卡时间限制
            return "打卡失败，第二次打卡时间间隔必须超过30分钟！";
        } else if ("3".equals(type)){//迟到打卡
            return "迟到，打卡成功！";
        } else if ("4".equals(type)){//非上课时间段打卡
            return "当前时间段不可进行上课打卡！";
        } else if ("5".equals(type)){//无法获取时间
            return "无法获取课题开始与结束时间！";
        } else if ("6".equals(type)){
            return "所选课题出现异常，无法进行上课打卡！";
        } else if ("7".equals(type)){
            return "无法获取课题签到有效时间！";
        } else if ("8".equals(type)){
            return "打卡失败！";
        } else if ("9".equals(type)){
            return "此人已经打过卡了！";
        } else if ("10".equals(type)){
            return "无法识别当前指纹信息！";
        }
        return "";
    }

}
