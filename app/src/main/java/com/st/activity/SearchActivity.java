package com.st.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.framework.base.BaseActivity;
import com.framework.json.J;
import com.framework.manager.AppCacheManager;
import com.framework.util.DateUtils;
import com.st.CMEApplication;
import com.st.R;
import com.st.activity.adapter.SearchAdapter;
import com.st.activity.project.PunchCardActivity;
import com.st.db.DBUtils;
import com.st.db.dao.CourseEntityDao;
import com.st.db.entity.CourseEntity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cgw on 2017-12-01.
 * 搜索课题界面
 */

public class SearchActivity extends BaseActivity {

    @BindView(R.id.search_courseRecycler)
    RecyclerView couRecyclerView;
    private EditText searchCourse;

    private SearchAdapter mAdapter;
    //课题
    private List<CourseEntity> listEntity;
    //请求控制类
    private MainPresenter mainPresenter;
    private AppCacheManager sharePrenManager;
    private DBUtils dbUtils;
    private CMEApplication app;
    private CourseEntityDao courseDao;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    public void onInitView() {
        ButterKnife.bind(this);
        app = (CMEApplication) getApplication();
        //显示搜索框
        setSearchLinear(true);
        //得到输入框对象
        searchCourse = getSearchEdit();
        mainPresenter = new MainPresenter();
        sharePrenManager = AppCacheManager.getInstance();
        dbUtils = DBUtils.getInstance(this,app);
        courseDao = app.getDaoSeeion().getCourseEntityDao();

        //初始化界面
        initRecycler();
        initListener(searchCourse);//输入框点击事件

    }

    private void initRecycler() {

        mAdapter = new SearchAdapter();
        couRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        couRecyclerView.setAdapter(mAdapter);
        //RecyclerView的item点击事件
        couRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                //打卡点击事件
                Intent intent = new Intent(SearchActivity.this, PunchCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course",listEntity.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                SearchActivity.this.finish();
            }
        });
    }

    //搜索框监听
    private void initListener(EditText searchEdit) {
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("logInfo","beforeTextChanged--->");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("logInfo","onTextChanged--->");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String coursesTask = searchCourse.getText().toString();
                if (!sharePrenManager.popBooleanFromPrefs(com.st.activity.data.Constant.LOGIN_STATE)){
                    outLineCourse(coursesTask);
                } else {
                    loadCourse(coursesTask);
                }
            }
        });
    }



    /**
     * 查询并缓存课题
     *
     * @param coursesTask
     */
    private void loadCourse( String coursesTask) {
        showDialog("正在查询中...");
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("page", "1");
        requestMap.put("rows", "10");
        requestMap.put("coursesTask", coursesTask);
        requestMap.put("startDate",  DateUtils.currentDate());

        mainPresenter.allRequestBase(RequestConfig.Url_QueryListCourse, requestMap, new ICallback() {
            @Override
            public void onFail(Throwable e) {
                dismissDialog();
                Log.d("logInfo", e.toString());

                showMessage("请检查服务器配置！");
            }

            @Override
            public void onSuccess(String string) {
                dismissDialog();
                Log.d("logInfo", string);
                String rows = J.getRows(string);
                //将json转化为List集合
                listEntity = J.getListEntity(rows, CourseEntity.class);
                mAdapter.replaceData(listEntity);
            }
        }, this);
    }


    private void outLineCourse(final String coursesTask){

        Observable.just("")
                .map(new Func1<String,  List<CourseEntity>>() {
                    @Override
                    public List<CourseEntity> call(String mVoid) {
                        return dbUtils.loadCourse(courseDao,coursesTask);
                    }
                }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                .subscribe(new Action1< List<CourseEntity>>() {
                    @Override
                    public void call( List<CourseEntity> courseEntities) {
                        listEntity = courseEntities;

                        mAdapter.replaceData(courseEntities);
                    }
                });
    }
}
