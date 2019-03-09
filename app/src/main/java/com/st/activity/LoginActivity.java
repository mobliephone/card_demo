package com.st.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.framework.base.BaseActivity;
import com.framework.json.J;
import com.framework.manager.AppCacheManager;
import com.framework.util.NetWorkUtil;
import com.framework.util.StringUtils;
import com.framework.view.DialogUIUtils;
import com.framework.view.listener.DialogUIListener;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.data.Constant;
import com.st.db.DBUtils;
import com.st.db.dao.CourseEntityDao;
import com.st.db.dao.UserEntityDao;
import com.st.db.entity.CourseEntity;
import com.st.db.entity.UserEntity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.UpRecordService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cgw on 2017/11/8.
 *
 * 登录界面
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.userName)
    EditText editUserName;
    @BindView(R.id.password)
    EditText editPassword;
    @BindView(R.id.login_btn)
    Button login_btn;
    @BindView(R.id.rememberPassword)
    CheckBox rememberPassword;

    private CMEApplication app;
    //控制类
    private MainPresenter mainPresenter;
    private static final int BAIDU_READ_PHONE_STATE= 1;
    private Context mContext;
    //弹出加载框
    private Dialog show;
    //查询本地课题信息
    private DBUtils dbUtils;
    //课题数据库类
    private CourseEntityDao courseDao;
    //用户数据库类
    private UserEntityDao userDao;

    private AppCacheManager sharePrenManager;
    private Intent launcherIntent;


    @OnClick({R.id.login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                login();
//                Intent intent = new Intent(LoginActivity.this, FingerActivity.class);
//                startActivity(intent);
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    /**
     * 初始化数据
     */
    @Override
    public void onInitView() {
        ButterKnife.bind(this);
        getBaseToolBar().setVisibility(View.GONE);
        mContext = getApplicationContext();
        //权限设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions( new String[]{ Manifest.permission.READ_PHONE_STATE ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ,Manifest.permission.ACCESS_COARSE_LOCATION},BAIDU_READ_PHONE_STATE );
            }
        }

        app = (CMEApplication) getApplication();
        mainPresenter = new MainPresenter();
        dbUtils = DBUtils.getInstance(this,app);
        courseDao = app.getDaoSeeion().getCourseEntityDao();
        userDao = app.getDaoSeeion().getUserEntityDao();
        sharePrenManager = AppCacheManager.getInstance();
        if(sharePrenManager.popBooleanFromPrefs("remember" , false )){
            rememberPassword.setChecked(true);
            if( rememberPassword.isChecked() ){
                editUserName.setText(sharePrenManager.popStringFromPrefs("userName"));
                editPassword.setText(sharePrenManager.popStringFromPrefs("passWord"));
            }
        }else{
            rememberPassword.setChecked(false);
        }
        //自动登录
        launcherIntent = getIntent();
        if (null != launcherIntent){
            if (launcherIntent.getBooleanExtra("isLogin",false)){
                login();
            }
        }
    }

    /**
     * 登录验证
     */
    private void login() {
        String userName = editUserName.getText().toString();
        String passWord = editPassword.getText().toString();

        if (StringUtils.isEmpty(userName)) {
            showMessage("请输入用户名！");
            return;
        }

        if (StringUtils.isEmpty(passWord)) {
            showMessage("请输入密码！");
            return;
        }

        if (NetWorkUtil.isNetWorkAvailable(this)) {
            showDialog("正在登录中...");
            mainPresenter.login(userName, passWord, new ICallback() {
                @Override
                public void onFail(Throwable e) {
                    dismissDialog();
                    showMessage("登录失败，请检查服务器程序！");
                }

                @Override
                public void onSuccess(String string) {
                    dismissDialog();
                    if (StringUtils.isEmpty(string)) {
                        showMessage("登录失败，请检查账户密码是否输入正确！");
                    } else {
                        String responseCode = J.getValue(string, "code");
                        String message = J.getValue(string,"message");
                        if (J.SUCCESS.equals(responseCode)) {
                            showMessage(message);

                            sharePrenManager.pushStringToPrefs(Constant.USERNAME, editUserName.getText().toString().trim());
                            sharePrenManager.pushStringToPrefs(Constant.PASSWORD , editPassword.getText().toString().trim());

                            if( rememberPassword.isChecked() ){
                                sharePrenManager.pushBooleanToPrefs(Constant.REMEMBER , true );
                            }else{
                                sharePrenManager.pushBooleanToPrefs(Constant.REMEMBER , false );
                            }
                            //在线登录时保存用户基本信息
                            outLineRecord(editUserName.getText().toString().trim(),editPassword.getText().toString().trim());

                            sharePrenManager.pushBooleanToPrefs(Constant.LOGIN_STATE,true);

                            String success = J.getValue(string, "message");
                            showMessage(success);
                            Intent intent = new Intent(LoginActivity.this, CMEActivity.class);
                            startActivity(intent);

                            Intent service = new Intent(LoginActivity.this, UpRecordService.class);
                            startService(service);

                            LoginActivity.this.finish();
                        } else if (J.FAILD.equals(responseCode)){
//                            Toast.makeText(app, message, Toast.LENGTH_SHORT).show();
                            sharePrenManager.pushStringToPrefs(Constant.USERNAME , "");
                            sharePrenManager.pushStringToPrefs(Constant.PASSWORD , "");
                            showMessage(message);
                        }
                    }
                }
            }, mContext);
        } else {

            if (verifyLocalUser(userName,passWord)){
                //离线登录
                showOutLine(LoginActivity.this,"离线登录","是否进入离线操作？");
            }
//            showMessage("请检查网络连接是否正常！");
        }
    }

    /**
     * 在线登录时，保存用户信息
     * @param username
     * @param password
     */
    private void outLineRecord(String username, String password) {
        UserEntity user = new UserEntity();
        user.setYhdh(username);
        user.setMm(password);
        userDao.insertOrReplace(user);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        switch(requestCode) {
            //requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到权限，做相应处理
                    //调用定位SDK应确保相关权限均被授权，否则会引起定位失败
                } else{
                    //没有获取到权限，做特殊处理
                    LoginActivity.this.finish();
                    showMessage("请开启定位功能或存储功能！");
                }
                break;
            default:
                break;
        }
    }


    //查询本地课题信息
    public void getCourseList(){

        showDialog("正在登录中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询数据
                Observable.just("")
                        .map(new Func1<String, List<CourseEntity>>() {
                            @Override
                            public List<CourseEntity> call(String mVoid) {
                                return dbUtils.loadAllCourse(courseDao);
                            }
                        }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                        .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                        .subscribe(new Action1<List<CourseEntity>>() {
                            @Override
                            public void call(List<CourseEntity> tempLifeList) {
                                dismissDialog();

                            }
                        });
            }
        }).start();
    }

    /**
     * 离线登录验证
     * @param username
     * @param password
     * @return
     */
    public boolean verifyLocalUser(String username,String password){

        List<UserEntity> userList = userDao.queryBuilder().where(UserEntityDao.Properties.Yhdh.eq(username)).build().list();
        if (null == userList || 0 == userList.size()){
            showMessage("本地无此用户！");
            return false;
        }

        UserEntity userEntity = userList.get(0);
        if (!password.equals(userEntity.getMm())){
            showMessage("用户密码有误，请输入正确的密码！");
            return false;
        }

        return true;
    }

    /**
     * 提示框
     * @param activity
     * @param title 标题
     * @param msg 内容
     */
    public void showOutLine(final Activity activity, CharSequence title, CharSequence msg){
        DialogUIUtils.showAlert(activity, title, msg, "", "", "取消", "确定", false
                , true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {//取消
                    }
                    @Override
                    public void onNegative() {//确定
                        Intent intent = new Intent(LoginActivity.this, CMEActivity.class);
                        LoginActivity.this.startActivity(intent);
                        sharePrenManager.pushBooleanToPrefs(Constant.LOGIN_STATE,false);
                        LoginActivity.this.finish();
                    }
                }).show();
    }

    /**
     * 提示加载框
     */
    protected void showDialog(String msg){
        show = DialogUIUtils.showLoading(this, msg, false, true, false, true).show();
    }

    /**Dialog
     * 提示加载框
     */
    protected void dismissDialog(){
        DialogUIUtils.dismiss(show);
    }


}
