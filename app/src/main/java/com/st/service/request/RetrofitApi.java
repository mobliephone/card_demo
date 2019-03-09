package com.st.service.request;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017-11-02.
 * 请求接口参数
 */

public interface RetrofitApi {

    /**
     * 登录
     * @param yhdh
     * @param mm
     * @return
     */
    @GET("EducationPlatform/login.do?method=applogin")
//    @GET("EducationPlatform/appLogin.do?method=login")
    Observable<String> appLogin(@Query("yhdh") String yhdh,@Query("mm") String mm);

    /**
     * 通用请求方法
     * @param url
     * @param map
     * @return
     */
    @GET
    Observable<String> get(@Url String url , @QueryMap Map<String,Object> map )  ;


    /**
     * 上传多条打卡记录
     * @param route
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("EducationPlatform/xfStudyRegistration.do?method=appInsertXfStudyRegistrationList")
    Call<ResponseBody> postStudyRegistration(@Body RequestBody route);//传入的参数为RequestBody

}
