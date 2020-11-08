package com.happy3w.toolkits.convert;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class TypeConverterTest {

    @Test
    public void should_convert_date_to_long_success() {
        long timestamp = 1604850105517l;
        Date date = new Date(timestamp);
        Assert.assertEquals(timestamp, TypeConverter.INSTANCE.convert(date, Long.class).longValue());
    }

    @Test
    public void should_convert_long_to_date_success() {
        long timestamp = 1604850105517l;
        Date date = new Date(timestamp);
        Assert.assertEquals(date, TypeConverter.INSTANCE.convert(timestamp, Date.class));
    }

    @Test
    public void should_convert_long_to_str_success() {
        long value = 1604850105517l;
        String str = String.valueOf(value);
        Assert.assertEquals(str, TypeConverter.INSTANCE.convert(value, String.class));
    }

    @Test
    public void should_convert_str_to_long_success() {
        long value = 1604850105517l;
        String str = String.valueOf(value);
        Assert.assertEquals(value, TypeConverter.INSTANCE.convert(str, Long.class).longValue());
    }
}
