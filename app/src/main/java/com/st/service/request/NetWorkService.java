package com.st.service.request;

import android.content.Context;
import android.util.Log;

import com.st.service.cookies.AddCookiesInterceptor;
import com.st.service.cookies.ReceivedCookiesInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017-11-02.
 * 网络请求基类
 */

public class NetWorkService {

    private RetrofitApi requestService;

    private static NetWorkService netWorkInstance;
    public static NetWorkService getInstance(){
        synchronized (NetWorkService.class){
            if (netWorkInstance == null){
                netWorkInstance = new NetWorkService();
                return netWorkInstance;
            }
        }
        return netWorkInstance;
    }

    public NetWorkService() {
    }

    public RetrofitApi getRetrofitService(Context context){
        if (requestService == null){
            //添加请求日志
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    //添加统一头部请求
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
//                                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                    .addHeader("Content-Type", "text/html; charset=GBK")
                                    .build();

                            Log.d("logInfo",""+chain.proceed(request));
                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(new AddCookiesInterceptor(context,"en"))
                    .addInterceptor(new ReceivedCookiesInterceptor(context))
                    //设置超时
                    .connectTimeout(45, TimeUnit.SECONDS)
                    .readTimeout(45, TimeUnit.SECONDS)
                    //错误重连
                    .retryOnConnectionFailure(true)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RequestConfig.Url_BaseIP)
                    .client(client)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //支持字符串解析
                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            requestService = retrofit.create(RetrofitApi.class);

        }

        return requestService;
    }
}
