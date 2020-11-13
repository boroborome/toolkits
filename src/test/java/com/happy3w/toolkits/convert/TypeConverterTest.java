package com.happy3w.toolkits.convert;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
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

    @Test
    public void should_convert_string_and_number_to_date_success() {
        Date d1 = TypeConverter.INSTANCE.convert("2019-09-01 12:00:00", Date.class);
        Date d2 = TypeConverter.INSTANCE.convert(1567310400000l, Date.class);

        Assert.assertEquals(d1, d2);

        Date d3 = TypeConverter.INSTANCE.convert("2019-09-01", Date.class);
        Date d4 = TypeConverter.INSTANCE.convert("2019/09/01", Date.class);
        Assert.assertEquals(d3, d4);
    }

    @Test
    public void should_convert_string_with_custom_date_format_and_timestamp() {
        TypeConverter.INSTANCE
                .findTci(DateStrBiTci.class)
                .appendConfig("yyyy-MM-dd")
                .appendConfig("yyyy年MM月dd日 HH:mm:ss");

        Assert.assertEquals(
                Timestamp.valueOf("2019-06-10 00:00:00").getTime(),
                TypeConverter.INSTANCE.convert("2019-06-10", Date.class).getTime()
        );

        Assert.assertEquals(
                Timestamp.valueOf("2019-06-10 12:11:14").getTime(),
                TypeConverter.INSTANCE.convert("2019-06-10 12:11:14", Date.class).getTime()
        );

        Assert.assertEquals(
                Timestamp.valueOf("2019-06-10 12:11:14").getTime(),
                TypeConverter.INSTANCE.convert("2019年06月10日 12:11:14", Date.class).getTime()
        );

        Assert.assertEquals(
                1560139874000l,
                TypeConverter.INSTANCE.convert(1560139874000l, Date.class).getTime()
        );
    }
}