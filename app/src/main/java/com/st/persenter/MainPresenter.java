package com.st.persenter;

import android.content.Context;
import android.widget.Toast;

import com.framework.util.NetWorkUtil;
import com.framework.util.toast.CustomToast;
import com.st.service.request.NetWorkService;

import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-11-02.
 * 网络请求
 */

public class MainPresenter extends BasePresenter{


    public MainPresenter() {
    }

    /**
     * 登陆
     * @param userName
     * @param password
     * @param callback
     * @param context
     */
    public void login(String userName , String password ,  ICallback callback , Context context ){

        BaseSubscriber subscriber = new BaseSubscriber(callback);
        final Subscription subscription = NetWorkService.getInstance().getRetrofitService(context)
                .appLogin(userName, password)
                .map(new BaseFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        addSubscription(subscription);
    }

    /**
     * 通用请求方法
     * @param url 请求地址
     * @param map 请求参数
     * @param callback 请求结果回调
     * @param context 上下文
     */
    public Subscription allRequestBase(String url,Map<String,Object> map,ICallback callback,Context context){

        BaseSubscriber subscriber = new BaseSubscriber(callback);
        final Subscription subscription = NetWorkService.getInstance().getRetrofitService(context)
                .get(url,map)
                .map(new BaseFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        //订阅
        addSubscription(subscription);

        return subscription ;

    }


}
