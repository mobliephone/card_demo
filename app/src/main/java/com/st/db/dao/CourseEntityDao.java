package com.st.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.st.db.entity.CourseEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COURSE_ENTITY".
*/
public class CourseEntityDao extends AbstractDao<CourseEntity, String> {

    public static final String TABLENAME = "COURSE_ENTITY";

    /**
     * Properties of entity CourseEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CourseId = new Property(0, String.class, "courseId", true, "COURSE_ID");
        public final static Property ProId = new Property(1, String.class, "proId", false, "PRO_ID");
        public final static Property ProName = new Property(2, String.class, "proName", false, "PRO_NAME");
        public final static Property ActivityId = new Property(3, String.class, "activityId", false, "ACTIVITY_ID");
        public final static Property TeacherId = new Property(4, String.class, "teacherId", false, "TEACHER_ID");
        public final static Property TeacherName = new Property(5, String.class, "teacherName", false, "TEACHER_NAME");
        public final static Property TeacherTitle = new Property(6, String.class, "teacherTitle", false, "TEACHER_TITLE");
        public final static Property TeacherDirection = new Property(7, String.class, "teacherDirection", false, "TEACHER_DIRECTION");
        public final static Property TeacherUnit = new Property(8, String.class, "teacherUnit", false, "TEACHER_UNIT");
        public final static Property CoursesTask = new Property(9, String.class, "coursesTask", false, "COURSES_TASK");
        public final static Property CoursesContent = new Property(10, String.class, "coursesContent", false, "COURSES_CONTENT");
        public final static Property Credit = new Property(11, String.class, "credit", false, "CREDIT");
        public final static Property Hours = new Property(12, String.class, "hours", false, "HOURS");
        public final static Property CoursesType = new Property(13, String.class, "coursesType", false, "COURSES_TYPE");
        public final static Property Isdownload = new Property(14, String.class, "isdownload", false, "ISDOWNLOAD");
        public final static Property Method = new Property(15, String.class, "method", false, "METHOD");
        public final static Property DeptId = new Property(16, String.class, "deptId", false, "DEPT_ID");
        public final static Property DeptName = new Property(17, String.class, "deptName", false, "DEPT_NAME");
        public final static Property CourseName = new Property(18, String.class, "courseName", false, "COURSE_NAME");
        public final static Property PlaceName = new Property(19, String.class, "placeName", false, "PLACE_NAME");
        public final static Property Position = new Property(20, String.class, "position", false, "POSITION");
        public final static Property State = new Property(21, String.class, "state", false, "STATE");
        public final static Property Isbegin = new Property(22, String.class, "isbegin", false, "ISBEGIN");
        public final static Property StartDate = new Property(23, String.class, "startDate", false, "START_DATE");
        public final static Property EndDate = new Property(24, String.class, "endDate", false, "END_DATE");
        public final static Property ValidTime = new Property(25, String.class, "validTime", false, "VALID_TIME");
        public final static Property CheckNumber = new Property(26, String.class, "checkNumber", false, "CHECK_NUMBER");
        public final static Property AutoCheck = new Property(27, String.class, "autoCheck", false, "AUTO_CHECK");
        public final static Property AddTime = new Property(28, String.class, "addTime", false, "ADD_TIME");
        public final static Property Remark1 = new Property(29, String.class, "remark1", false, "REMARK1");
        public final static Property Remark2 = new Property(30, String.class, "remark2", false, "REMARK2");
        public final static Property Remark3 = new Property(31, String.class, "remark3", false, "REMARK3");
        public final static Property Gxsj = new Property(32, String.class, "gxsj", false, "GXSJ");
        public final static Property Edu_obj_count = new Property(33, String.class, "edu_obj_count", false, "EDU_OBJ_COUNT");
    }


    public CourseEntityDao(DaoConfig config) {
        super(config);
    }
    
    public CourseEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COURSE_ENTITY\" (" + //
                "\"COURSE_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: courseId
                "\"PRO_ID\" TEXT," + // 1: proId
                "\"PRO_NAME\" TEXT," + // 2: proName
                "\"ACTIVITY_ID\" TEXT," + // 3: activityId
                "\"TEACHER_ID\" TEXT," + // 4: teacherId
                "\"TEACHER_NAME\" TEXT," + // 5: teacherName
                "\"TEACHER_TITLE\" TEXT," + // 6: teacherTitle
                "\"TEACHER_DIRECTION\" TEXT," + // 7: teacherDirection
                "\"TEACHER_UNIT\" TEXT," + // 8: teacherUnit
                "\"COURSES_TASK\" TEXT," + // 9: coursesTask
                "\"COURSES_CONTENT\" TEXT," + // 10: coursesContent
                "\"CREDIT\" TEXT," + // 11: credit
                "\"HOURS\" TEXT," + // 12: hours
                "\"COURSES_TYPE\" TEXT," + // 13: coursesType
                "\"ISDOWNLOAD\" TEXT," + // 14: isdownload
                "\"METHOD\" TEXT," + // 15: method
                "\"DEPT_ID\" TEXT," + // 16: deptId
                "\"DEPT_NAME\" TEXT," + // 17: deptName
                "\"COURSE_NAME\" TEXT," + // 18: courseName
                "\"PLACE_NAME\" TEXT," + // 19: placeName
                "\"POSITION\" TEXT," + // 20: position
                "\"STATE\" TEXT," + // 21: state
                "\"ISBEGIN\" TEXT," + // 22: isbegin
                "\"START_DATE\" TEXT," + // 23: startDate
                "\"END_DATE\" TEXT," + // 24: endDate
                "\"VALID_TIME\" TEXT," + // 25: validTime
                "\"CHECK_NUMBER\" TEXT," + // 26: checkNumber
                "\"AUTO_CHECK\" TEXT," + // 27: autoCheck
                "\"ADD_TIME\" TEXT," + // 28: addTime
                "\"REMARK1\" TEXT," + // 29: remark1
                "\"REMARK2\" TEXT," + // 30: remark2
                "\"REMARK3\" TEXT," + // 31: remark3
                "\"GXSJ\" TEXT," + // 32: gxsj
                "\"EDU_OBJ_COUNT\" TEXT);"); // 33: edu_obj_count
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COURSE_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CourseEntity entity) {
        stmt.clearBindings();
 
        String courseId = entity.getCourseId();
        if (courseId != null) {
            stmt.bindString(1, courseId);
        }
 
        String proId = entity.getProId();
        if (proId != null) {
            stmt.bindString(2, proId);
        }
 
        String proName = entity.getProName();
        if (proName != null) {
            stmt.bindString(3, proName);
        }
 
        String activityId = entity.getActivityId();
        if (activityId != null) {
            stmt.bindString(4, activityId);
        }
 
        String teacherId = entity.getTeacherId();
        if (teacherId != null) {
            stmt.bindString(5, teacherId);
        }
 
        String teacherName = entity.getTeacherName();
        if (teacherName != null) {
            stmt.bindString(6, teacherName);
        }
 
        String teacherTitle = entity.getTeacherTitle();
        if (teacherTitle != null) {
            stmt.bindString(7, teacherTitle);
        }
 
        String teacherDirection = entity.getTeacherDirection();
        if (teacherDirection != null) {
            stmt.bindString(8, teacherDirection);
        }
 
        String teacherUnit = entity.getTeacherUnit();
        if (teacherUnit != null) {
            stmt.bindString(9, teacherUnit);
        }
 
        String coursesTask = entity.getCoursesTask();
        if (coursesTask != null) {
            stmt.bindString(10, coursesTask);
        }
 
        String coursesContent = entity.getCoursesContent();
        if (coursesContent != null) {
            stmt.bindString(11, coursesContent);
        }
 
        String credit = entity.getCredit();
        if (credit != null) {
            stmt.bindString(12, credit);
        }
 
        String hours = entity.getHours();
        if (hours != null) {
            stmt.bindString(13, hours);
        }
 
        String coursesType = entity.getCoursesType();
        if (coursesType != null) {
            stmt.bindString(14, coursesType);
        }
 
        String isdownload = entity.getIsdownload();
        if (isdownload != null) {
            stmt.bindString(15, isdownload);
        }
 
        String method = entity.getMethod();
        if (method != null) {
            stmt.bindString(16, method);
        }
 
        String deptId = entity.getDeptId();
        if (deptId != null) {
            stmt.bindString(17, deptId);
        }
 
        String deptName = entity.getDeptName();
        if (deptName != null) {
            stmt.bindString(18, deptName);
        }
 
        String courseName = entity.getCourseName();
        if (courseName != null) {
            stmt.bindString(19, courseName);
        }
 
        String placeName = entity.getPlaceName();
        if (placeName != null) {
            stmt.bindString(20, placeName);
        }
 
        String position = entity.getPosition();
        if (position != null) {
            stmt.bindString(21, position);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(22, state);
        }
 
        String isbegin = entity.getIsbegin();
        if (isbegin != null) {
            stmt.bindString(23, isbegin);
        }
 
        String startDate = entity.getStartDate();
        if (startDate != null) {
            stmt.bindString(24, startDate);
        }
 
        String endDate = entity.getEndDate();
        if (endDate != null) {
            stmt.bindString(25, endDate);
        }
 
        String validTime = entity.getValidTime();
        if (validTime != null) {
            stmt.bindString(26, validTime);
        }
 
        String checkNumber = entity.getCheckNumber();
        if (checkNumber != null) {
            stmt.bindString(27, checkNumber);
        }
 
        String autoCheck = entity.getAutoCheck();
        if (autoCheck != null) {
            stmt.bindString(28, autoCheck);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(29, addTime);
        }
 
        String remark1 = entity.getRemark1();
        if (remark1 != null) {
            stmt.bindString(30, remark1);
        }
 
        String remark2 = entity.getRemark2();
        if (remark2 != null) {
            stmt.bindString(31, remark2);
        }
 
        String remark3 = entity.getRemark3();
        if (remark3 != null) {
            stmt.bindString(32, remark3);
        }
 
        String gxsj = entity.getGxsj();
        if (gxsj != null) {
            stmt.bindString(33, gxsj);
        }
 
        String edu_obj_count = entity.getEdu_obj_count();
        if (edu_obj_count != null) {
            stmt.bindString(34, edu_obj_count);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CourseEntity entity) {
        stmt.clearBindings();
 
        String courseId = entity.getCourseId();
        if (courseId != null) {
            stmt.bindString(1, courseId);
        }
 
        String proId = entity.getProId();
        if (proId != null) {
            stmt.bindString(2, proId);
        }
 
        String proName = entity.getProName();
        if (proName != null) {
            stmt.bindString(3, proName);
        }
 
        String activityId = entity.getActivityId();
        if (activityId != null) {
            stmt.bindString(4, activityId);
        }
 
        String teacherId = entity.getTeacherId();
        if (teacherId != null) {
            stmt.bindString(5, teacherId);
        }
 
        String teacherName = entity.getTeacherName();
        if (teacherName != null) {
            stmt.bindString(6, teacherName);
        }
 
        String teacherTitle = entity.getTeacherTitle();
        if (teacherTitle != null) {
            stmt.bindString(7, teacherTitle);
        }
 
        String teacherDirection = entity.getTeacherDirection();
        if (teacherDirection != null) {
            stmt.bindString(8, teacherDirection);
        }
 
        String teacherUnit = entity.getTeacherUnit();
        if (teacherUnit != null) {
            stmt.bindString(9, teacherUnit);
        }
 
        String coursesTask = entity.getCoursesTask();
        if (coursesTask != null) {
            stmt.bindString(10, coursesTask);
        }
 
        String coursesContent = entity.getCoursesContent();
        if (coursesContent != null) {
            stmt.bindString(11, coursesContent);
        }
 
        String credit = entity.getCredit();
        if (credit != null) {
            stmt.bindString(12, credit);
        }
 
        String hours = entity.getHours();
        if (hours != null) {
            stmt.bindString(13, hours);
        }
 
        String coursesType = entity.getCoursesType();
        if (coursesType != null) {
            stmt.bindString(14, coursesType);
        }
 
        String isdownload = entity.getIsdownload();
        if (isdownload != null) {
            stmt.bindString(15, isdownload);
        }
 
        String method = entity.getMethod();
        if (method != null) {
            stmt.bindString(16, method);
        }
 
        String deptId = entity.getDeptId();
        if (deptId != null) {
            stmt.bindString(17, deptId);
        }
 
        String deptName = entity.getDeptName();
        if (deptName != null) {
            stmt.bindString(18, deptName);
        }
 
        String courseName = entity.getCourseName();
        if (courseName != null) {
            stmt.bindString(19, courseName);
        }
 
        String placeName = entity.getPlaceName();
        if (placeName != null) {
            stmt.bindString(20, placeName);
        }
 
        String position = entity.getPosition();
        if (position != null) {
            stmt.bindString(21, position);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(22, state);
        }
 
        String isbegin = entity.getIsbegin();
        if (isbegin != null) {
            stmt.bindString(23, isbegin);
        }
 
        String startDate = entity.getStartDate();
        if (startDate != null) {
            stmt.bindString(24, startDate);
        }
 
        String endDate = entity.getEndDate();
        if (endDate != null) {
            stmt.bindString(25, endDate);
        }
 
        String validTime = entity.getValidTime();
        if (validTime != null) {
            stmt.bindString(26, validTime);
        }
 
        String checkNumber = entity.getCheckNumber();
        if (checkNumber != null) {
            stmt.bindString(27, checkNumber);
        }
 
        String autoCheck = entity.getAutoCheck();
        if (autoCheck != null) {
            stmt.bindString(28, autoCheck);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(29, addTime);
        }
 
        String remark1 = entity.getRemark1();
        if (remark1 != null) {
            stmt.bindString(30, remark1);
        }
 
        String remark2 = entity.getRemark2();
        if (remark2 != null) {
            stmt.bindString(31, remark2);
        }
 
        String remark3 = entity.getRemark3();
        if (remark3 != null) {
            stmt.bindString(32, remark3);
        }
 
        String gxsj = entity.getGxsj();
        if (gxsj != null) {
            stmt.bindString(33, gxsj);
        }
 
        String edu_obj_count = entity.getEdu_obj_count();
        if (edu_obj_count != null) {
            stmt.bindString(34, edu_obj_count);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public CourseEntity readEntity(Cursor cursor, int offset) {
        CourseEntity entity = new CourseEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // courseId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // proId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // proName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // activityId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // teacherId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // teacherName
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // teacherTitle
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // teacherDirection
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // teacherUnit
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // coursesTask
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // coursesContent
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // credit
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // hours
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // coursesType
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // isdownload
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // method
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // deptId
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // deptName
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // courseName
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // placeName
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // position
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // state
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // isbegin
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // startDate
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // endDate
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // validTime
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // checkNumber
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // autoCheck
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // addTime
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // remark1
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // remark2
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // remark3
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // gxsj
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33) // edu_obj_count
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CourseEntity entity, int offset) {
        entity.setCourseId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setProId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setProName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setActivityId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTeacherId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTeacherName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTeacherTitle(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTeacherDirection(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTeacherUnit(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCoursesTask(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCoursesContent(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCredit(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setHours(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCoursesType(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setIsdownload(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setMethod(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setDeptId(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setDeptName(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setCourseName(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setPlaceName(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setPosition(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setState(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setIsbegin(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setStartDate(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setEndDate(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setValidTime(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setCheckNumber(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setAutoCheck(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setAddTime(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setRemark1(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setRemark2(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setRemark3(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setGxsj(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setEdu_obj_count(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
     }
    
    @Override
    protected final String updateKeyAfterInsert(CourseEntity entity, long rowId) {
        return entity.getCourseId();
    }
    
    @Override
    public String getKey(CourseEntity entity) {
        if(entity != null) {
            return entity.getCourseId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CourseEntity entity) {
        return entity.getCourseId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}