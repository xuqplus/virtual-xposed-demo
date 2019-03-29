package io.github.xuqplus.vxp_test01;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Demo02 implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        XposedBridge.log("handleLoadPackage executed..");

        logLoadPackageParam(loadPackageParam);

        boolean isYiGeTestApp = isThatApp(loadPackageParam, "io.github.xuqplus.yigetestapp", "一个测试的app");

        if (isYiGeTestApp) {
            ClassLoader classLoader = (ClassLoader) loadPackageParam.getClass().getField("classLoader").get(loadPackageParam);
            Class clazz = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.MainActivity", classLoader);
//            Class clazz = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.MainActivity", loadPackageParam.classLoader); // crash
//            Class clazz = loadPackageParam.classLoader.loadClass("io.github.xuqplus.yigetestapp.MainActivity"); // class not found exception
            XposedBridge.log(String.format("clazz=%s", clazz));
            XposedBridge.log(String.format("clazz.getName()=%s", clazz.getName()));
            XposedHelpers.findAndHookMethod(clazz, "getToastContent", new Object[]{new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log(String.format("beforeHookedMethod param=%s", param));
                    XposedBridge.log(String.format("beforeHookedMethod param.args=%s", param.args));
                    XposedBridge.log(String.format("beforeHookedMethod Arrays.toString(param.args)=%s", Arrays.toString(param.args)));
                    XposedBridge.log(String.format("beforeHookedMethod param.thisObject=%s", param.thisObject));
                    XposedBridge.log(String.format("beforeHookedMethod param.thisObject.getClass()=%s", param.thisObject.getClass()));
                    XposedBridge.log(String.format("beforeHookedMethod JSON.toJSONString(param.thisObject)=%s", JSON.toJSONString(param.thisObject)));
                    XposedBridge.log(String.format("beforeHookedMethod param.method=%s", param.method));
                    XposedBridge.log(String.format("beforeHookedMethod param.getResult()=%s", param.getResult()));

                    param.setResult("beforeHookedMethod 修改返回值"); // 生效, 最终要看afterHookedMethod是否setResult
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log(String.format("param=%s", param));

                    XposedBridge.log(String.format("afterHookedMethod param=%s", param));
                    XposedBridge.log(String.format("afterHookedMethod param.args=%s", param.args));
                    XposedBridge.log(String.format("afterHookedMethod Arrays.toString(param.args)=%s", Arrays.toString(param.args)));
                    XposedBridge.log(String.format("afterHookedMethod param.thisObject=%s", param.thisObject));
                    XposedBridge.log(String.format("afterHookedMethod param.thisObject.getClass()=%s", param.thisObject.getClass()));
                    XposedBridge.log(String.format("afterHookedMethod JSON.toJSONString(param.thisObject)=%s", JSON.toJSONString(param.thisObject)));
                    XposedBridge.log(String.format("afterHookedMethod param.method=%s", param.method));
                    XposedBridge.log(String.format("afterHookedMethod param.getResult()=%s", param.getResult()));

//                    param.setResult("afterHookedMethod 修改返回值"); // 生效
                }
            }});
        }

        if (isYiGeTestApp) {
            ClassLoader classLoader = (ClassLoader) loadPackageParam.getClass().getField("classLoader").get(loadPackageParam);
            final Class clazz = XposedHelpers.findClass("io.github.xuqplus.yigetestapp.MainActivity", classLoader);
            XposedHelpers.findAndHookMethod(clazz, "onclick", new Object[]{String.class, new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);

                    if (null != param.args)
                        param.args[0] = "哈哈， 修改了参数123"; // 修改输入参数

//                    param.setThrowable(new RuntimeException("prevent method executed")); // 设置异常阻止方法执行
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
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
