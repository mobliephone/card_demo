package com.st.persenter;

import android.util.Log;

import rx.functions.Func1;

/**
 * Created by 13971 on 2017/11/8.
 */

public class BaseFunction implements Func1<String, Object> {
    @Override
    public Object call(String s) {
        Log.d("debug",s);
        return s;
    }
}
