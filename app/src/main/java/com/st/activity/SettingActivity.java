package com.st.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.framework.base.BaseActivity;
import com.framework.json.J;
import com.framework.manager.AppCacheManager;
import com.framework.util.DateUtils;
import com.framework.util.NetWorkUtil;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.data.Constant;
import com.st.db.DBUtils;
import com.st.db.cache.CacheUtils;
import com.st.db.dao.CourseEntityDao;
import com.st.db.dao.EmployeeEntityDao;
import com.st.db.dao.StudyRegistrationEntityDao;
import com.st.db.entity.CourseEntity;
import com.st.db.entity.EmployeeEntity;
import com.st.db.entity.StudyRegistrationEntity;
import com.st.db.up.UpUtils;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;
import com.st.view.SwitchView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cgw on 2018-01-02.
 * 设置界面
 */

public class SettingActivity extends BaseActivity{

    @BindView(R.id.setting_synchro)         SwitchView settingUpRecord;
    @BindView(R.id.setting_network)         TextView settingNetwork;
    @BindView(R.id.setting_wifi_synchro)    SwitchView settingWifiUpRecord;
    @BindView(R.id.setting_remind)          SwitchView settingRemind;
    @BindView(R.id.setting_linear_net)      LinearLayout settingLinearNet;
    @BindView(R.id.view_net)                View viewNet;
    @BindView(R.id.setting_linear_wifi)     LinearLayout settingLinearWifi;
    @BindView(R.id.view_wifi)               View viewWifi;
    @BindView(R.id.setting_linear_remind)   LinearLayout settingLinearRemind;
    @BindView(R.id.setting_linear_cacheEmployee)   LinearLayout cacheEmployee;
    @BindView(R.id.setting_linear_cacheCourse)   LinearLayout cacheCourse;
    @BindView(R.id.setting_linear_upRecord)   LinearLayout upRecord;

    @OnClick({R.id.setting_synchro,R.id.setting_wifi_synchro
            ,R.id.setting_remind,R.id.setting_linear_cacheEmployee
            ,R.id.setting_linear_cacheCourse,R.id.setting_linear_upRecord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_synchro:
                if (settingUpRecord.isOpened()) {
                    settingLinearNet.setVisibility(View.VISIBLE);
                    viewNet.setVisibility(View.VISIBLE);
                    settingLinearWifi.setVisibility(View.VISIBLE);
                    viewWifi.setVisibility(View.VISIBLE);

                    sharePrenManager.pushBooleanToPrefs(Constant.SETTING_SYNCHRO, true);

                    //发送广播
                    sendUpRecord(Constant.SETTING_SYNCHRO);
                } else {
                    settingLinearNet.setVisibility(View.GONE);
                    viewNet.setVisibility(View.GONE);
                    settingLinearWifi.setVisibility(View.GONE);
                    viewWifi.setVisibility(View.GONE);

                    settingWifiUpRecord.setOpened(false);

                    sharePrenManager.pushBooleanToPrefs(Constant.SETTING_SYNCHRO, false);
                    sharePrenManager.pushBooleanToPrefs(Constant.SETTING_WIFI_SYNCHRO, false);
                    sharePrenManager.pushBooleanToPrefs(Constant.SETTING_REMIND, false);
                }

                break;
            case R.id.setting_wifi_synchro:
                if (settingWifiUpRecord.isOpened()) {
                    sharePrenManager.pushBooleanToPrefs(Constant.SETTING_WIFI_SYNCHRO, true);

                    //发送广播
                    sendUpRecord(Constant.SETTING_WIFI_SYNCHRO);
                } else {
                    sharePrenManager.pushBooleanToPrefs(Constant.SETTING_WIFI_SYNCHRO, false);
                }
                break;
            case R.id.setting_remind:
                if (settingRemind.isOpened()) {
                    sharePrenManager.pushBooleanToPrefs(Constant.SETTING_REMIND, true);

                    //开启通知
                    sendUpRecord(Constant.SETTING_REMIND);
                } else {
                    sharePrenManager.pushBooleanToPrefs(Constant.SETTING_REMIND, false);
                }
                break;
            //缓存人员信息
            case R.id.setting_linear_cacheEmployee:
                if (NetWorkUtil.isNetWorkAvailable(this)){
                    //缓存人员信息
                    showCacheDialog("缓存人员信息",EMPLOYEE_TYPE);
                } else {
                    showMessage("请在有网络的情况下进行当前操作！");
                }
                break;
            //缓存课题信息
            case R.id.setting_linear_cacheCourse:
                if (NetWorkUtil.isNetWorkAvailable(this)){
                    //缓存课题信息
                    showCacheDialog("缓存课题信息",COURSE_TYPE);
                } else {
                    showMessage("请在有网络的情况下进行当前操作！");
                }
                break;
            //上传打卡记录
            case R.id.setting_linear_upRecord:
                upRecord();
                break;
        }
    }


