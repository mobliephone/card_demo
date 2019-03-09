package com.st.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017-11-24.
 * 签到记录登记表(新)
 */

@Entity
public class StudyRegistrationEntity  {

    @Id
    /**数据库列类型:String,数据库列注释:序号**/
    private String xh;

    /**数据库列类型:BigDecimal,数据库列注释:课程id**/
    private String courseId;

    /**数据库列类型:String,数据库列注释:学员医通卡号xf_employee表user_number字段**/
    private String empNumber;

    /**数据库列类型:BigDecimal,数据库列注释:单位id**/
    private String deptId;

    /**数据库列类型:String,数据库列注释:部门名称**/
    private String deptName;

    /**数据库列类型:String,数据库列注释:课程名称**/
    private String courseName;

    /**数据库列类型:String,数据库列注释:地点名称**/
    private String placeName;

    /**数据库列类型:String,数据库列注释:坐标经纬度**/
    private String position;

    /**数据库列类型:Date,数据库列注释:打卡登记时间**/
    private String dakaTime;

    /**数据库列类型:String,数据库列注释:打卡登记年份**/
    private String addYear;

    /**数据库列类型:Long,数据库列注释:打卡是否有效**/
    private String isvalid;

    /**数据库列类型:Long,数据库列注释:是否已受分**/
    private String isgrant;

    /**数据库列类型:String,数据库列注释:打卡状态：0 正常；1迟到；2其他**/
    private String awardstate;

    /**数据库列类型:String,数据库列注释:打卡来源：0 pos打卡；1 微信刷卡**/
    private String state;

    /**数据库列类型:Date,数据库列注释:添加时间（上传时间）**/
    private String addTime;

    /**数据库列类型:String,数据库列注释:null**/
    private String remarks1;

    /**数据库列类型:String,数据库列注释:null**/
    private String remarks2;

    /**数据库列类型:String,数据库列注释:null**/
    private String remarks3;

    /**数据库列类型:Date,数据库列注释:更新时间**/
    private String gxsj;

    /**本地数据上传成功标识**/
    private String mark;

    @Generated(hash = 1533919084)
    public StudyRegistrationEntity(String xh, String courseId, String empNumber,
            String deptId, String deptName, String courseName, String placeName,
            String position, String dakaTime, String addYear, String isvalid,
            String isgrant, String awardstate, String state, String addTime,
            String remarks1, String remarks2, String remarks3, String gxsj,
            String mark) {
        this.xh = xh;
        this.courseId = courseId;
        this.empNumber = empNumber;
        this.deptId = deptId;
        this.deptName = deptName;
        this.courseName = courseName;
        this.placeName = placeName;
        this.position = position;
        this.dakaTime = dakaTime;
        this.addYear = addYear;
        this.isvalid = isvalid;
        this.isgrant = isgrant;
        this.awardstate = awardstate;
        this.state = state;
        this.addTime = addTime;
        this.remarks1 = remarks1;
        this.remarks2 = remarks2;
        this.remarks3 = remarks3;
        this.gxsj = gxsj;
        this.mark = mark;
    }

    @Generated(hash = 1956148507)
    public StudyRegistrationEntity() {
    }

    public String getXh() {
        return this.xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getEmpNumber() {
        return this.empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDakaTime() {
        return this.dakaTime;
    }

    public void setDakaTime(String dakaTime) {
        this.dakaTime = dakaTime;
    }

    public String getAddYear() {
        return this.addYear;
    }

    public void setAddYear(String addYear) {
        this.addYear = addYear;
    }

    public String getIsvalid() {
        return this.isvalid;
    }

    public void setIsvalid(String isvalid) {
        this.isvalid = isvalid;
    }

    public String getIsgrant() {
        return this.isgrant;
    }

    public void setIsgrant(String isgrant) {
        this.isgrant = isgrant;
    }

    public String getAwardstate() {
        return this.awardstate;
    }

    public void setAwardstate(String awardstate) {
        this.awardstate = awardstate;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddTime() {
        return this.addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getRemarks1() {
        return this.remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public String getRemarks2() {
        return this.remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getRemarks3() {
        return this.remarks3;
    }

    public void setRemarks3(String remarks3) {
        this.remarks3 = remarks3;
    }

    public String getGxsj() {
        return this.gxsj;
    }

    public void setGxsj(String gxsj) {
        this.gxsj = gxsj;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}
