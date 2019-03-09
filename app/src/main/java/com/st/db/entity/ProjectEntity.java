package com.st.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017-11-09.
 * 项目课程实体类
 */

@Entity
public class ProjectEntity {
    @Id
    private String proId; //自增列 主键
    private String proCode;//项目编码
    private String proName;//项目名称
    private String holdUnit;//主办单位
    private String mane; //负责人姓名
    private String manTel;
    private String holdDays;//举办天数
    private String holdStartDate;//举办开始时间
    private String holdEndDate;//举办结束时间
    private String holdLocation;//举办地点
    private String eduCredit;//授予学分
    private String eduObject;//教学人数
    private String eduObjCount;
    private String addYear;//添加年份
    private String addTime;//添加时间
    private String addUnit;//申报单位（xf_dept表主键）
    private String addUnitMan;//申报单位联系人
    private String addUnitPhone;//申报单位联系电话
    private String subject;//二级学科
    private String subjectThree;//三级学科
    private String holdMode;//举办方式
    private String eduExamMode;//考试方式
    private String eduHours;//教学总学时
    private String eduTheoryHours;//理论实数
    private String eduExperimentHours;//实验实数
    private String holdUnitMan;//主办单位联系人
    private String holdUnitPhone;//主办单位联系电话
    private String proType;//项目类别(国家I类 国家II类 省级I类 省级II类)
    private String proSource;//项目来源（1.省报的项目 2.手工导入的项目 3.临时添加的项目）
    private String evaluate;//该项目是否需要学员评估
    private String proState;//项目状态 0：未举办 1：正在教学中 2：已结束
    private String remark1;//备注
    private String remark2;
    private String remark3;
    private String gxsj;//更新时间


    @Generated(hash = 1149544716)
    public ProjectEntity(String proId, String proCode, String proName,
            String holdUnit, String mane, String manTel, String holdDays,
            String holdStartDate, String holdEndDate, String holdLocation,
            String eduCredit, String eduObject, String eduObjCount, String addYear,
            String addTime, String addUnit, String addUnitMan, String addUnitPhone,
            String subject, String subjectThree, String holdMode,
            String eduExamMode, String eduHours, String eduTheoryHours,
            String eduExperimentHours, String holdUnitMan, String holdUnitPhone,
            String proType, String proSource, String evaluate, String proState,
            String remark1, String remark2, String remark3, String gxsj) {
        this.proId = proId;
        this.proCode = proCode;
        this.proName = proName;
        this.holdUnit = holdUnit;
        this.mane = mane;
        this.manTel = manTel;
        this.holdDays = holdDays;
        this.holdStartDate = holdStartDate;
        this.holdEndDate = holdEndDate;
        this.holdLocation = holdLocation;
        this.eduCredit = eduCredit;
        this.eduObject = eduObject;
        this.eduObjCount = eduObjCount;
        this.addYear = addYear;
        this.addTime = addTime;
        this.addUnit = addUnit;
        this.addUnitMan = addUnitMan;
        this.addUnitPhone = addUnitPhone;
        this.subject = subject;
        this.subjectThree = subjectThree;
        this.holdMode = holdMode;
        this.eduExamMode = eduExamMode;
        this.eduHours = eduHours;
        this.eduTheoryHours = eduTheoryHours;
        this.eduExperimentHours = eduExperimentHours;
        this.holdUnitMan = holdUnitMan;
        this.holdUnitPhone = holdUnitPhone;
        this.proType = proType;
        this.proSource = proSource;
        this.evaluate = evaluate;
        this.proState = proState;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
        this.gxsj = gxsj;
    }

    @Generated(hash = 939074542)
    public ProjectEntity() {
    }

