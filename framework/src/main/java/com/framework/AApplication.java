package com.framework;

import com.framework.exception.CrashHandler;
import com.framework.manager.AppCacheManager;
import com.framework.manager.ProjectPathManager;

import android.app.Application;

public class AApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		AppCacheManager.getInstance().init(getApplicationContext());
	}
	
	/**
	 * 方法		注册未处理异常捕获监听
	 * 日期		2017年2月23日
	 * 作者		continue
	 */
	public void startLogMonitor() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		crashHandler.setLogPath(ProjectPathManager.getInstance(this).getLogPat());
	}
}
