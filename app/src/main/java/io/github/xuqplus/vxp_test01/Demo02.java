package io.github.xuqplus.vxp_test01;

import android.app.Application;
import android.content.Context;
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
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedBridge.log("handleLoadPackage executed..");

        logLoadPackageParam(loadPackageParam);

        boolean isAlipaysApp = isThatApp(loadPackageParam, "com.eg.android.AlipayGphone", "支付宝");

        if (isAlipaysApp) {
            XposedBridge.log("检测到支付宝了..");
            XposedHelpers.findAndHookMethod(Application.class, "attach", new Object[]{Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) {
                    XposedBridge.log(String.format("afterHookedMethod, param=%s", JSON.toJSONString(param)));

//                    Object localObject = ((Context) param.args[0]).getClassLoader();
//                    securityCheckHook(classLoader);
//                    hookRpc(classLoader);                    try {
//                        Object o = ((ClassLoader) localObject).loadClass("com.alipay.mobile.socialchatsdk.chat.processer.ChatMessageProcesser");
//                        localObject = ((ClassLoader) localObject).loadClass("com.alipay.mobile.socialchatsdk.chat.sender.MessageFactory");
//                        XposedHelpers.findAndHookMethod((Class) localObject, "createTextMsg", new Object[]{String.class, String.class, String.class, String.class, String.class, Boolean.TYPE, new XC_MethodHook() {
//                            protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymous2MethodHookParam) throws Throwable {
//
//                            }
//
//                            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymous2MethodHookParam)
//                                    throws Throwable {
//                                XposedBridge.log("hookclass2k������");
//                                StringBuilder localStringBuilder = new StringBuilder();
//                                localStringBuilder.append("beforeHookedMethod ��1������:");
//                                localStringBuilder.append(paramAnonymous2MethodHookParam.args[0]);
//                                XposedBridge.log(localStringBuilder.toString());
//                                localStringBuilder = new StringBuilder();
//                                localStringBuilder.append("beforeHookedMethod  ��2������:");
//                                localStringBuilder.append(paramAnonymous2MethodHookParam.args[1]);
//                                XposedBridge.log(localStringBuilder.toString());
//                                localStringBuilder = new StringBuilder();
//                                localStringBuilder.append("beforeHookedMethod  ��3������:");
//                                localStringBuilder.append(paramAnonymous2MethodHookParam.args[2]);
//                                XposedBridge.log(localStringBuilder.toString());
//                                localStringBuilder = new StringBuilder();
//                                localStringBuilder.append("beforeHookedMethod  ��4������:");
//                                localStringBuilder.append(paramAnonymous2MethodHookParam.args[3]);
//                                XposedBridge.log(localStringBuilder.toString());
//                                localStringBuilder = new StringBuilder();
//                                localStringBuilder.append("beforeHookedMethod  ��5������:");
//                                localStringBuilder.append(paramAnonymous2MethodHookParam.args[4]);
//                                XposedBridge.log(localStringBuilder.toString());
//                                localStringBuilder = new StringBuilder();
//                                localStringBuilder.append("beforeHookedMethod  ��6������:");
//                                localStringBuilder.append(paramAnonymous2MethodHookParam.args[5]);
//                                XposedBridge.log(localStringBuilder.toString());
//                                localStringBuilder = new StringBuilder();
//                                localStringBuilder.append("beforeHookedMethod ����������:");
//                                localStringBuilder.append(paramAnonymous2MethodHookParam.getResult());
//                                XposedBridge.log(localStringBuilder.toString());
//                            }
//                        }});
//                        XposedHelpers.findAndHookMethod(paramAnonymousMethodHookParam, "processMessages", new Object[]{List.class, new XC_MethodHook() {
//                            protected void beforeHookedMethod(final XC_MethodHook.MethodHookParam paramAnonymous2MethodHookParam) throws Throwable {
//                                Object localObject1 = new StringBuilder();
//                                ((StringBuilder) localObject1).append("afterHookedMethod ��1������:");
//                                ((StringBuilder) localObject1).append(paramAnonymous2MethodHookParam.args[0]);
//                                XposedBridge.log(((StringBuilder) localObject1).toString());
//                                Object localObject2 = new String[6];
//                                localObject2[0] = "fromUId=(.*?)fromLoginId";
//                                localObject2[1] = "fromLoginId=(.*?)toUId";
//                                localObject2[2] = "templateCode=(.*?)templateData";
//                                localObject2[3] = "templateData=(.*?)hintMemo";
//                                localObject2[4] = "link=(.*?)Mills=";
//                                localObject2[5] = "toUId=(.*?)toLoginId=";
//                                String[] arrayOfString1 = new String[localObject2.length];
//                                int i = 0;
//                                Object localObject4;
//                                while (i < localObject2.length) {
//                                    localObject1 = "";
//                                    localObject4 = Pattern.compile(localObject2[i]).matcher(paramAnonymous2MethodHookParam.args[0].toString());
//                                    while (((Matcher) localObject4).find()) {
//                                        localObject1 = ((Matcher) localObject4).group(1);
//                                    }
//                                    arrayOfString1[i] = localObject1;
//                                    i += 1;
//                                }
//                                localObject2 = Pattern.compile("m\":\"(.*?)\"");
//                                localObject1 = Pattern.compile("/>(.*?)<a");
//                                localObject2 = ((Pattern) localObject2).matcher(arrayOfString1[3]);
//                                localObject1 = ((Pattern) localObject1).matcher(arrayOfString1[3]);
//                                while (((Matcher) localObject2).find()) {
//                                    ((Matcher) localObject2).group(1);
//                                }
//                                while (((Matcher) localObject1).find()) {
//                                    ((Matcher) localObject1).group(1);
//                                }
//                                localObject2 = com.alibaba.fastjson.JSONObject.parseObject(arrayOfString1[3]);
//                                String str2 = ((com.alibaba.fastjson.JSONObject) localObject2).getString("midTitle");
//                                if ((arrayOfString1[2].equals("8003")) && (arrayOfString1[3].contains("������������������")) && (collectRpcFac != null)) {
//                                    try {
//                                        paramAnonymous2MethodHookParam = new StringBuilder();
//                                        paramAnonymous2MethodHookParam.append("questUid=");
//                                        paramAnonymous2MethodHookParam.append(arrayOfString1[0]);
//                                        XposedBridge.log(paramAnonymous2MethodHookParam.toString());
//                                        paramAnonymous2MethodHookParam = arrayOfString1[0];
//                                        localObject1 = AndroidNetworking.post("http://192.168.124.72:8080/vxp/bindOrderPayUser");
//                                        try {
//                                            ((ANRequest.PostRequestBuilder) localObject1).addBodyParameter("pay_user_name", arrayOfString1[0]).setTag("test").setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
//                                                public void onError(ANError paramAnonymous3ANError) {
//                                                    StringBuilder localStringBuilder = new StringBuilder();
//                                                    localStringBuilder.append("");
//                                                    localStringBuilder.append(paramAnonymous3ANError);
//                                                    XposedBridge.log(localStringBuilder.toString());
//                                                }
//
//                                                public void onResponse(org.json.JSONObject paramAnonymous3JSONObject) {
//                                                    Object localObject1 = new StringBuilder();
//                                                    ((StringBuilder) localObject1).append("response1=");
//                                                    ((StringBuilder) localObject1).append(paramAnonymous3JSONObject);
//                                                    XposedBridge.log(((StringBuilder) localObject1).toString());
//                                                    paramAnonymous3JSONObject = com.alibaba.fastjson.JSONObject.parseObject(paramAnonymous3JSONObject.toString());
//                                                    if (paramAnonymous3JSONObject.getString("code").equals("0")) {
//                                                        paramAnonymous3JSONObject = paramAnonymous3JSONObject.getString("data");
//                                                        localObject1 = new StringBuilder();
//                                                        ((StringBuilder) localObject1).append("res.data=");
//                                                        ((StringBuilder) localObject1).append(paramAnonymous3JSONObject);
//                                                        XposedBridge.log(((StringBuilder) localObject1).toString());
//                                                        Object localObject2 = com.alibaba.fastjson.JSONObject.parseObject(paramAnonymous3JSONObject);
//                                                        paramAnonymous3JSONObject = ((com.alibaba.fastjson.JSONObject) localObject2).getString("order_amount");
//                                                        localObject1 = new StringBuilder();
//                                                        ((StringBuilder) localObject1).append("res.money=");
//                                                        ((StringBuilder) localObject1).append(paramAnonymous3JSONObject);
//                                                        XposedBridge.log(((StringBuilder) localObject1).toString());
//                                                        localObject1 = ((com.alibaba.fastjson.JSONObject) localObject2).getString("pay_user_name");
//                                                        localObject2 = ((com.alibaba.fastjson.JSONObject) localObject2).getString("order_sn");
//                                                        com.alibaba.fastjson.JSONObject localJSONObject = new com.alibaba.fastjson.JSONObject();
//                                                        localJSONObject.put("userid", localObject1);
//                                                        localJSONObject.put("amount", paramAnonymous3JSONObject);
//                                                        localJSONObject.put("order", localObject2);
//                                                        shoukuanlist.put(localObject2, localJSONObject.toJSONString());
//                                                        orderRecord.put(localObject2, localJSONObject.toJSONString());
//                                                        paramAnonymous3JSONObject = new StringBuilder();
//                                                        paramAnonymous3JSONObject.append("switch==");
//                                                        paramAnonymous3JSONObject.append(isDrawRedPackage);
//                                                        XposedBridge.log(paramAnonymous3JSONObject.toString());
//                                                        if (!isDrawRedPackage) {
//                                                            collectList(Main .2.
//                                                            this.val$classLoader);
//                                                        }
//                                                        return;
//                                                    }
//                                                    delectContact(Main .2.
//                                                    this.val$classLoader, paramAnonymous2MethodHookParam)
//                                                    ;
//                                                }
//                                            });
//                                        } catch (Exception paramAnonymous2MethodHookParam) {
//                                        }
//                                        return;
//                                    } catch (Exception paramAnonymous2MethodHookParam) {
//                                    }
//                                }
//                                Object localObject3;
//                                if (arrayOfString1[2].equals("105")) {
//                                    paramAnonymous2MethodHookParam = new StringBuilder();
//                                    paramAnonymous2MethodHookParam.append("deletvom=");
//                                    paramAnonymous2MethodHookParam.append(arrayOfString1[5]);
//                                    XposedBridge.log(paramAnonymous2MethodHookParam.toString());
//                                    delectContact(Main .2. this.val$classLoader, arrayOfString1[5]);
//                                    localObject4 = orderRecord.values().iterator();
//                                    paramAnonymous2MethodHookParam = (XC_MethodHook.MethodHookParam) localObject2;
//                                    while (((Iterator) localObject4).hasNext()) {
//                                        String str3 = (String) ((Iterator) localObject4).next();
//                                        localObject2 = new StringBuilder();
//                                        ((StringBuilder) localObject2).append("orderRecord=");
//                                        ((StringBuilder) localObject2).append(str3);
//                                        XposedBridge.log(((StringBuilder) localObject2).toString());
//                                        com.alibaba.fastjson.JSONObject localJSONObject = com.alibaba.fastjson.JSONObject.parseObject(str3);
//                                        if (localJSONObject.getString("order").equals(str2)) {
//                                            Object localObject5 = new String[1];
//                                            localObject5[0] = "tradeNO=(.*?)createTime";
//                                            String[] arrayOfString2 = new String[localObject5.length];
//                                            i = 0;
//                                            localObject2 = localObject1;
//                                            for (; ; ) {
//                                                localObject1 = paramAnonymous2MethodHookParam;
//                                                int j = localObject5.length;
//                                                paramAnonymous2MethodHookParam = (XC_MethodHook.MethodHookParam) localObject2;
//                                                if (i >= j) {
//                                                    break;
//                                                }
//                                                String str1 = "";
//                                                localObject2 = Pattern.compile(localObject5[i]);
//                                                Matcher localMatcher = ((Pattern) localObject2).matcher(arrayOfString1[4]);
//                                                while (localMatcher.find()) {
//                                                    str1 = localMatcher.group(1);
//                                                }
//                                                arrayOfString2[i] = str1;
//                                                i += 1;
//                                                localObject2 = paramAnonymous2MethodHookParam;
//                                                paramAnonymous2MethodHookParam = (XC_MethodHook.MethodHookParam) localObject1;
//                                            }
//                                            localObject2 = new StringBuilder();
//                                            ((StringBuilder) localObject2).append("RegxlinkResult=");
//                                            ((StringBuilder) localObject2).append(arrayOfString2[0]);
//                                            XposedBridge.log(((StringBuilder) localObject2).toString());
//                                            try {
//                                                localObject2 = AndroidNetworking.post("http://192.168.124.72:8080/vxp/updateOutTradeNo").addBodyParameter("order_sn", localJSONObject.getString("order"));
//                                                try {
//                                                    ((ANRequest.PostRequestBuilder) localObject2).addBodyParameter("trade_no", arrayOfString2[0]).setTag("test").setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
//                                                        public void onError(ANError paramAnonymous3ANError) {
//                                                            StringBuilder localStringBuilder = new StringBuilder();
//                                                            localStringBuilder.append("get_bind_order_payusererror=");
//                                                            localStringBuilder.append(paramAnonymous3ANError);
//                                                            XposedBridge.log(localStringBuilder.toString());
//                                                        }
//
//                                                        public void onResponse(org.json.JSONObject paramAnonymous3JSONObject) {
//                                                            StringBuilder localStringBuilder = new StringBuilder();
//                                                            localStringBuilder.append("get_bind_order_payuserresponse=");
//                                                            localStringBuilder.append(paramAnonymous3JSONObject);
//                                                            XposedBridge.log(localStringBuilder.toString());
//                                                        }
//                                                    });
//                                                } catch (Exception localException1) {
//                                                }
//                                                localObject5 = new StringBuilder();
//                                            } catch (Exception localException2) {
//                                            }
//                                            ((StringBuilder) localObject5).append("����trade_no_Exception=");
//                                            ((StringBuilder) localObject5).append(localException2);
//                                            XposedBridge.log(((StringBuilder) localObject5).toString());
//                                            orderRecord.remove(str3);
//                                        } else {
//                                            localObject3 = paramAnonymous2MethodHookParam;
//                                            paramAnonymous2MethodHookParam = (XC_MethodHook.MethodHookParam) localObject1;
//                                            localObject1 = localObject3;
//                                        }
//                                        localObject3 = paramAnonymous2MethodHookParam;
//                                        paramAnonymous2MethodHookParam = (XC_MethodHook.MethodHookParam) localObject1;
//                                        localObject1 = localObject3;
//                                    }
//                                    return;
//                                }
//                                if ((arrayOfString1[2].equals("8003")) && (arrayOfString1[3].contains("��������")) && (arrayOfString1[3].length() > 185) && (arrayOfString1[3].length() < 200) && (paramAnonymous2MethodHookParam.args[0].toString().length() < 760)) {
//                                    localObject1 = new String[1];
//                                    localObject1[0] = "tradeNO=(.*?)createTime";
//                                    localObject3 = new String[localObject1.length];
//                                    i = 0;
//                                    while (i < localObject1.length) {
//                                        paramAnonymous2MethodHookParam = "";
//                                        localObject4 = Pattern.compile(localObject1[i]).matcher(arrayOfString1[4]);
//                                        while (((Matcher) localObject4).find()) {
//                                            paramAnonymous2MethodHookParam = ((Matcher) localObject4).group(1);
//                                        }
//                                        localObject3[i] = paramAnonymous2MethodHookParam;
//                                        i += 1;
//                                    }
//                                    if (localObject3[0].length() == 32) {
//                                        try {
//                                            AndroidNetworking.post("http://192.168.124.72:8080/vxp/notify").addBodyParameter("trade_no", localObject3[0]).setTag("test").setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
//                                                public void onError(ANError paramAnonymous3ANError) {
//                                                    StringBuilder localStringBuilder = new StringBuilder();
//                                                    localStringBuilder.append("notify_tradeno_orders_error=");
//                                                    localStringBuilder.append(paramAnonymous3ANError);
//                                                    XposedBridge.log(localStringBuilder.toString());
//                                                }
//
//                                                public void onResponse(org.json.JSONObject paramAnonymous3JSONObject) {
//                                                    StringBuilder localStringBuilder = new StringBuilder();
//                                                    localStringBuilder.append("notify_tradeno_orders_response=");
//                                                    localStringBuilder.append(paramAnonymous3JSONObject);
//                                                    XposedBridge.log(localStringBuilder.toString());
//                                                }
//                                            });
//                                            return;
//                                        } catch (Exception paramAnonymous2MethodHookParam) {
//                                            localObject1 = new StringBuilder();
//                                            ((StringBuilder) localObject1).append("����trade_no_Exception=");
//                                            ((StringBuilder) localObject1).append(paramAnonymous2MethodHookParam);
//                                            XposedBridge.log(((StringBuilder) localObject1).toString());
//                                        }
//                                    }
//                                }
//                            }
//                        }});
//                        return;
//                    } catch (Exception paramAnonymousMethodHookParam) {
//                        localObject = new StringBuilder();
//                        ((StringBuilder) localObject).append("");
//                        ((StringBuilder) localObject).append(paramAnonymousMethodHookParam);
//                        Log.e("����", ((StringBuilder) localObject).toString());
//                    }
                }
            }});
        }
    }

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

    private void logLoadPackageParam(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (null != loadPackageParam) {
            XposedBridge.log(String.format("String.valueOf(loadPackageParam)=%s", String.valueOf(loadPackageParam)));
            XposedBridge.log(String.format("JSON.toJSONString(loadPackageParam)=%s", JSON.toJSONString(loadPackageParam)));
            XposedBridge.log(String.format("loadPackageParam.appInfo=%s", loadPackageParam.appInfo));
            XposedBridge.log(String.format("loadPackageParam.processName=%s", loadPackageParam.processName));
            XposedBridge.log(String.format("loadPackageParam.packageName=%s", loadPackageParam.packageName));
            Field[] fields = loadPackageParam.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    Object o = field.get(loadPackageParam);
                    XposedBridge.log(String.format("fieldName=%s, o=%s", field.getName(), o));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            XposedBridge.log(String.format("loadPackageParam是个null"));
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
     * 什么安全检查
     */
    private void securityCheckHook(ClassLoader classLoader) {
        try {
            Object o = XposedHelpers.findClass("com.alipay.mobile.base.security.CI", classLoader);
            XposedHelpers.findAndHookMethod(o.getClass(), "a", String.class, String.class, String.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object o1 = param.getResult();
                    XposedHelpers.setBooleanField(o1, "a", false);
                    param.setResult(o1);
                    super.afterHookedMethod(param);
                }
            });
            XposedHelpers.findAndHookMethod(o.getClass(), "a", Class.class, String.class, String.class, new XC_MethodReplacement() {
                protected Object replaceHookedMethod(MethodHookParam param) {
                    return Byte.valueOf((byte) 0);
                }
            });
            XposedHelpers.findAndHookMethod(o.getClass(), "a", ClassLoader.class, String.class, new XC_MethodReplacement() {
                protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam) {
                    return Byte.valueOf((byte) 0);
                }
            });
            XposedHelpers.findAndHookMethod(o.getClass(), "a", new XC_MethodReplacement() {
                protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam) {
                    return Boolean.valueOf(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 什么rpc
     */
    public void hookRpc(ClassLoader classLoader) {
//        XposedHelpers.findAndHookConstructor("com.alipay.mobile.socialcommonsdk.bizdata.contact.data.AliAccountDaoOp", classLoader, new Object[]{String.class, new XC_MethodHook() {
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
//                    throws Throwable {
//                Main.access$802(Main.this, paramAnonymousMethodHookParam.thisObject);
//                paramAnonymousMethodHookParam = new StringBuilder();
//                paramAnonymousMethodHookParam.append("contactDao===>");
//                paramAnonymousMethodHookParam.append(Main.this.contactDao);
//                XposedBridge.log(paramAnonymousMethodHookParam.toString());
//            }
//        }});
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
