package com.baidu.lbsapi.panodemo.indoor.model;

import java.util.ArrayList;
import java.util.List;

public class AlbumTypeInfo {
    private String mName = null;
    private List<AlbumPicInfo> mLstPicInfo = new ArrayList<AlbumPicInfo>();

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public boolean isSameName(String name) {
        boolean isSame = false;
        if (mName != null && mName.equals(name)) {
            isSame = true;
        }
        return isSame;
    }

    public List<AlbumPicInfo> getLstPicInfo() {
        return mLstPicInfo;
    }

    public void addPicInfo(AlbumPicInfo picInfo) {
        mLstPicInfo.add(picInfo);
    }
}
