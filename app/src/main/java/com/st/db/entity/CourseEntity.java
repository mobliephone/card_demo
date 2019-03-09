package com.st.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-11-09.
 * 课题管理实体类
 */

@Entity
public class CourseEntity implements Serializable {

    private static final long serialVersionUID = 7247714666080613254L;

    @Id
    private String courseId; //课程ID 项目id+

    private String proId; //项目id .

    private String proName; //项目名称 .

    private String activityId; //活动id
    //-----------------教师信息-------------------
    private String teacherId; //老师id .

    private String teacherName; //老师姓名 .

    private String teacherTitle; //老师职称 .

    private String teacherDirection; //研究方向 .

    private String teacherUnit; //所在单位 .
    //------------------------------------------
    private String coursesTask; //课题 .

    private String coursesContent; //课程内容 .

    private String credit; //学分   *.

    private String hours; //学时 .

    private String coursesType; //类型（1项目  2活动） *

    private String isdownload; //是否下载 *

    private String method; //教学方法 .

    private String deptId; //部门ID *.

    private String deptName; //部门名称 *.

    private String courseName; //课程名称  .

    private String placeName; //地点名称 .

    private String position; //坐标经纬度 .

    private String state;//状态 *

    private String isbegin; //是否开始：0关闭，1开始 0

    private String startDate; //开始日期 *

    private String endDate; //结束日期 *

    private String validTime; //签到有效时间 09:00-12:00,13:30-17:30

    private String checkNumber; //有效签到次数

    private String autoCheck; //是否自动签到，1：是 0：否

    private String addTime; //添加时间 *

    private String remark1;

    private String remark2;

    private String remark3;

    private String gxsj; //更新时间 *

    private String edu_obj_count; //教学人数

    @Generated(hash = 1724175662)
    public CourseEntity(String courseId, String proId, String proName,
            String activityId, String teacherId, String teacherName,
            String teacherTitle, String teacherDirection, String teacherUnit,
            String coursesTask, String coursesContent, String credit, String hours,
            String coursesType, String isdownload, String method, String deptId,
            String deptName, String courseName, String placeName, String position,
            String state, String isbegin, String startDate, String endDate,
            String validTime, String checkNumber, String autoCheck, String addTime,
            String remark1, String remark2, String remark3, String gxsj,
            String edu_obj_count) {
        this.courseId = courseId;
        this.proId = proId;
        this.proName = proName;
        this.activityId = activityId;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.teacherTitle = teacherTitle;
        this.teacherDirection = teacherDirection;
        this.teacherUnit = teacherUnit;
        this.coursesTask = coursesTask;
        this.coursesContent = coursesContent;
        this.credit = credit;
        this.hours = hours;
        this.coursesType = coursesType;
        this.isdownload = isdownload;
        this.method = method;
        this.deptId = deptId;
        this.deptName = deptName;
        this.courseName = courseName;
        this.placeName = placeName;
        this.position = position;
        this.state = state;
        this.isbegin = isbegin;
        this.startDate = startDate;
        this.endDate = endDate;
        this.validTime = validTime;
        this.checkNumber = checkNumber;
        this.autoCheck = autoCheck;
        this.addTime = addTime;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
        this.gxsj = gxsj;
        this.edu_obj_count = edu_obj_count;
    }

    @Generated(hash = 483818505)
    public CourseEntity() {
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProId() {
        return this.proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return this.proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getActivityId() {
        return this.activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getTeacherId() {
        return this.teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return this.teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherTitle() {
        return this.teacherTitle;
    }

    public void setTeacherTitle(String teacherTitle) {
        this.teacherTitle = teacherTitle;
    }

    public String getTeacherDirection() {
        return this.teacherDirection;
    }

    public void setTeacherDirection(String teacherDirection) {
        this.teacherDirection = teacherDirection;
    }

    public String getTeacherUnit() {
        return this.teacherUnit;
    }

    public void setTeacherUnit(String teacherUnit) {
        this.teacherUnit = teacherUnit;
    }

    public String getCoursesTask() {
        return this.coursesTask;
    }

    public void setCoursesTask(String coursesTask) {
        this.coursesTask = coursesTask;
    }

    public String getCoursesContent() {
        return this.coursesContent;
    }

    public void setCoursesContent(String coursesContent) {
        this.coursesContent = coursesContent;
    }

    public String getCredit() {
        return this.credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getHours() {
        return this.hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getCoursesType() {
        return this.coursesType;
    }

    public void setCoursesType(String coursesType) {
        this.coursesType = coursesType;
    }

    public String getIsdownload() {
        return this.isdownload;
    }

    public void setIsdownload(String isdownload) {
        this.isdownload = isdownload;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsbegin() {
        return this.isbegin;
    }

    public void setIsbegin(String isbegin) {
        this.isbegin = isbegin;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getValidTime() {
        return this.validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getCheckNumber() {
        return this.checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getAutoCheck() {
        return this.autoCheck;
    }

    public void setAutoCheck(String autoCheck) {
        this.autoCheck = autoCheck;
    }

    public String getAddTime() {
        return this.addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getRemark1() {
        return this.remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return this.remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return this.remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getGxsj() {
        return this.gxsj;
    }

    public void setGxsj(String gxsj) {
        this.gxsj = gxsj;
    }

    public String getEdu_obj_count() {
        return this.edu_obj_count;
    }

    public void setEdu_obj_count(String edu_obj_count) {
        this.edu_obj_count = edu_obj_count;
    }
}
