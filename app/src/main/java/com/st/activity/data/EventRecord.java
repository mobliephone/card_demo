package com.st.activity.data;

/**
 * Created by cgw on 2018-01-05.
 * 打卡记录
 */

public class EventRecord {

    public static final int USERNUMBER = 1000001;

    public int TAG;
    public String userNumber;//医通卡号
    public String awardState;//打卡状态：0 正常；1迟到；2其他
    public boolean isWeChat;

    public EventRecord() {
    }

    public EventRecord(int TAG, String userNumber, String awardState, boolean isWeChat) {
        this.TAG = TAG;
        this.userNumber = userNumber;
        this.awardState = awardState;
        this.isWeChat = isWeChat;
    }
}
