package com.happy3w.toolkits.convert;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

public class SimpleConverterTest {

    @Test
    public void should_convert_string_and_number_to_date_success() {
        Date d1 = SimpleConverter.getInstance().convert("2019-09-01 12:00:00", Date.class);
        Date d2 = SimpleConverter.getInstance().convert(1567310400000l, Date.class);

        Assert.assertEquals(d1, d2);

        Date d3 = SimpleConverter.getInstance().convert("2019-09-01", Date.class);
        Date d4 = SimpleConverter.getInstance().convert("2019/09/01", Date.class);
        Assert.assertEquals(d3, d4);
    }

    @Test
    public void should_convert_string_with_custom_date_format() {
        SimpleConverter.getInstance()
                .findConverter(Date.class)
                .appendConfig("MM/dd/yyyy HH:mm:ss")
                .defaultConfig("yyyy-MM-dd HH:mm:ss");
        Date d1 = SimpleConverter.getInstance().convert("2019-09-01 12:00:00", Date.class);
        Date d2 = SimpleConverter.getInstance().convert("09/01/2019 12:00:00", Date.class);
        Assert.assertEquals(d1, d2);

        Assert.assertEquals("2019-09-01 12:00:00", SimpleConverter.getInstance()
                .convert(d2, String.class));
    }

    @Test
    public void should_convert_string_with_custom_date_format_and_timestamp() {
        SimpleConverter.getInstance()
                .findConverter(Date.class)
                .appendConfig("yyyy-MM-dd")
                .appendConfig("yyyy年MM月dd日 HH:mm:ss");

        Assert.assertEquals(
                Timestamp.valueOf("2019-06-10 00:00:00").getTime(),
                SimpleConverter.getInstance().convert("2019-06-10", Date.class).getTime()
        );

        Assert.assertEquals(
                Timestamp.valueOf("2019-06-10 12:11:14").getTime(),
                SimpleConverter.getInstance().convert("2019-06-10 12:11:14", Date.class).getTime()
        );

        Assert.assertEquals(
                Timestamp.valueOf("2019-06-10 12:11:14").getTime(),
                SimpleConverter.getInstance().convert("2019年06月10日 12:11:14", Date.class).getTime()
        );

        Assert.assertEquals(
                Timestamp.valueOf("2019-06-10 12:11:14").getTime(),
                SimpleConverter.getInstance().convert("1560139874000", Timestamp.class).getTime()
        );
    }
}
