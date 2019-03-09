package com.framework.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.framework.enity.AppUpdateInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {

	private Context mContext;
	//返回的安装包url
	private String apkUrl = "" ;
	
    private String apkName = "学分打卡.apk" ;
    
    private String apkPath  = "" ; 
    
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;
    
    private static final int DOWN_OVER = 2;
    
    private int progress;
    
    private Thread downLoadThread;
    
    private boolean interceptFlag = false;
	String downLoadPath ;
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				if( mProgress != null && dialog != null ){
					mProgress.setProgress(progress);
					dialog.setProgress(progress);
					dialog.getBuilder().content("正在准备安装...") ;
				}
				break;
			case DOWN_OVER:
				if( dialog != null && dialog.isShowing()){
					dialog.dismiss();
				}
				installApk();
				break;
			default:
				break;
			}
    	};
    };
    
	public UpdateManager(Context context) {
		this.mContext = context;
		downLoadPath = ProjectPathManager.getInstance(context).getAppFilePath() ;
		this.apkPath = downLoadPath + "/" + apkName ;
	}
	
	
	/**
	 * 设置app 下载路径
	 * @param apkUrl
	 */
	public void setApkUrl(String apkUrl){
		this.apkUrl = apkUrl ; 
	}
	
	public void setApkName(String apkName){
		this.apkName = apkName ; 
		this.apkPath = downLoadPath + "/" + apkName ;
	}
	
	/**
	 * 检测程序是否需要更新
	 * @param serverVersion
	 * @return false 不需要更新 true 需要更新
	 */
	public boolean checkUpdateInfo( String serverVersion){
		// 检测是否需要更新apk
		String currVersion = getVersion(mContext) ;
		try{
			float f_currentVersion = Float.valueOf( currVersion ) ;
			float f_serverVersion = Float.valueOf( serverVersion ) ;
			if( f_currentVersion >= f_serverVersion ){
				return false ;
			}else{
				return true ;
			}
		}catch (NumberFormatException e){
			return false ;
		}
	}

	public String  getVersion(Context context ) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return "Version";
		}
	}

	public void updateApp(AppUpdateInfo info){
		showNoticeDialog(info);
	}
	
	private void showNoticeDialog(AppUpdateInfo info){
		new MaterialDialog.Builder(mContext)
				.title(info.getTitle() + "")
				.titleGravity(GravityEnum.CENTER)
				.content(info.getDesc() + "")
				.positiveText("立即更新")
				.negativeText("以后再说")
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						dialog.dismiss();
						showDownloadDialog() ;
					}
				})
				.onNegative(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						dialog.dismiss();
						cancel() ;
					}
				}).canceledOnTouchOutside( false )
				.show();
	}
	MaterialDialog dialog ;
	private void showDownloadDialog(){
		new MaterialDialog.Builder(mContext)
				.title("正在更新")
				.content("请稍后...")
				.contentGravity(GravityEnum.CENTER)
				.progress(false, 100 , true)
				.cancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						interceptFlag = true;
						cancel() ;
					}
				})
				.showListener(new DialogInterface.OnShowListener() {
					@Override
					public void onShow(DialogInterface dialogInterface) {
						dialog = (MaterialDialog) dialogInterface;
						mProgress = dialog.getProgressBar() ;
						downloadApk();
					}
				})
				.canceledOnTouchOutside( false ).
				show();

	}
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				String apkFile = apkPath;
				File ApkFile = new File(apkFile);
				if( !ApkFile.exists() ){
					ApkFile.getParentFile().mkdirs() ;
					ApkFile.createNewFile() ; 
				}else{
					ApkFile.delete() ; 
					ApkFile.createNewFile() ;
				}
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){
		    			//下载完成通知安装
		    			mHandler.sendEmptyMessage(DOWN_OVER);
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				onFaild();
			} catch(IOException e){
				onFaild(); 
			}
		}
	};
	
	 /**
     * 下载apk
     */
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	 /**
     * 安装apk
     */
	private void installApk(){
		File apkfile = new File(apkPath);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	}
	
	private IUpdateListener listener ; 
	
	public void setListener(IUpdateListener listener) {
		this.listener = listener;
	}

	public interface IUpdateListener{
		void onFaild() ; 
		void onFinish() ; 
		void onCancel() ; 
	}
	
	private void onFaild(){
		if(listener == null) return ; 
		listener.onFaild(); 
	}
	
	private void onFinish(){
		if(listener == null) return ; 
		listener.onFinish(); 
	}
	
	private void cancel(){
		if(listener == null) return ; 
		listener.onCancel();
	}
}