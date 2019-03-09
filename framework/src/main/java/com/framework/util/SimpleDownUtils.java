package com.framework.util;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 13971 on 2017/7/14.
 * 简单的下载器
 */

public class SimpleDownUtils {

    private boolean interceptFlag = false ;

    public boolean isInterceptFlag() {
        return interceptFlag;
    }

    public void setInterceptFlag(boolean interceptFlag) {
        this.interceptFlag = interceptFlag;
    }

    public interface  Callback{
        void onFaild() ;
        void onSuccess(String path) ;
    }

    /**
     *
     * @param downUrl 下载路径
     * @param filePath 保存的文件路径（包含文件名）
     * @param callback 下载回调方法
     */
    public void downUrl(final String downUrl ,  final String filePath ,final Callback callback){
        Runnable mdownApkRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("showmap" , "downUrl = " + downUrl ) ;
                    URL url = new URL(downUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    File file = new File(filePath);
                    if( !file.exists() ){
                        file.getParentFile().mkdirs() ;
                        file.createNewFile() ;
                    }else{
                        file.delete() ;
                        file.createNewFile() ;
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    int count = 0;
                    byte buf[] = new byte[1024];

                    do{
                        int numread = is.read(buf);
                        count += numread;
                        //更新进度
                        if(numread <= 0){
                            //下载完成通知安装
                            break;
                        }
                        fos.write(buf,0,numread);
                    }while(!interceptFlag);//点击取消就停止下载.
                    fos.close();
                    is.close();

                    if( null != callback ){
                        callback.onSuccess(file.getAbsolutePath());
                    }

                } catch (MalformedURLException e) {
                    if( null != callback ){
                        callback.onFaild();
                    }
                } catch(IOException e){
                    if( null != callback ){
                        callback.onFaild();
                    }
                }
            }
        };
        Thread thread = new Thread(mdownApkRunnable) ;
        thread.start();
    }

}
