package com.engineer.panorama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engineer.panorama.ui.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        map.setOnClickListener {
            startActivity(Intent(this, MapViewActivity::class.java))
        }

        map_list.setOnClickListener {
            startActivity(Intent(this, ListMapViewActivity::class.java))
        }

        pano_list.setOnClickListener {
            startActivity(Intent(this, ListPanoViewActivity::class.java))
        }

        pano_pager.setOnClickListener {
            startActivity(Intent(this, PanoPagerActivity::class.java))
        }

        multi_pano.setOnClickListener {
            startActivity(Intent(this, MultiPanoViewActivity::class.java))
        }
    }
}
