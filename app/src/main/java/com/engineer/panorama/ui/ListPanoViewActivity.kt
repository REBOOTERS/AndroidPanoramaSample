package com.engineer.panorama.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.lbsapi.panoramaview.PanoramaView
import com.engineer.panorama.R

class ListPanoViewActivity : BaseFullScreenActivity() {
    private var datas: ArrayList<String> = ArrayList()
    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(datas)
        list.adapter = adapter

        refresh()
    }

    override fun provideLayout(): Int {
        return R.layout.activity_list_pano_view
    }

    private fun refresh() {
        datas.add("1400220012180629140838219UZ")
        datas.add("0802150001151204150545865UZ")
        datas.add("09002200001503210057381717E")
        datas.add("1802100000140610112407862IN")
        datas.add("1400030012180614003743111UZ")
        datas.add("09002500001505030202473035X")
        datas.add("0900600012170406114723000IN")
        datas.add("0901740000071126021842800IN")
        datas.add("1402430012180315162159339UZ")
        adapter.notifyDataSetChanged()
    }

    private fun getSurfaceView(panoramaView: PanoramaView?): SurfaceView? {
        var mSurfaceView: SurfaceView? = null
        if (panoramaView != null && panoramaView.childCount > 0) {
            val temp = panoramaView.getChildAt(0)
            if (temp is SurfaceView) {
                mSurfaceView = temp
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            }
        }
        return mSurfaceView
    }

    private inner class MyAdapter(var datas: ArrayList<String>) : RecyclerView.Adapter<MyAdapter.MyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

            return MyHolder(view)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {

            val LOCAL_URL = "https://www.baidu.com"
            holder.panoview.loadUrl(LOCAL_URL)
        }


        override fun getItemCount(): Int {
            return datas.size
        }


        inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
            val panoview = view.findViewById<WebView>(R.id.panorama_item)
            val settings = panoview.settings

            init {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
            }
        }
    }
}
