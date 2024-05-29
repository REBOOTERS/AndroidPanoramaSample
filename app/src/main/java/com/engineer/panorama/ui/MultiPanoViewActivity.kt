package com.engineer.panorama.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baidu.lbsapi.panoramaview.PanoramaView
import com.engineer.panorama.R

class MultiPanoViewActivity : BaseFullScreenActivity() {
    private lateinit var panorama1: PanoramaView
    private lateinit var panorama2: PanoramaView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        panorama1 = findViewById<PanoramaView>(R.id.panorama1)
        panorama2 = findViewById<PanoramaView>(R.id.panorama2)
        panorama1.setPanorama("0100220000130817164838355J5")
        panorama2.setPanorama("0300220000131105191740485IN")

        Toast.makeText(this, "show two panorama view same time failed ", Toast.LENGTH_LONG).show()
    }

    override fun provideLayout(): Int {
        return R.layout.activity_multi_pano_view
    }

    override fun onPause() {
        super.onPause()
        panorama1.onPause()
        panorama2.onPause()
    }

    override fun onResume() {
        super.onResume()
        panorama1.onResume()
        panorama2.onResume()
    }

    override fun onDestroy() {
        panorama1.destroy()
        panorama2.destroy()
        super.onDestroy()
    }
}
