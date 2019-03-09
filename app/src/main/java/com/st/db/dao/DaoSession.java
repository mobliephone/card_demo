package com.st.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.st.db.entity.CourseEntity;
import com.st.db.entity.CreditRecordEntity;
import com.st.db.entity.EmployeeEntity;
import com.st.db.entity.FingerPrint;
import com.st.db.entity.FrmDepartmentEntity;
import com.st.db.entity.ProjectEntity;
import com.st.db.entity.StudyRegistrationEntity;
import com.st.db.entity.UserEntity;

import com.st.db.dao.CourseEntityDao;
import com.st.db.dao.CreditRecordEntityDao;
import com.st.db.dao.EmployeeEntityDao;
import com.st.db.dao.FingerPrintDao;
import com.st.db.dao.FrmDepartmentEntityDao;
import com.st.db.dao.ProjectEntityDao;
import com.st.db.dao.StudyRegistrationEntityDao;
import com.st.db.dao.UserEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig courseEntityDaoConfig;
    private final DaoConfig creditRecordEntityDaoConfig;
    private final DaoConfig employeeEntityDaoConfig;
    private final DaoConfig fingerPrintDaoConfig;
    private final DaoConfig frmDepartmentEntityDaoConfig;
    private final DaoConfig projectEntityDaoConfig;
    private final DaoConfig studyRegistrationEntityDaoConfig;
    private final DaoConfig userEntityDaoConfig;

    private final CourseEntityDao courseEntityDao;
    private final CreditRecordEntityDao creditRecordEntityDao;
    private final EmployeeEntityDao employeeEntityDao;
    private final FingerPrintDao fingerPrintDao;
    private final FrmDepartmentEntityDao frmDepartmentEntityDao;
    private final ProjectEntityDao projectEntityDao;
    private final StudyRegistrationEntityDao studyRegistrationEntityDao;
    private final UserEntityDao userEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        courseEntityDaoConfig = daoConfigMap.get(CourseEntityDao.class).clone();
        courseEntityDaoConfig.initIdentityScope(type);

        creditRecordEntityDaoConfig = daoConfigMap.get(CreditRecordEntityDao.class).clone();
        creditRecordEntityDaoConfig.initIdentityScope(type);

        employeeEntityDaoConfig = daoConfigMap.get(EmployeeEntityDao.class).clone();
        employeeEntityDaoConfig.initIdentityScope(type);

        fingerPrintDaoConfig = daoConfigMap.get(FingerPrintDao.class).clone();
        fingerPrintDaoConfig.initIdentityScope(type);

        frmDepartmentEntityDaoConfig = daoConfigMap.get(FrmDepartmentEntityDao.class).clone();
        frmDepartmentEntityDaoConfig.initIdentityScope(type);

        projectEntityDaoConfig = daoConfigMap.get(ProjectEntityDao.class).clone();
        projectEntityDaoConfig.initIdentityScope(type);

        studyRegistrationEntityDaoConfig = daoConfigMap.get(StudyRegistrationEntityDao.class).clone();
        studyRegistrationEntityDaoConfig.initIdentityScope(type);

        userEntityDaoConfig = daoConfigMap.get(UserEntityDao.class).clone();
        userEntityDaoConfig.initIdentityScope(type);

        courseEntityDao = new CourseEntityDao(courseEntityDaoConfig, this);
        creditRecordEntityDao = new CreditRecordEntityDao(creditRecordEntityDaoConfig, this);
        employeeEntityDao = new EmployeeEntityDao(employeeEntityDaoConfig, this);
        fingerPrintDao = new FingerPrintDao(fingerPrintDaoConfig, this);
        frmDepartmentEntityDao = new FrmDepartmentEntityDao(frmDepartmentEntityDaoConfig, this);
        projectEntityDao = new ProjectEntityDao(projectEntityDaoConfig, this);
        studyRegistrationEntityDao = new StudyRegistrationEntityDao(studyRegistrationEntityDaoConfig, this);
        userEntityDao = new UserEntityDao(userEntityDaoConfig, this);

        registerDao(CourseEntity.class, courseEntityDao);
        registerDao(CreditRecordEntity.class, creditRecordEntityDao);
        registerDao(EmployeeEntity.class, employeeEntityDao);
        registerDao(FingerPrint.class, fingerPrintDao);
        registerDao(FrmDepartmentEntity.class, frmDepartmentEntityDao);
        registerDao(ProjectEntity.class, projectEntityDao);
        registerDao(StudyRegistrationEntity.class, studyRegistrationEntityDao);
        registerDao(UserEntity.class, userEntityDao);
    }
    
    public void clear() {
        courseEntityDaoConfig.clearIdentityScope();
        creditRecordEntityDaoConfig.clearIdentityScope();
        employeeEntityDaoConfig.clearIdentityScope();
        fingerPrintDaoConfig.clearIdentityScope();
        frmDepartmentEntityDaoConfig.clearIdentityScope();
        projectEntityDaoConfig.clearIdentityScope();
        studyRegistrationEntityDaoConfig.clearIdentityScope();
        userEntityDaoConfig.clearIdentityScope();
    }

    public CourseEntityDao getCourseEntityDao() {
        return courseEntityDao;
    }

    public CreditRecordEntityDao getCreditRecordEntityDao() {
        return creditRecordEntityDao;
    }

    public EmployeeEntityDao getEmployeeEntityDao() {
        return employeeEntityDao;
    }

    public FingerPrintDao getFingerPrintDao() {
        return fingerPrintDao;
    }

    public FrmDepartmentEntityDao getFrmDepartmentEntityDao() {
        return frmDepartmentEntityDao;
    }

    public ProjectEntityDao getProjectEntityDao() {
        return projectEntityDao;
    }

    public StudyRegistrationEntityDao getStudyRegistrationEntityDao() {
        return studyRegistrationEntityDao;
    }

    public UserEntityDao getUserEntityDao() {
        return userEntityDao;
    }

}