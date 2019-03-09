package com.st.db.entity;



import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by cgw on 2018/3/27.
 * 指纹信息
 */

@Entity
public class FingerPrint {

    @Id
    private String id;
    private int slotId;
    private byte[] fingerData;
    private String empNumber;
    @Generated(hash = 1733872292)
    public FingerPrint(String id, int slotId, byte[] fingerData, String empNumber) {
        this.id = id;
        this.slotId = slotId;
        this.fingerData = fingerData;
        this.empNumber = empNumber;
    }
    @Generated(hash = 1565153282)
    public FingerPrint() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getSlotId() {
        return this.slotId;
    }
    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }
    public byte[] getFingerData() {
        return this.fingerData;
    }
    public void setFingerData(byte[] fingerData) {
        this.fingerData = fingerData;
    }
    public String getEmpNumber() {
        return this.empNumber;
    }
    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }


}
