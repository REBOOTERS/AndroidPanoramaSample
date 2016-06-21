package com.baidu.lbsapi.panodemo.indoor.model;

import java.io.UnsupportedEncodingException;

import com.baidu.lbsapi.panodemo.indoor.util.SysUtil;

public class AlbumPicInfo {
    private String mInfo = null;
    private String mPid = null;
    private String mDir = null;
    private String mType = null;
    private String mCatalog = null;
    private String mFloor = null;

    public String getInfo() {
        return mInfo;
    }

    public String getShortInfo() {
        String nameContainer = null;
        if (mInfo != null) {
            nameContainer = "";
            if (mInfo.length() > 10) {
                int charCount = 0;
                for (int i = 0; i < mInfo.length(); i++) {
                    if (charCount > 19) {
                        break;
                    }
                    char temp = mInfo.charAt(i);
                    try {
                        if (SysUtil.isChinese(temp)) {
                            charCount += 2;
                        } else {
                            charCount++;
                        }
                        nameContainer += temp;
                    } catch (UnsupportedEncodingException e) {
                    }
                }
                try {
                    char temp = nameContainer.charAt(nameContainer.length() - 1);
                    if (SysUtil.isChinese(temp)) {
                        nameContainer = nameContainer.substring(0, nameContainer.length() - 1);
                    } else {
                        nameContainer = nameContainer.substring(0, nameContainer.length() - 2);
                    }
                    nameContainer += "...";
                } catch (UnsupportedEncodingException e) {
                }

            } else {
                nameContainer = mInfo;
            }
        }
        return nameContainer;
    }

    public void setInfo(String info) {
        mInfo = info;
    }

    public String getPid() {
        return mPid;
    }

    public void setPid(String pid) {
        mPid = pid;
    }

    public String getDir() {
        return mDir;
    }

    public void setDir(String dir) {
        mDir = dir;
    }

    public String getType() {
        return mType;
    }

    public boolean isExit() {
        boolean isExit = false;
        if (mType != null && mType.equals("3")) {
            isExit = true;
        }
        return isExit;
    }

    public boolean isShowInEveryFloor() {
        boolean isShow = false;
        if (mType != null) {
            if (mType.equals("0") || mType.equals("2") || mType.equals("6") || mType.equals("21")) {
                isShow = true;
            }
        }
        return isShow;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getCatalog() {
        return mCatalog;
    }

    public void setCatalog(String catalog) {
        mCatalog = catalog;
    }

    public String getFloor() {
        return mFloor;
    }

    public void setFloor(String floor) {
        mFloor = floor;
    }

}
