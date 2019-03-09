package com.st.persenter;

import rx.Subscriber;

/**
 * Created by 13971 on 2017/11/8.
 */

public class BaseSubscriber<T> extends Subscriber<T>{

    private ICallback callback ;

    public BaseSubscriber(ICallback callback){
        this.callback = callback ;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if( callback == null ) return ;
        callback.onFail(e);
    }

    @Override
    public void onNext(T string) {
        if( callback == null ) return ;
        callback.onSuccess(string.toString());
    }

}
