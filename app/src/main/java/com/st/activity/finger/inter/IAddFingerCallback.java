package com.st.activity.finger.inter;

/**
 * Created by cgw on 2018/3/27.
 * 指纹录入
 */

public interface IAddFingerCallback {

    void onAddFinger( int finger_index , byte[] finger_data) ;
}
