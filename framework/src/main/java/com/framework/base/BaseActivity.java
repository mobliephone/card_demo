package com.framework.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.R;
import com.framework.manager.AppManager;
import com.framework.util.L;
import com.framework.util.toast.CustomToast;
import com.framework.view.DialogUIUtils;
import com.framework.view.listener.DialogUIListener;

import butterknife.ButterKnife;

/**
 * 作者 ：continue
 * 时间 ：2017/2/27 0027.
 * 描述 ：Activity 基类
 */

public abstract class BaseActivity extends BaseAppCompatActivity {

    public  Toolbar tbHeadBar;
    private LinearLayout mainContent,searchLinear;
    private EditText searchCourse;
    private TextView tvTitle ;
    public  Context mContext ;
    //弹出加载框
    private Dialog show;
    private boolean isShowNavigation = true;
    private CustomToast customToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this ;
        AppManager.getAppManager().addActivity(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        ViewGroup contentFrameLayout =  findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 15) {
            parentView.setFitsSystemWindows(true);
        }

        setContentView(R.layout.activity_base);
        //禁止软键盘的弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tbHeadBar =  findViewById(R.id.tbHeadBar);
        tbHeadBar.setNavigationIcon(R.drawable.iv_back_white);
        mainContent =  findViewById(R.id.main_content);
        tvTitle = findViewById(R.id.toolbar_title);
        searchCourse = findViewById(R.id.base_search_edit);
        searchLinear = findViewById(R.id.base_search_linear);
        View contentView = getLayoutInflater().inflate(getLayoutResId() , null );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainContent.addView( contentView ,params);
        ButterKnife.bind(this);

        customToast = new CustomToast().getInstance();
        initBase() ;
        onInitView() ;

        initSearch();
    }

    private void initSearch(){
        Drawable drawable = getResources().getDrawable(R.mipmap.search_icon);
        //四个参数分别是设置图片的左、上、右、下的尺寸
        drawable.setBounds(0,0,45,45);
        //这个是选择将图片绘制在EditText的位置，参数对应的是：左、上、右、下
        searchCourse.setCompoundDrawables(drawable,null,null,null);
    }


    private void initBase(){
        setSupportActionBar(tbHeadBar);
        /*以下俩方法设置返回键可用*/
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             /*设置标题文字不可显示*/
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        tbHeadBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.e( " setNavigationOnClickListener ");
                if (isShowNavigation){
                    AppManager.getAppManager().finishActivity((Activity)mContext);
                }
            }
        });

    }

    public Toolbar getBaseToolBar(){
        return tbHeadBar ;
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle( String title){
        tvTitle.setText(title);
    }
    /**
     * 返回当前Activity布局文件的id
     *
     * @return
     */
    public abstract  int getLayoutResId();
    public abstract void onInitView() ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    /**
     * 消息提示
     * @param strMsg
     */
    public void showMessage( String strMsg ){
        customToast.showToast(this,strMsg);
    }

    /**
     * 消息提示
     * @param resourceID
     */
    public void showMessage( int resourceID ){
        customToast.showToast( this,getResources().getString(resourceID));
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

    /**
     * 获得输入框
     * @return
     */
    public EditText getSearchEdit(){
        return searchCourse;
    }

    /**
     * 是否显示搜索框
     * @param isShow
     */
    public void setSearchLinear(boolean isShow){
        if (!isShow){
            searchLinear.setVisibility(View.GONE);
        } else {
            searchLinear.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 物理键退出监听
     * @param activity
     * @param title 标题
     * @param msg 内容
     */
    public void showAlertHorizontal(final Activity activity, CharSequence title, CharSequence msg){
        DialogUIUtils.showAlert(activity, title, msg, "", "", "取消", "退出", false
                , true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {

                    }
                    @Override
                    public void onNegative() {
                        AppManager.getAppManager().AppExit();
                    }
                }).show();
    }

    /**
     * 提示信息
     * @param activity
     * @param title 标题
     * @param msg 内容
     */
    public void showAlertExit(final Activity activity, CharSequence title, CharSequence msg){
        DialogUIUtils.showAlert(activity, title, msg, "", "", "", "", false
                , true, true, new DialogUIListener() {
                    @Override
                    public void onPositive() {

                    }
                    @Override
                    public void onNegative() {

                    }
                }).show();
    }

    /**
     * 是否显示退出按钮
     * @param showNavigation
     */
    public void setShowNavigation(boolean showNavigation) {
        isShowNavigation = showNavigation;
    }

}