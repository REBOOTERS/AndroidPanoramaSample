package com.baidu.lbsapi.panodemo.indoor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.baidu.lbsapi.panodemo.R;
import com.baidu.pano.platform.plugin.PluginHttpExecutor;

/**
 * 异步图片加载
 */
public class AsyncImageView extends ImageView {

    private int mType;

    public AsyncImageView(Context context, int type) {
        super(context, null);
        mType = type;
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 加载原图
     */
    public void loadImage(String url) {
        new AsyncTask<String, Integer, Bitmap>() {

            @Override
            protected void onPreExecute() {
                if (mType == 0) {
                    setScaleType(ScaleType.CENTER);
                    setImageResource(R.drawable.baidupano_photo_default);
                }
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                try {

                    byte[] b = PluginHttpExecutor.getThumbnail(getContext(), params[0]);
                    if (b != null && b.length != 0) {

                        return BitmapFactory.decodeByteArray(b, 0, b.length);
                    } else {
                        return null;
                    }

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    setScaleType(ScaleType.CENTER_CROP);
                    setAdjustViewBounds(false);
                    setImageBitmap(bitmap);
                } else {
                    // setImageBitmap(IndoorResGlobal.IMAGELOADFAIL);
                }
            }
        }.execute(url);
    }
}
