package com.st.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.st.db.entity.EmployeeEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EMPLOYEE_ENTITY".
*/
public class EmployeeEntityDao extends AbstractDao<EmployeeEntity, String> {

    public static final String TABLENAME = "EMPLOYEE_ENTITY";

    /**
     * Properties of entity EmployeeEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Usernumber = new Property(0, String.class, "usernumber", true, "USERNUMBER");
        public final static Property UserOidId = new Property(1, String.class, "userOidId", false, "USER_OID_ID");
        public final static Property Username = new Property(2, String.class, "username", false, "USERNAME");
        public final static Property Sex = new Property(3, String.class, "sex", false, "SEX");
        public final static Property Idcard = new Property(4, String.class, "idcard", false, "IDCARD");
        public final static Property Qq = new Property(5, String.class, "qq", false, "QQ");
        public final static Property Cardtype = new Property(6, String.class, "cardtype", false, "CARDTYPE");
        public final static Property Depart = new Property(7, String.class, "depart", false, "DEPART");
        public final static Property Yiyuan = new Property(8, String.class, "yiyuan", false, "YIYUAN");
        public final static Property Zhuanye = new Property(9, String.class, "zhuanye", false, "ZHUANYE");
        public final static Property Zt = new Property(10, String.class, "zt", false, "ZT");
        public final static Property Xueli = new Property(11, String.class, "xueli", false, "XUELI");
        public final static Property Shengri = new Property(12, String.class, "shengri", false, "SHENGRI");
        public final static Property Zhicheng = new Property(13, String.class, "zhicheng", false, "ZHICHENG");
        public final static Property Zhichenglv = new Property(14, String.class, "zhichenglv", false, "ZHICHENGLV");
        public final static Property Minzu = new Property(15, String.class, "minzu", false, "MINZU");
        public final static Property Zhengzi = new Property(16, String.class, "zhengzi", false, "ZHENGZI");
        public final static Property Zhiwu = new Property(17, String.class, "zhiwu", false, "ZHIWU");
        public final static Property Xuewei = new Property(18, String.class, "xuewei", false, "XUEWEI");
        public final static Property Diaodongzhuangtai = new Property(19, String.class, "diaodongzhuangtai", false, "DIAODONGZHUANGTAI");
        public final static Property Byyuabxiao = new Property(20, String.class, "byyuabxiao", false, "BYYUABXIAO");
        public final static Property Usertel = new Property(21, String.class, "usertel", false, "USERTEL");
        public final static Property Userphone = new Property(22, String.class, "userphone", false, "USERPHONE");
        public final static Property Useremail = new Property(23, String.class, "useremail", false, "USEREMAIL");
        public final static Property PfttcNumbers = new Property(24, String.class, "pfttcNumbers", false, "PFTTC_NUMBERS");
        public final static Property UserZyid = new Property(25, String.class, "userZyid", false, "USER_ZYID");
        public final static Property Photo = new Property(26, String.class, "photo", false, "PHOTO");
        public final static Property AddTime = new Property(27, String.class, "addTime", false, "ADD_TIME");
        public final static Property BankaRes = new Property(28, String.class, "bankaRes", false, "BANKA_RES");
        public final static Property Xkbs = new Property(29, String.class, "xkbs", false, "XKBS");
        public final static Property Xkrq = new Property(30, String.class, "xkrq", false, "XKRQ");
        public final static Property Dqsj = new Property(31, String.class, "dqsj", false, "DQSJ");
        public final static Property Uresource = new Property(32, String.class, "uresource", false, "URESOURCE");
        public final static Property Beizhu = new Property(33, String.class, "beizhu", false, "BEIZHU");
        public final static Property State = new Property(34, String.class, "state", false, "STATE");
        public final static Property Updatetime = new Property(35, String.class, "updatetime", false, "UPDATETIME");
    }


    public EmployeeEntityDao(DaoConfig config) {
        super(config);
    }
    
    public EmployeeEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EMPLOYEE_ENTITY\" (" + //
                "\"USERNUMBER\" TEXT PRIMARY KEY NOT NULL ," + // 0: usernumber
                "\"USER_OID_ID\" TEXT," + // 1: userOidId
                "\"USERNAME\" TEXT," + // 2: username
                "\"SEX\" TEXT," + // 3: sex
                "\"IDCARD\" TEXT," + // 4: idcard
                "\"QQ\" TEXT," + // 5: qq
                "\"CARDTYPE\" TEXT," + // 6: cardtype
                "\"DEPART\" TEXT," + // 7: depart
                "\"YIYUAN\" TEXT," + // 8: yiyuan
                "\"ZHUANYE\" TEXT," + // 9: zhuanye
                "\"ZT\" TEXT," + // 10: zt
                "\"XUELI\" TEXT," + // 11: xueli
                "\"SHENGRI\" TEXT," + // 12: shengri
                "\"ZHICHENG\" TEXT," + // 13: zhicheng
                "\"ZHICHENGLV\" TEXT," + // 14: zhichenglv
                "\"MINZU\" TEXT," + // 15: minzu
                "\"ZHENGZI\" TEXT," + // 16: zhengzi
                "\"ZHIWU\" TEXT," + // 17: zhiwu
                "\"XUEWEI\" TEXT," + // 18: xuewei
                "\"DIAODONGZHUANGTAI\" TEXT," + // 19: diaodongzhuangtai
                "\"BYYUABXIAO\" TEXT," + // 20: byyuabxiao
                "\"USERTEL\" TEXT," + // 21: usertel
                "\"USERPHONE\" TEXT," + // 22: userphone
                "\"USEREMAIL\" TEXT," + // 23: useremail
                "\"PFTTC_NUMBERS\" TEXT," + // 24: pfttcNumbers
                "\"USER_ZYID\" TEXT," + // 25: userZyid
                "\"PHOTO\" TEXT," + // 26: photo
                "\"ADD_TIME\" TEXT," + // 27: addTime
                "\"BANKA_RES\" TEXT," + // 28: bankaRes
                "\"XKBS\" TEXT," + // 29: xkbs
                "\"XKRQ\" TEXT," + // 30: xkrq
                "\"DQSJ\" TEXT," + // 31: dqsj
                "\"URESOURCE\" TEXT," + // 32: uresource
                "\"BEIZHU\" TEXT," + // 33: beizhu
                "\"STATE\" TEXT," + // 34: state
                "\"UPDATETIME\" TEXT);"); // 35: updatetime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EMPLOYEE_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EmployeeEntity entity) {
        stmt.clearBindings();
 
        String usernumber = entity.getUsernumber();
        if (usernumber != null) {
            stmt.bindString(1, usernumber);
        }
 
        String userOidId = entity.getUserOidId();
        if (userOidId != null) {
            stmt.bindString(2, userOidId);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(3, username);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(4, sex);
        }
 
        String idcard = entity.getIdcard();
        if (idcard != null) {
            stmt.bindString(5, idcard);
        }
 
        String qq = entity.getQq();
        if (qq != null) {
            stmt.bindString(6, qq);
        }
 
        String cardtype = entity.getCardtype();
        if (cardtype != null) {
            stmt.bindString(7, cardtype);
        }
 
        String depart = entity.getDepart();
        if (depart != null) {
            stmt.bindString(8, depart);
        }
 
        String yiyuan = entity.getYiyuan();
        if (yiyuan != null) {
            stmt.bindString(9, yiyuan);
        }
 
        String zhuanye = entity.getZhuanye();
        if (zhuanye != null) {
            stmt.bindString(10, zhuanye);
        }
 
        String zt = entity.getZt();
        if (zt != null) {
            stmt.bindString(11, zt);
        }
 
        String xueli = entity.getXueli();
        if (xueli != null) {
            stmt.bindString(12, xueli);
        }
 
        String shengri = entity.getShengri();
        if (shengri != null) {
            stmt.bindString(13, shengri);
        }
 
        String zhicheng = entity.getZhicheng();
        if (zhicheng != null) {
            stmt.bindString(14, zhicheng);
        }
 
        String zhichenglv = entity.getZhichenglv();
        if (zhichenglv != null) {
            stmt.bindString(15, zhichenglv);
        }
 
        String minzu = entity.getMinzu();
        if (minzu != null) {
            stmt.bindString(16, minzu);
        }
 
        String zhengzi = entity.getZhengzi();
        if (zhengzi != null) {
            stmt.bindString(17, zhengzi);
        }
 
        String zhiwu = entity.getZhiwu();
        if (zhiwu != null) {
            stmt.bindString(18, zhiwu);
        }
 
        String xuewei = entity.getXuewei();
        if (xuewei != null) {
            stmt.bindString(19, xuewei);
        }
 
        String diaodongzhuangtai = entity.getDiaodongzhuangtai();
        if (diaodongzhuangtai != null) {
            stmt.bindString(20, diaodongzhuangtai);
        }
 
        String byyuabxiao = entity.getByyuabxiao();
        if (byyuabxiao != null) {
            stmt.bindString(21, byyuabxiao);
        }
 
        String usertel = entity.getUsertel();
        if (usertel != null) {
            stmt.bindString(22, usertel);
        }
 
        String userphone = entity.getUserphone();
        if (userphone != null) {
            stmt.bindString(23, userphone);
        }
 
        String useremail = entity.getUseremail();
        if (useremail != null) {
            stmt.bindString(24, useremail);
        }
 
        String pfttcNumbers = entity.getPfttcNumbers();
        if (pfttcNumbers != null) {
            stmt.bindString(25, pfttcNumbers);
        }
 
        String userZyid = entity.getUserZyid();
        if (userZyid != null) {
            stmt.bindString(26, userZyid);
        }
 
        String photo = entity.getPhoto();
        if (photo != null) {
            stmt.bindString(27, photo);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(28, addTime);
        }
 
        String bankaRes = entity.getBankaRes();
        if (bankaRes != null) {
            stmt.bindString(29, bankaRes);
        }
 
        String xkbs = entity.getXkbs();
        if (xkbs != null) {
            stmt.bindString(30, xkbs);
        }
 
        String xkrq = entity.getXkrq();
        if (xkrq != null) {
            stmt.bindString(31, xkrq);
        }
 
        String dqsj = entity.getDqsj();
        if (dqsj != null) {
            stmt.bindString(32, dqsj);
        }
 
        String uresource = entity.getUresource();
        if (uresource != null) {
            stmt.bindString(33, uresource);
        }
 
        String beizhu = entity.getBeizhu();
        if (beizhu != null) {
            stmt.bindString(34, beizhu);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(35, state);
        }
 
        String updatetime = entity.getUpdatetime();
        if (updatetime != null) {
            stmt.bindString(36, updatetime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EmployeeEntity entity) {
        stmt.clearBindings();
 
        String usernumber = entity.getUsernumber();
        if (usernumber != null) {
            stmt.bindString(1, usernumber);
        }
 
        String userOidId = entity.getUserOidId();
        if (userOidId != null) {
            stmt.bindString(2, userOidId);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(3, username);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(4, sex);
        }
 
        String idcard = entity.getIdcard();
        if (idcard != null) {
            stmt.bindString(5, idcard);
        }
 
        String qq = entity.getQq();
        if (qq != null) {
            stmt.bindString(6, qq);
        }
 
        String cardtype = entity.getCardtype();
        if (cardtype != null) {
            stmt.bindString(7, cardtype);
        }
 
        String depart = entity.getDepart();
        if (depart != null) {
            stmt.bindString(8, depart);
        }
 
        String yiyuan = entity.getYiyuan();
        if (yiyuan != null) {
            stmt.bindString(9, yiyuan);
        }
 
        String zhuanye = entity.getZhuanye();
        if (zhuanye != null) {
            stmt.bindString(10, zhuanye);
        }
 
        String zt = entity.getZt();
        if (zt != null) {
            stmt.bindString(11, zt);
        }
 
        String xueli = entity.getXueli();
        if (xueli != null) {
            stmt.bindString(12, xueli);
        }
 
        String shengri = entity.getShengri();
        if (shengri != null) {
            stmt.bindString(13, shengri);
        }
 
        String zhicheng = entity.getZhicheng();
        if (zhicheng != null) {
            stmt.bindString(14, zhicheng);
        }
 
        String zhichenglv = entity.getZhichenglv();
        if (zhichenglv != null) {
            stmt.bindString(15, zhichenglv);
        }
 
        String minzu = entity.getMinzu();
        if (minzu != null) {
            stmt.bindString(16, minzu);
        }
 
        String zhengzi = entity.getZhengzi();
        if (zhengzi != null) {
            stmt.bindString(17, zhengzi);
        }
 
        String zhiwu = entity.getZhiwu();
        if (zhiwu != null) {
            stmt.bindString(18, zhiwu);
        }
 
        String xuewei = entity.getXuewei();
        if (xuewei != null) {
            stmt.bindString(19, xuewei);
        }
 
        String diaodongzhuangtai = entity.getDiaodongzhuangtai();
        if (diaodongzhuangtai != null) {
            stmt.bindString(20, diaodongzhuangtai);
        }
 
        String byyuabxiao = entity.getByyuabxiao();
        if (byyuabxiao != null) {
            stmt.bindString(21, byyuabxiao);
        }
 
        String usertel = entity.getUsertel();
        if (usertel != null) {
            stmt.bindString(22, usertel);
        }
 
        String userphone = entity.getUserphone();
        if (userphone != null) {
            stmt.bindString(23, userphone);
        }
 
        String useremail = entity.getUseremail();
        if (useremail != null) {
            stmt.bindString(24, useremail);
        }
 
        String pfttcNumbers = entity.getPfttcNumbers();
        if (pfttcNumbers != null) {
            stmt.bindString(25, pfttcNumbers);
        }
 
        String userZyid = entity.getUserZyid();
        if (userZyid != null) {
            stmt.bindString(26, userZyid);
        }
 
        String photo = entity.getPhoto();
        if (photo != null) {
            stmt.bindString(27, photo);
        }
 
        String addTime = entity.getAddTime();
        if (addTime != null) {
            stmt.bindString(28, addTime);
        }
 
        String bankaRes = entity.getBankaRes();
        if (bankaRes != null) {
            stmt.bindString(29, bankaRes);
        }
 
        String xkbs = entity.getXkbs();
        if (xkbs != null) {
            stmt.bindString(30, xkbs);
        }
 
        String xkrq = entity.getXkrq();
        if (xkrq != null) {
            stmt.bindString(31, xkrq);
        }
 
        String dqsj = entity.getDqsj();
        if (dqsj != null) {
            stmt.bindString(32, dqsj);
        }
 
        String uresource = entity.getUresource();
        if (uresource != null) {
            stmt.bindString(33, uresource);
        }
 
        String beizhu = entity.getBeizhu();
        if (beizhu != null) {
            stmt.bindString(34, beizhu);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(35, state);
        }
 
        String updatetime = entity.getUpdatetime();
        if (updatetime != null) {
            stmt.bindString(36, updatetime);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public EmployeeEntity readEntity(Cursor cursor, int offset) {
        EmployeeEntity entity = new EmployeeEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // usernumber
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userOidId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // username
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // sex
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // idcard
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // qq
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // cardtype
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // depart
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // yiyuan
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // zhuanye
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // zt
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // xueli
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // shengri
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // zhicheng
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // zhichenglv
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // minzu
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // zhengzi
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // zhiwu
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // xuewei
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // diaodongzhuangtai
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // byyuabxiao
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // usertel
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // userphone
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // useremail
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // pfttcNumbers
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // userZyid
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // photo
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27), // addTime
            cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28), // bankaRes
            cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29), // xkbs
            cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30), // xkrq
            cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31), // dqsj
            cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32), // uresource
            cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33), // beizhu
            cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34), // state
            cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35) // updatetime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EmployeeEntity entity, int offset) {
        entity.setUsernumber(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUserOidId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUsername(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSex(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIdcard(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setQq(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCardtype(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDepart(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setYiyuan(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setZhuanye(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setZt(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setXueli(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setShengri(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setZhicheng(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setZhichenglv(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setMinzu(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setZhengzi(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setZhiwu(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setXuewei(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setDiaodongzhuangtai(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setByyuabxiao(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setUsertel(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setUserphone(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setUseremail(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setPfttcNumbers(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setUserZyid(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setPhoto(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setAddTime(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
        entity.setBankaRes(cursor.isNull(offset + 28) ? null : cursor.getString(offset + 28));
        entity.setXkbs(cursor.isNull(offset + 29) ? null : cursor.getString(offset + 29));
        entity.setXkrq(cursor.isNull(offset + 30) ? null : cursor.getString(offset + 30));
        entity.setDqsj(cursor.isNull(offset + 31) ? null : cursor.getString(offset + 31));
        entity.setUresource(cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32));
        entity.setBeizhu(cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33));
        entity.setState(cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34));
        entity.setUpdatetime(cursor.isNull(offset + 35) ? null : cursor.getString(offset + 35));
     }
    
    @Override
    protected final String updateKeyAfterInsert(EmployeeEntity entity, long rowId) {
        return entity.getUsernumber();
    }
    
    @Override
    public String getKey(EmployeeEntity entity) {
        if(entity != null) {
            return entity.getUsernumber();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(EmployeeEntity entity) {
        return entity.getUsernumber() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
