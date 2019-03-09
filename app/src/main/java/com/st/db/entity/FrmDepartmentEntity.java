package com.st.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017-11-13.
 * 部门列表
 */

@Entity
public class FrmDepartmentEntity {

    @Id
    private String glbm;//管理部门 主键
    private String bmmc;//部门全称
    private String bmjc;//部门名称
    private String yzmc;//印章名称
    private String bmjb;//部门级别
    private String kclyw;//部门可处理业务
    private String ywlb;//业务类别
    private String fzr;//负责人
    private String lxr;//联系人
    private String lxdh;//联系电话
    private String czhm;//传真号码
    private String lxdz;//联系地址
    private String sjbm;//上级部门
    private String bz;//备注
    private String jlzt;//记录状态 1-正常 0-删除
    private String gxsj;//更新时间
    private String type;//1:单位；0:科室
    private String yhjb;//
    private String fzjg;//
    private String city;//
    @Generated(hash = 2056956338)
    public FrmDepartmentEntity(String glbm, String bmmc, String bmjc, String yzmc,
            String bmjb, String kclyw, String ywlb, String fzr, String lxr,
            String lxdh, String czhm, String lxdz, String sjbm, String bz,
            String jlzt, String gxsj, String type, String yhjb, String fzjg,
            String city) {
        this.glbm = glbm;
        this.bmmc = bmmc;
        this.bmjc = bmjc;
        this.yzmc = yzmc;
        this.bmjb = bmjb;
        this.kclyw = kclyw;
        this.ywlb = ywlb;
        this.fzr = fzr;
        this.lxr = lxr;
        this.lxdh = lxdh;
        this.czhm = czhm;
        this.lxdz = lxdz;
        this.sjbm = sjbm;
        this.bz = bz;
        this.jlzt = jlzt;
        this.gxsj = gxsj;
        this.type = type;
        this.yhjb = yhjb;
        this.fzjg = fzjg;
        this.city = city;
    }
    @Generated(hash = 1147749097)
    public FrmDepartmentEntity() {
    }
    public String getGlbm() {
        return this.glbm;
    }
    public void setGlbm(String glbm) {
        this.glbm = glbm;
    }
    public String getBmmc() {
        return this.bmmc;
    }
    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }
    public String getBmjc() {
        return this.bmjc;
    }
    public void setBmjc(String bmjc) {
        this.bmjc = bmjc;
    }
    public String getYzmc() {
        return this.yzmc;
    }
    public void setYzmc(String yzmc) {
        this.yzmc = yzmc;
    }
    public String getBmjb() {
        return this.bmjb;
    }
    public void setBmjb(String bmjb) {
        this.bmjb = bmjb;
    }
    public String getKclyw() {
        return this.kclyw;
    }
    public void setKclyw(String kclyw) {
        this.kclyw = kclyw;
    }
    public String getYwlb() {
        return this.ywlb;
    }
    public void setYwlb(String ywlb) {
        this.ywlb = ywlb;
    }
    public String getFzr() {
        return this.fzr;
    }
    public void setFzr(String fzr) {
        this.fzr = fzr;
    }
    public String getLxr() {
        return this.lxr;
    }
    public void setLxr(String lxr) {
        this.lxr = lxr;
    }
    public String getLxdh() {
        return this.lxdh;
    }
    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }
    public String getCzhm() {
        return this.czhm;
    }
    public void setCzhm(String czhm) {
        this.czhm = czhm;
    }
    public String getLxdz() {
        return this.lxdz;
    }
    public void setLxdz(String lxdz) {
        this.lxdz = lxdz;
    }
    public String getSjbm() {
        return this.sjbm;
    }
    public void setSjbm(String sjbm) {
        this.sjbm = sjbm;
    }
    public String getBz() {
        return this.bz;
    }
    public void setBz(String bz) {
        this.bz = bz;
    }
    public String getJlzt() {
        return this.jlzt;
    }
    public void setJlzt(String jlzt) {
        this.jlzt = jlzt;
    }
    public String getGxsj() {
        return this.gxsj;
    }
    public void setGxsj(String gxsj) {
        this.gxsj = gxsj;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getYhjb() {
        return this.yhjb;
    }
    public void setYhjb(String yhjb) {
        this.yhjb = yhjb;
    }
    public String getFzjg() {
        return this.fzjg;
    }
    public void setFzjg(String fzjg) {
        this.fzjg = fzjg;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }

}
