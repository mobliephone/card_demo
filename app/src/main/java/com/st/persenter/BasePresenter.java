package com.st.persenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017-11-02.
 * 处理网络请求基类
 */

public class BasePresenter {

    private CompositeSubscription compositeSubscription ;
    //将所有正在处理的Subscription都添加到CompositeSubscription中。统一退出的时候注销观察
    public void addSubscription(Subscription subscription){
        if (compositeSubscription == null){
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    //在界面退出等需要解绑观察者的情况下调用此方法统一解绑，防止Rx造成的内存泄漏
    public void unSubscription(Subscription subscription){
        if (compositeSubscription != null && subscription.isUnsubscribed()){
            compositeSubscription.unsubscribe();
        }
    }
}
