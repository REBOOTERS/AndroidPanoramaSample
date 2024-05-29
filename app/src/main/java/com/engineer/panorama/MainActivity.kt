package com.engineer.panorama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.engineer.google.GoogleMapsActivity
import com.engineer.panorama.ui.*


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
    }
}
