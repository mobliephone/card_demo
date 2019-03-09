package com.st.activity.finger.help;

import android.util.Base64;
import android.util.Log;

import com.st.db.entity.EmployeeEntity;
import com.upek.android.ptapi.PtConnectionI;
import com.upek.android.ptapi.PtConstants;
import com.upek.android.ptapi.PtException;
import com.upek.android.ptapi.callback.PtGuiStateCallback;
import com.upek.android.ptapi.struct.PtFingerListItem;
import com.upek.android.ptapi.struct.PtGuiSampleImage;

import java.util.List;

/**
 * 指纹验证
 */
public abstract class OpVerifyAll extends Thread implements PtGuiStateCallback {
    
    private PtConnectionI mConn;
    private List<EmployeeEntity> listEntity;

    public OpVerifyAll(PtConnectionI conn,List<EmployeeEntity> listEntity){
        super("VerifyAllThread");
        mConn = conn;
        this.listEntity = listEntity;
    }
    
    /**
     * Callback function called by PTAPI.
     */
    public byte guiStateCallbackInvoke(int guiState, int message, byte progress,
                                       PtGuiSampleImage sampleBuffer, byte[] data) {
        String s = PtHelper.GetGuiStateCallbackMessage(guiState,message,progress);
            
        if(s != null) {
            onDisplayMessage(s);
        }

        // With sensor only solution isn't necessary because of PtCancel() presence
        return isInterrupted() ? PtConstants.PT_CANCEL : PtConstants.PT_CONTINUE;
    }
    
    /**
     * Cancel running operation
     *
     * 取消运行
     */
    @Override
    public void interrupt() {
        
        super.interrupt();
        
        try{
            mConn.cancel(0);
        } catch (PtException e){
            // Ignore errors
        }
    }

    /** 
     * Operation execution code.
     *
     * 指纹对比
     */
    @Override
    public void run() {
        try {
            int index = -1;

            // Register notification callback of operation state
            // Valid for entire PTAPI session lifetime
            mConn.setGUICallbacks(null, this);
            
            // List fingers stored in device
            PtFingerListItem[] fingerList = mConn.listAllFingers();
            
            if((fingerList == null) || (fingerList.length == 0)) {
                onDisplayMessage("No templates enrolled");
            } else {
            
                // Run verification against all templates stored at device
                // It is supposed that device contains only templates enrolled by
                // this sample. In opposite case, verifyEx() with listed templates
                // would be preferred call.
                index = mConn.verifyAll(null, null, null, null,
                        null, null, null,
                                PtConstants.PT_BIO_INFINITE_TIMEOUT, true,
                                null, null, null);
                if (-1 != index) {
                    // Display finger ID 显示手指ID
                    for(int i=0; i<fingerList.length; i++) {
                        PtFingerListItem item = fingerList[i];
                        int slotId = item.slotNr;
                        if(slotId == index) {
                            byte[] fingerData = item.fingerData;

                            if((fingerData != null) && (fingerData.length >= 1)) {
                                int fingerId = item.fingerData[0];
                                onDisplayMessage(FingerId.NAMES[fingerId] + " finger matched");

//                                Log.e("logInfo","fingerData--verify-->"+fingerData);
                            } else {
                                onDisplayMessage("No fingerData set");
                            }
                        }
                    }
                } else {
                    onDisplayMessage("No match found.");
                }
                
            }
        } catch (PtException e){
            onDisplayMessage("Verification failed - " + e.getMessage());
        }


//        verifyFingerId();

    }

    
    /** 
     * Display message. To be overridden by sample activity.
     * @param message Message text.
     */
    abstract protected void onDisplayMessage(String message);
    
    /** 
     * Called, if operation is finished.
     * @param empNumber Message text.
     */
    abstract protected void onFinished(String empNumber);


    private void verifyFingerId(){
        String empNumber = null;

        try {
            int index = -1;

            // Register notification callback of operation state
            // Valid for entire PTAPI session lifetime
            mConn.setGUICallbacks(null, OpVerifyAll.this);

            if (null == listEntity || listEntity.size() == 0){
//                onDisplayMessage("No templates enrolled");
                onDisplayMessage("初始化失败！");
            } else {
                // Run verification against all templates stored at device
                // It is supposed that device contains only templates enrolled by
                // this sample. In opposite case, verifyEx() with listed templates
                // would be preferred call.
                index = mConn.verifyAll(null, null, null, null,
                        null, null, null,
                        PtConstants.PT_BIO_INFINITE_TIMEOUT, true,
                        null, null, null);
                if (-1 != index) {
                    // Display finger ID 显示手指ID
                    for(int i=0; i<listEntity.size(); i++) {
                        EmployeeEntity entity = listEntity.get(i);
                        int slotId = Integer.valueOf(entity.getUserOidId());
                        if(slotId == index) {

                            Log.d("logInfo", "verifyFingerId--->"+entity.getUserZyid());

                            byte[] fingerData = Base64.decode(entity.getUserZyid(), Base64.DEFAULT);

//                            byte[] fingerData = (entity.getUserZyid()).getBytes();

                            if((fingerData != null) && (fingerData.length >= 1)) {

                                int fingerId = fingerData[0];
                                onDisplayMessage(FingerId.NAMES[fingerId] + "，指纹匹配！");
//                                onDisplayMessage(FingerId.NAMES[fingerId] + " finger matched");

                                empNumber = entity.getUsernumber();
//                                Log.e("logInfo","fingerData--verify-->"+fingerData);
                            } else {
//                                onDisplayMessage("No fingerData set");
                                onDisplayMessage("无法识别当前指纹信息！");
                            }
                        }
                    }
                } else {
//                    onDisplayMessage("No match found.");
                    onDisplayMessage("无法匹配当前指纹信息！");
                }

            }
        } catch (PtException e){
//            onDisplayMessage("Verification failed - " + e.getMessage());
            onDisplayMessage("指纹验证失败 - " + e.getMessage()+"！");
        }


        onFinished(empNumber);
    }
}
