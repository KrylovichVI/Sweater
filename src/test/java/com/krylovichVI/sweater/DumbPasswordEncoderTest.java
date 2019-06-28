package com.krylovichVI.sweater;


import org.junit.Assert;
import org.junit.Test;


public class DumbPasswordEncoderTest {
    @Test
    public void encode() throws Exception {
        DumbPasswordEncoder encoder = new DumbPasswordEncoder();

        Assert.assertEquals("secret: 'mypwd'", encoder.encode("mypwd"));
    }

}