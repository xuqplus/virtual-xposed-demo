package io.github.xuqplus.vxp_test01;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Demo05 implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        XposedBridge.log("handleLoadPackage executed..");

        logLoadPackageParam(loadPackageParam);

        boolean isYiGeTestApp = isThatApp(loadPackageParam, "io.github.xuqplus.yigetestapp", "一个测试的app");

        if (isYiGeTestApp) {
            final ClassLoader classLoader = loadPackageParam.classLoader;

            final Class aaa = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.Main3Activity$AAA", classLoader);
            logFM(aaa);
            final Class c1 = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.Main3Activity$1", classLoader);
            logFM(c1);
            final Class c2 = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.Main3Activity$2", classLoader);
            logFM(c2);
            final Class c3 = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.Main3Activity$3", classLoader);
            logFM(c3);
            final Class c4 = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.Main3Activity$4", classLoader);
            logFM(c4);

            /**
             * 检测网络请求
             */
            XposedBridge.hookAllMethods(aaa, "onSuccess", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("#### hookAllMethods ..");
                    XposedBridge.log(String.format("#### hookAllMethods .. param=%s", param));
                    XposedBridge.log(String.format("#### hookAllMethods .. param=%s", Arrays.toString(param.args)));
                    int o0 = (int) param.args[0];
                    Header[] o1 = (Header[]) param.args[0];
                    byte[] o2 = (byte[]) param.args[0];
                    final String r = new String(o2);
                }
            });
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
        } else {
            XposedBridge.log(String.format("loadPackageParam is null"));
        }
    }

    private void logFM(Class claxx) {
        XposedBridge.log(String.format("#### class=%s", claxx));
        XposedBridge.log(String.format("#### class=%s, getFields=%s", claxx, Arrays.toString(claxx.getFields())));
        XposedBridge.log(String.format("#### class=%s, getDeclaredFields=%s", claxx, Arrays.toString(claxx.getDeclaredFields())));
        XposedBridge.log(String.format("#### class=%s, getMethods=%s", claxx, Arrays.toString(claxx.getMethods())));
        XposedBridge.log(String.format("#### class=%s, getDeclaredMethods=%s", claxx, Arrays.toString(claxx.getDeclaredMethods())));
    }
}
