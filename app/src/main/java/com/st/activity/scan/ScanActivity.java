package com.st.activity.scan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;

import com.framework.base.BaseActivity;
import com.st.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cgw on 2018-01-09.
 * 手机设备扫描枪扫描一维码
 */

public class ScanActivity extends BaseActivity {

    @BindView(R.id.scan_btn)    Button scanBtn;

    private String m_Broadcastname;


    @OnClick(R.id.scan_btn)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.scan_btn:
                // sendKeyEvent(220);
                Intent intent = new Intent();
                intent.setAction("com.barcode.sendBroadcastScan");
                sendBroadcast(intent);
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_scan;
    }

    @Override
    public void onInitView() {
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        m_Broadcastname = "com.barcode.sendBroadcast";// com.barcode.sendBroadcastScan
        intentFilter.addAction(m_Broadcastname);
        registerReceiver(receiver, intentFilter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equals(m_Broadcastname)) {
                String str = arg1.getStringExtra("BARCODE");
                if (!"".equals(str)) {
                    showMessage("扫描结果："+str);
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

}
