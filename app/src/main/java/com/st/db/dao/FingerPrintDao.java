package com.st.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.st.db.entity.FingerPrint;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FINGER_PRINT".
*/
public class FingerPrintDao extends AbstractDao<FingerPrint, String> {

    public static final String TABLENAME = "FINGER_PRINT";

    /**
     * Properties of entity FingerPrint.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property SlotId = new Property(1, int.class, "slotId", false, "SLOT_ID");
        public final static Property FingerData = new Property(2, byte[].class, "fingerData", false, "FINGER_DATA");
        public final static Property EmpNumber = new Property(3, String.class, "empNumber", false, "EMP_NUMBER");
    }


    public FingerPrintDao(DaoConfig config) {
        super(config);
    }
    
    public FingerPrintDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FINGER_PRINT\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"SLOT_ID\" INTEGER NOT NULL ," + // 1: slotId
                "\"FINGER_DATA\" BLOB," + // 2: fingerData
                "\"EMP_NUMBER\" TEXT);"); // 3: empNumber
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FINGER_PRINT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FingerPrint entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
        stmt.bindLong(2, entity.getSlotId());
 
        byte[] fingerData = entity.getFingerData();
        if (fingerData != null) {
            stmt.bindBlob(3, fingerData);
        }
 
        String empNumber = entity.getEmpNumber();
        if (empNumber != null) {
            stmt.bindString(4, empNumber);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FingerPrint entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
        stmt.bindLong(2, entity.getSlotId());
 
        byte[] fingerData = entity.getFingerData();
        if (fingerData != null) {
            stmt.bindBlob(3, fingerData);
        }
 
        String empNumber = entity.getEmpNumber();
        if (empNumber != null) {
            stmt.bindString(4, empNumber);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public FingerPrint readEntity(Cursor cursor, int offset) {
        FingerPrint entity = new FingerPrint( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.getInt(offset + 1), // slotId
            cursor.isNull(offset + 2) ? null : cursor.getBlob(offset + 2), // fingerData
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // empNumber
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FingerPrint entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setSlotId(cursor.getInt(offset + 1));
        entity.setFingerData(cursor.isNull(offset + 2) ? null : cursor.getBlob(offset + 2));
        entity.setEmpNumber(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final String updateKeyAfterInsert(FingerPrint entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(FingerPrint entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(FingerPrint entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
