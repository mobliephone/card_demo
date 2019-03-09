package com.st.activity.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.json.J;
import com.framework.manager.AppCacheManager;
import com.framework.util.DateUtils;
import com.framework.util.StringUtils;
import com.framework.view.DialogUIUtils;
import com.framework.view.bean.TieBean;
import com.framework.view.listener.DialogUIItemListener;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.data.Constant;
import com.st.activity.data.EventRecord;
import com.st.db.DBUtils;
import com.st.db.dao.EmployeeEntityDao;
import com.st.db.dao.StudyRegistrationEntityDao;
import com.st.db.entity.CourseEntity;
import com.st.db.entity.EmployeeEntity;
import com.st.db.entity.GpsData;
import com.st.db.entity.StudyRegistrationEntity;
import com.st.nfc.BasicActivity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.framework.view.DialogUIUtils.showToast;
import static com.st.nfc.activity.KeyMapCreatorActivity.EXTRA_KEYS_DIR;

/**
 * Created by cgw on 2017-12-01.
 * 打卡和查看课题信息界面
 */

public class PunchCardActivity extends BasicActivity {

    @BindView(R.id.pro_CourseName)    TextView proCourseName;
    @BindView(R.id.pro_startDate)    TextView proStartDate;
    @BindView(R.id.pro_holdUnit)    TextView proHoldUnit;
    @BindView(R.id.button_punchCard)    Button punchCard;
    @BindView(R.id.text_location)    TextView textLocation;
    @BindView(R.id.punchCard_Linear)    LinearLayout punchCardLinear;
    @BindView(R.id.local_punchCard)    LinearLayout localLinear;


    //课题实体类
    private CourseEntity courseBean;
    private CMEApplication app;
    private MainPresenter mainPresenter;
    private StudyRegistrationEntityDao studyDao;
    private DBUtils dbUtils;
    private EmployeeEntityDao employeeDao;
    private AppCacheManager sharePrenManager;


