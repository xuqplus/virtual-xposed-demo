package io.github.xuqplus.vxp_test01;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Implement3Test implements IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log("Implement3Test handleLoadPackage executed..");
        if (null != loadPackageParam) {
            Log.i("xposed", "handleLoadPackage loadPackageParam=" + JSON.toJSONString(loadPackageParam));
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        XposedBridge.log("Implement3Test initZygote executed..");
        if (null != startupParam) {
            Log.i("xposed", "initZygote startupParam=" + JSON.toJSONString(startupParam));
        }
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        XposedBridge.log("Implement3Test handleInitPackageResources executed..");
        if (null != resparam) {
            Log.i("xposed", "handleInitPackageResources resparam=" + JSON.toJSONString(resparam));
        }
    }
}
