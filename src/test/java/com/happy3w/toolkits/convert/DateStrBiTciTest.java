package com.happy3w.toolkits.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

public class DateStrBiTciTest {

    @Test
    public void toSource() throws NoSuchAlgorithmException {
        DateStrBiTci tci = new DateStrBiTci("yyyy-MM-dd")
                .appendConfig("yyyy-MM-dd HH:mm:ss");
        Date date = tci.toSource("2020-11-17 14:26:41");
        Assertions.assertEquals(Timestamp.valueOf("2020-11-17 14:26:41").getTime(), date.getTime());
    }
}
