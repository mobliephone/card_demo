package com.framework.manager;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by continue on 2016/9/24.
 * 程序缓存数据管理 基本字段的缓存
 */
public class AppCacheManager {
    private static AppCacheManager instance ;

    public static AppCacheManager getInstance( ){
        if( instance == null ){
            instance = new AppCacheManager() ;
        }
        return instance ;
    }

    private AppCacheManager(){

    }

    public void init(Context context ){
        this.context = context ;
        onCreate() ;
    }
    private static final String SHARED_PREFEREANCES_PREFIX = "_app_";
    private SharedPreferences _mPrefs;
    private Context context  ;

    public void onCreate(){
//        _mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        _mPrefs = context.getSharedPreferences("user",MODE_PRIVATE);
    }

    public void pushStringToPrefs(String key,String value){
        SharedPreferences.Editor editor = _mPrefs.edit();
        editor.putString(SHARED_PREFEREANCES_PREFIX + key, value);
        editor.commit();
    }

    public void removeStringToPrefs(String key){
        SharedPreferences.Editor editor = _mPrefs.edit();
        editor.remove(SHARED_PREFEREANCES_PREFIX + key);
        editor.commit();
    }

    public String popStringFromPrefs(String key,String defaultStr){
        return _mPrefs.getString(SHARED_PREFEREANCES_PREFIX + key, defaultStr);
    }

    public String popStringFromPrefs(String key){
        return popStringFromPrefs(key, "");
    }

    public void pushIntToPrefs(String key,int value){
        SharedPreferences.Editor editor = _mPrefs.edit();
        editor.putInt(SHARED_PREFEREANCES_PREFIX + key, value);
        editor.commit();
    }

    public void pushLongToPrefs(String key,long value){
        SharedPreferences.Editor editor = _mPrefs.edit();
        editor.putFloat(SHARED_PREFEREANCES_PREFIX + key, value);
        editor.commit();
    }

    public long popLongFromPrefs(String key){
        return _mPrefs.getLong(SHARED_PREFEREANCES_PREFIX + key, 0);
    }


    public void pushBooleanToPrefs(String key,boolean value){
        SharedPreferences.Editor editor = _mPrefs.edit();
        editor.putBoolean(SHARED_PREFEREANCES_PREFIX + key, value);
        editor.commit();
    }


    public int popIntFromPrefs(String key){
        return _mPrefs.getInt(SHARED_PREFEREANCES_PREFIX + key, 0);
    }

    public int popIntFromPrefs(String key,int defaultValse){
        return _mPrefs.getInt(SHARED_PREFEREANCES_PREFIX + key, defaultValse);
    }

    public boolean popBooleanFromPrefs(String key){
        return popBooleanFromPrefs(key,false);
    }
    public boolean popBooleanFromPrefs(String key,boolean defaultValue){
        return _mPrefs.getBoolean(SHARED_PREFEREANCES_PREFIX + key, defaultValue);
    }

    public void pushFloatToPrefs(String key,float value){
        SharedPreferences.Editor editor = _mPrefs.edit();
        editor.putFloat(SHARED_PREFEREANCES_PREFIX + key, value);
        editor.commit();
    }

    public float popFloatFromPrefs(String key){
        return popFloatFromPrefs(key, 0);
    }

    public float popFloatFromPrefs(String key,float defaultValue){
        return _mPrefs.getFloat(SHARED_PREFEREANCES_PREFIX + key, defaultValue);
    }

    public SharedPreferences getPreference(){
        return _mPrefs;
    }
}
