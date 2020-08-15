package com.engineer.panorama.ui

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.baidu.lbsapi.panoramaview.PanoramaView
import com.baidu.mapapi.model.LatLng
import com.engineer.panorama.R
import com.engineer.panorama.interfaces.SimplePanoListener
import kotlinx.android.synthetic.main.activity_pano_pager.*

class PanoPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_pano_pager)
        setView()
    }

    private fun setView() {
        val list = getDatas()
//        val adapter = MyAdapter(list)
        val adapter = MyFragmentAdapter(list, this)
        view_pager.offscreenPageLimit = 1
        view_pager.orientation = ViewPager2.ORIENTATION_VERTICAL
        view_pager.adapter = adapter
    }

    private fun getDatas(): ArrayList<LatLng> {
        val list = ArrayList<LatLng>()
        var latLng = LatLng(39.963175, 116.400244)
        list.add(latLng)
        latLng = LatLng(39.947246, 116.390471)
        list.add(latLng)
        latLng = LatLng(39.902095, 116.32608)
        list.add(latLng)
        latLng = LatLng(39.871534, 116.384147)
        list.add(latLng)
        return list
    }

    class MyFragmentAdapter(private var datas: List<LatLng>,
                            activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return datas.size
        }

        override fun createFragment(position: Int): Fragment {
            return CardFragment.create(datas[position])
        }

    }

    class CardFragment : Fragment() {
        val TAG = "CardFragment"
        private lateinit var panoView: PanoramaView

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
            Log.d(TAG, "onCreateView() called");
            val view = inflater.inflate(R.layout.pager_pano_item, container, false)
            panoView = view.findViewById(R.id.pano)
            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            Log.d(TAG, "onViewCreated() called");
            super.onViewCreated(view, savedInstanceState)
            val latLng: LatLng? = arguments?.getParcelable("data")
            latLng?.apply {
                panoView.setPanorama(longitude, latitude)
                panoView.setPanoramaViewListener(object : SimplePanoListener() {
                    override fun onMoveStart() {
                        try {
                            super.onMoveStart()
                        } catch (e: Exception) {
                            Log.e("zyq", "error is ${e.message}")
                        }
                    }
                })
            }
        }

        override fun onResume() {
            Log.d(TAG, "onResume() called");
            super.onResume()
            panoView.onResume()
        }

        override fun onPause() {
            Log.d(TAG, "onPause() called");
            super.onPause()
            panoView.onPause()
        }

        override fun onDestroyView() {
            Log.d(TAG, "onDestroyView() called");
            super.onDestroyView()
            panoView.destroy()
        }

        override fun onDestroy() {
            Log.d(TAG, "onDestroy() called");
            super.onDestroy()

        }

        companion object {

            /** Creates a Fragment for a given [Card]  */
            fun create(card: LatLng): CardFragment {
                val fragment = CardFragment()
                val bundle = Bundle()
                bundle.putParcelable("data", card)
                fragment.arguments = bundle
                return fragment
            }
        }
    }


    class MyAdapter(private var datas: List<LatLng>) : RecyclerView.Adapter<MyAdapter.PagerHolder>() {

        override fun onBindViewHolder(holder: PagerHolder, position: Int) {
            holder.text.text = "pos_$position"
            holder.pano.setPanorama(datas[position].longitude, datas[position].latitude)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.pager_pano_item, parent, false)
            return PagerHolder(view)
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        inner class PagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text = itemView.findViewById<TextView>(R.id.text)
            val pano = itemView.findViewById<PanoramaView>(R.id.pano)
            val surfaceView = pano.getChildAt(0)

            init {

//                pano.onResume()
                if (surfaceView is SurfaceView) {
                    Log.e("zyq", "$surfaceView")
                    surfaceView.setZOrderOnTop(true)
                    surfaceView.holder.setFormat(PixelFormat.TRANSPARENT)
                    surfaceView.setBackgroundColor(Color.parseColor("#00000000"))
                }
            }
        }
    }
}
