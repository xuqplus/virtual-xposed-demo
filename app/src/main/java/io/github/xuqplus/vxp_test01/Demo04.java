package io.github.xuqplus.vxp_test01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Demo04 implements IXposedHookLoadPackage {

    private boolean isMainToMain2 = true;
    private boolean isMain2ToMain = false;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        XposedBridge.log("handleLoadPackage executed..");

        logLoadPackageParam(loadPackageParam);

        boolean isYiGeTestApp = isThatApp(loadPackageParam, "io.github.xuqplus.yigetestapp", "一个测试的app");

        if (isYiGeTestApp) {
            final ClassLoader classLoader = (ClassLoader) loadPackageParam.getClass().getField("classLoader").get(loadPackageParam);
            final Class main = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.MainActivity", classLoader);
            final Class main2 = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.Main2Activity", classLoader);

            /**
             * MainActivity直接跳到Main2Activity
             */
            XposedHelpers.findAndHookMethod(main, "onCreate", new Object[]{Bundle.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    if (isMainToMain2) return;

                    XposedBridge.log(String.format("###### 1 param.thisObject=%s", param.thisObject));

                    Object intent = XposedHelpers.newInstance(Intent.class,
                            new Class[]{Context.class, Class.class},
                            new Object[]{param.thisObject, XposedHelpers.findClass("io.github.xuqplus.yigetestapp.Main2Activity", classLoader)});
                    XposedHelpers.callMethod(param.thisObject,
                            "startActivity",
                            new Class[]{Intent.class},
                            new Object[]{intent});

                    XposedBridge.log(String.format("###### 1 param.thisObject=%s", param.thisObject));
                    isMainToMain2 = true;
                }
            }});

            /**
             * Main2回到Main
             */
            XposedHelpers.findAndHookMethod(main2, "onCreate", new Object[]{Bundle.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    if (isMainToMain2 && isMain2ToMain) return;
                    XposedBridge.log(String.format("###### 2 param.thisObject=%s", param.thisObject));

                    Object intent = XposedHelpers.newInstance(Intent.class,
                            new Class[]{Context.class, Class.class},
                            new Object[]{param.thisObject, XposedHelpers.findClass("io.github.xuqplus.yigetestapp.MainActivity", classLoader)});
                    XposedHelpers.callMethod(param.thisObject,
                            "startActivity",
                            new Class[]{Intent.class},
                            new Object[]{intent});

                    XposedBridge.log(String.format("###### 2 param.thisObject=%s", param.thisObject));
                    isMain2ToMain = true;
                }
            }});
        }
    }

    /**
     * 检查启动的是不是这个app
     */
    private boolean isThatApp(XC_LoadPackage.LoadPackageParam loadPackageParam, String packageName, String appName) {
        if (null != loadPackageParam) {
            try {
                Field field = loadPackageParam.getClass().getDeclaredField("packageName");
                if (packageName.equals(field.get(loadPackageParam))) {
                    XposedBridge.log(String.format("%s app founded..", appName));
                    return true;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 打印LoadPackageParam相关信息
     */
    private void logLoadPackageParam(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (null != loadPackageParam) {
            XposedBridge.log(String.format("String.valueOf(loadPackageParam)=%s", String.valueOf(loadPackageParam)));
            XposedBridge.log(String.format("JSON.toJSONString(loadPackageParam)=%s", JSON.toJSONString(loadPackageParam)));
            XposedBridge.log(String.format("loadPackageParam.appInfo=%s", loadPackageParam.appInfo));
            XposedBridge.log(String.format("loadPackageParam.isFirstApplication=%s", loadPackageParam.isFirstApplication));
            XposedBridge.log(String.format("loadPackageParam.processName=%s", loadPackageParam.processName));
            XposedBridge.log(String.format("loadPackageParam.packageName=%s", loadPackageParam.packageName));
            XposedBridge.log(String.format("loadPackageParam.classLoader=%s", loadPackageParam.classLoader));
            XposedBridge.log(String.format("loadPackageParam.classLoader.getClass().getName()=%s", loadPackageParam.classLoader.getClass().getName()));
            logDeclaredFields(loadPackageParam);
        } else {
            XposedBridge.log(String.format("loadPackageParam is null"));
        }
    }

    private void logDeclaredFields(Object obj) {
        if (null == obj) return;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                boolean accessible = field.isAccessible();
                if (!accessible) field.setAccessible(true);
                Object o = field.get(obj);
                if (!accessible) field.setAccessible(false);
                XposedBridge.log(String.format("fieldName=%s, o=%s", field.getName(), o));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
