package com.st.activity.project;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.framework.base.BaseActivity;
import com.st.CMEApplication;
import com.st.R;
import com.st.db.dao.StudyRegistrationEntityDao;
import com.st.db.entity.CourseEntity;
import com.st.db.entity.StudyRegistrationEntity;
import com.st.activity.adapter.CardRecordAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cgw on 2017-12-12.
 * 打卡记录
 */

public class CardRecordActivity extends BaseActivity {

    @BindView(R.id.cardRecord_Recycler)    RecyclerView cardRecordRecycler;

    private CardRecordAdapter recordAdapter;
    private CMEApplication app;
    //打卡记录
    private StudyRegistrationEntityDao studyDao;
    //课题对象
    private CourseEntity courseBean;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_card_record;
    }

    @Override
    public void onInitView() {
        ButterKnife.bind(this);

        setTitle("打卡记录");
        app = (CMEApplication) getApplication();
        studyDao = app.getDaoSeeion().getStudyRegistrationEntityDao();

        Bundle bundle = getIntent().getExtras();
        if (null != bundle){
            courseBean = (CourseEntity) bundle.getSerializable("course");
        }

        recordAdapter = new CardRecordAdapter();
        cardRecordRecycler.setLayoutManager(new LinearLayoutManager(this));
        cardRecordRecycler.setAdapter(recordAdapter);
        initData();
    }

    private void initData() {

        showDialog("正在查询打卡记录...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //查询数据
                Observable.just("")
                        .map(new Func1<String, List<StudyRegistrationEntity>>() {
                            @Override
                            public List<StudyRegistrationEntity> call(String mVoid) {
                                return studyDao.queryBuilder().where(StudyRegistrationEntityDao.Properties.CourseName.eq(courseBean.getCourseName())).build().list();
                            }
                        }).subscribeOn(Schedulers.io())//把工作线程指定为了IO线程
                        .observeOn(AndroidSchedulers.mainThread())//把回调线程指定为了UI线程
                        .subscribe(new Action1<List<StudyRegistrationEntity>>() {
                            @Override
                            public void call(List<StudyRegistrationEntity> tempLifeList) {
                                dismissDialog();
                                recordAdapter.addData(tempLifeList);
                            }
                        });
            }
        }).start();
    }
}