    @OnClick({R.id.punchCard_Linear,R.id.button_punchCard,R.id.local_punchCard})
    public void onClick(View view){
//        Intent intent = null;
        switch (view.getId()){
            case R.id.punchCard_Linear:
                // 查看课题点击事件
                showBottomDialog();
//                intent = new Intent(PunchCardActivity.this,CourseInfoActivity.class);

                break;
            case R.id.button_punchCard:
                if (verifyCourse()){
                    //打卡点击事件
                    Intent intent = new Intent(this, PayByCardResultActivity.class);
                    intent.putExtra(EXTRA_KEYS_DIR,
                            CMEApplication.getFileFromStorage(CMEApplication.HOME_DIR + "/" +
                                    CMEApplication.KEYS_DIR).getAbsolutePath());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course",courseBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                break;
            case R.id.local_punchCard:
                showMessage("当前位置信息已刷新！");
                nHandler.sendEmptyMessage(2);
                break;
        }

    }

    public void showBottomDialog() {

        List<TieBean> strings = new ArrayList<TieBean>();
        strings.add(new TieBean("查看课题信息"));
        strings.add(new TieBean("查看打卡记录"));
        DialogUIUtils.showSheet(PunchCardActivity.this, strings, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {

            Intent intent = null;
            @Override
            public void onItemClick(CharSequence text, int position) {
                if (0 == position){
                    intent = new Intent(PunchCardActivity.this,CourseInfoActivity.class);
                } else if (1 == position){
                    intent = new Intent(PunchCardActivity.this,CardRecordActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("course",courseBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onBottomBtnClick() {
//                showMessage("取消");
            }
        }).show();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_punch_card;
    }

    @Override
    public void onInitView() {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        app = (CMEApplication) getApplication();
        setTitle("课题信息");
        mainPresenter = new MainPresenter();
        studyDao = app.getDaoSeeion().getStudyRegistrationEntityDao();
        dbUtils = DBUtils.getInstance(getApplicationContext(),app);
        employeeDao = app.getDaoSeeion().getEmployeeEntityDao();
        sharePrenManager = AppCacheManager.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (null != bundle){
            courseBean = (CourseEntity) bundle.getSerializable("course");
        }
        new TimeThread().start(); //启动新的线程
        initData(courseBean);//初始化数据


    }

    private void initData(CourseEntity entity) {

        proCourseName.setText("课程名称："+entity.getCourseName());
        proStartDate.setText("开始时间："+entity.getStartDate().substring(0,11));
        proHoldUnit.setText("上课地点："+entity.getPlaceName());

        //离线状态
        if (!sharePrenManager.popBooleanFromPrefs(com.st.activity.data.Constant.LOGIN_STATE)){
            GpsData gpsData = new GpsData();
            gpsData.setPosition(sharePrenManager.popStringFromPrefs(Constant.GPS_POSITION));
            gpsData.setPlaceName(sharePrenManager.popStringFromPrefs(Constant.GPS_PLACENAME));
            app.setCurrentLocation(gpsData);
        }

        verifyCourse();

    }


    private boolean verifyCourse(){
        try {

            if (StringUtils.isEmpty(courseBean.getCheckNumber())){
                showBottomDialog("所选课题出现异常，不可进行打卡！",20f);
                return false;
            }

            if ("null/null/null/null".contains(app.getCurrentLocation().getPlaceName())
                    || "".equals(app.getCurrentLocation().getPlaceName())){
                textLocation.setText("请点击刷新");
//                showBottomDialog("无法获取当前位置信息，不可进行打卡！",20f);
//                return false;
            }
//            else {

            textLocation.setText(app.getCurrentLocation().getPlaceName());

//                return true;
//            }
            return true;
        }catch (Exception e){
            textLocation.setText("无法获取当前位置信息");
//            return false;
            return true;
        }

    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    nHandler.sendMessage(msg);// 每隔1秒发送一个msg给nHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    private Handler nHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();//获取系统时间
                    Date date = new Date(sysTime);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    punchCard.setText("打卡"+'\n'+format.format(date)); //更新时间
                    break;
                case 2:
                    try {
                        Thread.sleep(50);
                        if ("null/null/null/null".contains(app.getCurrentLocation().getPlaceName())
                                || "".equals(app.getCurrentLocation().getPlaceName())){
                            textLocation.setText("请点击刷新");
                        }
                        textLocation.setText(app.getCurrentLocation().getPlaceName());
                    } catch (InterruptedException | NullPointerException e) {
                        e.printStackTrace();
                        textLocation.setText("无法获取当前位置信息");
                    }
                    break;
                default:
                    break;

            }
        }
    };

    /**'
     * 接收打卡信息
     * @param record
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshRecord(EventRecord record){
        switch (record.TAG){
            case EventRecord.USERNUMBER:
                if (!sharePrenManager.popBooleanFromPrefs(Constant.LOGIN_STATE)){
                    VerifyUserNumber(record.userNumber,courseBean,record.awardState,record.isWeChat);
                } else {
                    searchEmployee(record.userNumber,courseBean,record.awardState,record.isWeChat);
                }
                break;
        }
    }

    /**
     * 查询人员信息
     */
    public void searchEmployee(final String userNumber, final CourseEntity bean, final String awardState, final boolean isWeChat) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("userNumber",userNumber);
                mainPresenter.allRequestBase(RequestConfig.Url_QueryEmployeeByUserNumber, requestMap, new ICallback() {
                    @Override
                    public void onFail(Throwable e) {
                        Log.d("logInfo", e.toString());
                        showMessage("人员信息异常，无法上传打卡记录！");
                    }

                    @Override
                    public void onSuccess(String string) {
                        Log.d("logInfo", string);
                        //将json转化为List集合
                        //如果当前界面存在，打卡成功显示底部弹出框
                        EmployeeEntity entity = J.getEntity(string, EmployeeEntity.class);
                        String usernumber = entity.getUsernumber();
                        String username = entity.getUsername();
                        upData(usernumber,username,bean,awardState,isWeChat);

                    }
                }, getApplicationContext());
            }
        }).start();

    }

    /**
     * 验证医通卡号
     * @param userNumber 医通卡号
     * @return
     */
    private void VerifyUserNumber(final String userNumber, final CourseEntity bean
            , final String awardState,final boolean isWeChat){

        Observable.just("")
                .map(new Func1<String,  List<EmployeeEntity>>() {
                    @Override
                    public List<EmployeeEntity> call(String mVoid) {
                        return dbUtils.likeNumberEmployee(employeeDao,userNumber);
                    }
                }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                .subscribe(new Action1< List<EmployeeEntity>>() {
                    @Override
                    public void call( List<EmployeeEntity> courseEntities) {
                        if ( courseEntities.size() == 1){
                            upData(userNumber,courseEntities.get(0).getUsername(),bean,awardState,isWeChat);
                        } else {
                            showMessage("人员信息异常，无法上传打卡记录！");
                        }
                    }
                });
    }

    /**
     * 根据医通卡，设置数据
     * @param empNumber
     * @param username
     */
    private void upData(final String empNumber,final String username,final CourseEntity bean
            ,final String awardState,final boolean isWeChat) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询数据
                Observable.just("")
                        .map(new Func1<String,  List<StudyRegistrationEntity>>() {
                            @Override
                            public  List<StudyRegistrationEntity> call(String mVoid) {
                               return dbUtils.loadStudyByEmp(studyDao,empNumber,bean.getCourseId());
                            }
                        }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                        .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                        .subscribe(new Action1< List<StudyRegistrationEntity>>() {
                            @Override
                            public void call( List<StudyRegistrationEntity> entities) {
                                StudyRegistrationEntity dbEntity  ;

                                if (bean != null){
                                    if (entities.size() > 0 ){
                                        dbEntity = entities.get(0);
                                        if (bean.getCheckNumber().equals("2") && dbEntity.getIsvalid().equals("1")){
                                            if (dbEntity.getMark().equals("1")){
                                                dbEntity.setMark("0");
                                            }
                                            dbEntity.setIsvalid("2");
                                        }

                                        String placeName = app.getCurrentLocation().getPlaceName();
                                        String position = app.getCurrentLocation().getPosition();
                                        dbEntity.setPlaceName(placeName);
                                        dbEntity.setPosition(position);

//                                        dbEntity.setDakaTime(DateUtils.currentTime());
                                        dbEntity.setAddYear(DateUtils.currentYear());
                                        dbEntity.setAddTime(DateUtils.currentTime());
                                        dbEntity.setGxsj(DateUtils.currentTime());

                                        studyDao.update(dbEntity);
                                    } else if (entities.size() == 0 ) {
                                        dbEntity = new StudyRegistrationEntity();
                                        dbEntity.setXh(DateUtils.UUID());
                                        dbEntity.setCourseId(bean.getCourseId());
                                        dbEntity.setEmpNumber(empNumber);
                                        dbEntity.setDeptId(bean.getDeptId());
                                        dbEntity.setDeptName(bean.getDeptName());
                                        dbEntity.setCourseName(bean.getCourseName());

                                        String placeName = app.getCurrentLocation().getPlaceName();
                                        String position = app.getCurrentLocation().getPosition();
                                        dbEntity.setPlaceName(placeName);
                                        dbEntity.setPosition(position);

                                        dbEntity.setDakaTime(DateUtils.currentTime());
                                        dbEntity.setAddYear(DateUtils.currentYear());
                                        if (isWeChat){//第二次打卡
                                            dbEntity.setIsvalid("2");
                                        } else {
                                            dbEntity.setIsvalid("1");
                                        }
                                        dbEntity.setIsgrant("0");
                                        dbEntity.setAwardstate(awardState);
                                        dbEntity.setState("0");
                                        dbEntity.setAddTime(DateUtils.currentTime());
                                        dbEntity.setRemarks1(username);
//                                        dbEntity.setRemarks2("无");
//                                        dbEntity.setRemarks3("无");
                                        dbEntity.setGxsj(DateUtils.currentTime());
                                        dbEntity.setMark("0");
                                        //将数据存入数据库
                                        studyDao.insertOrReplace(dbEntity);
                                    }
                                }

                            }
                        });
            }
        }).start();
    }


}
