package com.st.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.framework.manager.AppCacheManager;
import com.framework.util.NetWorkUtil;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.SettingActivity;
import com.st.activity.data.Constant;
import com.st.db.DBUtils;
import com.st.db.dao.StudyRegistrationEntityDao;
import com.st.db.entity.StudyRegistrationEntity;
import com.st.db.up.UpUtils;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cgw on 2017-12-13.
 * 上传服务
 */

public class UpRecordService extends IntentService{

    private CMEApplication app;
    private StudyRegistrationEntityDao studyDao;
    private DBUtils dbUtils;
    private UpUtils upUtils;
    private AppCacheManager sharePrenManager;

    public UpRecordService() {
        super("upRecord");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("logInfo", "UpRecordService--->onCreate");

        app = (CMEApplication) getApplication();
        dbUtils = DBUtils.getInstance(getApplicationContext(),app);
        upUtils = UpUtils.getInstance(getApplicationContext(),app);

        sharePrenManager = AppCacheManager.getInstance();
        studyDao = app.getDaoSeeion().getStudyRegistrationEntityDao();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d("logInfo", "UpRecordService--->onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("logInfo", "UpRecordService--->onHandleIntent");


        if ("startUpRecordService".equals(intent.getAction())) {
            String msg = intent.getStringExtra("msg");
            Log.d("logInfo","UpRecordService--->Action--msg-->"+msg);

            if (msg.equals(Constant.SETTING_SYNCHRO)){
                Log.d("logInfo","UpRecordService--->Action--->开启自动上传！");
                upLoad();

            } else if(msg.equals(Constant.SETTING_WIFI_SYNCHRO)
                    && NetWorkUtil.isWifiConnected(getApplicationContext())){
                Log.d("logInfo","UpRecordService--->Action--->Wi-Fi自动上传！");
                upLoad();
            } else if (msg.equals(Constant.SETTING_REMIND)
                    && sharePrenManager.popBooleanFromPrefs(Constant.SETTING_REMIND)){
                Log.d("logInfo","UpRecordService--->Action--->开启通知！");
                showNotice();
            }

        } else if (sharePrenManager.popBooleanFromPrefs(Constant.SETTING_SYNCHRO)){
            if (!sharePrenManager.popBooleanFromPrefs(Constant.SETTING_WIFI_SYNCHRO)){
                Log.d("logInfo","UpRecordService--->开启自动上传！");
                upLoad();

            } else if (sharePrenManager.popBooleanFromPrefs(Constant.SETTING_WIFI_SYNCHRO)
                    && NetWorkUtil.isWifiConnected(getApplicationContext())){
                Log.d("logInfo","UpRecordService--->Wi-Fi自动上传！");
                upLoad();
            }
        } else if (sharePrenManager.popBooleanFromPrefs(Constant.SETTING_REMIND)){
            Log.d("logInfo","UpRecordService--->开启通知！");
            showNotice();
        }

    }

    /**
     * 上传
     */
    private void upLoad() {

        if (NetWorkUtil.isWifiConnected(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //查询数据
                    Observable.just("")
                            .map(new Func1<String, List<StudyRegistrationEntity>>() {
                                @Override
                                public List<StudyRegistrationEntity> call(String mVoid) {
                                    return studyDao.queryBuilder().where(StudyRegistrationEntityDao.Properties.Mark.eq("0")).build().list();
                                }
                            }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                            .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                            .subscribe(new Action1<List<StudyRegistrationEntity>>() {
                                @Override
                                public void call(List<StudyRegistrationEntity> tempLifeList) {
                                    if (tempLifeList.size() == 0 || tempLifeList == null) {
//                                            showMessage("本地无最新的打卡记录，无需再进行上传！");
                                        return;
                                    }
                                    //调用上传工具类
                                    upUtils.upCardRecordService(studyDao, getApplication(), tempLifeList, dbUtils);
                                }
                            });
                }
            }).start();
        }
    }

    /**
     * 上传
     */
    private void showNotice() {

        if (NetWorkUtil.isWifiConnected(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //查询数据
                    Observable.just("")
                            .map(new Func1<String, List<StudyRegistrationEntity>>() {
                                @Override
                                public List<StudyRegistrationEntity> call(String mVoid) {
                                    return studyDao.queryBuilder().where(StudyRegistrationEntityDao.Properties.Mark.eq("0")).build().list();
                                }
                            }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                            .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                            .subscribe(new Action1<List<StudyRegistrationEntity>>() {
                                @Override
                                public void call(List<StudyRegistrationEntity> tempLifeList) {
                                    if (tempLifeList.size() == 0 || tempLifeList == null) {
//                                            showMessage("本地无最新的打卡记录，无需再进行上传！");
                                        return;
                                    }
                                    showNotification(getApplicationContext());
                                }
                            });
                }
            }).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("logInfo", "UpRecordService--->onDestroy");
    }

    public void showMessage( String strMsg ){
        Toast.makeText(getApplicationContext() , strMsg , Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示通知
     * @param context
     */
    public void showNotification(Context context) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(context, SettingActivity.class);//将要跳转的界面
        builder.setAutoCancel(true);//点击后消失
        builder.setSmallIcon(R.mipmap.app_icon);//设置通知栏消息标题的头像，大小24*24dp
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
        builder.setTicker("打卡记录未同步");
        builder.setContentText("本地打卡记录未同步到服务器，请及时上传！");//通知内容
        builder.setContentTitle("数据同步");
        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
