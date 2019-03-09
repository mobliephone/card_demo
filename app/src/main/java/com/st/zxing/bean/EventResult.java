package com.st.zxing.bean;

/**
 * Created by Administrator on 2017-11-22.
 * EventBus传递条形码扫描结果
 */

public class EventResult {

    public static final int ZXING_RESLUT = 1000001;//扫描结果

    public int TAG ;
    public String reslut;

    public EventResult() {
    }

    public EventResult(int TAG, String reslut) {
        this.TAG = TAG;
        this.reslut = reslut;
    }


}
