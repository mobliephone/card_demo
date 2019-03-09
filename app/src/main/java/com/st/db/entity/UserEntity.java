package com.st.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 13971 on 2017/11/8.
 * 用户类
 */

@Entity
public class UserEntity {

    @Id
    private Long  id;

    private String yhdh;

    private String mm;

    private String ip;

    private String mac;

    private String result;

    private String xm;

    private String yzm;

    private String dlms;

    private String glbm;

    private String yhjb;

    @Generated(hash = 1338144269)
    public UserEntity(Long id, String yhdh, String mm, String ip, String mac,
            String result, String xm, String yzm, String dlms, String glbm,
            String yhjb) {
        this.id = id;
        this.yhdh = yhdh;
        this.mm = mm;
        this.ip = ip;
        this.mac = mac;
        this.result = result;
        this.xm = xm;
        this.yzm = yzm;
        this.dlms = dlms;
        this.glbm = glbm;
        this.yhjb = yhjb;
    }

    @Generated(hash = 1433178141)
    public UserEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYhdh() {
        return this.yhdh;
    }

    public void setYhdh(String yhdh) {
        this.yhdh = yhdh;
    }

    public String getMm() {
        return this.mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getXm() {
        return this.xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getYzm() {
        return this.yzm;
    }

    public void setYzm(String yzm) {
        this.yzm = yzm;
    }

    public String getDlms() {
        return this.dlms;
    }

    public void setDlms(String dlms) {
        this.dlms = dlms;
    }

    public String getGlbm() {
        return this.glbm;
    }

    public void setGlbm(String glbm) {
        this.glbm = glbm;
    }

    public String getYhjb() {
        return this.yhjb;
    }

    public void setYhjb(String yhjb) {
        this.yhjb = yhjb;
    }


}
