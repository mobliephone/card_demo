package com.st.activity.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.framework.base.BaseActivity;
import com.st.R;
import com.st.db.entity.CourseEntity;
import com.st.persenter.ICallback;
import com.st.persenter.MainPresenter;
import com.st.service.request.RequestConfig;
import com.st.view.widget.FloatingLable;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cgw on 2017-11-23.
 * 课题编辑界面
 */

public class CourseInfoActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.coursesTask)    FloatingLable coursesTask;//课题
    @BindView(R.id.proName)    FloatingLable proName;//项目名称
    @BindView(R.id.deptName)    FloatingLable deptName;//部门名称
    @BindView(R.id.placeName)    FloatingLable placeName;//举办地点
    @BindView(R.id.teacherName)    FloatingLable teacherName;//老师姓名
    @BindView(R.id.credit)    FloatingLable credit;//学分
    @BindView(R.id.startDate)    FloatingLable startDate;//开始日期
    @BindView(R.id.endDate)    FloatingLable endDate;//结束日期
    @BindView(R.id.edu_obj_count)    FloatingLable eduObjCount;//教学人数
    @BindView(R.id.updateCourse)    Button updateCourse;


    //课题对象
    private CourseEntity courseBean;
    //控制类
    private MainPresenter mainPresenter;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_pro_course;
    }

    @Override
    public void onInitView() {
        ButterKnife.bind(this);

        mainPresenter = new MainPresenter();

        setTitle("详细课题信息");
        Bundle bundle = getIntent().getExtras();
        if (null != bundle){
            courseBean = (CourseEntity) bundle.getSerializable("course");
        }
        //初始化数据
        initData(courseBean);

        initListener();
        //禁止编辑
        initFocusable();
    }

    private void initFocusable() {
        coursesTask.setEditableAndisFocusableInTouchMode(false,false);
        proName.setEditableAndisFocusableInTouchMode(false,false);
        deptName.setEditableAndisFocusableInTouchMode(false,false);
        placeName.setEditableAndisFocusableInTouchMode(false,false);
        teacherName.setEditableAndisFocusableInTouchMode(false,false);
        credit.setEditableAndisFocusableInTouchMode(false,false);
        startDate.setEditableAndisFocusableInTouchMode(false,false);
        endDate.setEditableAndisFocusableInTouchMode(false,false);
        eduObjCount.setEditableAndisFocusableInTouchMode(false,false);
    }

    //设置监听
    private void initListener() {

        updateCourse.setOnClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
    }

    //初始化数据
    private void initData(CourseEntity course) {

        coursesTask.setText(course.getCoursesTask());
        proName.setText(course.getProName());
        deptName.setText(course.getDeptName());
        placeName.setText(course.getPlaceName());
        teacherName.setText(course.getTeacherName());
        credit.setText(course.getCredit());
        startDate.setText(course.getStartDate());
        endDate.setText(course.getEndDate());
        eduObjCount.setText(course.getEdu_obj_count());

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.updateCourse:
                upDataCourse();
                break;
            case R.id.startDate:
//                DateSelectUtils.showDateSelect(getSupportFragmentManager(),startDate.getEditText(),this);
                break;
            case R.id.endDate:
//                DateSelectUtils.showDateSelect(getSupportFragmentManager(),endDate.getEditText(),this);
                break;
        }
    }

    /**
     * 数据提交
     */
    private void upDataCourse(){

        if (!ValidationData()){
            showMessage("当前操作不可进行！");
            return;
        }
        showDialog("正在修改中...");
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("coursesTask",courseBean.getCoursesTask());
        Log.d("logInfo", courseBean.getCoursesTask());

        requestMap.put("proName",courseBean.getProName());
        Log.d("logInfo", courseBean.getProName());

        requestMap.put("deptName",courseBean.getDeptName());
        requestMap.put("placeName",courseBean.getPlaceName());
        requestMap.put("teacherName",courseBean.getTeacherName());
        requestMap.put("credit",courseBean.getCredit());
        requestMap.put("startDate",courseBean.getStartDate());
        requestMap.put("endDate",courseBean.getEndDate());
        requestMap.put("courseId",courseBean.getCourseId());


        mainPresenter.allRequestBase(RequestConfig.Url_UpdataCourse, requestMap, new ICallback() {
            @Override
            public void onFail(Throwable e) {
                dismissDialog();
                Log.d("logInfo", e.toString());
                showMessage("课题修改失败！");
            }

            @Override
            public void onSuccess(String string) {
                dismissDialog();
                Log.d("logInfo", string);

                showMessage("课题修改成功！");
                finish();
            }
        }, this);
    }

    /**
     * 数据验证
     * @return
     */
    public boolean ValidationData(){

        courseBean.setCoursesTask(coursesTask.getText());
        courseBean.setProName(proName.getText());
        courseBean.setDeptName(deptName.getText());
        courseBean.setPlaceName(placeName.getText());
        courseBean.setTeacherName(teacherName.getText());
        courseBean.setCredit(credit.getText());
        courseBean.setStartDate(startDate.getText());
        courseBean.setEndDate(endDate.getText());

        return true;
    }
}
