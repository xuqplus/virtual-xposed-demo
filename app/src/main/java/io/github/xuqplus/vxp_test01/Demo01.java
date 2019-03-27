package io.github.xuqplus.vxp_test01;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 1.这个类需要注册到src/main/assets/xposed_init文件里
 * 2.IXposedHookLoadPackage 来自库 api-82.jar, api-82-sources.jar
 */
public class Demo01 implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log("handleLoadPackage executed.."); // 在logcat里搜xposed关键词可以找到这条日志
    }
}
