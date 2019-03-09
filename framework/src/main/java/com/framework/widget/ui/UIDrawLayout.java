package com.framework.widget.ui;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;

/**
 * 作者 ：continue
 * 时间 ：2017/2/28 0028.
 * 描述 ：TODO
 */

public class UIDrawLayout extends DrawerLayout{

    public UIDrawLayout(Context context) {
        super(context);
    }

    public UIDrawLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIDrawLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