    public String getProId() {
        return this.proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProCode() {
        return this.proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public String getProName() {
        return this.proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getHoldUnit() {
        return this.holdUnit;
    }

    public void setHoldUnit(String holdUnit) {
        this.holdUnit = holdUnit;
    }

    public String getMane() {
        return this.mane;
    }

    public void setMane(String mane) {
        this.mane = mane;
    }

    public String getManTel() {
        return this.manTel;
    }

    public void setManTel(String manTel) {
        this.manTel = manTel;
    }

    public String getHoldDays() {
        return this.holdDays;
    }

    public void setHoldDays(String holdDays) {
        this.holdDays = holdDays;
    }

    public String getHoldStartDate() {
        return this.holdStartDate;
    }

    public void setHoldStartDate(String holdStartDate) {
        this.holdStartDate = holdStartDate;
    }

    public String getHoldEndDate() {
        return this.holdEndDate;
    }

    public void setHoldEndDate(String holdEndDate) {
        this.holdEndDate = holdEndDate;
    }

    public String getHoldLocation() {
        return this.holdLocation;
    }

    public void setHoldLocation(String holdLocation) {
        this.holdLocation = holdLocation;
    }

    public String getEduCredit() {
        return this.eduCredit;
    }

    public void setEduCredit(String eduCredit) {
        this.eduCredit = eduCredit;
    }

    public String getEduObject() {
        return this.eduObject;
    }

    public void setEduObject(String eduObject) {
        this.eduObject = eduObject;
    }

    public String getEduObjCount() {
        return this.eduObjCount;
    }

    public void setEduObjCount(String eduObjCount) {
        this.eduObjCount = eduObjCount;
    }

    public String getAddYear() {
        return this.addYear;
    }

    public void setAddYear(String addYear) {
        this.addYear = addYear;
    }

    public String getAddTime() {
        return this.addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAddUnit() {
        return this.addUnit;
    }

    public void setAddUnit(String addUnit) {
        this.addUnit = addUnit;
    }

    public String getAddUnitMan() {
        return this.addUnitMan;
    }

    public void setAddUnitMan(String addUnitMan) {
        this.addUnitMan = addUnitMan;
    }

    public String getAddUnitPhone() {
        return this.addUnitPhone;
    }

    public void setAddUnitPhone(String addUnitPhone) {
        this.addUnitPhone = addUnitPhone;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectThree() {
        return this.subjectThree;
    }

    public void setSubjectThree(String subjectThree) {
        this.subjectThree = subjectThree;
    }

    public String getHoldMode() {
        return this.holdMode;
    }

    public void setHoldMode(String holdMode) {
        this.holdMode = holdMode;
    }

    public String getEduExamMode() {
        return this.eduExamMode;
    }

    public void setEduExamMode(String eduExamMode) {
        this.eduExamMode = eduExamMode;
    }

    public String getEduHours() {
        return this.eduHours;
    }

    public void setEduHours(String eduHours) {
        this.eduHours = eduHours;
    }

    public String getEduTheoryHours() {
        return this.eduTheoryHours;
    }

    public void setEduTheoryHours(String eduTheoryHours) {
        this.eduTheoryHours = eduTheoryHours;
    }

    public String getEduExperimentHours() {
        return this.eduExperimentHours;
    }

    public void setEduExperimentHours(String eduExperimentHours) {
        this.eduExperimentHours = eduExperimentHours;
    }

    public String getHoldUnitMan() {
        return this.holdUnitMan;
    }

    public void setHoldUnitMan(String holdUnitMan) {
        this.holdUnitMan = holdUnitMan;
    }

    public String getHoldUnitPhone() {
        return this.holdUnitPhone;
    }

    public void setHoldUnitPhone(String holdUnitPhone) {
        this.holdUnitPhone = holdUnitPhone;
    }

    public String getProType() {
        return this.proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getProSource() {
        return this.proSource;
    }

    public void setProSource(String proSource) {
        this.proSource = proSource;
    }

    public String getEvaluate() {
        return this.evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getProState() {
        return this.proState;
    }

    public void setProState(String proState) {
        this.proState = proState;
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
}
