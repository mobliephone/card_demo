package com.st.persenter;

/**
 * Created by 13971 on 2017/11/8.
 * 网络请求回调
 */

public interface ICallback {
     void onFail(Throwable e);
     void onSuccess(String string) ;
}
