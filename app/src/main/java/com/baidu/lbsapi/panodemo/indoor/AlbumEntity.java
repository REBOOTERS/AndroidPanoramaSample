package com.baidu.lbsapi.panodemo.indoor;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.baidu.lbsapi.panodemo.R;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.pano.platform.plugin.indooralbum.IndoorAlbumCallback;

/**
 * 开发者自定义相册只需要实现IndoorAlbumCallback接口返回相册View
 */
public class AlbumEntity implements IndoorAlbumCallback {

    /**
     * 调用相册组件
     *
     * @param panoramaView
     * @param info
     */
    @Override
    public View loadAlbumView(final PanoramaView panoramaView, final EntryInfo info) {

        if (panoramaView != null && info != null) {
            View albumView = LayoutInflater.from(panoramaView.getContext())
                    .inflate(R.layout.baidupano_photoalbum_container, null);
            if (albumView != null) {
                AlbumContainer mAlbumContainer = (AlbumContainer) albumView.findViewById(R.id.page_pano_album_view);
                TextView mTvAddress = (TextView) albumView.findViewById(R.id.page_pano_album_address);
                mAlbumContainer.setControlView(panoramaView, mTvAddress);
            }
            LayoutParams lp = (LayoutParams) albumView.getLayoutParams();
            if (lp == null) {
                lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }
            lp.gravity = Gravity.BOTTOM;
            albumView.setLayoutParams(lp);
            AlbumContainer albumContainer = (AlbumContainer) albumView.findViewById(R.id.page_pano_album_view);
            albumContainer.startLoad(panoramaView.getContext(), info);
            return albumView;
        } else {
            return null;
        }
    }
}
