package com.st.utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseArray;

import com.st.CMEApplication;
import com.st.R;


/**
 * 作者：created by cgw on 2018/4/8 14:33
 * 类注释：提示音管理类
 */
public class HintsManager {

    private static HintsManager hintsManager;

    private SoundPool mSoundPool ;

    private SparseArray<Integer> soundPoolMap;

    //打卡成功
    private final int KEY_SOUND_SUCCESS = 1;

    //打卡失败
    private final int KEY_SOUND_FAIL = 2;

    //迟到打卡
    private final int KEY_SOUND_LATE = 3;

    //指纹信息录入成功
    private final int KEY_SOUND_FINGER_SUCCESS = 4;

    //指纹信息录入失败
    private final int KEY_SOUND_FINGER_FAIL = 5;

    //医通卡号录入成功
    private final int KEY_SOUND_WRITE_CARD_SUCCESS = 6;

    //医通卡号录入失败
    private final int KEY_SOUND_WRITE_CARD_FAIL = 7;

    //当前时间段不可进行上课打卡
    private final int KEY_SOUND_PUNCH_CARD_ERROR_TIME = 8;

    //此人已经打过卡了
    private final int KEY_SOUND_PUNCH_CARD_ALREADY_CARD = 9;

    public static synchronized HintsManager getInstance(){
        if (hintsManager == null){
            hintsManager = new HintsManager();
        }

        return hintsManager;
    }

    public void init(Context context){

        mSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        soundPoolMap = new SparseArray();

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

            }
        });

        soundPoolMap.put(KEY_SOUND_SUCCESS, mSoundPool.load(context,R.raw.punch_card_success,1));
        soundPoolMap.put(KEY_SOUND_FAIL, mSoundPool.load(context,R.raw.punch_card_fail,1));
        soundPoolMap.put(KEY_SOUND_LATE, mSoundPool.load(context,R.raw.punch_card_late,1));
        soundPoolMap.put(KEY_SOUND_FINGER_SUCCESS, mSoundPool.load(context,R.raw.finger_print_success,1));
        soundPoolMap.put(KEY_SOUND_FINGER_FAIL, mSoundPool.load(context,R.raw.finger_print_fail,1));
        soundPoolMap.put(KEY_SOUND_WRITE_CARD_SUCCESS, mSoundPool.load(context,R.raw.write_card_success,1));
        soundPoolMap.put(KEY_SOUND_WRITE_CARD_FAIL, mSoundPool.load(context,R.raw.write_card_fail,1));
        soundPoolMap.put(KEY_SOUND_PUNCH_CARD_ERROR_TIME, mSoundPool.load(context,R.raw.punch_card_error_time,1));
        soundPoolMap.put(KEY_SOUND_PUNCH_CARD_ALREADY_CARD, mSoundPool.load(context,R.raw.punch_card_already_card,1));

    }

    public void playVoice(final int type){
        if (1 == type){
            mSoundPool.play(soundPoolMap.get(KEY_SOUND_SUCCESS), 1, 1, 0, 0, 1);

        } else if (2 == type){
            mSoundPool.play(soundPoolMap.get(KEY_SOUND_FAIL), 1, 1, 0, 0, 1);

        } else if (3 == type){
            mSoundPool.play(soundPoolMap.get(KEY_SOUND_LATE), 1, 1, 0, 0, 1);

        } else if (4 == type){
            mSoundPool.play(soundPoolMap.get(KEY_SOUND_FINGER_SUCCESS), 1, 1, 0, 0, 1);

        } else if (5 == type){
            mSoundPool.play(soundPoolMap.get(KEY_SOUND_FINGER_FAIL), 1, 1, 0, 0, 1);

        } else if (6 == type){
            mSoundPool.play(soundPoolMap.get(KEY_SOUND_WRITE_CARD_SUCCESS), 1, 1, 0, 0, 1);

        } else if (7 == type){
            mSoundPool.play(soundPoolMap.get(KEY_SOUND_WRITE_CARD_FAIL), 1, 1, 0, 0, 1);

        } else if (8 == type){
            mSoundPool.play(soundPoolMap.get(KEY_SOUND_PUNCH_CARD_ERROR_TIME), 1, 1, 0, 0, 1);

        } else if (9 == type){
            mSoundPool.play(soundPoolMap.get(KEY_SOUND_PUNCH_CARD_ALREADY_CARD), 1, 1, 0, 0, 1);

        }
    }

}
