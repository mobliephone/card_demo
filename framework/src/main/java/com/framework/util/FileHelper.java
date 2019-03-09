package com.framework.util;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Break
 * // TODO
 * 2015/8/9 0009.
 */
public class FileHelper {

    /**
     * 读取destName文件夹目录路径
     * @param destName
     * @return
     */
    public static String getRootPath(String destName) {
//		File sdDir = null;
        // 判断SD卡是否可以读写。
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在

        File sdDir1 = Environment.getExternalStorageDirectory();// 如果没有SD卡，则存放于内存卡根目录
//		File sdDir2 = Environment.getRootDirectory();// 获取跟目录
        String sd = sdDir1.toString();

        if (sdCardExist && isContainMapFile(sdDir1 ,destName)) {
            return sdDir1.toString();
        } else {
            File file = new File(sd.split("/")[1]);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    if (isContainMapFile(files[i] , destName )) {
                        return files[i].getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取Android 所有的存储设备路径
     * @return
     */
    public List<String> getStorages() {
        File sdDir1 = Environment.getExternalStorageDirectory();
        String sd = sdDir1.toString();
        File file = new File(sd.split("/")[1]);
        File[] storagesF = file.listFiles();
        List<String> storages = new ArrayList<String>(storagesF.length);
        for (int i = 0; i < storagesF.length; i++) {
            storages.add(storagesF[i].getAbsolutePath());
        }
        return storages;
    }

    /**
     * 判断file文件夹下是否包含destName 的文件
     * @param file
     * @param destName
     * @return
     */
    private static boolean isContainMapFile(File file, String destName) {
        File[] files = file.listFiles();
        if (null == files) {
            return false;
        }
        for (int i = 0; i < files.length; i++) {
            if (destName.equals(files[i].getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     *  获取外部存储根目录
     * @return
     */
    public static String getRootStoryPath() {
        File sdDir ;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) // 如果SD卡存在，则获取跟目录
        {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            sdDir = Environment.getRootDirectory();// 如果没有SD卡，则存放于内存卡根目录
        }
        return sdDir.toString();
    }

    /**
     * 把InputStream流拷贝到指定的文件目录下fileName
     * @param is
     * @param fileName
     * @throws IOException
     */
    public static void copyFile(InputStream is, String fileName)
            throws IOException {
        BufferedOutputStream outBuff = null;
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        try {
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(file));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = is.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();

        } finally {
            // 关闭流
            if (is != null)
                is.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
}
