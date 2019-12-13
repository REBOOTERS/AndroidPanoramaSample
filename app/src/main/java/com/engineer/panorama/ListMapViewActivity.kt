package com.engineer.panorama

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.mapapi.map.MapView
import kotlinx.android.synthetic.main.activity_list_pano_view.*

class ListMapViewActivity : AppCompatActivity() {
    private var datas: ArrayList<String> = ArrayList()
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


    private inner class MyAdapter(var datas: ArrayList<String>) : RecyclerView.Adapter<MyAdapter.MyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_mapview, parent, false)

            return MyHolder(view)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {

//            holder.panoview
        }


        override fun getItemCount(): Int {
            return datas.size
        }


        inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
            val panoview = view.findViewById<MapView>(R.id.map_item)
        }
    }
}
