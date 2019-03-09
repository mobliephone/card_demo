package com.st.db.up;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.framework.view.DialogUIUtils;
import com.google.gson.Gson;
import com.st.CMEApplication;
import com.st.db.DBUtils;
import com.st.db.dao.DaoSession;
import com.st.db.dao.StudyRegistrationEntityDao;
import com.st.db.entity.StudyRegistrationEntity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;
import com.st.service.request.RetrofitApi;
import com.st.service.cookies.AddCookiesInterceptor;
import com.st.service.cookies.ReceivedCookiesInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cgw on 2017-11-23.
 * 上传相关数据
 */

public class UpUtils {

    private static UpUtils instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private Dialog show;

    public static UpUtils getInstance(Context context, CMEApplication app){
        if (instance == null) {
            instance = new UpUtils();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = app.getDaoSeeion();
        }
        return instance;
    }


    /**
     * 在线上传单条打卡记录
     * @param entity
     * @param mainPresenter
     */
    public void upCardRecord(StudyRegistrationEntity entity, MainPresenter mainPresenter) {
        show = DialogUIUtils.showLoading(appContext, "正在上传打卡信息...", false, true, false, true).show();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("xh", entity.getXh());
        requestMap.put("courseId", entity.getCourseId());
        requestMap.put("empNumber", entity.getEmpNumber());
        requestMap.put("deptId", entity.getDeptId());
        requestMap.put("deptName", entity.getDeptName());
        requestMap.put("courseName", entity.getCourseName());
        requestMap.put("placeName", entity.getPlaceName());
        requestMap.put("position", entity.getPosition());
        requestMap.put("dakaTime", entity.getDakaTime());
        requestMap.put("addYear", entity.getAddYear());
        requestMap.put("isvalid", entity.getIsvalid());
        requestMap.put("isgrant", entity.getIsgrant());
        requestMap.put("awardstate", entity.getAwardstate());
        requestMap.put("state", entity.getState());
        requestMap.put("addTime", entity.getAddTime());
        requestMap.put("remarks1", entity.getRemarks1());
        requestMap.put("remarks2", entity.getRemarks2());
        requestMap.put("remarks3", entity.getRemarks3());
        requestMap.put("gxsj", entity.getGxsj());

        mainPresenter.allRequestBase(RequestConfig.Url_UpStudyRegistration, requestMap, new ICallback() {
            @Override
            public void onFail(Throwable e) {
                show.dismiss();
                Log.d("logInfo", "打卡信息上传失败！"+e.toString());
                showMessage("打卡信息上传失败！");
            }

            @Override
            public void onSuccess(String string) {
                show.dismiss();
                Log.d("logInfo", "打卡信息上传成功！"+string);

                showMessage("打卡信息上传成功！");
            }
        }, appContext);
    }

    /**
     * 上传打卡记录工具类
     * @param studyDao
     * @param context
     * @param studyRegistrationList
     * @param dbUtils 查询并修改批量打卡记录
     */
    public void upCardRecordList(final StudyRegistrationEntityDao studyDao
            , Context context, final List<StudyRegistrationEntity> studyRegistrationList
            , final DBUtils dbUtils) {

        //将List对象转化为Json格式的String字符串
        Gson gson = new Gson();
        String route = gson.toJson(studyRegistrationList);
        Log.e("logInfo", route);

        //添加请求日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AddCookiesInterceptor(context,"en"))
                .addInterceptor(new ReceivedCookiesInterceptor(context))
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .build();

        //Retrofit进行网络请求
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(RequestConfig.Url_BaseIP)//请求IP
                .client(client)
                .addConverterFactory( GsonConverterFactory.create())//添加Gson解析工厂
                .build();
        //Retrofit请求接口
        RetrofitApi postRoute = retrofit.create(RetrofitApi.class);
        final RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),route);
        Call<ResponseBody> call = postRoute.postStudyRegistration(body);

        show = DialogUIUtils.showLoading(appContext, "正在上传打卡记录...", false, true, false, true).show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                show.dismiss();

                String jsonString = null;
                try {
                    jsonString = new String(response.body().bytes(), "utf-8");
                    if (jsonString.contains("成功")){
                        showMessage("打卡记录上传成功！");
                        if (studyRegistrationList.size() != 0){
                            StudyRegistrationEntity entity = new StudyRegistrationEntity();
                            for (int i = 0; i < studyRegistrationList.size(); i++) {
                                entity = studyRegistrationList.get(i);
                                //修改上传成功后本地数据的Mark
                                entity.setMark("1");
                            }
                            dbUtils.saveStudyList(studyRegistrationList,studyDao);
                        }
                    } else {
                        showMessage("打卡记录上传失败！");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("logInfo", jsonString);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                show.dismiss();

                showMessage("打卡记录上传失败，服务器出现了问题！");
                Log.e("logInfo","t.toString()------>"+t.toString());
            }
        });
    }

    /**
     * 上传打卡记录工具类---UpRecordService后台上传
     * @param studyDao
     * @param context
     * @param studyRegistrationList
     * @param dbUtils 查询并修改批量打卡记录
     */
    public void upCardRecordService(final StudyRegistrationEntityDao studyDao
            , Context context, final List<StudyRegistrationEntity> studyRegistrationList
            , final DBUtils dbUtils) {

        //将List对象转化为Json格式的String字符串
        Gson gson = new Gson();
        String route = gson.toJson(studyRegistrationList);
        Log.e("logInfo", route);

        //添加请求日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AddCookiesInterceptor(context,"en"))
                .addInterceptor(new ReceivedCookiesInterceptor(context))
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .build();

        //Retrofit进行网络请求
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(RequestConfig.Url_BaseIP)//请求IP
                .client(client)
                .addConverterFactory( GsonConverterFactory.create())//添加Gson解析工厂
                .build();
        //Retrofit请求接口
        RetrofitApi postRoute = retrofit.create(RetrofitApi.class);
        final RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),route);
        Call<ResponseBody> call = postRoute.postStudyRegistration(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonString = null;
                try {
                    jsonString = new String(response.body().bytes(), "utf-8");
                    if (jsonString.contains("成功")){
                        Log.e("logInfo", "UpRecordService--->打卡记录上传成功");
                        if (studyRegistrationList.size() != 0){
                            StudyRegistrationEntity entity = new StudyRegistrationEntity();
                            for (int i = 0; i < studyRegistrationList.size(); i++) {
                                entity = studyRegistrationList.get(i);
                                //修改上传成功后本地数据的Mark
                                entity.setMark("1");
                            }
                            dbUtils.saveStudyList(studyRegistrationList,studyDao);
                        }
                    } else {
                        Log.e("logInfo", "UpRecordService--->打卡记录上传失败");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("logInfo", jsonString);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("logInfo", "UpRecordService--->打卡记录上传失败，服务器出现了问题");
                Log.e("logInfo","t.toString()------>"+t.toString());
            }
        });
    }


    public void showMessage( String strMsg ){
        Toast.makeText(appContext , strMsg , Toast.LENGTH_SHORT).show();
    }
}
