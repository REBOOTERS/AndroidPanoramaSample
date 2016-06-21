package com.baidu.lbsapi.panodemo.indoor.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * 像素尺寸工具类
 */
public class ScreenUtils {
    private static Display defaultDisplay;

    private static float mDensity = 0;
    private static float mScaledDensity = 0;
    private static int mDensityDpi;

    public static float getDensity(Context context) {
        if (mDensity == 0) {
            mDensity = context.getResources().getDisplayMetrics().density;
        }
        return mDensity;
    }

    public static int getDensityDpi(Context context) {
        if (mDensityDpi == 0) {
            mDensityDpi = context.getResources().getDisplayMetrics().densityDpi;
        }
        return mDensityDpi;
    }

    public static float getScaledDensity(Context context) {
        if (mScaledDensity == 0) {
            mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        }
        return mScaledDensity;
    }

    public static int dip2px(int dip, Context context) {
        return (int) (0.5F + getDensity(context) * dip);
    }

    public static int px2dip(int px, Context context) {
        return (int) (0.5F + px / getDensity(context));
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue, Context context) {
        return (int) (pxValue / getScaledDensity(context) + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue, Context context) {
        return (int) (spValue * getScaledDensity(context) + 0.5f);
    }

    // get screen width value int pixels
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    // get screen height value int pixels
    public static int getSreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static Display getDefaultDisplay(Context context) {
        if (defaultDisplay == null) {
            defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        }
        return defaultDisplay;
    }

    public static int getHeight(Context context) {
        return getDefaultDisplay(context).getHeight();
    }

    public static int getWidth(Context context) {
        return getDefaultDisplay(context).getWidth();
    }

    public static int percentHeight(float percent, Context context) {
        return (int) (percent * getHeight(context));
    }

    public static int percentWidth(float percent, Context context) {
        return (int) (percent * getWidth(context));
    }

}
