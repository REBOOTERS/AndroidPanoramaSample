package com.baidu.lbsapi.panodemo;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.panodemo.bean.HotCityPanoBean;
import com.baidu.lbsapi.panodemo.bean.PanoramaBean;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.baidu.lbsapi.tools.CoordinateConverter;
import com.baidu.lbsapi.tools.Point;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * 全景Demo主Activity
 */
public class PanoViewActivity extends Activity {

    private static final String LTAG = "BaiduPanoSDKDemo";

    private PanoramaView mPanoView;
    private TextView textTitle;

    private double latitude = 39.963175;
    private double longitude = 116.400244;


    //地图
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    //定义操作动作
    private final int ACTION_DRAG = 0;
    private final int ACTION_CLICK = 1;

    //title是否可见
    private boolean titleVisible = true;
    //title显示隐藏动画
    private Animation animationShow, animationHide;
    //地图层
    private GLSurfaceView sv;

    private MyHandler handler;

    //添加Marker
    private LatLng point;
    //构建Marker图标
    private BitmapDescriptor bitmap;
    //构建MarkerOption，用于在地图上添加Marker
    private OverlayOptions option;

    private  String name,pid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 先初始化BMapManager
        initBMapManager();
        setContentView(R.layout.panodemo_main);

        if (getIntent() != null) {
            latitude = getIntent().getDoubleExtra("latitude", 0);
            longitude = getIntent().getDoubleExtra("longitude", 0);
            name = "name";
            pid = "4373943294879844934";
        }

        initView();

        testPano();
    }

    private void initBMapManager() {
        PanoDemoApplication app = (PanoDemoApplication) this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(app);
            app.mBMapManager.init(new PanoDemoApplication.MyGeneralListener());
        }
    }

    private void initView() {
        textTitle = (TextView) findViewById(R.id.panodemo_main_title);
        mPanoView = (PanoramaView) findViewById(R.id.panorama);


        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        sv = (GLSurfaceView) mMapView.getChildAt(0);
        sv.setZOrderMediaOverlay(true);

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        //定义Maker坐标点
        point = new LatLng(latitude, longitude);
        //构建Marker图标
        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);


        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);


        animationShow = AnimationUtils.loadAnimation(this, R.anim.pop_show_animation);
        animationHide = AnimationUtils.loadAnimation(this, R.anim.pop_hidden_animation);

    }

    private void testPano() {
        handler = new MyHandler();

        mPanoView.setShowTopoLink(true);


        mPanoView.setPanorama(longitude, latitude);

        // 测试回调函数,需要注意的是回调函数要在setPanorama()之前调用，否则回调函数可能执行异常
        mPanoView.setPanoramaViewListener(new PanoramaViewListener() {

            @Override
            public void onLoadPanoramaBegin() {
                Log.i(LTAG, "onLoadPanoramaStart...");
            }

            @Override
            public void onLoadPanoramaEnd(String json) {
//                Log.e(LTAG, "onLoadPanoramaEnd : " + json);
            }

            @Override
            public void onLoadPanoramaError(String error) {
//                Log.e(LTAG, "onLoadPanoramaError : " + error);

            }

            @Override
            public void onDescriptionLoadEnd(String json) {

                PanoramaBean panoramaBean = JSON.parseObject(json, PanoramaBean.class);

                Point pointll= CoordinateConverter.MCConverter2LL(panoramaBean.getX(), panoramaBean.getY());

                HotCityPanoBean hotCityPanoBean=new HotCityPanoBean();
                hotCityPanoBean.setPid(pid);
                hotCityPanoBean.setName(name);
                hotCityPanoBean.setLatitude(pointll.y);
                hotCityPanoBean.setLongitude(pointll.x);


                Log.e(LTAG, "onDescriptionLoadEnd : " + hotCityPanoBean.getLatitude()+", "
                        +hotCityPanoBean.getLongitude()+"\n"
                        +hotCityPanoBean.getName()+", "
                        +hotCityPanoBean.getPid());



            }

            @Override
            public void onMessage(String msgName, int msgType) {
//                Log.e(LTAG, "msgName--->" + msgName + ", msgType--->" + msgType);
                switch (msgType) {
                    case 8213:
                        //旋转
//                        Log.e(PanoViewActivity.class.getSimpleName(), "now,the heading is " + mPanoView.getPanoramaHeading());
                        Message message = new Message();
                        message.what = ACTION_DRAG;
                        message.arg1 = (int) mPanoView.getPanoramaHeading();
                        handler.sendMessage(message);
                        break;
                    case 12302:
                        //点击
//                        Log.e(PanoViewActivity.class.getSimpleName(), "clicked");
                        Message msg = new Message();
                        msg.what = ACTION_CLICK;
                        handler.sendMessage(msg);
                        break;
                    default:
                        break;
                }


            }

            @Override
            public void onCustomMarkerClick(String key) {

            }
        });


    }


    private class MyHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ACTION_CLICK:
                    if (titleVisible) {
                        titleVisible = false;
                        textTitle.startAnimation(animationHide);
                        textTitle.setVisibility(View.GONE);
                        sv.setVisibility(View.GONE);
                    } else {
                        titleVisible = true;
                        textTitle.startAnimation(animationShow);
                        textTitle.setVisibility(View.VISIBLE);
                        sv.setVisibility(View.VISIBLE);

                    }

                    break;
                case ACTION_DRAG:
                    float heading = (float) msg.arg1;

                    mBaiduMap.clear();

                    //构建MarkerOption，用于在地图上添加Marker
                    option = new MarkerOptions()
                            .position(point)
                            .rotate(360-heading)
                            .icon(bitmap);
                    //在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(option);
                    break;
                default:
                    break;
            }
        }


    }


    @Override
    protected void onDestroy() {
        mPanoView.destroy();
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
