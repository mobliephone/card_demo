package com.framework.util.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.framework.R;


/**
 * 作者：created by cgw on 2018/4/8 17:37
 * 类注释：
 */
public class CustomToast {

    private CustomToast customToast;
    public synchronized CustomToast getInstance(){
        if (customToast == null){
            customToast = new CustomToast();
        }
        return customToast;
    }


    public void showToast(Context mContext,String message) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(mContext).inflate(R.layout.layout_toast, null);
        //初始化布局控件
        TextView mTextView = (TextView) toastRoot.findViewById(R.id.message);
        ImageView mImageView = (ImageView) toastRoot.findViewById(R.id.imageView);
        //为控件设置属性
        mTextView.setText(message);
        mImageView.setImageResource(R.drawable.bg);
        //Toast的初始化
        Toast toastStart = new Toast(mContext);
        //获取屏幕高度
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (null != wm){
            int height = wm.getDefaultDisplay().getHeight();
            //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
            toastStart.setGravity(Gravity.TOP, 0, height / 2);
            toastStart.setDuration(Toast.LENGTH_SHORT);
            toastStart.setView(toastRoot);
            toastStart.show();
        }

    }


}
