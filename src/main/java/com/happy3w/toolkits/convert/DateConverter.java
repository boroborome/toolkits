package com.happy3w.toolkits.convert;

import com.happy3w.toolkits.utils.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DateConverter implements ISimpleConverter<Date, DateConverter> {
    private static final Pattern LONG_NUMBER_PATTERN = Pattern.compile("^\\d+$");

    private Map<String, DateFormat> formaterMap = new HashMap<>();
    private DateFormat defaultDateFormater;

    public DateConverter() {
        defaultConfig("yyyy-MM-dd HH:mm:ss");
        appendConfig("yyyy-MM-dd");
        appendConfig("yyyy/MM/dd HH:mm:ss");
        appendConfig("yyyy/MM/dd");
    }

    @Override
    public Class[] dataTypes() {
        return new Class[] {Timestamp.class, Date.class};
    }

    @Override
    public String convertToString(Date value) {
        if (value == null) {
            return null;
        }

        return defaultDateFormater == null
                ? String.valueOf(value.getTime())
                : defaultDateFormater.format(value);
    }

    @Override
    public DateConverter appendConfig(String config) {
        SimpleDateFormat sdf = new SimpleDateFormat(config);
        formaterMap.put(config, sdf);
        return this;
    }

    @Override
    public DateConverter defaultConfig(String config) {
        if (StringUtils.hasText(config)) {
            SimpleDateFormat sdf = new SimpleDateFormat(config);
            formaterMap.put(config, sdf);
            defaultDateFormater = sdf;
        } else {
            defaultDateFormater = null;
        }
        return this;
    }

    @Override
    public Date convertFromString(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }

        Date date = tryNumberParseDate(str);
        if (date != null) {
            return date;
        }

        date = parseDate(str);
        if (date == null) {
            throw new IllegalArgumentException("Can't parse date value from text:" + str);
        }
        return date;
    }

    private Date parseDate(String strDate) {
        Date date = null;
        synchronized (formaterMap) {
            for (DateFormat dateFormat : formaterMap.values()) {
                try {
                    date = dateFormat.parse(strDate);
                    date = new Timestamp(date.getTime());
                    break;
                } catch (ParseException e) {
                    continue;
                }
            }
        }

        return date;
    }

    private static Date tryNumberParseDate(String strDate) {
        if (LONG_NUMBER_PATTERN.matcher(strDate).matches()) {
            return new Timestamp(Long.parseLong(strDate));
        }
        return null;
    }
}
