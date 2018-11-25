package com.baidu.lbsapi.panodemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by rookie on 2016/11/10.
 * <p>
 * 文件读取工具类
 */


public class FileUtil {
    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @param name
     * @return
     */
    public static String savaBitmap2SDcard(Context context, Bitmap bitmap, String name) {

        String result = "";
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "DCIM" + File.separator + "Camera" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String filename = name + ".jpg";
        File file = new File(fileDir, filename);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            result = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获取bitmap
     *
     * @param filename
     * @return
     */
    public static Bitmap getBitmapFormSDcard(String filename) {
        Bitmap bitmap;
        String filepath = Environment.getExternalStorageDirectory() + File.separator + "Screenshots" + File.separator + filename;
        bitmap = BitmapFactory.decodeFile(filepath);
        return bitmap;
    }


    /**
     * 从本地assets  文件读取 json 文件，模拟网络请求结果
     *
     * @return
     */
    public static String getLocalResponse(Context mContext, String FileName) {

        String result = "";

        try {
            InputStream inputStream = mContext.getAssets().open(FileName);
            StringBuffer sb = new StringBuffer();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            inputStream.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 保存String 到手机存储
     *
     * @param str
     * @param fileName
     * @return
     */
    public static boolean saveStrToSDCard(String str, String fileName) {
        boolean success = false;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String path = Environment.getExternalStorageDirectory() + File.separator + fileName;

            File file = new File(path);
            try {
                OutputStream os = new FileOutputStream(file);
                byte[] b = str.getBytes();
                os.write(b);
                os.close();
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return success;
    }

}
