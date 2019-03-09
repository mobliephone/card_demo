package com.st.service.request;

/**
 * Created by Administrator on 2017-11-02.
 * 请求地址
 */

public class RequestConfig {

    /** 访问IP*/
//    public static String Url_BaseIP = "http://192.168.2.107:8080/";
    /** 本机IP*/
//    public static String Url_BaseIP = "http://192.168.2.107:8080/";
//    public static String Url_BaseIP = "http://192.168.0.102:8080/";
//    public static String Url_BaseIP = "http://192.168.2.3:8080/";
//    public static String Url_BaseIP = "http://saint-soft.gicp.net:10952/";
    /**青海外网*/
    public static String Url_BaseIP = "http://101.200.235.74:8880/";

    //分页----查询所有人员信息
    public static String Url_QueryListEmployee = "EducationPlatform/xfEmployee.do?method=appQueryListEmployee";

    //分页----查询课题
    public static String Url_QueryListCourse = "EducationPlatform/xfCourse.do?method=appQueryListCourse";

    //上传单条打卡记录
    public static String Url_UpStudyRegistration = "EducationPlatform/xfStudyRegistration.do?method=appInsertXfStudyRegistration";

    //验证医通卡号---根据完整医通卡查询人员信息---不分页
    public static String Url_QueryEmployeeByUserNumber = "EducationPlatform/xfEmployee.do?method=appGetEmployeeByUserNumber";

    //验证医通卡号---根据医通卡号模糊查询人员信息---分页
    public static String Url_QueryEmployeeLikeNumber = "EducationPlatform/xfEmployee.do?method=appGetListEmployeeLikeUserNumber";

    //分页---查询所有课题
    public static String Url_QueryCourseByAll = "EducationPlatform/xfCourse.do?method=appGetCourseByAll";

    //分页---查询打卡记录
    public static String Url_QueryListStudyRegistration = "EducationPlatform/xfStudyRegistration.do?method=appQueryListStudyRegistration";

    //查询并修改人员信息---根据医通卡号
    public static String Url_UpDataEmployeeInfo = "EducationPlatform/xfEmployee.do?method=appUpdateEmployeeInfo";

    //查询所有人员信息
    public static String Url_QueryEmployeeAll = "EducationPlatform/xfEmployee.do?method=appGetEmployeeByAll";

    //更新APP
    public static String Url_UpdataApp = "";

    //项目管理/修改课题信息
    public static String Url_UpdataCourse = "EducationPlatform/xfCourse.do?method=updateAppXfCourse";

}
