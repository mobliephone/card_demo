package com.st.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017-11-01.
 * 人员信息表
 */
@Entity
public class EmployeeEntity {

    /**数据库列类型:String,数据库列注释:用户登录名（医通卡号）**/
    @Id
    private String usernumber;

    /**数据库列类型:String,数据库列注释:旧身份证号**/
    private String userOidId;

    /**数据库列类型:String,数据库列注释:用户姓名**/
    private String username;

    /**数据库列类型:String,数据库列注释:性别**/
    private String sex;

    /**数据库列类型:String,数据库列注释:身份证号**/
    private String idcard;

    /**数据库列类型:String,数据库列注释:QQ**/
    private String qq;

    /**数据库列类型:String,数据库列注释:卡号类型**/
    private String cardtype;

    /**数据库列类型:String,数据库列注释:用户所在部门编号**/
    private String depart;

    /**数据库列类型:String,数据库列注释:用户所在医院编号**/
    private String yiyuan;

    /**数据库列类型:String,数据库列注释:专业**/
    private String zhuanye;

    /**数据库列类型:String,数据库列注释:tb_dictdata表主键(155：正常156：注销)**/
    private String zt;

    /**数据库列类型:String,数据库列注释:学历**/
    private String xueli;

    /**数据库列类型:String,数据库列注释:生日**/
    private String shengri;

    /**数据库列类型:String,数据库列注释:职称**/
    private String zhicheng;

    /**数据库列类型:String,数据库列注释:null**/
    private String zhichenglv;

    /**数据库列类型:String,数据库列注释:民族**/
    private String minzu;

    /**数据库列类型:String,数据库列注释:政资**/
    private String zhengzi;

    /**数据库列类型:String,数据库列注释:职务**/
    private String zhiwu;

    /**数据库列类型:String,数据库列注释:学位**/
    private String xuewei;

    /**数据库列类型:String,数据库列注释:调动状态**/
    private String diaodongzhuangtai;

    /**数据库列类型:String,数据库列注释:null**/
    private String byyuabxiao;

    /**数据库列类型:String,数据库列注释:手机**/
    private String usertel;

    /**数据库列类型:String,数据库列注释:电话**/
    private String userphone;

    /**数据库列类型:String,数据库列注释:邮箱**/
    private String useremail;

    /**数据库列类型:String,数据库列注释:null**/
    private String pfttcNumbers;

    /**数据库列类型:String,数据库列注释:null**/
    private String userZyid;

    /**数据库列类型:String,数据库列注释:照片**/
    private String photo;

    /**数据库列类型:Date,数据库列注释:添加时间**/
    private String addTime;

    /**数据库列类型:String,数据库列注释:办卡状态**/
    private String bankaRes;

    /**数据库列类型:Long,数据库列注释:null**/
    private String xkbs;

    /**数据库列类型:String,数据库列注释:写卡日期**/
    private String xkrq;

    /**数据库列类型:String,数据库列注释:到期时间**/
    private String dqsj;

    /**数据库列类型:Long,数据库列注释:null**/
    private String uresource;

    /**数据库列类型:String,数据库列注释: 备注**/
    private String beizhu;

    /**数据库列类型:String,数据库列注释:缴费状态**/
    private String state;

    /**数据库列类型:Date,数据库列注释:修改时间**/
    private String updatetime;

    @Generated(hash = 830339805)
    public EmployeeEntity(String usernumber, String userOidId, String username,
            String sex, String idcard, String qq, String cardtype, String depart,
            String yiyuan, String zhuanye, String zt, String xueli, String shengri,
            String zhicheng, String zhichenglv, String minzu, String zhengzi,
            String zhiwu, String xuewei, String diaodongzhuangtai,
            String byyuabxiao, String usertel, String userphone, String useremail,
            String pfttcNumbers, String userZyid, String photo, String addTime,
            String bankaRes, String xkbs, String xkrq, String dqsj,
            String uresource, String beizhu, String state, String updatetime) {
        this.usernumber = usernumber;
        this.userOidId = userOidId;
        this.username = username;
        this.sex = sex;
        this.idcard = idcard;
        this.qq = qq;
        this.cardtype = cardtype;
        this.depart = depart;
        this.yiyuan = yiyuan;
        this.zhuanye = zhuanye;
        this.zt = zt;
        this.xueli = xueli;
        this.shengri = shengri;
        this.zhicheng = zhicheng;
        this.zhichenglv = zhichenglv;
        this.minzu = minzu;
        this.zhengzi = zhengzi;
        this.zhiwu = zhiwu;
        this.xuewei = xuewei;
        this.diaodongzhuangtai = diaodongzhuangtai;
        this.byyuabxiao = byyuabxiao;
        this.usertel = usertel;
        this.userphone = userphone;
        this.useremail = useremail;
        this.pfttcNumbers = pfttcNumbers;
        this.userZyid = userZyid;
        this.photo = photo;
        this.addTime = addTime;
        this.bankaRes = bankaRes;
        this.xkbs = xkbs;
        this.xkrq = xkrq;
        this.dqsj = dqsj;
        this.uresource = uresource;
        this.beizhu = beizhu;
        this.state = state;
        this.updatetime = updatetime;
    }

