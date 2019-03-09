package com.st.activity.base;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.base.BaseActivity;
import com.framework.manager.AppCacheManager;
import com.framework.manager.AppManager;
import com.framework.util.NetWorkUtil;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.data.Constant;
import com.st.activity.project.inter.OnBottomListener;
import com.st.db.entity.GpsData;
import com.framework.util.toast.CustomToast;

/**
 * Created by Administrator on 2017-11-24.
 * 带有定位的Activity基类
 */

public abstract class ABaseActivity extends BaseActivity {


    private GpsData currentLocation ;
    private GpsReceiver gpsReceiver ;
    private CMEApplication app;
    private AppCacheManager shareManager;
    private AppManager appManager;
    private static Dialog bottomDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (CMEApplication) getApplication();

        IntentFilter filter = new IntentFilter() ;
        filter.addAction("gpsService");
        gpsReceiver = new GpsReceiver() ;
        registerReceiver(gpsReceiver , filter ) ;
        shareManager = AppCacheManager.getInstance();
        appManager = AppManager.getAppManager();

        bottomDialog = new Dialog(this, R.style.BottomDialog);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(gpsReceiver);
    }

    private class GpsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("gpsService".equals(intent.getAction()) ){
                Bundle bundle = intent.getExtras() ;
                currentLocation = bundle.getParcelable("gpsinfo") ;
                app.setCurrentLocation(currentLocation);
                shareManager.pushStringToPrefs(Constant.GPS_POSITION,currentLocation.getPosition());
                shareManager.pushStringToPrefs(Constant.GPS_PLACENAME,currentLocation.getPlaceName());
//                Log.e("logInfo" , "-----经纬度-----" + currentLocation.getPosition() ) ;
//                Log.e("logInfo" , "-----地点描述-----" + currentLocation.getPlaceName() ) ;
                getCurrentLocation();
                exitApp();
            }
        }
    }

    /**
     * 获取当前经纬度信息
     * @return
     */
    public GpsData getCurrentLocation() {
        if (null != currentLocation){
            return currentLocation;
        } else {
            GpsData gpsData = new GpsData();
            gpsData.setPosition("未知的经纬度");
            gpsData.setPlaceName("未知的地点");
            return gpsData;
        }

    }

    /**
     * 在线状态切换离线状态App退出
     */
    private void exitApp(){
        if (shareManager.popBooleanFromPrefs(com.st.activity.data.Constant.LOGIN_STATE)
                && !NetWorkUtil.isNetWorkAvailable(this)){
            Toast.makeText(this, "请在离线登录时进行当前操作！", Toast.LENGTH_SHORT).show();
            appManager.AppExit();
        }
    }

    /**
     * 底部弹出框
     * @param msg 提示信息
     * @param msgSize 文字大小
     */
    public void showBottomDialog(String msg,float msgSize){

        View contentView = LayoutInflater.from(this).inflate(R.layout.item_bottom_dialog, null);
        TextView textView = contentView.findViewById(R.id.bottom_text);
        textView.setText(msg);
        textView.setTextSize(msgSize);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = this.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        Window bottomWindow = bottomDialog.getWindow();
        if (null != bottomWindow){
            bottomWindow.setGravity(Gravity.BOTTOM);
            bottomDialog.setCanceledOnTouchOutside(true);
            bottomWindow.setWindowAnimations(R.style.BottomDialog_Animation);
            if (!isFinishing() && !bottomDialog.isShowing() && null != bottomDialog){
                bottomDialog.show();
            }
        }

    }

    public void dismissBottomDialog(){
        if (!isFinishing() && bottomDialog.isShowing() && null != bottomDialog){
            bottomDialog.dismiss();
        }
    }

    public boolean isDialogShowing(){

        if (!isFinishing() && bottomDialog !=null){
            if (bottomDialog.isShowing()){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 底部弹出框监听事件
     * @param onBottomListener
     */
    public void bottomDialogListener(final OnBottomListener onBottomListener){
        bottomDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onBottomListener.onBottomClick();
            }
        });
    }

}
