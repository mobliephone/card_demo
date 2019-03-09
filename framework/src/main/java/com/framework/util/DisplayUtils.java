package com.framework.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.LinearLayout;

/**
 * Created by 13971 on 2017/6/13.
 */

public class DisplayUtils {

    public static int getScreenWidth(Context context){
        int screenWidth = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
        return screenWidth ;
    }

    public static int getViewWidth(final LinearLayout view ){
        view.measure( 0 , 0 );
        int width =view.getMeasuredWidth();
        return width ;
    }


    public static  int dip2px(Context context,float dipValue)
    {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }

}
