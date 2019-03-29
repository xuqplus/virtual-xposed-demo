package io.github.xuqplus.vxp_test01;

import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Demo03Test {

    /**
     * 加好友的消息解析
     */
    @Test
    public void a() {
        String a = "incrementalId=" +
                "1553837396606fromUId=" +
                "2088702132008225fromLoginId=" +
                "xuq***@live.cntoUId=" +
                "2088012758570434toLoginId=" +
                "nullmsgId=" +
                "190329133004220956clientMsgId=" +
                "MRELATION-FRIEND_208870213200822520880127585704341553837404817templateCode=" +
                "8003templateData=" +
                "{\"icon\":\"\",\"m\":\"你已经添加了QQ，现在可以开始聊天了。\",\"voiceOverText\":\"\"}hintMemo=" +
                "nullbizMemo=" +
                "nullbizType=" +
                "MR-F-ACCegg=" +
                "MR-F-ACClink=" +
                "nullcreateTimeMills=" +
                "1553837404819createTime=" +
                "nullrecent=" +
                "nullread=" +
                "nullaction=" +
                "4bizRemind=" +
                "nullmsgIndex=" +
                "8232f73f6ba7f3b24a6500ec47658165_190329133004220956msgOptType=" +
                "null";
        String[] as = a.split("=");
        Map<String, Object> map = new HashMap() {{
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

        System.out.println();
    }
}