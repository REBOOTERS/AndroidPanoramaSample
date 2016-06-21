package com.baidu.lbsapi.panodemo.indoor.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlbumDataHelper {

    public static final int IMAGE_TYPE_MAP = 2;// 内景imagetype，小地图模式
    public static final int PHOTO_TYPE_EXIT = 3;// 相册类型，出口为3

    public static final String SS_PHOTO_TYPE = "type";
    public static final String SS_PHOTO_FLOOR = "floor";
    public static final String SS_PHOTO_CATALOG = "catalog";

    private static final String[] parentCatalogs =
            {"其他", "正门", "房型", "设施", "正门", "餐饮设施", "其他设施", "正门", "设施", "观影厅", "其他设施"};
    private static final int[] catalogs =
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 5, 5, 5, 6, 6, 7, 8, 8, 8, 9, 10};

    /**
     * @param json
     * @param lstResult
     * @return
     */
    public static boolean parseGuideJson(String json, List<AlbumTypeInfo> lstResult) {
        boolean showBirdEye = false;
        lstResult.clear();
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);

                String content = jsonObject.optString("content");
                if (null != content && !"".equals(content)) {
                    ArrayList<String> lstTypeTemp = new ArrayList<String>();
                    JSONArray photoInfoArray = new JSONArray(content);

                    if (photoInfoArray != null) {
                        boolean hasCatalog = false;
                        ArrayList<AlbumPicInfo> lstData = new ArrayList<AlbumPicInfo>();
                        AlbumPicInfo exitInfo = null;
                        for (int i = 0; i < photoInfoArray.length(); i++) {
                            JSONObject jsoObj = photoInfoArray.getJSONObject(i);
                            AlbumPicInfo picInfo = new AlbumPicInfo();
                            String info = jsoObj.optString("Info");
                            String pid = jsoObj.optString("PID");
                            String dir = jsoObj.optString("Dir");
                            String type = jsoObj.optString("Type");
                            if (type != null) {
                                if (type.equals("2") || type.equals("6")) {
                                    continue;
                                }
                            }
                            String catalog = null;
                            String tmpCatalog = jsoObj.optString("Catalog");
                            String floor = null;
                            int floorNum = jsoObj.optInt("Floor");
                            if (floorNum != 0) {
                                if (floorNum < 0) {
                                    floorNum = -floorNum;
                                    floor = String.format("B%d", floorNum);
                                } else {
                                    floor = String.format("F%d", floorNum);
                                }
                            }
                            if (!TextUtils.isEmpty(tmpCatalog)) {
                                hasCatalog = true;
                                int typeNum = Integer.parseInt(tmpCatalog);
                                int catalogNum = 0;
                                if (typeNum < catalogs.length) {
                                    catalogNum = catalogs[typeNum];
                                }
                                catalog = parentCatalogs[catalogNum];
                            } else {
                                catalog = parentCatalogs[0];
                            }
                            picInfo.setInfo(info);
                            picInfo.setPid(pid);
                            picInfo.setDir(dir);
                            picInfo.setType(type);
                            picInfo.setCatalog(catalog);
                            picInfo.setFloor(floor);

                            lstData.add(picInfo);
                            if (picInfo.isExit()) {
                                exitInfo = picInfo;
                            }
                        }
                        if (hasCatalog) { // 以Catalog划分
                            for (int i = 0; i < lstData.size(); i++) {
                                AlbumPicInfo picInfo = lstData.get(i);
                                if (picInfo != null) {
                                    String catalog = picInfo.getCatalog();
                                    if (catalog != null && !picInfo.isExit()) {
                                        AlbumTypeInfo typeInfo = null; // 当前数据对应的目录对象
                                        for (int j = 0; j < lstResult.size(); j++) {
                                            if (lstResult.get(j).isSameName(catalog)) {
                                                typeInfo = lstResult.get(j);
                                                break;
                                            }
                                        }
                                        if (typeInfo == null) {
                                            typeInfo = new AlbumTypeInfo();
                                            typeInfo.setName(catalog);
                                            lstResult.add(typeInfo);
                                            if (exitInfo != null) {
                                                typeInfo.addPicInfo(exitInfo);
                                            }
                                        }
                                        typeInfo.addPicInfo(picInfo);
                                    }
                                }
                            }
                            // 排序
                            if (lstResult.size() > 1) {
                                for (int i = 0; i < parentCatalogs.length; i++) {
                                    String catalog = parentCatalogs[i];
                                    for (int j = 0; j < lstResult.size(); j++) {
                                        AlbumTypeInfo typeInfo = lstResult.get(j);
                                        if (typeInfo.isSameName(catalog)) {
                                            // 在结果队列中，把数据从当前位置移动到队尾，完成排序。
                                            lstResult.remove(typeInfo);
                                            lstResult.add(typeInfo);
                                            break;
                                        }
                                    }
                                }
                            }
                        } else { // 以Floor划分
                            for (int i = 0; i < lstData.size(); i++) {
                                AlbumPicInfo picInfo = lstData.get(i);
                                if (picInfo != null) {
                                    String floor = picInfo.getFloor();
                                    if (floor != null && !picInfo.isExit()) {
                                        AlbumTypeInfo typeInfo = null; // 当前数据对应的目录对象
                                        for (int j = 0; j < lstResult.size(); j++) {
                                            if (lstResult.get(j).isSameName(floor)) {
                                                typeInfo = lstResult.get(j);
                                                break;
                                            }
                                        }
                                        if (typeInfo == null) {
                                            typeInfo = new AlbumTypeInfo();
                                            typeInfo.setName(floor);
                                            lstResult.add(typeInfo);
                                            if (exitInfo != null) {
                                                typeInfo.addPicInfo(exitInfo);
                                            }
                                        }
                                        typeInfo.addPicInfo(picInfo);
                                    }
                                }
                            }
                        }
                    }
                }
                // 内景小地图的控释显示与否从guide接口读取
                if (null != jsonObject.optString("ExtInfo") && !"".equals(jsonObject.optString("ExtInfo"))) {
                    JSONObject jsonExtInfoObj = new JSONObject(jsonObject.optString("ExtInfo"));
                    if (jsonExtInfoObj.optInt("ImageType") == IMAGE_TYPE_MAP) {
                        showBirdEye = true;
                    }
                }

            } catch (JSONException e) {

            }
        }
        return showBirdEye;
    }

    /**
     * 根据楼层类型返回该类型图片列表
     */
    public static ArrayList<HashMap<String, String>> getPhotoListByType(String typeName,
                                                                        ArrayList<HashMap<String, String>> lstPoint) {
        ArrayList<HashMap<String, String>> photoList = new ArrayList<HashMap<String, String>>();
        ArrayList<Integer> catalogList = new ArrayList<Integer>();
        // 增加出口
        for (HashMap<String, String> photo : lstPoint) {
            if (PHOTO_TYPE_EXIT == Integer.parseInt(photo.get(SS_PHOTO_TYPE))) {
                photoList.add(photo);
            }
        }
        // 不谈了，终于看明白了：服务端给的数据的数字，本地存了数字和文案的映射表，多对一，catalogList存储此文案对应的所有数字类型
        for (int i = 0; i < parentCatalogs.length; i++) {
            if (parentCatalogs[i].equals(typeName)) {
                for (int j = 0; j < catalogs.length; j++) {
                    if (catalogs[j] == i) {
                        catalogList.add(j);
                    }
                }
            }
        }
        for (HashMap<String, String> photo : lstPoint) {
            if (null != photo.get(SS_PHOTO_CATALOG) && !"".equals(photo.get(SS_PHOTO_CATALOG))) {
                if (catalogList.contains(Integer.parseInt(photo.get(SS_PHOTO_CATALOG)))) {
                    photoList.add(photo);
                } else {
                    if (catalogList.contains(0) && Integer.parseInt(photo.get(SS_PHOTO_CATALOG)) >= catalogs.length
                            && PHOTO_TYPE_EXIT != Integer.parseInt(photo.get(SS_PHOTO_TYPE))) {
                        photoList.add(photo);
                    }
                }
            }
        }
        return photoList;
    }

    /**
     * 根据楼层名返回该楼层图片列表
     */
    public static ArrayList<HashMap<String, String>> getPhotoListByFloor(String floorName,
                                                                         ArrayList<HashMap<String, String>> lstPoint) {
        ArrayList<HashMap<String, String>> photoList = new ArrayList<HashMap<String, String>>();
        for (HashMap<String, String> photo : lstPoint) {
            if (PHOTO_TYPE_EXIT == Integer.parseInt(photo.get(SS_PHOTO_TYPE))) {
                photoList.add(photo);
            }
        }
        int floorNum = 0;
        if (floorName.startsWith("B")) {
            floorNum = -Integer.parseInt(floorName.replaceAll("B", ""));
        } else if (floorName.startsWith("F")) {
            floorNum = Integer.parseInt(floorName.replaceAll("F", ""));
        }
        for (HashMap<String, String> photo : lstPoint) {

            // type为2,6,21在每层都显示,floor为0在每层都显示
            if (null != photo.get(SS_PHOTO_TYPE) && !"".equals(photo.get(SS_PHOTO_TYPE))) {
                if (2 == Integer.parseInt(photo.get(SS_PHOTO_TYPE)) || 6 == Integer.parseInt(photo.get(SS_PHOTO_TYPE))
                        || 21 == Integer.parseInt(photo.get(SS_PHOTO_TYPE))) {
                    photoList.add(photo);
                }
            }
            if (null != photo.get(SS_PHOTO_FLOOR) && !"".equals(photo.get(SS_PHOTO_FLOOR))) {
                if (floorNum == Integer.parseInt(photo.get(SS_PHOTO_FLOOR))
                        || 0 == Integer.parseInt(photo.get(SS_PHOTO_FLOOR))) {
                    photoList.add(photo);
                }
            }
        }
        return photoList;
    }
}
