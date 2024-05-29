package com.engineer.panorama.ui

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.engineer.StatusBarUtil
import com.engineer.panorama.util.dp

abstract class BaseFullScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(provideLayout())
        StatusBarUtil.translucentStatusBar(this)
        addExitButton()
    }

    abstract fun provideLayout(): Int

    private fun addExitButton() {
        val button = Button(this)
        button.text = "exit"
        button.setOnClickListener { finish() }
        val p = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        p.marginEnd = 15.dp
        p.topMargin = 65.dp
        p.gravity = Gravity.END
        addContentView(button, p)
    }
}