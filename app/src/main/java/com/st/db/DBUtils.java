package com.st.db;

import android.content.Context;

import com.framework.util.DateUtils;
import com.st.CMEApplication;
import com.st.db.dao.CourseEntityDao;
import com.st.db.dao.DaoSession;
import com.st.db.dao.EmployeeEntityDao;
import com.st.db.dao.StudyRegistrationEntityDao;
import com.st.db.entity.CourseEntity;
import com.st.db.entity.EmployeeEntity;
import com.st.db.entity.StudyRegistrationEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-11-22.
 * 用户操作类
 */

public class DBUtils {

    private static DBUtils instance;
    private static Context appContext;
    private DaoSession mDaoSession;

    private DBUtils() {
    }

    /**
     * 采用单例模式
     * @param context     上下文
     * @return            dbservice
     */
    public static DBUtils getInstance(Context context, CMEApplication app) {
        if (instance == null) {
            instance = new DBUtils();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = app.getDaoSeeion();
        }
        return instance;
    }


    /**
     * 查询所有人员
     * @param employeeDao
     * @return
     */
    public List<EmployeeEntity> loadAllEmployee(EmployeeEntityDao employeeDao){
        return employeeDao.loadAll();
    }

    /**
     * like模糊查询人员信息
     * @param employeeDao
     * @param userNumber 医通卡号
     * @return
     */
    public List<EmployeeEntity> likeNumberEmployee(EmployeeEntityDao employeeDao,String userNumber){
        return employeeDao.queryBuilder().where(EmployeeEntityDao
                .Properties.Usernumber.like("%"+userNumber+"%")).build().list();
    }

    /**
     * equals查询人员信息
     * @param employeeDao
     * @param userNumber 医通卡号
     * @return
     */
    public List<EmployeeEntity> eqNumberEmployee(EmployeeEntityDao employeeDao,String userNumber){
        return employeeDao.queryBuilder().where(EmployeeEntityDao
                .Properties.Usernumber.eq(userNumber)).build().list();
    }


    /**
     * 查询所有课题
     * @param courseDao
     * @return
     */
    public List<CourseEntity> loadAllCourse(CourseEntityDao courseDao){
        return courseDao.loadAll();
    }

    /**
     * 查询时间段内的所有课题
     * @param courseDao
     * @return
     */
    public List<CourseEntity> loadAllCourseByTime(CourseEntityDao courseDao){

        return courseDao.queryBuilder().where(CourseEntityDao.Properties.StartDate
                .between(DateUtils.reduceMonthBefore(new Date()),DateUtils.reduceMonthAfter(new Date()))).build().list();
    }

    /**
     * 根据课题名称查询课题
     * @param courseDao
     * @return
     */
    public List<CourseEntity> loadCourse(CourseEntityDao courseDao,String courseName){
        return courseDao.queryBuilder().where(CourseEntityDao
                .Properties.CoursesTask.like("%"+courseName+"%")).build().list();
    }


    /**
     * 查询所有打卡记录
     * @param studyDao
     * @return
     */
    public List<StudyRegistrationEntity> loadAllStudy(StudyRegistrationEntityDao studyDao){
        return studyDao.loadAll();
    }

    /**
     * 根据医通卡号查询打卡记录
     * @param studyDao
     * @return
     */
    public List<StudyRegistrationEntity> loadStudyByEmp(StudyRegistrationEntityDao studyDao, String empNumber ,String courseId){
        return studyDao.queryBuilder().where(StudyRegistrationEntityDao.Properties.EmpNumber
                .eq(empNumber),StudyRegistrationEntityDao.Properties.CourseId
                .eq(courseId)).build().list();
    }


    /**
     * 查询并修改打卡记录
     * @param list
     * @param studyDao
     */
    public void saveStudyList(final List<StudyRegistrationEntity> list,final StudyRegistrationEntityDao studyDao){
        if(list == null || list.isEmpty()){
            return;
        }
        studyDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    StudyRegistrationEntity user = list.get(i);
                    studyDao.insertOrReplace(user);
                }
            }
        });

    }


}
