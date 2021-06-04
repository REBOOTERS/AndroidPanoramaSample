package com.engineer.panorama.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
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
import com.engineer.StatusBarUtil;
import com.engineer.panorama.R;
import com.engineer.panorama.bean.HotCityPanoBean;
import com.engineer.panorama.bean.PanoramaBean;
import com.engineer.panorama.util.ScreenView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 全景Demo主Activity
 */
public class PanoViewActivity extends BaseFullScreenActivity {

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


    private MyHandler handler;

    //添加Marker
    private LatLng point;
    //构建Marker图标
    private BitmapDescriptor bitmap;
    //构建MarkerOption，用于在地图上添加Marker
    private OverlayOptions option;

    private String name, pid;

    private ScreenView mScreenView;

    private SurfaceView mSurfaceView;

    private RelativeLayout mToolbbar;

    private ValueAnimator valueAnimator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        setContentView(R.layout.panodemo_main);

        if (getIntent() != null) {
            latitude = getIntent().getDoubleExtra("latitude", 0);
            longitude = getIntent().getDoubleExtra("longitude", 0);
            name = "name";
            pid = "4373943294879844934";
        }

        initView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mToolbbar.getLayoutParams();
        params.topMargin = StatusBarUtil.INSTANCE.getStatusBarHeight(this);
        mToolbbar.setLayoutParams(params);
        testPano();
    }


    private void initView() {
        textTitle = (TextView) findViewById(R.id.panodemo_main_title);
        mPanoView = (PanoramaView) findViewById(R.id.panorama);


        mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

//        sv = (GLSurfaceView) mMapView.getChildAt(0);
//        sv.setZOrderMediaOverlay(true);

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

        mToolbbar = findViewById(R.id.toolbar);
        animationShow = AnimationUtils.loadAnimation(this, R.anim.pop_show_animation);
        animationHide = AnimationUtils.loadAnimation(this, R.anim.pop_hidden_animation);

        findViewById(R.id.crop).setOnClickListener(v -> requestPermission());
        findViewById(R.id.menu).setOnClickListener(v -> {
                    startActivity(new Intent(this, ListPanoViewActivity.class));
                    finish();
                }
        );
        findViewById(R.id.list).setOnClickListener(v -> {
            startActivity(new Intent(this, ListMapViewActivity.class));
            finish();
        });

        FrameLayout cover = findViewById(R.id.black_cover);
        FrameLayout layoutContent = findViewById(R.id.content_layout);
        RecyclerView recyclerView = findViewById(R.id.bottom_list);
        findViewById(R.id.scale).setOnClickListener(v -> {
            Toast.makeText(PanoViewActivity.this,
                    "scale", Toast.LENGTH_SHORT).show();
            recyclerView.animate().translationY(0).start();
            layoutContent.animate().scaleX(0.8f).start();
            layoutContent.animate().scaleY(0.8f).start();
            cover.animate().alpha(1f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    cover.setVisibility(View.VISIBLE);

                }
            }).start();


        });

        cover.setOnClickListener(v -> {
            Toast.makeText(PanoViewActivity.this,
                    "cover", Toast.LENGTH_SHORT).show();
            recyclerView.animate().translationY(1000).start();
            layoutContent.animate().scaleX(1f).start();
            layoutContent.animate().scaleY(1f).start();
            cover.setVisibility(View.GONE);
//                        cover.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//
//                                cover.setOnClickListener(null);
//                            }
//                        }).start();
        });


        if (mPanoView != null && mPanoView.getChildCount() > 0) {
            if (mPanoView.getChildAt(0) instanceof SurfaceView) {
                mSurfaceView = (SurfaceView) mPanoView.getChildAt(0);

                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void crop1() {
        final Bitmap bitmap = Bitmap.createBitmap(mPanoView.getWidth(), mPanoView.getHeight(), Bitmap.Config.ARGB_8888);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            PixelCopy.request(mSurfaceView, bitmap, copyResult -> {
                Log.e("xxx", "copyResult==" + copyResult);
                saveBitmap(bitmap);

            }, new Handler(Looper.getMainLooper()));
        }
    }


    public void saveBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(this, "bitmap is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator;
        } else {
            Log.e("xxx", "saveBitmap: 1return");
            return;
        }


        try {
            filePic = new File(savePath + System.currentTimeMillis() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("xxx", "saveBitmap: 2return");
            return;
        }
        Log.e("xxx", "saveBitmap: " + filePic.getAbsolutePath());
    }


    private void testPano() {
        handler = new MyHandler();

        mPanoView.setShowTopoLink(true);




//         测试回调函数,需要注意的是回调函数要在setPanorama()之前调用，否则回调函数可能执行异常
        mPanoView.setPanoramaViewListener(new PanoramaViewListener() {

            @Override
            public void onLoadPanoramaBegin() {
                Log.i(LTAG, "onLoadPanoramaStart...");
            }

            @Override
            public void onLoadPanoramaEnd(String json) {
//                Log.e(LTAG, "onLoadPanoramaEnd : " + json);
//                startAuto();
            }

            @Override
            public void onLoadPanoramaError(String error) {
//                Log.e(LTAG, "onLoadPanoramaError : " + error);

            }

            @Override
            public void onDescriptionLoadEnd(String json) {

                PanoramaBean panoramaBean = JSON.parseObject(json, PanoramaBean.class);

                Point pointll = CoordinateConverter.MCConverter2LL(panoramaBean.getX(), panoramaBean.getY());

                HotCityPanoBean hotCityPanoBean = new HotCityPanoBean();
                hotCityPanoBean.setPid(pid);
                hotCityPanoBean.setName(name);
                hotCityPanoBean.setLatitude(pointll.y);
                hotCityPanoBean.setLongitude(pointll.x);


                Log.e(LTAG, "onDescriptionLoadEnd : " + hotCityPanoBean.getLatitude() + ", "
                        + hotCityPanoBean.getLongitude() + "\n"
                        + hotCityPanoBean.getName() + ", "
                        + hotCityPanoBean.getPid());


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

            @Override
            public void onMoveStart() {

            }

            @Override
            public void onMoveEnd() {

            }
        });
        mPanoView.setPanorama(longitude, latitude);

    }

    private void startAuto() {
        if (valueAnimator == null) {
            runOnUiThread(() -> {
                valueAnimator = ValueAnimator.ofFloat(0, 360);
                valueAnimator.addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    Log.e("zz", "value==" + value);
                    mPanoView.setPanoramaHeading(value);
                });
                valueAnimator.setDuration(36000);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.start();
            });
        }


    }

    @Override
    public int provideLayout() {
        return R.layout.panodemo_main;
    }


    private class MyHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ACTION_CLICK:
                    if (titleVisible) {
                        titleVisible = false;
                        mToolbbar.startAnimation(animationHide);
//                        textTitle.setVisibility(View.GONE);
                    } else {
                        titleVisible = true;
                        mToolbbar.startAnimation(animationShow);
//                        textTitle.setVisibility(View.VISIBLE);

                    }

                    break;
                case ACTION_DRAG:
                    float heading = (float) msg.arg1;

                    mBaiduMap.clear();

                    //构建MarkerOption，用于在地图上添加Marker
                    option = new MarkerOptions()
                            .position(point)
                            .rotate(360 - heading)
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
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.end();
            valueAnimator.removeAllUpdateListeners();
            valueAnimator = null;
        }
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


    @SuppressLint("CheckResult")
    private void requestPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        crop1();
                    }
                });
    }
}
