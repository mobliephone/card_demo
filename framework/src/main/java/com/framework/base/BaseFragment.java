package com.framework.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.framework.view.DialogUIUtils;

/**
 * 作者 ：continue
 * 时间 ：2017/3/2 0002.
 * 描述 ：TODO
 */

public abstract class BaseFragment extends Fragment{


    //弹出加载框
    private Dialog show;
    protected BaseActivity mActivity;

    //获取布局文件ID
    protected abstract int getLayoutId();

    protected abstract void initView(View view, Bundle savedInstanceState);

    //获取宿主Activity
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
        //禁止软键盘的弹出
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //添加fragment
    protected void addFragment(BaseFragment fragment) {
        if (null != fragment) {
//            getHoldingActivity().addFragment(fragment);
        }
    }

    //移除fragment
    protected void removeFragment() {
//        getHoldingActivity().removeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    /**
     * 消息提示
     * @param strMsg
     */
    public void showMessage( String strMsg ){
        Toast.makeText(getContext() , strMsg , Toast.LENGTH_SHORT).show();
    }

    /**
     * 提示加载框
     */
    protected void showDialog(String msg){
        show = DialogUIUtils.showLoading(getHoldingActivity(), msg, false, true, false, true).show();
    }

    /**Dialog
     * 提示加载框
     */
    protected void dismissDialog(){
        DialogUIUtils.dismiss(show);
    }



}
