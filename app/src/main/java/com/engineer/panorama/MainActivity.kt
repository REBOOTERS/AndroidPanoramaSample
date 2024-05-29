package com.engineer.panorama

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import com.engineer.google.GoogleMapsActivity
import com.engineer.panorama.ui.FullActivity
import com.engineer.panorama.ui.ListMapViewActivity
import com.engineer.panorama.ui.ListPanoViewActivity
import com.engineer.panorama.ui.MapViewActivity
import com.engineer.panorama.ui.MultiPanoViewActivity
import com.engineer.panorama.ui.PanoPagerActivity
import com.engineer.panorama.util.dp


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.map).setOnClickListener {
            startActivity(Intent(this, MapViewActivity::class.java))
        }

        findViewById<View>(R.id.full_screen).setOnClickListener {
            startActivity(Intent(this, FullActivity::class.java))
        }

        findViewById<View>(R.id.map_list).setOnClickListener {
            startActivity(Intent(this, ListMapViewActivity::class.java))
        }

        findViewById<View>(R.id.pano_list).setOnClickListener {
            startActivity(Intent(this, ListPanoViewActivity::class.java))
        }

        findViewById<View>(R.id.pano_pager).setOnClickListener {
            startActivity(Intent(this, PanoPagerActivity::class.java))
        }

        findViewById<View>(R.id.multi_pano).setOnClickListener {
            startActivity(Intent(this, MultiPanoViewActivity::class.java))
        }

        findViewById<View>(R.id.google_map).setOnClickListener {
            startActivity(Intent(this, GoogleMapsActivity::class.java))
        }

        val textView = TextView(this)
        textView.text = "100.dp = ${100.dp}"
        val k = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        k.setMargins(10.dp)

        val k1 = FrameLayout(this)
        k1.setBackgroundColor(Color.GREEN)
        val kk = FrameLayout.LayoutParams(10.dp, 10.dp)

        val area = FrameLayout(this)
        area.setBackgroundColor(Color.MAGENTA)
        val pp = FrameLayout.LayoutParams(100.dp, 100.dp)
        pp.gravity = Gravity.END or Gravity.BOTTOM

        area.addView(k1, kk)
        area.addView(textView, k)
        addContentView(area, pp)


        val exit = Button(this)
        exit.text = "exit"
        exit.setOnClickListener { finish() }
        val p = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        p.marginEnd = 100.dp
        p.bottomMargin = 100.dp
        p.gravity = Gravity.END or Gravity.BOTTOM
        addContentView(exit, p)
    }
}
