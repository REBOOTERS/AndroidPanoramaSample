package com.engineer.panorama.util;

import android.app.Activity;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DeviceUtil {

    private static volatile Class<?> sMiuiClazz;
    private static volatile Field sMiuiField1;
    private static volatile Method sMiuiField2;
    private static boolean sMiuiError = false;

    /**
     * MIUI 系统中设置 StatusBar 的文本颜色为黑色
     */
    public static boolean setMIUIStatusBarLightMode(Activity activity, boolean lightmode) {
        if (sMiuiError) return false;

        boolean result = false;
        try {
            int darkModeFlag = 0;
            if (sMiuiClazz == null) {
                synchronized (DeviceUtil.class) {
                    if (sMiuiClazz == null) {
                        sMiuiClazz = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                        sMiuiField1 = sMiuiClazz.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                        sMiuiField2 = activity.getWindow().getClass()
                                .getMethod("setExtraFlags", int.class, int.class);
                    }
                }
            }

            darkModeFlag = sMiuiField1.getInt(sMiuiClazz);
            sMiuiField2.invoke(activity.getWindow(), lightmode ? darkModeFlag : 0, darkModeFlag);
            result = true;
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException ignored) {
            sMiuiError = true;
        } catch (Exception ignored) {
        }
        return result;
    }

    private static volatile Field sFlymeField1;
    private static volatile Field sFlymeField2;
    private static boolean sFlymeError = false;

    /**
     * Flyme 系统中设置 StatusBar 的文本颜色为黑色
     */
    public static boolean setFlymeStatusBarLightMode(Activity activity, boolean lightmode) {
        if (sFlymeError) return false;

        boolean result = false;
        try {
            if (sFlymeField1 == null) {
                synchronized (DeviceUtil.class) {
                    if (sFlymeField1 == null) {
                        sFlymeField1 = WindowManager.LayoutParams.class
                                .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                        sFlymeField2 = WindowManager.LayoutParams.class
                                .getDeclaredField("meizuFlags");
                        sFlymeField1.setAccessible(true);
                        sFlymeField2.setAccessible(true);
                    }
                }
            }

            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

            int bit = sFlymeField1.getInt(null);
            int value = sFlymeField2.getInt(lp);
            if (lightmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            sFlymeField2.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            result = true;
        } catch (NoSuchFieldException ignored) {
            sFlymeError = true;
        } catch (Exception ignored) {
        }
        return result;
    }
}
