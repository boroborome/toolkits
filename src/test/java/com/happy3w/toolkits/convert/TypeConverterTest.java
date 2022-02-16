package com.happy3w.toolkits.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

public class TypeConverterTest {
    @Test
    public void should_convert_str_to_date_with_yyyyMMdd_success() {
        TypeConverter converter = TypeConverter.INSTANCE.newCopy()
                .regist(new DateStrBiTci()
                        .defaultConfig("yyyyMMdd"));

        Date date = converter.convert("20200705", Date.class);
        Assertions.assertEquals(Timestamp.valueOf("2020-07-05 00:00:00").getTime(), date.getTime());
    }

    @Test
    public void should_convert_str_to_date_with_default_success() throws ParseException {
        TypeConverter converter = TypeConverter.INSTANCE;
        converter.findTci(DateStrBiTci.class)
                .appendConfig("yyyyMMdd");

        Date date = converter.convert("2020-11-17 14:26:41", Date.class);
        Assertions.assertEquals(Timestamp.valueOf("2020-11-17 14:26:41").getTime(), date.getTime());
    }

    @Test
    public void should_convert_date_to_long_success() {
        long timestamp = 1604850105517l;
        Date date = new Date(timestamp);
        Assertions.assertEquals(timestamp, TypeConverter.INSTANCE.convert(date, Long.class).longValue());
    }

    @Test
    public void should_convert_long_to_date_success() {
        long timestamp = 1604850105517l;
        Date date = new Date(timestamp);
        Assertions.assertEquals(date, TypeConverter.INSTANCE.convert(timestamp, Date.class));
    }

    @Test
    public void should_convert_long_to_str_success() {
        long value = 1604850105517l;
        String str = String.valueOf(value);
        Assertions.assertEquals(str, TypeConverter.INSTANCE.convert(value, String.class));
    }

    @Test
    public void should_convert_str_to_long_success() {
        long value = 1604850105517l;
        String str = String.valueOf(value);
        Assertions.assertEquals(value, TypeConverter.INSTANCE.convert(str, Long.class).longValue());
    }

    @Test
    public void should_convert_double_to_decimal_success() {
        ITypeConvertItem<Double, BigDecimal> tci = TypeConverter.INSTANCE.findTci(double.class, BigDecimal.class);
        Assertions.assertEquals("java.lang.Number -> java.math.BigDecimal", tci.toString());
    }

    @Test
    public void should_convert_str_to_double_success() {
        String str = "2.3450";
        Assertions.assertEquals(2.345d, TypeConverter.INSTANCE.convert(str, double.class).doubleValue(), 0.01);
    }

    @Test
    public void should_convert_string_and_number_to_date_success() {
        Date d1 = TypeConverter.INSTANCE.convert("2019-09-01 12:00:00", Date.class);
        Date d2 = TypeConverter.INSTANCE.convert(1567310400000l, Date.class);

        Assertions.assertEquals(d1, d2);

        Date d3 = TypeConverter.INSTANCE.convert("2019-09-01", Date.class);
        Date d4 = TypeConverter.INSTANCE.convert("2019/09/01", Date.class);
        Assertions.assertEquals(d3, d4);
    }

    @Test
    public void should_convert_string_with_custom_date_format_and_timestamp() {
        TypeConverter.INSTANCE
                .findTci(DateStrBiTci.class)
                .appendConfig("yyyy-MM-dd")
                .appendConfig("yyyy年MM月dd日 HH:mm:ss");

        Assertions.assertEquals(
                Timestamp.valueOf("2019-06-10 00:00:00").getTime(),
                TypeConverter.INSTANCE.convert("2019-06-10", Date.class).getTime()
        );

        Assertions.assertEquals(
                Timestamp.valueOf("2019-06-10 12:11:14").getTime(),
                TypeConverter.INSTANCE.convert("2019-06-10 12:11:14", Date.class).getTime()
        );

        Assertions.assertEquals(
                Timestamp.valueOf("2019-06-10 12:11:14").getTime(),
                TypeConverter.INSTANCE.convert("2019年06月10日 12:11:14", Date.class).getTime()
        );

        Assertions.assertEquals(
                1560139874000l,
                TypeConverter.INSTANCE.convert(1560139874000l, Date.class).getTime()
        );
    }
}
