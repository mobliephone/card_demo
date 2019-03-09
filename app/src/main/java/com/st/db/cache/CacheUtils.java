package com.st.db.cache;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.framework.json.J;
import com.framework.util.DateUtils;
import com.framework.view.DialogUIUtils;
import com.st.CMEApplication;
import com.st.db.dao.CourseEntityDao;
import com.st.db.dao.DaoSession;
import com.st.db.dao.EmployeeEntityDao;
import com.st.db.entity.CourseEntity;
import com.st.db.entity.EmployeeEntity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-11-23.
 * 缓存相关数据
 */

public class CacheUtils {

    private static CacheUtils instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private Dialog show;

    public static CacheUtils getInstance(Context context, CMEApplication app){
        if (instance == null) {
            instance = new CacheUtils();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = app.getDaoSeeion();
        }
        return instance;
    }


    /**
     * 查询并缓存课题
     * @param mainPresenter
     * @param courseDao
     */
    public void cacheCourse( final MainPresenter mainPresenter, final CourseEntityDao courseDao) {
        show = DialogUIUtils.showLoading(appContext, "正在缓存课题信息...",
                false, true, false, true).show();

        Map<String, Object> requestMap = new HashMap<>();
        mainPresenter.allRequestBase(RequestConfig.Url_QueryCourseByAll, requestMap, new ICallback() {
            @Override
            public void onFail(Throwable e) {
                DialogUIUtils.dismiss(show);
                Log.d("logInfo", "课题缓存失败！"+e.toString());
                showMessage("课题信息缓存失败！");
            }

            @Override
            public void onSuccess( String string) {
//                DialogUIUtils.dismiss(show);

                Log.d("logInfo", "课题缓存成功！"+string);

                //将json转化为List集合
                final List<CourseEntity> listEntity = J.getListEntity(string, CourseEntity.class);
                courseDao.getSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        DialogUIUtils.dismiss(show);

                        courseDao.deleteAll();
                        courseDao.insertOrReplaceInTx(listEntity);
                        showMessage("课题信息缓存成功！");
                    }
                });

            }
        }, appContext);

    }

    /**
     * 查询并缓存人员信息
     * @param mainPresenter
     * @param employeeDao
     */
    public void cacheEmployee(MainPresenter mainPresenter, final EmployeeEntityDao employeeDao) {
        show = DialogUIUtils.showLoading(appContext, "正在缓存人员信息...", false, true, false, true).show();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("page", "1");
        requestMap.put("rows", "1000");
        mainPresenter.allRequestBase(RequestConfig.Url_QueryListEmployee, requestMap, new ICallback() {

            @Override
            public void onFail(Throwable e) {
                Log.d("logInfo", e.toString());

                showMessage("人员信息缓存失败！");
            }

            @Override
            public void onSuccess(final String string) {
                Log.d("logInfo", string);

                employeeDao.getSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        DialogUIUtils.dismiss(show);
                        Log.d("logInfo", DateUtils.currentTime());

                        employeeDao.deleteAll();
                        String rows = J.getRows(string);
                        //将json转化为List集合
                        List<EmployeeEntity> listEntity = J.getListEntity(rows, EmployeeEntity.class);
                        employeeDao.insertOrReplaceInTx(listEntity);
                        showMessage("人员信息缓存成功！");

                    }
                });
            }
        }, appContext);
    }

    public void showMessage( String strMsg ){
        Toast.makeText(appContext , strMsg , Toast.LENGTH_SHORT).show();
    }

}