    @Generated(hash = 249963266)
    public EmployeeEntity() {
    }

    public String getUsernumber() {
        return this.usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getUserOidId() {
        return this.userOidId;
    }

    public void setUserOidId(String userOidId) {
        this.userOidId = userOidId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getCardtype() {
        return this.cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getDepart() {
        return this.depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getYiyuan() {
        return this.yiyuan;
    }

    public void setYiyuan(String yiyuan) {
        this.yiyuan = yiyuan;
    }

    public String getZhuanye() {
        return this.zhuanye;
    }

    public void setZhuanye(String zhuanye) {
        this.zhuanye = zhuanye;
    }

    public String getZt() {
        return this.zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getXueli() {
        return this.xueli;
    }

    public void setXueli(String xueli) {
        this.xueli = xueli;
    }

    public String getShengri() {
        return this.shengri;
    }

    public void setShengri(String shengri) {
        this.shengri = shengri;
    }

    public String getZhicheng() {
        return this.zhicheng;
    }

    public void setZhicheng(String zhicheng) {
        this.zhicheng = zhicheng;
    }

    public String getZhichenglv() {
        return this.zhichenglv;
    }

    public void setZhichenglv(String zhichenglv) {
        this.zhichenglv = zhichenglv;
    }

    public String getMinzu() {
        return this.minzu;
    }

    public void setMinzu(String minzu) {
        this.minzu = minzu;
    }

    public String getZhengzi() {
        return this.zhengzi;
    }

    public void setZhengzi(String zhengzi) {
        this.zhengzi = zhengzi;
    }

    public String getZhiwu() {
        return this.zhiwu;
    }

    public void setZhiwu(String zhiwu) {
        this.zhiwu = zhiwu;
    }

    public String getXuewei() {
        return this.xuewei;
    }

    public void setXuewei(String xuewei) {
        this.xuewei = xuewei;
    }

    public String getDiaodongzhuangtai() {
        return this.diaodongzhuangtai;
    }

    public void setDiaodongzhuangtai(String diaodongzhuangtai) {
        this.diaodongzhuangtai = diaodongzhuangtai;
    }

    public String getByyuabxiao() {
        return this.byyuabxiao;
    }

    public void setByyuabxiao(String byyuabxiao) {
        this.byyuabxiao = byyuabxiao;
    }

    public String getUsertel() {
        return this.usertel;
    }

    public void setUsertel(String usertel) {
        this.usertel = usertel;
    }

    public String getUserphone() {
        return this.userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUseremail() {
        return this.useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getPfttcNumbers() {
        return this.pfttcNumbers;
    }

    public void setPfttcNumbers(String pfttcNumbers) {
        this.pfttcNumbers = pfttcNumbers;
    }

    public String getUserZyid() {
        return this.userZyid;
    }

    public void setUserZyid(String userZyid) {
        this.userZyid = userZyid;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddTime() {
        return this.addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getBankaRes() {
        return this.bankaRes;
    }

    public void setBankaRes(String bankaRes) {
        this.bankaRes = bankaRes;
    }

    public String getXkbs() {
        return this.xkbs;
    }

    public void setXkbs(String xkbs) {
        this.xkbs = xkbs;
    }

    public String getXkrq() {
        return this.xkrq;
    }

    public void setXkrq(String xkrq) {
        this.xkrq = xkrq;
    }

    public String getDqsj() {
        return this.dqsj;
    }

    public void setDqsj(String dqsj) {
        this.dqsj = dqsj;
    }

    public String getUresource() {
        return this.uresource;
    }

    public void setUresource(String uresource) {
        this.uresource = uresource;
    }

    public String getBeizhu() {
        return this.beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdatetime() {
        return this.updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }


}