    //内存缓存管理
    private AppCacheManager sharePrenManager;
    //缓存工具类
    private CacheUtils cacheUtils;
    private CMEApplication app;
    //数据请控制类
    private MainPresenter mainPresenter;
    //人员信息
    private EmployeeEntityDao employeeDao;
    //课题信息
    private CourseEntityDao courseDao;
    //打卡记录
    private StudyRegistrationEntityDao studyDao;
    //上传工具类
    private UpUtils upUtils;
    //查询数据类
    private DBUtils dbUtils;
    //网络请求订阅对象
    private Subscription subscription ;

    private MaterialDialog dialog ;
    private ProgressBar mProgress;
    private int progress;

    private String EMPLOYEE_TYPE = "employee";
    private String COURSE_TYPE = "course";

    private final static int  CACHE_START = 1;
    private final static int  CACHE_CANCEL = 2;
    private int requestNum = 1;

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CACHE_START:
                    if( mProgress != null && dialog != null ){
                        mProgress.setProgress(progress);
                        dialog.setProgress(progress);
                    }
                    break;
                case CACHE_CANCEL:
                    if( dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    public void onInitView() {
        ButterKnife.bind(this);
        setTitle("设置");
        sharePrenManager = AppCacheManager.getInstance();
        app = (CMEApplication) getApplication();
        cacheUtils = CacheUtils.getInstance(getApplicationContext(),app);
        upUtils = UpUtils.getInstance(getApplicationContext(),app);
        dbUtils = DBUtils.getInstance(getApplicationContext(),app);
        mainPresenter = new MainPresenter();
        employeeDao = app.getDaoSeeion().getEmployeeEntityDao();
        courseDao = app.getDaoSeeion().getCourseEntityDao();
        studyDao = app.getDaoSeeion().getStudyRegistrationEntityDao();

        //初始化数据
        initSwitch();
        //自动同步
        boolean synchro = sharePrenManager.popBooleanFromPrefs(Constant.SETTING_SYNCHRO, false);
        settingUpRecord.setOpened(synchro);
        //仅WiFi自动同步
        boolean wifiSynchro = sharePrenManager.popBooleanFromPrefs(Constant.SETTING_WIFI_SYNCHRO, false);
        settingWifiUpRecord.setOpened(wifiSynchro);
        //同步提醒
        boolean remind = sharePrenManager.popBooleanFromPrefs(Constant.SETTING_REMIND, false);
        settingRemind.setOpened(remind);
        //当前网络
        netWork();

        if (settingUpRecord.isOpened()) {
            settingLinearNet.setVisibility(View.VISIBLE);
            viewNet.setVisibility(View.VISIBLE);
            settingLinearWifi.setVisibility(View.VISIBLE);
            viewWifi.setVisibility(View.VISIBLE);

            sharePrenManager.pushBooleanToPrefs(Constant.SETTING_SYNCHRO, true);
        }

    }

    /**
     * 初始化界面
     */
    private void initSwitch() {
        //自动同步
        boolean synchro = sharePrenManager.popBooleanFromPrefs(Constant.SETTING_SYNCHRO, false);
        settingUpRecord.setOpened(synchro);
        //仅WiFi自动同步
        boolean wifiSynchro = sharePrenManager.popBooleanFromPrefs(Constant.SETTING_WIFI_SYNCHRO, false);
        settingWifiUpRecord.setOpened(wifiSynchro);
        //同步提醒
        boolean remind = sharePrenManager.popBooleanFromPrefs(Constant.SETTING_REMIND, false);
        settingRemind.setOpened(remind);
        //当前网络
        netWork();

        if (settingUpRecord.isOpened()) {
            settingLinearNet.setVisibility(View.VISIBLE);
            viewNet.setVisibility(View.VISIBLE);
            settingLinearWifi.setVisibility(View.VISIBLE);
            viewWifi.setVisibility(View.VISIBLE);

            sharePrenManager.pushBooleanToPrefs(Constant.SETTING_SYNCHRO, true);
        }

    }

    /**
     * 当前网络状态
     */
    public void netWork(){

        if (NetWorkUtil.isNetWorkAvailable(SettingActivity.this)){
            if (NetWorkUtil.is3gConnected(SettingActivity.this)){
                settingNetwork.setText("3G/4G");
            } else if (NetWorkUtil.isWifiConnected(SettingActivity.this)){
                settingNetwork.setText("Wi-Fi");
            } else {
                settingNetwork.setText("未知");
            }
        } else {
            settingNetwork.setText("未知");
        }
    }

    /**
     * 发送广播开启上传服务
     * @param type
     */
    private void sendUpRecord(String type) {
        //创建Intent对象，action为ELITOR_CLOCK
        Intent intent = new Intent("startUpRecordService");
        if (type.equals(Constant.SETTING_SYNCHRO)){
            intent.putExtra("msg",Constant.SETTING_SYNCHRO);
        } else if (type.equals(Constant.SETTING_WIFI_SYNCHRO)){
            intent.putExtra("msg",Constant.SETTING_WIFI_SYNCHRO);
        } else if (type.equals(Constant.SETTING_REMIND) && settingRemind.isOpened()){
            intent.putExtra("msg",Constant.SETTING_REMIND);
            //定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
            //也就是发送了action 为"ELITOR_CLOCK"的intent
            PendingIntent pi = PendingIntent.getBroadcast(this,0,intent,0);
            //AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            //设置闹钟从当前时间开始，每隔20min执行一次PendingIntent对象pi，注意第一个参数与第二个参数的关系
            // 5秒后通过PendingIntent pi对象发送广播
            am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),20*1000,pi);
        }
        sendBroadcast(intent);
    }


    /**
     * 上传打卡记录
     */
    private void upRecord() {

        if (NetWorkUtil.isNetWorkAvailable(this)){

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
                                    if (tempLifeList.size() ==0 || tempLifeList == null){
                                        showMessage("本地无最新的打卡记录，无需再进行上传！");
                                    } else {
                                        //调用上传工具类
                                        upUtils.upCardRecordList(studyDao,SettingActivity.this,tempLifeList,dbUtils);
                                    }
                                }
                            });
                }
            }).start();
        } else {
            showMessage("请在有网络的情况下进行当前操作！");
        }
    }

    /**
     * 缓存进度弹出框
     * @param title
     * @param type
     */
    @SuppressLint("ResourceAsColor")
    private void showCacheDialog(String title, final String type){
        new MaterialDialog.Builder(mContext)
                .title(title)
                .content("缓存中...")
                .negativeText("取消")
                .negativeColor(R.color.black)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 100 , true)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog = (MaterialDialog) dialogInterface;
                        mProgress = dialog.getProgressBar() ;
                        if (EMPLOYEE_TYPE.equals(type)){
                            cacheEmployee(employeeDao,String.valueOf(requestNum));
                        } else if (COURSE_TYPE.equals(type)){
                            cacheCourse(courseDao,String.valueOf(requestNum));
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {//取消
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        requestNum = 10000;
                        dialog.dismiss();
                        if( null == subscription ){
                            return ;
                        }
                        mainPresenter.unSubscription( subscription );
                    }
                }).canceledOnTouchOutside( false )
                .show();

    }

    /**
     * 查询并缓存人员信息
     * @param employeeDao
     * @param page
     */
    public void cacheEmployee(final EmployeeEntityDao employeeDao,String page) {

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("page", page);
        requestMap.put("rows", "1000");
        subscription = mainPresenter.allRequestBase(RequestConfig.Url_QueryListEmployee, requestMap, new ICallback() {

            @Override
            public void onFail(Throwable e) {
                Log.d("logInfo", e.toString());
                mHandler.sendEmptyMessage(CACHE_CANCEL);
                showMessage("人员信息缓存失败！");
            }

            @Override
            public void onSuccess(final String string) {
                Log.d("logInfo", string);

                employeeDao.getSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("logInfo", DateUtils.currentTime());

                        String rows = J.getRows(string);
                        //将json转化为List集合
                        List<EmployeeEntity> listEntity = J.getListEntity(rows, EmployeeEntity.class);
                        employeeDao.insertOrReplaceInTx(listEntity);

                        String total = J.getValue(string, "total");
                        if (null == total || "".equals(total)){
                            total = "0";
                        }
                        Double aDouble = Double.valueOf(total);
                        //向下转换
                        double ceil = Math.ceil(aDouble/1000);
                        int allPage = (int) ceil;
                        progress = (int)(((float)requestNum / allPage) * 100);
                        if (allPage+1 >= requestNum){
                            requestNum ++;
                            cacheEmployee(employeeDao,String.valueOf(requestNum));
                            mHandler.sendEmptyMessage(CACHE_START);
                        } else {
                            mHandler.sendEmptyMessage(CACHE_CANCEL);
                            showMessage("人员信息缓存成功！");
                            dialog.setProgress(0);
                            requestNum = 1;
                        }

                    }
                });
            }
        }, this);
    }


    /**
     * 查询并缓存课题
     * @param courseDao
     * @param page
     */
    public void cacheCourse(  final CourseEntityDao courseDao,final String page) {

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("page", page);
        requestMap.put("rows", "100");
        requestMap.put("startDate", DateUtils.currentDate());
        subscription = mainPresenter.allRequestBase(RequestConfig.Url_QueryCourseByAll, requestMap, new ICallback() {
            @Override
            public void onFail(Throwable e) {
                Log.d("logInfo", "课题缓存失败！"+e.toString());
                mHandler.sendEmptyMessage(CACHE_CANCEL);
                showMessage("课题信息缓存失败！");
            }

            @Override
            public void onSuccess( final String string) {
                Log.d("logInfo", "课题缓存成功！"+string);

                courseDao.getSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        //将json转化为List集合

                        String rows = J.getRows(string);
                        final List<CourseEntity> listEntity = J.getListEntity(rows, CourseEntity.class);
                        courseDao.insertOrReplaceInTx(listEntity);

                        String total = J.getValue(string, "total");
                        if (null == total || "".equals(total)){
                            total = "0";
                        }
                        Double aDouble = Double.valueOf(total);
                        //向下转换
                        double ceil = Math.ceil(aDouble/100);
                        int allPage = (int) ceil;
                        progress = (int)(((float)requestNum / allPage) * 100);
                        if (allPage+1 >= requestNum){
                            requestNum ++;
                            cacheCourse(courseDao,String.valueOf(requestNum));
                            mHandler.sendEmptyMessage(CACHE_START);
                        } else {
                            mHandler.sendEmptyMessage(CACHE_CANCEL);
                            showMessage("课题信息缓存成功！");
                            dialog.setProgress(0);
                            requestNum = 1;
                        }


                    }
                });

            }
        }, this);
    }

}
