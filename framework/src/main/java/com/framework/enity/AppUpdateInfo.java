package com.framework.enity;

/**
 * Created by continue on 2016/11/15.
 */
public class AppUpdateInfo {

    /**
     * title : 新版本已准备好
     * versionCode : 1
     * versionName : 1.0
     * size : 16.3MB
     * time : 2016-11-15 13:00
     * desc : 家电采购通平台，主要为甘肃家电行业的供应商和零售商建立交互平台，便于供应商和零售商之间的沟通和交流
     * url : /apk/jdcgt-android.apk
     */

    private String title;
    private String versionCode;
    private String versionName;
    private String size;
    private String time;
    private String desc;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
