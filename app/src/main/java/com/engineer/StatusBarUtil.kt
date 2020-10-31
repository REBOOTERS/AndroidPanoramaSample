package com.engineer

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.engineer.panorama.util.DeviceUtil

object StatusBarUtil {

    fun translucentStatusBar(activity: Activity) {
        val window  = activity.window

        // navigation_bar 不透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        // 全屏

        // 全屏
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // StatusBar 去除颜色
            cleanStatusBarBackgroundColor(activity)
        } else {
            // Content 填充 & StatusBar 颜色叠加
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        //
        val contentView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val contentChild = contentView.getChildAt(0)
        contentChild?.fitsSystemWindows =false
    }

    private fun cleanStatusBarBackgroundColor(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 设置 StatusBar 的文本颜色为黑色
     *
     * @link <a href="http://www.jianshu.com/p/932568ed31af"/>
     */
    fun setStatusBarLightMode(activity: Activity, lightMode: Boolean) {
        DeviceUtil.setMIUIStatusBarLightMode(activity, lightMode)
        DeviceUtil.setFlymeStatusBarLightMode(activity, lightMode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
            var flag = decorView.systemUiVisibility
            if (lightMode) {
                flag = flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flag = flag and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = flag
        }
    }

    fun getStatusBarHeight(context: Context):Int {
        val resourceId = context.resources
                .getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }
}