package io.github.xuqplus.vxp_test01;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

public class Demo03 implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        XposedBridge.log("handleLoadPackage executed..");

        logLoadPackageParam(loadPackageParam);

        boolean isAlipaysApp = isThatApp(loadPackageParam, "com.eg.android.AlipayGphone", "支付宝");

        if (isAlipaysApp) {
            XposedBridge.log("检测到支付宝了..");

            final ClassLoader classLoader = (ClassLoader) loadPackageParam.getClass().getField("classLoader").get(loadPackageParam);

            XposedHelpers.findAndHookMethod(Application.class, "attach", new Object[]{Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(final XC_MethodHook.MethodHookParam param) throws Throwable {
                    XposedBridge.log("#### attach afterHookedMethod ####");
                    ClassLoader args0 = ((Context) param.args[0]).getClassLoader();
                    XposedBridge.log(String.format("#### attach afterHookedMethod args0=%s", args0));
                    XposedBridge.log(String.format("#### attach afterHookedMethod args0.getClass()=%s", args0.getClass()));
                    XposedBridge.log(String.format("#### attach afterHookedMethod args0.getClass().getName()=%s", args0.getClass().getName()));
                    XposedBridge.log(String.format("#### attach afterHookedMethod param.args=%s", param.args));
                    XposedBridge.log(String.format("#### attach afterHookedMethod Arrays.toString(param.args)=%s", Arrays.toString(param.args)));
//                    securityCheckHook(classLoader);
//                    hookRpc(classLoader);
                    try {
                        XposedBridge.log("#### 1");
                        Class classChatMessageProcesser = args0.loadClass("com.alipay.mobile.socialchatsdk.chat.processer.ChatMessageProcesser");
                        Class classMessageFactory = args0.loadClass("com.alipay.mobile.socialchatsdk.chat.sender.MessageFactory");
                        XposedBridge.log(String.format("afterHookedMethod #### classChatMessageProcesser=%s", classChatMessageProcesser.getName()));
                        XposedBridge.log(String.format("afterHookedMethod #### classChatMessageProcesser=%s", classMessageFactory.getName()));
                        XposedBridge.log("#### 2");
                        /* MessageFactory.createTextMsg */
                        XposedHelpers.findAndHookMethod(classMessageFactory, "createTextMsg", new Object[]{String.class, String.class, String.class, String.class, String.class, Boolean.TYPE, new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param1) throws Throwable {
                                XposedBridge.log("#### createTextMsg beforeHookedMethod");
                                XposedBridge.log(String.format("#### createTextMsg beforeHookedMethod param1=%s", param1));
                                XposedBridge.log(String.format("#### createTextMsg beforeHookedMethod param1=%s", param1.args));
                                XposedBridge.log(String.format("#### createTextMsg beforeHookedMethod param1=%s", Arrays.toString(param1.args)));
                            }

                            @Override
                            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param1) throws Throwable {
                                XposedBridge.log("#### createTextMsg afterHookedMethod");
                                XposedBridge.log(String.format("#### createTextMsg afterHookedMethod param1=%s", param1));
                                XposedBridge.log(String.format("#### createTextMsg afterHookedMethod param1=%s", param1.args));
                                XposedBridge.log(String.format("#### createTextMsg afterHookedMethod param1=%s", Arrays.toString(param1.args)));
                            }
                        }});
                        /* ChatMessageProcesser.processMessages */
                        XposedHelpers.findAndHookMethod(classChatMessageProcesser, "processMessages", new Object[]{List.class, new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(final XC_MethodHook.MethodHookParam param1) throws Throwable {
                                XposedBridge.log("#### processMessages beforeHookedMethod");
                                XposedBridge.log(String.format("#### processMessages beforeHookedMethod param1=%s", param1));
                                XposedBridge.log(String.format("#### processMessages beforeHookedMethod param1=%s", param1.args));
                                XposedBridge.log(String.format("#### processMessages beforeHookedMethod param1=%s", Arrays.toString(param1.args)));

                                /* 处理消息 */
                                handleMessage(param1.args);
                            }

                            @Override
                            protected void afterHookedMethod(final XC_MethodHook.MethodHookParam param1) throws Throwable {
                                XposedBridge.log("#### processMessages afterHookedMethod");
                                XposedBridge.log(String.format("#### processMessages afterHookedMethod param1=%s", param1));
                                XposedBridge.log(String.format("#### processMessages afterHookedMethod param1=%s", param1.args));
                                XposedBridge.log(String.format("#### processMessages afterHookedMethod param1=%s", Arrays.toString(param1.args)));
                            }
                        }});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }});
        }
    }

    private void handleMessage(Object[] args) {
        if (null != args) {
            // [incrementalId=1553835554173fromUId=2088702132008225fromLoginId=xuq***@live.cntoUId=2088012758570434toLoginId=nullmsgId=190329125950220675clientMsgId=MRELATION-FRIEND_208870213200822520880127585704341553835590260templateCode=8003templateData={"icon":"","m":"你已经添加了QQ，现在可以开始聊天了。","voiceOverText":""}hintMemo=nullbizMemo=nullbizType=MR-F-ACCegg=MR-F-ACClink=nullcreateTimeMills=1553835590261createTime=nullrecent=nullread=nullaction=4bizRemind=nullmsgIndex=8232f73f6ba7f3b24a6500ec47658165_190329125950220675msgOptType=null]
            Map<String, String> msg = parseMessage(((List) args[0]).get(0).toString());
            XposedBridge.log(String.format("#### parseMessage fromUId=%s", msg.get("fromUId")));
            XposedBridge.log(String.format("#### parseMessage fromLoginId=%s", msg.get("fromLoginId")));
            XposedBridge.log(String.format("#### parseMessage toUId=%s", msg.get("toUId")));
            XposedBridge.log(String.format("#### parseMessage toLoginId=%s", msg.get("toLoginId")));
            XposedBridge.log(String.format("#### parseMessage templateCode=%s", msg.get("templateCode")));
            XposedBridge.log(String.format("#### parseMessage templateData=%s", msg.get("templateData")));
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
                        JSONObject localObject4 = JSONObject.parseObject(next);
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
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return (byte) 1;
                }
            });
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", ClassLoader.class, String.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return (byte) 1;
                }
            });
            XposedHelpers.findAndHookMethod(securityCheckClazz, "a", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
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
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
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

    /**
     * 解析加好友的消息
     */
    private Map parseMessage(String msg) {
        String[] as = msg.split("=");
        final Map<String, Object> map = new HashMap() {{
            put("incrementalId", null);
            put("fromUId", null);
            put("fromLoginId", null);
            put("toUId", null);
            put("toLoginId", null);
            put("msgId", null);
            put("clientMsgId", null);
            put("templateCode", null);
            put("templateData", null);
            put("hintMemo", null);
            put("bizMemo", null);
            put("bizType", null);
            put("egg", null);
            put("link", null);
            put("createTimeMills", null);
            put("createTime", null);
            put("recent", null);
            put("read", null);
            put("action", null);
            put("bizRemind", null);
            put("msgIndex", null);
            put("msgOptType", null);
        }};
        for (int i = 0; i < as.length; i++) {
            String s = as[i];
            for (int j = s.length(); j > 0; j--) {
                String key = s.substring(j, s.length());
                if (map.containsKey(key)) {
                    as[i] = s.substring(0, j);
                    map.put(key, i);
                    break;
                }
            }
        }
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> kv = (Map.Entry) it.next();
            Object v = kv.getValue();
            if (null == v || "null".equals(v)) v = 0;
            map.put(kv.getKey(), as[(Integer) v + 1]);
        }
        return map;
    }
}
