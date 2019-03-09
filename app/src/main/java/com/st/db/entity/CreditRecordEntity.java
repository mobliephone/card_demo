package com.st.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017-11-10.
 * 学分记录表实体类
 */

@Entity
public class CreditRecordEntity {

    @Id
    private String xh;//序号 主键
    private String deptId;//授分部门id
    private String deptName;//授分部门名称
    private String manId;//人员id
    private String manName;//人员名称
    private String manType;//登记学分者类型(1单位  2科室  3个人  4老师)
    private String credit;//学分
    private String hours;//学时
    private String type;//学分类型
    private String proId;//项目id
    private String proName;//项目名称
    private String actId;//活动id
    private String actName;//活动名称
    private String isCheck;//是否核入
    private String checkSay;//核入意见
    private String verifyUnit;//审核单位
    private String verifyUnitName;//审核单位名称
    private String verifyTime;//审核时间
    private String verifyState;//审核状态
    private String verifySay;//审核意见
    private String declareState;//上报状态
    private String declareTime;//上报时间
    private String moduleSource;//学分模块来源(1项目I类  2非项目II类)
    private String functionSource;//学分功能来源(1pos机   2手工登记学院学分  3手工登记老师学分)
    private String printtime;//打印时间
    private String isCertificatePrint;//判断学分证书是否打印(0未打印    1打印)
    private String state;//状态  1正常   -1删除
    private String addTime;//添加时间
    private String addYear;//添加年份
    private String remark1;//备注
    private String remark2;//备注
    private String remark3;//备注
    private String gxsj;//更新时间
    @Generated(hash = 1378351848)
    public CreditRecordEntity(String xh, String deptId, String deptName,
            String manId, String manName, String manType, String credit,
            String hours, String type, String proId, String proName, String actId,
            String actName, String isCheck, String checkSay, String verifyUnit,
            String verifyUnitName, String verifyTime, String verifyState,
            String verifySay, String declareState, String declareTime,
            String moduleSource, String functionSource, String printtime,
            String isCertificatePrint, String state, String addTime, String addYear,
            String remark1, String remark2, String remark3, String gxsj) {
        this.xh = xh;
        this.deptId = deptId;
        this.deptName = deptName;
        this.manId = manId;
        this.manName = manName;
        this.manType = manType;
        this.credit = credit;
        this.hours = hours;
        this.type = type;
        this.proId = proId;
        this.proName = proName;
        this.actId = actId;
        this.actName = actName;
        this.isCheck = isCheck;
        this.checkSay = checkSay;
        this.verifyUnit = verifyUnit;
        this.verifyUnitName = verifyUnitName;
        this.verifyTime = verifyTime;
        this.verifyState = verifyState;
        this.verifySay = verifySay;
        this.declareState = declareState;
        this.declareTime = declareTime;
        this.moduleSource = moduleSource;
        this.functionSource = functionSource;
        this.printtime = printtime;
        this.isCertificatePrint = isCertificatePrint;
        this.state = state;
        this.addTime = addTime;
        this.addYear = addYear;
        this.remark1 = remark1;
        this.remark2 = remark2;
        this.remark3 = remark3;
        this.gxsj = gxsj;
    }
    @Generated(hash = 898056632)
    public CreditRecordEntity() {
    }
    public String getXh() {
        return this.xh;
    }
    public void setXh(String xh) {
        this.xh = xh;
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
    public String getManId() {
        return this.manId;
    }
    public void setManId(String manId) {
        this.manId = manId;
    }
    public String getManName() {
        return this.manName;
    }
    public void setManName(String manName) {
        this.manName = manName;
    }
    public String getManType() {
        return this.manType;
    }
    public void setManType(String manType) {
        this.manType = manType;
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
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
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
    public String getActId() {
        return this.actId;
    }
    public void setActId(String actId) {
        this.actId = actId;
    }
    public String getActName() {
        return this.actName;
    }
    public void setActName(String actName) {
        this.actName = actName;
    }
    public String getIsCheck() {
        return this.isCheck;
    }
    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }
    public String getCheckSay() {
        return this.checkSay;
    }
    public void setCheckSay(String checkSay) {
        this.checkSay = checkSay;
    }
    public String getVerifyUnit() {
        return this.verifyUnit;
    }
    public void setVerifyUnit(String verifyUnit) {
        this.verifyUnit = verifyUnit;
    }
    public String getVerifyUnitName() {
        return this.verifyUnitName;
    }
    public void setVerifyUnitName(String verifyUnitName) {
        this.verifyUnitName = verifyUnitName;
    }
    public String getVerifyTime() {
        return this.verifyTime;
    }
    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }
    public String getVerifyState() {
        return this.verifyState;
    }
    public void setVerifyState(String verifyState) {
        this.verifyState = verifyState;
    }
    public String getVerifySay() {
        return this.verifySay;
    }
    public void setVerifySay(String verifySay) {
        this.verifySay = verifySay;
    }
    public String getDeclareState() {
        return this.declareState;
    }
    public void setDeclareState(String declareState) {
        this.declareState = declareState;
    }
    public String getDeclareTime() {
        return this.declareTime;
    }
    public void setDeclareTime(String declareTime) {
        this.declareTime = declareTime;
    }
    public String getModuleSource() {
        return this.moduleSource;
    }
    public void setModuleSource(String moduleSource) {
        this.moduleSource = moduleSource;
    }
    public String getFunctionSource() {
        return this.functionSource;
    }
    public void setFunctionSource(String functionSource) {
        this.functionSource = functionSource;
    }
    public String getPrinttime() {
        return this.printtime;
    }
    public void setPrinttime(String printtime) {
        this.printtime = printtime;
    }
    public String getIsCertificatePrint() {
        return this.isCertificatePrint;
    }
    public void setIsCertificatePrint(String isCertificatePrint) {
        this.isCertificatePrint = isCertificatePrint;
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
    public String getAddYear() {
        return this.addYear;
    }
    public void setAddYear(String addYear) {
        this.addYear = addYear;
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
