package com.baidu.lbsapi.panodemo.indoor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.lbsapi.panodemo.R;
import com.baidu.lbsapi.panodemo.indoor.model.AlbumDataHelper;
import com.baidu.lbsapi.panodemo.indoor.model.AlbumPicInfo;
import com.baidu.lbsapi.panodemo.indoor.model.AlbumTypeInfo;
import com.baidu.lbsapi.panodemo.indoor.view.FloorsTabView;
import com.baidu.lbsapi.panodemo.indoor.view.PhotoAlbumOnClickListener;
import com.baidu.lbsapi.panodemo.indoor.view.PhotoAlbumView;
import com.baidu.lbsapi.panoramaview.PanoramaRequest;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.pano.platform.plugin.indooralbum.IndoorAlbumCallback;
import com.baidu.pano.platform.plugin.indooralbum.IndoorAlbumPlugin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AlbumContainer extends LinearLayout {

    private TextView mTvAddress = null; // 控件中的文字显示
    private PanoramaView mPanoView = null;

    public AlbumContainer(Context context) {
        super(context);
    }

    public AlbumContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setControlView(PanoramaView panoView, TextView tvAddress) {
        mPanoView = panoView;
        mTvAddress = tvAddress;
    }

    private String mCurrentPid = null; // 背景图片的pid
    private IndoorAlbumCallback.EntryInfo mEntryInfo = null;
    private List<AlbumTypeInfo> mLstType = new ArrayList<AlbumTypeInfo>();

    public void startLoad(final Context context, IndoorAlbumCallback.EntryInfo info) {
        if (context != null) {
            setCurrentPid(null);
            mEntryInfo = info;

            // 开启异步task获取数据
            new AsyncTask<String, Integer, String[]>() {
                @Override
                protected void onPreExecute() {
                }

                @Override
                protected String[] doInBackground(String... params) {
                    String pid = params[0];
                    String result = PanoramaRequest.getInstance(context).getPanoramaRecommendInfo(pid);
                    return new String[]{pid, result};
                }

                @Override
                protected void onPostExecute(String[] aryResult) {
                    if (aryResult != null && aryResult.length == 2) {
                        String pid = aryResult[0];
                        String result = aryResult[1];
                        if (pid != null) {
                            synchronized (IndoorAlbumPlugin.getInstance().getLock()) {
                                updateUi(pid, result);
                            }
                        }
                    }
                }
            }.execute(info.getEnterPid());
        }
    }

    /**
     * 运行在Lock锁中，根据list数据刷新界面
     *
     * @param pid
     * @param json
     */
    public void updateUi(String pid, String json) {

        if (pid != null && json != null && mEntryInfo != null) {
            if (pid.equals(mEntryInfo.getEnterPid())) { // 保证线程安全，
                boolean showBirdEye = AlbumDataHelper.parseGuideJson(json, mLstType);
                final FloorsTabView floorsTab = (FloorsTabView) findViewById(R.id.page_pano_album_catalog);
                final PhotoAlbumView bottomAlbum = (PhotoAlbumView) findViewById(R.id.page_pano_album_list);
                if (mLstType.size() == 0) {
                    choicePic(pid, false, null, false);
                    floorsTab.setVisibility(GONE);
                    bottomAlbum.setVisibility(GONE);
                } else {
                    bottomAlbum.setVisibility(VISIBLE);
                    bottomAlbum.setOnPhotoClickListener(new PhotoAlbumOnClickListener() {
                        @Override
                        public void onItemClicked(AlbumPicInfo picInfo) {
                            if (picInfo != null) {
                                choicePic(picInfo.getPid(), picInfo.isExit(), picInfo.getShortInfo(), true);
                            }
                        }
                    });
                    // 判断是否显示分类列表
                    if (mLstType.size() == 1) { // 若楼层数小于等于1,则隐藏掉楼层按钮
                        floorsTab.setVisibility(View.GONE);
                        bottomAlbum.updateUi(mLstType.get(0).getLstPicInfo());
                        if (!jumpToDefaultPic(pid, bottomAlbum, floorsTab, false)) {
                            mTvAddress.setText("");
                        }
                    } else {
                        floorsTab.setVisibility(View.VISIBLE);
                        // 展示类型or楼层数据
                        LinkedHashMap<String, OnClickListener> tabsStrListener = new LinkedHashMap<String, OnClickListener>();
                        for (int i = 0; i < mLstType.size(); i++) {
                            final AlbumTypeInfo typeInfo = mLstType.get(i);
                            final int index = i;
                            tabsStrListener.put(typeInfo.getName(), new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bottomAlbum.updateUi(typeInfo.getLstPicInfo());

                                    if (typeInfo.getLstPicInfo().size() > 0) {
                                        if (typeInfo.getLstPicInfo().get(0).isExit()) { // 如果第一个数据是出口
                                            if (typeInfo.getLstPicInfo().size() > 1) {
                                                choicePic(typeInfo.getLstPicInfo().get(1).getPid(), false, typeInfo.getLstPicInfo().get(1).getShortInfo(), true);
                                                bottomAlbum.setSinglePhotoHighLight(1);
                                                bottomAlbum.smoothScrollToPosition(0);
                                            }
                                        } else { // 不是出口
                                            choicePic(typeInfo.getLstPicInfo().get(0).getPid(), false, typeInfo.getLstPicInfo().get(0).getShortInfo(), true);
                                            bottomAlbum.setSinglePhotoHighLight(0);
                                            bottomAlbum.smoothScrollToPosition(0);
                                        }
                                    }
                                    floorsTab.setCheck(index);
                                }
                            });
                        }
                        floorsTab.setTabsStrListener(tabsStrListener);
                        if (!jumpToDefaultPic(pid, bottomAlbum, floorsTab, false)) {
                            mTvAddress.setText("");
                        }
                    }
                }
            }
        }
    }

    /**
     * 首次进入时，自动跳转
     *
     * @param pid
     * @param bottomAlbum
     * @param floorsTab
     */
    public boolean jumpToDefaultPic(String pid, PhotoAlbumView bottomAlbum, FloorsTabView floorsTab, boolean needChange) {
        boolean hasJump = false;
        if (bottomAlbum != null && pid != null && mLstType.size() > 0) {
            if (mLstType.size() == 1) {
                List<AlbumPicInfo> lstPicInfo = mLstType.get(0).getLstPicInfo();
                for (int i = 0; i < lstPicInfo.size(); i++) {
                    AlbumPicInfo picInfo = lstPicInfo.get(i);
                    if (pid.equals(picInfo.getPid())) {
                        // 跳转
                        choicePic(pid, false, picInfo.getShortInfo(), needChange);
                        bottomAlbum.setSinglePhotoHighLight(i);
                        int showIndex = i;
                        if (showIndex < 3) {
                            showIndex = 0;
                        }
                        bottomAlbum.smoothScrollToPosition(showIndex);
                        hasJump = true;
                        break;
                    }
                }
            } else {
                if (floorsTab != null) {
                    for (int i = 0; i < mLstType.size(); i++) {
                        List<AlbumPicInfo> lst = mLstType.get(i).getLstPicInfo();
                        for (int j = 0; j < lst.size(); j++) {
                            AlbumPicInfo picInfo = lst.get(j);
                            if (pid.equals(picInfo.getPid())) {
                                // 跳转
                                floorsTab.setCheck(i, true);
                                bottomAlbum.updateUi(lst);
                                choicePic(pid, false, picInfo.getShortInfo(), needChange);
                                bottomAlbum.setSinglePhotoHighLight(j);
                                int showIndex = j;
                                if (showIndex < 3) {
                                    showIndex = 0;
                                }
                                bottomAlbum.smoothScrollToPosition(showIndex);
                                hasJump = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (hasJump) {
            mCurrentPid = pid;
        }
        return hasJump;
    }

    public String getCurrentPid() {
        return mCurrentPid;
    }

    public void setCurrentPid(String pid) {
        mCurrentPid = pid;
    }

    public IndoorAlbumCallback.EntryInfo getEntryInfo() {
        return mEntryInfo;
    }


    protected void choicePic(String pid, boolean isExit, String name, boolean change) {
        synchronized (IndoorAlbumPlugin.getInstance().getLock()) {
            if (change) {
                String currentPid = getCurrentPid();
                if (currentPid != null && currentPid.equals(pid) && !isExit) { // 如果是在相册列表点击了当前的数据，则不响应
                    return;
                } else {
                    setCurrentPid(pid);
                    if (mPanoView != null) {
                        if (isExit) {
                            IndoorAlbumCallback.EntryInfo info = getEntryInfo();
                            if (info != null) {
                                if (info.getExitUid() != null) {
                                    mPanoView.setPanoramaByUid(info.getExitUid(), PanoramaView.PANOTYPE_STREET);
                                } else if (pid != null) {
                                    mPanoView.setPanorama(pid);
                                }
                                removeAllViews();
                                mTvAddress = null;
                            }
                        } else {
                            mPanoView.setPanorama(pid);
                        }
                    }
                }
            }
            if (mTvAddress != null) {
                mTvAddress.setText(name);
            }
        }
    }
}
