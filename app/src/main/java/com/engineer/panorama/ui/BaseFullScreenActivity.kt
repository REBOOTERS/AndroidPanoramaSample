package com.engineer.panorama.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.engineer.StatusBarUtil

abstract class BaseFullScreenActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideLayout())
        StatusBarUtil.translucentStatusBar(this)
    }

    abstract fun provideLayout():Int
}