package com.happy3w.toolkits.convert;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

public class DateStrBiTciTest {

    @Test
    public void toSource() throws NoSuchAlgorithmException {
MessageDigest md = MessageDigest.getInstance("MD5");
md.update("Hello".getBytes());
System.out.println(new BigInteger(1, md.digest()).toString(16));

        DateStrBiTci tci = new DateStrBiTci("yyyy-MM-dd")
                .appendConfig("yyyy-MM-dd HH:mm:ss");
        Date date = tci.toSource("2020-11-17 14:26:41");
        Assert.assertEquals(Timestamp.valueOf("2020-11-17 14:26:41").getTime(), date.getTime());
    }
}
