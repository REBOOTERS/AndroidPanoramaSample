package com.baidu.lbsapi.panodemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;

import com.baidu.pano.platform.comapi.map.InnerPanoramaView;

/**
 * @author: zhuyongging
 * @since: 2018-11-25
 */
public abstract class ScreenView extends InnerPanoramaView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    private Thread mThread;

    public ScreenView(Context context) {
        super(context);
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mThread = new Thread(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void start() {
        if (mThread != null) {
            mThread.start();
        }
    }

    @Override
    public void run() {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        doDraw(canvas);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void doDraw(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(new RectF(100, 100, 1000, 550), mPaint);
    }

    //调用该方法将doDraw绘制的图案绘制在自己的canvas上
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        doDraw(canvas);
        return bitmap;
    }
}

