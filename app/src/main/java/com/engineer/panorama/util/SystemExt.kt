package com.engineer.panorama.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources

/**
 * Created on 2020/9/5.
 * @author rookie
 */


val Number.dp get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()

val Number.sp get() = (toInt() * Resources.getSystem().displayMetrics.scaledDensity)

fun <T, R> T.easy(block: (T) -> R, ifNull: () -> Unit) {
    if (this != null) {
        block(this)
    } else {
        ifNull()
    }
}


fun Context.getNightMode(): Int {
    return this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
}

val screenWidth = Resources.getSystem().displayMetrics.widthPixels
val screenHeight = Resources.getSystem().displayMetrics.heightPixels