package com.engineer.panorama;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.lbsapi.model.BaiduPanoData;
import com.baidu.lbsapi.panoramaview.PanoramaRequest;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by co-mall on 2016/6/21.
 */
public class MapViewActivity extends AppCompatActivity {
    private Context mContext;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //添加Marker
    private LatLng point;
    //构建Marker图标
    private BitmapDescriptor bitmap;
    //构建MarkerOption，用于在地图上添加Marker
    private OverlayOptions option;

    private final String baseUrl = "http://pcsv1.map.bdimg.com/scape/?qt=pdata&pos=0_0&z=0&sid=";


    /**
     * 假设我们当前的位置在此
     */
    private final double latitude = 39.963175;
    private final double longitude = 116.400244;

    //添加自定义View至mapView
    private View view;
    private ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        mContext = this;
        mMapView = (MapView) findViewById(R.id.bmapView);


        mBaiduMap = mMapView.getMap();
        //定义Maker坐标点
        point = new LatLng(latitude, longitude);
        //定义地图状态
        final MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        //构建Marker图标
        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_markc);

        //构建MarkerOption，用于在地图上添加Marker
        option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);


        view = LayoutInflater.from(mContext).inflate(R.layout.pano_overlay, null);
        pic = (ImageView) view.findViewById(R.id.panoImageView);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PanoViewActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });


        showMiniWindow();


    }

    @SuppressLint("CheckResult")
    private void showMiniWindow() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                PanoramaRequest request = PanoramaRequest.getInstance(mContext);
                BaiduPanoData locationPanoData = request.getPanoramaInfoByLatLon(longitude, latitude);
                //开发者可以判断是否有外景(街景)
                if (locationPanoData.hasStreetPano()) {
                    String url = baseUrl + locationPanoData.getPid();
                    emitter.onNext(url);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Glide.with(mContext).load(s).into(pic);
                        InfoWindow mInfoWindow = new InfoWindow(view, point, -57);
                        //显示InfoWindow
                        mBaiduMap.showInfoWindow(mInfoWindow);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
