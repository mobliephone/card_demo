package com.framework.manager;

import android.content.Context;

import com.framework.util.AppInfoUtil;
import com.framework.util.FileHelper;

import java.io.File;

public class ProjectPathManager {
	
	private static ProjectPathManager instatnce ; 
	private static String LOG_PATH_NAME = "LOG" ;
	private static String VOICE_PATH_NAME = "VOICE" ;
	private static String DB_PATH_NAME = "DB" ;
	private static String PHOTO_PATH_NAME = "PHOTOS";
	private static String FILE_SEPARATOR ="/" ;

	public static final String DBNAME = "st_cme.db" ;

	private  String appName ; 
	private Context context ; 
	private String appRoot ; 
	public static ProjectPathManager getInstance(Context context){
		if( instatnce == null ){
			instatnce = new ProjectPathManager(context) ; 
		}
		return instatnce ; 
	}
	
	private ProjectPathManager(Context context){
		this.context = context ; 
	}

	/**
	 * 方法		app文件结构初始化
	 * 日期		2017年2月23日
	 * 作者		continue
	 */
	public void init( ){
		appName = AppInfoUtil.getAppName(context) ;
		appRoot = FileHelper.getRootPath(appName) ;
        if( appRoot == null ) {
        	appRoot = FileHelper.getRootStoryPath() ; 
        }
	}
	
	/**
	 * 方法		获取app日志路径
	 * 日期		2017年2月23日
	 * 作者		continue
	 */
	public String getLogPat(){
		String logPath = appRoot + FILE_SEPARATOR+ appName + FILE_SEPARATOR +LOG_PATH_NAME ;
		File temp = new File( logPath ) ; 
		if( temp.exists()){
			temp.mkdirs() ; 
		}
		return logPath ; 
	}

	public String getAppFilePath(){
		String tempPath = appRoot + FILE_SEPARATOR+ appName + FILE_SEPARATOR +VOICE_PATH_NAME ;
		File temp = new File( tempPath ) ;
		if( temp.exists()){
			temp.mkdirs() ;
		}
		return tempPath  ;
	}

	/**
	 * 获取数据库目录
	 * @return
	 */
	public String getDBPath(){
		String dbPath = appRoot + FILE_SEPARATOR+ appName + FILE_SEPARATOR +DB_PATH_NAME ;
		File temp = new File( dbPath ) ;
		if( !temp.exists()){
			boolean is = temp.mkdirs() ;
		}
		return dbPath ;
	}

	public String getDbFilePath(){
		String tempPath = appRoot + FILE_SEPARATOR+ appName + FILE_SEPARATOR +DB_PATH_NAME ;
		File temp = new File( tempPath ) ;
		if( temp.exists()){
			temp.mkdirs() ;
		}
		return tempPath  ;
	}

	/**
	 * 获取照片路径
	 * @return
	 */
	public String getPhotosFilePath(){
		String tempPath = appRoot + FILE_SEPARATOR+ appName + FILE_SEPARATOR +PHOTO_PATH_NAME ;
		File temp = new File( tempPath ) ;
		if( !temp.exists()){
			temp.mkdirs() ;
		}
		return tempPath  ;
	}

}
