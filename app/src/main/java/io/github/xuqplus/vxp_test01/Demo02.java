package io.github.xuqplus.vxp_test01;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Demo02 implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        XposedBridge.log("handleLoadPackage executed..");

        logLoadPackageParam(loadPackageParam);

        boolean isAlipaysApp = isThatApp(loadPackageParam, "com.eg.android.AlipayGphone", "支付宝");

        if (isAlipaysApp) {
            XposedBridge.log("检测到支付宝了..");
//            final ClassLoader classLoader = loadPackageParam.classLoader;
//            XposedHelpers.findAndHookMethod(Application.class, "attach", new Object[]{Context.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    super.afterHookedMethod(param);
//                    Context context = (Context) param.args[0];
//                    ClassLoader appClassLoader = context.getClassLoader();
//                    securityCheckHook(appClassLoader);
//                    hookRpc(appClassLoader);
//                }
//            }});
        }

        boolean isYiGeTestApp = isThatApp(loadPackageParam, "io.github.xuqplus.yigetestapp", "一个测试的app");

/*
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
*/

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

    private Object collectRpcFac = null;
    private Object contactDao;
    private boolean isDrawRedPackage = false;
    private Map<String, String> orderRecord = new ConcurrentHashMap();
    private Object relationRpcFac;
    private Map<String, String> shoukuanlist = new ConcurrentHashMap();

    private void collectList(final ClassLoader paramClassLoader) {
        this.isDrawRedPackage = true;
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Iterator<String> it = shoukuanlist.values().iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception localException) {
                    }
                    if (next != null) {
                        JSONObject localObject4 = com.alibaba.fastjson.JSONObject.parseObject(next);
                        String userid = localObject4.getString("userid");
                        String amount = localObject4.getString("amount");
                        String order = localObject4.getString("order");
//                        collectMoney(paramClassLoader, userid, amount, order);
                        shoukuanlist.remove(localObject4);
                    }
                }
            }
        };
        new Timer().schedule(timerTask, 1L, 2000L);
    }

    /**
     * 发起主动收款
     */
    private void collectMoney(ClassLoader classLoader, String userId, String payAmount, String desc) {
        Object o = XposedHelpers.newInstance(XposedHelpers.findClass("com.alipay.android.phone.personalapp.socialpayee.rpc.req.SingleCreateReq", classLoader));
        XposedHelpers.setObjectField(o, "userId", userId);
        XposedHelpers.setObjectField(o, "logonId", "");
        XposedHelpers.setObjectField(o, "payAmount", payAmount);
        XposedHelpers.setObjectField(o, "userName", "");
        XposedHelpers.setObjectField(o, "billName", "个人收款");
        XposedHelpers.setObjectField(o, "source", "chat");
        XposedHelpers.setObjectField(o, "desc", desc);
        Object o1 = XposedHelpers.callMethod(this.collectRpcFac, "createBill", o);
        Log.i("xposed", JSON.toJSONString(o1));
    }

    /**
     * 删除好友
     */
    private void delectContact(ClassLoader classLoader, String userId) {
        Object o = XposedHelpers.callMethod(this.contactDao, "getAccountById", userId);
        Object o1 = XposedHelpers.newInstance(XposedHelpers.findClass("com.alipay.mobilerelation.biz.shared.req.HandleRelationReq", classLoader));
        XposedHelpers.setObjectField(o1, "targetUserId", userId);
        XposedHelpers.setObjectField(o1, "alipayAccount", o);
        XposedHelpers.setObjectField(o1, "bizType", "2");
        XposedHelpers.callMethod(XposedHelpers.callMethod(this.relationRpcFac, "getRpcProxy", XposedHelpers.findClass("com.alipay.mobilerelation.biz.shared.rpc.AlipayRelationManageService", classLoader)), "handleRelation", o1);
    }

    /**
     * 反反hook
     */
    private void securityCheckHook(ClassLoader classLoader) {
        try {
            Class securityCheckClazz = XposedHelpers.findClass("com.alipay.mobile.base.security.CI", classLoader);
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", String.class, String.class, String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object object = param.getResult();
                    XposedHelpers.setBooleanField(object, "a", false);
                    param.setResult(object);
                    super.afterHookedMethod(param);
                }
            });
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", Class.class, String.class, String.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    return (byte) 1;
                }
            });
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", ClassLoader.class, String.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    return (byte) 1;
                }
            });
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    return false;
                }
            });
        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }

    private void securityCheckHook0(ClassLoader classLoader) {
        try {
            Class clazz = XposedHelpers.findClass("com.alipay.mobile.base.security.CI", classLoader);
            XposedHelpers.findAndHookMethod(clazz, "a", clazz, Activity.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });
        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 什么rpc
     */
    public void hookRpc(ClassLoader classLoader) {
        XposedHelpers.findAndHookConstructor("com.alipay.mobile.socialcommonsdk.bizdata.contact.data.AliAccountDaoOp", classLoader, new Object[]{String.class, new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                XposedBridge.log("sssss==" + param);
            }
        }});
//        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("com.alipay.mobile.common.rpc.RpcFactory", paramClassLoader), "getRpcProxy", new Object[]{Class.class, new XC_MethodHook() {
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
//                    throws Throwable {
//                if (XposedHelpers.callMethod(paramAnonymousMethodHookParam.args[0], "getName", new Object[0]).equals("com.alipay.mobilerelation.biz.shared.rpc.AlipayRelationManageService")) {
//                    Main.access$902(Main.this, paramAnonymousMethodHookParam.thisObject);
//                }
//            }
//        }});
//        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("com.alipay.android.phone.personalapp.socialpayee.ui.SocialPersonalActivity", paramClassLoader), "a", new Object[]{new XC_MethodHook() {
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
//                    throws Throwable {
//                StringBuilder localStringBuilder = new StringBuilder();
//                localStringBuilder.append("dangqianobj======");
//                localStringBuilder.append(paramAnonymousMethodHookParam.thisObject);
//                XposedBridge.log(localStringBuilder.toString());
//                Main.access$302(Main.this, XposedHelpers.getObjectField(paramAnonymousMethodHookParam.thisObject, "g"));
//            }
//        }});
    }
}
