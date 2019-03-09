package com.st.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.framework.enity.AppUpdateInfo;
import com.framework.json.J;
import com.framework.manager.AppCacheManager;
import com.framework.manager.UpdateManager;
import com.framework.util.StringUtils;
import com.st.CMEApplication;
import com.st.R;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by cgw on 2017/11/8.
 * 加载界面
 */

public class LauncherActivity extends AppCompatActivity {

    //当前界面
    private View view;
    private CMEApplication app;
    //更新Manger
    private UpdateManager updateManager;
    private Handler mHandler;
    //网络请求控制类
    private MainPresenter mainPresenter;
    private  Animation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = View.inflate(this, R.layout.activity_launcher, null);
        setContentView(view);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ;
            if( hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                } , 1);
            }
        }

        app = (CMEApplication) getApplication();
        app.init();

        mainPresenter = new MainPresenter();
        updateManager = new UpdateManager(this);
        updateManager.setListener(new UpdateManager.IUpdateListener() {
            @Override
            public void onFaild() {
                Message msg = mHandler.obtainMessage();
                msg.what = 1002;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFinish() {
                Message msg = mHandler.obtainMessage();
                msg.what = 1001;
                mHandler.sendMessage(msg);
                showMessage("更新完成！");
            }

            @Override
            public void onCancel() {
                Message msg = mHandler.obtainMessage();
                msg.what = 1003;
                mHandler.sendMessage(msg);
            }
        });
        mHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1001:
                        showMessage("更新完成");
                        start2Next();
                        break ;
                    case 1002:
                        showMessage("更新失败");
                        start2Next();
                        break ;
                    case 1003:
                        showMessage("更新取消");
                        start2Next();
                        break ;
                    case 1005:
                        checkUpdate() ;
                        break ;
                }
            }

        };

        initAmin();
    }

    /**
     * 开启动画
     */
    private void initAmin() {
        animation = AnimationUtils.loadAnimation(this,R.anim.launcher_alpha);
        view.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                Message msg = mHandler.obtainMessage() ;
//                msg.what = 1005 ;
//                mHandler.sendMessage(msg) ;
                start2Next();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    //启动activity
    public void start2Next() {
        Intent intent;
        int intoApp = AppCacheManager.getInstance().popIntFromPrefs("intoApp", -1);
        if (intoApp != 1) {
//            intent = new Intent(LauncherActivity.this, LoginActivity.class);
//            intent.putExtra("isLogin",true);

            intent = new Intent(LauncherActivity.this, CMEActivity.class);
            startActivity(intent);
            // 设置Activity的切换效果
            LauncherActivity.this.finish();
        }
    }

    /**
     * 选择更新
     */
    private void checkUpdate() {
        Map<String,Object> requestMap = new HashMap<>();
        mainPresenter.allRequestBase(RequestConfig.Url_UpdataApp, requestMap, new ICallback() {
            @Override
            public void onFail(Throwable e) {
                start2Next() ;
            }

            @Override
            public void onSuccess(String string) {
                if(!StringUtils.isEmpty( string )){
                    AppUpdateInfo info = J.getEntity(string , AppUpdateInfo.class) ;
                    if( updateManager.checkUpdateInfo(info.getVersionName())){
                        // 更新App
                        String url = RequestConfig.Url_BaseIP + info.getUrl() ;
                        updateManager.setApkUrl( RequestConfig.Url_BaseIP + info.getUrl() );
                        updateManager.updateApp(info);
                    }else{
                        start2Next() ;
                    }
                }
            }
        }, LauncherActivity.this);
    }

    /**
     * Toast
     * @param msg
     */
    private void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
