package io.github.xuqplus.vxp_test01;

import org.junit.Assert;
import org.junit.Test;

public class Demo02Test {

    @Test
    public void a() {
        Assert.assertNotEquals("com\\.eg\\.android\\.AlipayGphone", "com.eg.android.AlipayGphone");
    }
}