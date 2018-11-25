package com.baidu.lbsapi.panodemo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by engineer on 2017/8/20.
 */

public class SavePhotoTask extends AsyncTask<String, Void, String> {

    private Context mContext;


    public SavePhotoTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPostExecute(String filepath) {
        super.onPostExecute(filepath);


        if (!TextUtils.isEmpty(filepath)) {


            Toast.makeText(mContext, "保存图片成功", Toast.LENGTH_SHORT).show();


            Intent mIntent = new Intent();
            mIntent.setAction(Intent.ACTION_VIEW);
            Uri contentUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 将文件转换成content://Uri的形式
                contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", new File(filepath));
                // 申请临时访问权限
                mIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                contentUri = Uri.fromFile(new File(filepath));
            }

            mIntent.setDataAndType(contentUri, "image/*");
//                startActivity(mIntent);
            PendingIntent mPendingIntent = PendingIntent.getActivity(mContext
                    , 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            new NotificationHelper(mContext).createNotification("点击查看", "图片保存在: " + filepath, mPendingIntent);
        } else {
            Toast.makeText(mContext, "保存图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }




    @Override
    protected String doInBackground(String... params) {

        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "IMG_" + timeStamp + ".jpg";
            File localFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + fileName);
            OutputStream mOutputStream = new FileOutputStream(localFile);
            URL url = new URL(params[0]);
            HttpURLConnection mConnection = (HttpURLConnection) url.openConnection();
            InputStream mInputStream = mConnection.getInputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = mInputStream.read(buffer)) != -1) {
                mOutputStream.write(buffer, 0, len);
            }
            mOutputStream.flush();
            mInputStream.close();
            mOutputStream.close();
            return localFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
