package com.st.service.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.framework.util.NetWorkUtil;
import com.st.service.UpRecordService;

/**
 * Created by cgw on 2017-12-13.
 * 上传打卡记录广播接收器
 */

public class CMEReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //接受广播
        String data = intent.getStringExtra("msg");
        if (null != data && NetWorkUtil.isWifiConnected(context)){
            sendMsgByIntentService(context, data);
        }
    }

    /**
     * 开启上传打卡记录服务
     * @param context
     * @param data
     */
    private void sendMsgByIntentService(Context context, String data) {
        Intent intent = new Intent(context, UpRecordService.class);
        intent.setAction("startUpRecordService");
        intent.putExtra("msg", data);
        context.startService(intent);
    }
}
