package io.github.xuqplus.vxp_test01;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
                    hookRpc(classLoader);
                    try {
                        XposedBridge.log("#### 1");
                        Class classChatMessageProcesser = args0.loadClass("com.alipay.mobile.socialchatsdk.chat.processer.ChatMessageProcesser");
                        Class classMessageFactory = args0.loadClass("com.alipay.mobile.socialchatsdk.chat.sender.MessageFactory");
                        XposedBridge.log(String.format("#### attach afterHookedMethod classChatMessageProcesser=%s", classChatMessageProcesser.getName()));
                        XposedBridge.log(String.format("#### attach afterHookedMethod classMessageFactory=%s", classMessageFactory.getName()));
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
                                Map<String, String> msg = handleMessage(param1.args);

                                /* 是新的好友吗 */
                                boolean isNewFriend = isNewFriend(msg);

                                /* 发起收款 */
                                if (isNewFriend) {
                                    boolean isCollected = collectMoney(classLoader, msg.get("fromUId"), "1.24", "哈哈, 付早上的饭钱1.24");
                                    /* 删除好友 */
                                    if (isCollected) {
                                        deleteContact(classLoader, msg.get("fromUId"));
                                    }
                                }
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

    /**
     * 是新添加好友的通知消息吗?
     */
    private boolean isNewFriend(Map<String, String> msg) {
        if (null != msg && "8003".equals(msg.get("templateCode")) && msg.get("templateData").contains("现在可以开始聊天了。")) {
            return true;
        }
        return false;
    }

    private Map handleMessage(Object[] args) {
        if (null != args) {
            // [incrementalId=1553835554173fromUId=2088702132008225fromLoginId=xuq***@live.cntoUId=2088012758570434toLoginId=nullmsgId=190329125950220675clientMsgId=MRELATION-FRIEND_208870213200822520880127585704341553835590260templateCode=8003templateData={"icon":"","m":"你已经添加了QQ，现在可以开始聊天了。","voiceOverText":""}hintMemo=nullbizMemo=nullbizType=MR-F-ACCegg=MR-F-ACClink=nullcreateTimeMills=1553835590261createTime=nullrecent=nullread=nullaction=4bizRemind=nullmsgIndex=8232f73f6ba7f3b24a6500ec47658165_190329125950220675msgOptType=null]
            Map<String, String> msg = parseMessage(((List) args[0]).get(0).toString());
            XposedBridge.log(String.format("#### parseMessage fromUId=%s", msg.get("fromUId")));
            XposedBridge.log(String.format("#### parseMessage fromLoginId=%s", msg.get("fromLoginId")));
            XposedBridge.log(String.format("#### parseMessage toUId=%s", msg.get("toUId")));
            XposedBridge.log(String.format("#### parseMessage toLoginId=%s", msg.get("toLoginId")));
            XposedBridge.log(String.format("#### parseMessage templateCode=%s", msg.get("templateCode")));
            XposedBridge.log(String.format("#### parseMessage templateData=%s", msg.get("templateData")));
            return msg;
        }
        return null;
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

    /**
     * 发起主动收款
     */
    private boolean collectMoney(ClassLoader classLoader, String userId, String payAmount, String desc) {
        XposedBridge.log(String.format("#### collectMoney, userId=%s, payAmount=%s, desc=%s", userId, payAmount, desc));
        Object o = XposedHelpers.newInstance(XposedHelpers.findClass("com.alipay.android.phone.personalapp.socialpayee.rpc.req.SingleCreateReq", classLoader));
        XposedHelpers.setObjectField(o, "userId", userId);
        XposedHelpers.setObjectField(o, "logonId", "");
        XposedHelpers.setObjectField(o, "payAmount", payAmount);
        XposedHelpers.setObjectField(o, "userName", "");
        XposedHelpers.setObjectField(o, "billName", "个人收款");
        XposedHelpers.setObjectField(o, "source", "chat");
        XposedHelpers.setObjectField(o, "desc", desc);
        XposedBridge.log(String.format("#### collectMoney, JSON.toJSONString(o)=%s", JSON.toJSONString(o)));
        Object r = XposedHelpers.callMethod(this.socialPersonalActivity, "createBill", new Object[]{o});
        XposedBridge.log(String.format("#### collectMoney, JSON.toJSONString(r)=%s", JSON.toJSONString(r)));
        return false;
    }

    /**
     * 删除好友
     */
    private boolean deleteContact(ClassLoader classLoader, String userId) {
        XposedBridge.log(String.format("#### deleteContact, userId=%s", userId));
        Object alipayAccount = XposedHelpers.callMethod(this.aliAccountDaoOp, "getAccountById", userId);
        XposedBridge.log(String.format("#### deleteContact, alipayAccount=%s", alipayAccount));
        // JSON.toJSONString(alipayAccount)=
        // {"account":"340824a05nw.cdb@sina.cn","accountType":"2","active":true,"alipayAccount":true,"area":"青浦区","blacked":false,"displayName":"xuqplus",
        // "displayNickName":"xuqplus","exposedAlipayAccount":"340824a05nw.cdb@sina.cn","
        // extSocialInfo":"{\"age\":\"25\",\"bgImgUrl\":\"\",\"constellation\":\"TX\",\"displayArea\":\"上海 青浦区\",\"height\":\"\",\"income\":\"\",\"interest\":\"\",\"profession\":\"\",\"weight\":\"\"}",
        // "extVersion":1534386419799,"firstAlphaChar":"X","friendStatus":1,"gender":"m","groupMemberCount":0,"headImageUrl":"http://tfs.alipayobjects.com/images/partner/T1zRNCXdtcXXXXXXXX_160X160",
        // "hideFriendMoments":"N","hideRealName":false,"isDelete":false,"isFrom":"account","isTop":false,"loginId":"340824a05nw.cdb@sina.cn","matchedPinyinStr":"XUQPLUS",
        // "mobileMatched":0,"myFriend":true,"name":"许群群","nameExceptGroupNick":"xuqplus","nickName":"xuqplus","notDisturb":false,"notShareMyMoments":"N",
        // "phoneNumber":"","province":"上海","realNameStatus":"Y","realNameVisable":true,"showAsEnterprise":"N","source":"通过好友验证添加","sourceDec":"by_f_v","starFriend":false,
        // "userGrade":"大众会员","userId":"2088012758570434","userType":"1","version":0,"zmCreditText":"","zmCreditUrl":""}
        XposedBridge.log(String.format("#### deleteContact, JSON.toJSONString(alipayAccount)=%s", JSON.toJSONString(alipayAccount)));
        Object o = XposedHelpers.newInstance(XposedHelpers.findClass("com.alipay.mobilerelation.biz.shared.req.HandleRelationReq", classLoader));
        XposedHelpers.setObjectField(o, "targetUserId", userId);
        XposedHelpers.setObjectField(o, "alipayAccount", XposedHelpers.getObjectField(alipayAccount, "account"));
        XposedHelpers.setObjectField(o, "bizType", "2");
        Object r = XposedHelpers.callMethod(XposedHelpers.callMethod(this.rpcFactory, "getRpcProxy", XposedHelpers.findClass("com.alipay.mobilerelation.biz.shared.rpc.AlipayRelationManageService", classLoader)), "handleRelation", o);
        // {"resultCode":100,"success":true,"toastType":1}
        XposedBridge.log(String.format("#### deleteContact, JSON.toJSONString(r)=%s", JSON.toJSONString(r)));
        return XposedHelpers.getBooleanField(r, "success");
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
    private Object socialPersonalActivity = null;
    private Object aliAccountDaoOp = null;
    private Object rpcFactory = null;

    public void hookRpc(ClassLoader classLoader) {
//        if (null == aliAccountDaoOp) {
        try {
            XposedHelpers.findAndHookConstructor("com.alipay.mobile.socialcommonsdk.bizdata.contact.data.AliAccountDaoOp", classLoader, new Object[]{String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    aliAccountDaoOp = param.thisObject;
                    XposedBridge.log(String.format("#### hookRpc aliAccountDaoOp set.."));
                }
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (null == rpcFactory) {
        try {
            XposedHelpers.findAndHookMethod(XposedHelpers.findClass("com.alipay.mobile.common.rpc.RpcFactory", classLoader), "getRpcProxy", new Object[]{Class.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
//                    if (null != param && null != param.args && param.args[0].toString().contains("com.alipay.mobilerelation.biz.shared.rpc.AlipayRelationManageService")) {
//                        rpcFactory = param.thisObject;
//                        XposedBridge.log(String.format("#### hookRpc rpcFactory set.."));
//                    }
                    Object z = XposedHelpers.callMethod(param.args[0], "getName", new Object[0]);
                    XposedBridge.log(String.format("#### hookRpc getName z=%s", z));
                    XposedBridge.log(String.format("#### hookRpc getName z=%s", JSON.toJSONString(z)));
                    if (z.toString().equals("com.alipay.mobilerelation.biz.shared.rpc.AlipayRelationManageService")) {
                        rpcFactory = param.thisObject;
                        XposedBridge.log(String.format("#### hookRpc rpcFactory set.."));
                    }
                }
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (null == socialPersonalActivity) {
        try {
            XposedHelpers.findAndHookMethod(XposedHelpers.findClass("com.alipay.android.phone.personalapp.socialpayee.ui.SocialPersonalActivity", classLoader), "a", new Object[]{new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    socialPersonalActivity = XposedHelpers.getObjectField(param.thisObject, "g");
                    XposedBridge.log(String.format("#### hookRpc socialPersonalActivity set.."));
                }
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }
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
