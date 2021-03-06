package com.st.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.st.db.entity.ProjectEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PROJECT_ENTITY".
*/
public class ProjectEntityDao extends AbstractDao<ProjectEntity, String> {

    public static final String TABLENAME = "PROJECT_ENTITY";

    /**
     * Properties of entity ProjectEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ProId = new Property(0, String.class, "proId", true, "PRO_ID");
        public final static Property ProCode = new Property(1, String.class, "proCode", false, "PRO_CODE");
        public final static Property ProName = new Property(2, String.class, "proName", false, "PRO_NAME");
        public final static Property HoldUnit = new Property(3, String.class, "holdUnit", false, "HOLD_UNIT");
        public final static Property Mane = new Property(4, String.class, "mane", false, "MANE");
        public final static Property ManTel = new Property(5, String.class, "manTel", false, "MAN_TEL");
        public final static Property HoldDays = new Property(6, String.class, "holdDays", false, "HOLD_DAYS");
        public final static Property HoldStartDate = new Property(7, String.class, "holdStartDate", false, "HOLD_START_DATE");
        public final static Property HoldEndDate = new Property(8, String.class, "holdEndDate", false, "HOLD_END_DATE");
        public final static Property HoldLocation = new Property(9, String.class, "holdLocation", false, "HOLD_LOCATION");
        public final static Property EduCredit = new Property(10, String.class, "eduCredit", false, "EDU_CREDIT");
        public final static Property EduObject = new Property(11, String.class, "eduObject", false, "EDU_OBJECT");
        public final static Property EduObjCount = new Property(12, String.class, "eduObjCount", false, "EDU_OBJ_COUNT");
        public final static Property AddYear = new Property(13, String.class, "addYear", false, "ADD_YEAR");
        public final static Property AddTime = new Property(14, String.class, "addTime", false, "ADD_TIME");
        public final static Property AddUnit = new Property(15, String.class, "addUnit", false, "ADD_UNIT");
        public final static Property AddUnitMan = new Property(16, String.class, "addUnitMan", false, "ADD_UNIT_MAN");
        public final static Property AddUnitPhone = new Property(17, String.class, "addUnitPhone", false, "ADD_UNIT_PHONE");
        public final static Property Subject = new Property(18, String.class, "subject", false, "SUBJECT");
        public final static Property SubjectThree = new Property(19, String.class, "subjectThree", false, "SUBJECT_THREE");
        public final static Property HoldMode = new Property(20, String.class, "holdMode", false, "HOLD_MODE");
        public final static Property EduExamMode = new Property(21, String.class, "eduExamMode", false, "EDU_EXAM_MODE");
        public final static Property EduHours = new Property(22, String.class, "eduHours", false, "EDU_HOURS");
        public final static Property EduTheoryHours = new Property(23, String.class, "eduTheoryHours", false, "EDU_THEORY_HOURS");
        public final static Property EduExperimentHours = new Property(24, String.class, "eduExperimentHours", false, "EDU_EXPERIMENT_HOURS");
        public final static Property HoldUnitMan = new Property(25, String.class, "holdUnitMan", false, "HOLD_UNIT_MAN");
        public final static Property HoldUnitPhone = new Property(26, String.class, "holdUnitPhone", false, "HOLD_UNIT_PHONE");
        public final static Property ProType = new Property(27, String.class, "proType", false, "PRO_TYPE");
        public final static Property ProSource = new Property(28, String.class, "proSource", false, "PRO_SOURCE");
        public final static Property Evaluate = new Property(29, String.class, "evaluate", false, "EVALUATE");
        public final static Property ProState = new Property(30, String.class, "proState", false, "PRO_STATE");
        public final static Property Remark1 = new Property(31, String.class, "remark1", false, "REMARK1");
        public final static Property Remark2 = new Property(32, String.class, "remark2", false, "REMARK2");
        public final static Property Remark3 = new Property(33, String.class, "remark3", false, "REMARK3");
        public final static Property Gxsj = new Property(34, String.class, "gxsj", false, "GXSJ");
    }


    public ProjectEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ProjectEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PROJECT_ENTITY\" (" + //
                "\"PRO_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: proId
                "\"PRO_CODE\" TEXT," + // 1: proCode
                "\"PRO_NAME\" TEXT," + // 2: proName
                "\"HOLD_UNIT\" TEXT," + // 3: holdUnit
                "\"MANE\" TEXT," + // 4: mane
                "\"MAN_TEL\" TEXT," + // 5: manTel
                "\"HOLD_DAYS\" TEXT," + // 6: holdDays
                "\"HOLD_START_DATE\" TEXT," + // 7: holdStartDate
                "\"HOLD_END_DATE\" TEXT," + // 8: holdEndDate
                "\"HOLD_LOCATION\" TEXT," + // 9: holdLocation
                "\"EDU_CREDIT\" TEXT," + // 10: eduCredit
                "\"EDU_OBJECT\" TEXT," + // 11: eduObject
                "\"EDU_OBJ_COUNT\" TEXT," + // 12: eduObjCount
                "\"ADD_YEAR\" TEXT," + // 13: addYear
                "\"ADD_TIME\" TEXT," + // 14: addTime
                "\"ADD_UNIT\" TEXT," + // 15: addUnit
                "\"ADD_UNIT_MAN\" TEXT," + // 16: addUnitMan
                "\"ADD_UNIT_PHONE\" TEXT," + // 17: addUnitPhone
                "\"SUBJECT\" TEXT," + // 18: subject
                "\"SUBJECT_THREE\" TEXT," + // 19: subjectThree
                "\"HOLD_MODE\" TEXT," + // 20: holdMode
                "\"EDU_EXAM_MODE\" TEXT," + // 21: eduExamMode
                "\"EDU_HOURS\" TEXT," + // 22: eduHours
                "\"EDU_THEORY_HOURS\" TEXT," + // 23: eduTheoryHours
                "\"EDU_EXPERIMENT_HOURS\" TEXT," + // 24: eduExperimentHours
                "\"HOLD_UNIT_MAN\" TEXT," + // 25: holdUnitMan
                "\"HOLD_UNIT_PHONE\" TEXT," + // 26: holdUnitPhone
                "\"PRO_TYPE\" TEXT," + // 27: proType
                "\"PRO_SOURCE\" TEXT," + // 28: proSource
                "\"EVALUATE\" TEXT," + // 29: evaluate
                "\"PRO_STATE\" TEXT," + // 30: proState
                "\"REMARK1\" TEXT," + // 31: remark1
                "\"REMARK2\" TEXT," + // 32: remark2
                "\"REMARK3\" TEXT," + // 33: remark3
                "\"GXSJ\" TEXT);"); // 34: gxsj
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PROJECT_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ProjectEntity entity) {
        stmt.clearBindings();
 
        String proId = entity.getProId();
        if (proId != null) {
            stmt.bindString(1, proId);
        }
 
        String proCode = entity.getProCode();
        if (proCode != null) {
            stmt.bindString(2, proCode);
        }
 
        String proName = entity.getProName();
        if (proName != null) {
            stmt.bindString(3, proName);
        }
 
        String holdUnit = entity.getHoldUnit();
        if (holdUnit != null) {
            stmt.bindString(4, holdUnit);
        }
 
        String mane = entity.getMane();
        if (mane != null) {
            stmt.bindString(5, mane);
        }
 
        String manTel = entity.getManTel();
        if (manTel != null) {
            stmt.bindString(6, manTel);
        }
 
        String holdDays = entity.getHoldDays();
        if (holdDays != null) {
            stmt.bindString(7, holdDays);
        }
 
        String holdStartDate = entity.getHoldStartDate();
        if (holdStartDate != null) {
            stmt.bindString(8, holdStartDate);
        }
 
        String holdEndDate = entity.getHoldEndDate();
        if (holdEndDate != null) {
            stmt.bindString(9, holdEndDate);
        }
 
        String holdLocation = entity.getHoldLocation();
        if (holdLocation != null) {
            stmt.bindString(10, holdLocation);
        }
 
        String eduCredit = entity.getEduCredit();
        if (eduCredit != null) {
            stmt.bindString(11, eduCredit);
        }
 
        String eduObject = entity.getEduObject();
        if (eduObject != null) {
            stmt.bindString(12, eduObject);
        }
 
        String eduObjCount = entity.getEduObjCount();
        if (eduObjCount != null) {
            stmt.bindString(13, eduObjCount);
        }
 
        String addYear = entity.getAddYear();
        if (addYear != null) {
            stmt.bindString(14, addYear);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(15, addTime);
        }
 
        String addUnit = entity.getAddUnit();
        if (addUnit != null) {
            stmt.bindString(16, addUnit);
        }
 
        String addUnitMan = entity.getAddUnitMan();
        if (addUnitMan != null) {
            stmt.bindString(17, addUnitMan);
        }
 
        String addUnitPhone = entity.getAddUnitPhone();
        if (addUnitPhone != null) {
            stmt.bindString(18, addUnitPhone);
        }
 
        String subject = entity.getSubject();
        if (subject != null) {
            stmt.bindString(19, subject);
        }
 
        String subjectThree = entity.getSubjectThree();
        if (subjectThree != null) {
            stmt.bindString(20, subjectThree);
        }
 
        String holdMode = entity.getHoldMode();
        if (holdMode != null) {
            stmt.bindString(21, holdMode);
        }
 
        String eduExamMode = entity.getEduExamMode();
        if (eduExamMode != null) {
            stmt.bindString(22, eduExamMode);
        }
 
        String eduHours = entity.getEduHours();
        if (eduHours != null) {
            stmt.bindString(23, eduHours);
        }
 
        String eduTheoryHours = entity.getEduTheoryHours();
        if (eduTheoryHours != null) {
            stmt.bindString(24, eduTheoryHours);
        }
 
        String eduExperimentHours = entity.getEduExperimentHours();
        if (eduExperimentHours != null) {
            stmt.bindString(25, eduExperimentHours);
        }
 
        String holdUnitMan = entity.getHoldUnitMan();
        if (holdUnitMan != null) {
            stmt.bindString(26, holdUnitMan);
        }
 
        String holdUnitPhone = entity.getHoldUnitPhone();
        if (holdUnitPhone != null) {
            stmt.bindString(27, holdUnitPhone);
        }
 
        String proType = entity.getProType();
        if (proType != null) {
            stmt.bindString(28, proType);
        }
 
        String proSource = entity.getProSource();
        if (proSource != null) {
            stmt.bindString(29, proSource);
        }
 
        String evaluate = entity.getEvaluate();
        if (evaluate != null) {
            stmt.bindString(30, evaluate);
        }
 
        String proState = entity.getProState();
        if (proState != null) {
            stmt.bindString(31, proState);
        }
 
        String remark1 = entity.getRemark1();
        if (remark1 != null) {
            stmt.bindString(32, remark1);
        }
 
        String remark2 = entity.getRemark2();
        if (remark2 != null) {
            stmt.bindString(33, remark2);
        }
 
        String remark3 = entity.getRemark3();
        if (remark3 != null) {
            stmt.bindString(34, remark3);
        }
 
        String gxsj = entity.getGxsj();
        if (gxsj != null) {
            stmt.bindString(35, gxsj);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ProjectEntity entity) {
        stmt.clearBindings();
 
        String proId = entity.getProId();
        if (proId != null) {
            stmt.bindString(1, proId);
        }
 
        String proCode = entity.getProCode();
        if (proCode != null) {
            stmt.bindString(2, proCode);
        }
 
        String proName = entity.getProName();
        if (proName != null) {
            stmt.bindString(3, proName);
        }
 
        String holdUnit = entity.getHoldUnit();
        if (holdUnit != null) {
            stmt.bindString(4, holdUnit);
        }
 
        String mane = entity.getMane();
        if (mane != null) {
            stmt.bindString(5, mane);
        }
 
        String manTel = entity.getManTel();
        if (manTel != null) {
            stmt.bindString(6, manTel);
        }
 
        String holdDays = entity.getHoldDays();
        if (holdDays != null) {
            stmt.bindString(7, holdDays);
        }
 
        String holdStartDate = entity.getHoldStartDate();
        if (holdStartDate != null) {
            stmt.bindString(8, holdStartDate);
        }
 
        String holdEndDate = entity.getHoldEndDate();
        if (holdEndDate != null) {
            stmt.bindString(9, holdEndDate);
        }
 
        String holdLocation = entity.getHoldLocation();
        if (holdLocation != null) {
            stmt.bindString(10, holdLocation);
        }
 
        String eduCredit = entity.getEduCredit();
        if (eduCredit != null) {
            stmt.bindString(11, eduCredit);
        }
 
        String eduObject = entity.getEduObject();
        if (eduObject != null) {
            stmt.bindString(12, eduObject);
        }
 
        String eduObjCount = entity.getEduObjCount();
        if (eduObjCount != null) {
            stmt.bindString(13, eduObjCount);
        }
 
        String addYear = entity.getAddYear();
        if (addYear != null) {
            stmt.bindString(14, addYear);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(15, addTime);
        }
 
        String addUnit = entity.getAddUnit();
        if (addUnit != null) {
            stmt.bindString(16, addUnit);
        }
 
        String addUnitMan = entity.getAddUnitMan();
        if (addUnitMan != null) {
            stmt.bindString(17, addUnitMan);
        }
 
        String addUnitPhone = entity.getAddUnitPhone();
        if (addUnitPhone != null) {
            stmt.bindString(18, addUnitPhone);
        }
 
        String subject = entity.getSubject();
        if (subject != null) {
            stmt.bindString(19, subject);
        }
 
        String subjectThree = entity.getSubjectThree();
        if (subjectThree != null) {
            stmt.bindString(20, subjectThree);
        }
 
        String holdMode = entity.getHoldMode();
        if (holdMode != null) {
            stmt.bindString(21, holdMode);
        }
 
        String eduExamMode = entity.getEduExamMode();
        if (eduExamMode != null) {
            stmt.bindString(22, eduExamMode);
        }
 
        String eduHours = entity.getEduHours();
        if (eduHours != null) {
            stmt.bindString(23, eduHours);
        }
 
        String eduTheoryHours = entity.getEduTheoryHours();
        if (eduTheoryHours != null) {
            stmt.bindString(24, eduTheoryHours);
        }
 
        String eduExperimentHours = entity.getEduExperimentHours();
        if (eduExperimentHours != null) {
            stmt.bindString(25, eduExperimentHours);
        }
 
        String holdUnitMan = entity.getHoldUnitMan();
        if (holdUnitMan != null) {
            stmt.bindString(26, holdUnitMan);
        }
 
        String holdUnitPhone = entity.getHoldUnitPhone();
        if (holdUnitPhone != null) {
            stmt.bindString(27, holdUnitPhone);
        }
 
        String proType = entity.getProType();
        if (proType != null) {
            stmt.bindString(28, proType);
        }
 
        String proSource = entity.getProSource();
        if (proSource != null) {
            stmt.bindString(29, proSource);
        }
 
        String evaluate = entity.getEvaluate();
        if (evaluate != null) {
            stmt.bindString(30, evaluate);
        }
 
        String proState = entity.getProState();
        if (proState != null) {
            stmt.bindString(31, proState);
        }
 
        String remark1 = entity.getRemark1();
        if (remark1 != null) {
            stmt.bindString(32, remark1);
        }
 
        String remark2 = entity.getRemark2();
        if (remark2 != null) {
            stmt.bindString(33, remark2);
        }
 
        String remark3 = entity.getRemark3();
        if (remark3 != null) {
            stmt.bindString(34, remark3);
        }
 
        String gxsj = entity.getGxsj();
        if (gxsj != null) {
            stmt.bindString(35, gxsj);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public ProjectEntity readEntity(Cursor cursor, int offset) {
        ProjectEntity entity = new ProjectEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // proId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // proCode
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // proName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // holdUnit
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // mane
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // manTel
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // holdDays
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // holdStartDate
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // holdEndDate
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // holdLocation
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // eduCredit
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // eduObject
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // eduObjCount
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // addYear
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // addTime
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // addUnit
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // addUnitMan
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // addUnitPhone
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // subject
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // subjectThree
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // holdMode
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // eduExamMode
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // eduHours
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // eduTheoryHours
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // eduExperimentHours
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // holdUnitMan
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // holdUnitPhone
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // proType
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // proSource
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // evaluate
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // proState
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // remark1
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // remark2
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // remark3
            cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34) // gxsj
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ProjectEntity entity, int offset) {
        entity.setProId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setProCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setProName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHoldUnit(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMane(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setManTel(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setHoldDays(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setHoldStartDate(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setHoldEndDate(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setHoldLocation(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setEduCredit(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setEduObject(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setEduObjCount(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setAddYear(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setAddTime(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setAddUnit(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setAddUnitMan(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setAddUnitPhone(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setSubject(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setSubjectThree(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setHoldMode(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setEduExamMode(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setEduHours(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setEduTheoryHours(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setEduExperimentHours(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setHoldUnitMan(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setHoldUnitPhone(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setProType(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setProSource(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setEvaluate(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setProState(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setRemark1(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setRemark2(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setRemark3(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setGxsj(cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34));
     }
    
    @Override
    protected final String updateKeyAfterInsert(ProjectEntity entity, long rowId) {
        return entity.getProId();
    }
    
    @Override
    public String getKey(ProjectEntity entity) {
        if(entity != null) {
            return entity.getProId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ProjectEntity entity) {
        return entity.getProId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
