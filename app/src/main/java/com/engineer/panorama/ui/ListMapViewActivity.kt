package com.engineer.panorama.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.TextureMapView
import com.baidu.mapapi.model.LatLng
import com.engineer.panorama.R
import kotlinx.android.synthetic.main.activity_list_pano_view.*


class ListMapViewActivity : AppCompatActivity() {
    private var datas: ArrayList<LatLng> = ArrayList()
    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pano_view)
        list.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(datas)
        list.adapter = adapter

        refresh()
    }

    private fun refresh() {


        val GEO_BEIJING = LatLng(39.945, 116.404)
        for (i in 0..10) {
            val delta = i / 10f
            val data = LatLng(GEO_BEIJING.latitude + delta, GEO_BEIJING.longitude + delta)
            datas.add(data)
        }
        adapter.notifyDataSetChanged()
    }


    private inner class MyAdapter(var datas: ArrayList<LatLng>) : RecyclerView.Adapter<MyAdapter.MyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_mapview, parent, false)

            return MyHolder(view)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            val status = MapStatusUpdateFactory.newLatLng(datas[position])
            holder.mapview.map.setMapStatus(status)
        }


        override fun getItemCount(): Int {
            return datas.size
        }


        inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mapview = view.findViewById<TextureMapView>(R.id.map_item)
        }
    }

}
