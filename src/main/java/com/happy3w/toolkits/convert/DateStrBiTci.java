package com.happy3w.toolkits.convert;

import com.happy3w.toolkits.utils.StringUtils;
import lombok.Getter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DateStrBiTci extends AbstractBiTci<Date, String> {

    private Map<String, DateFormat> formaterMap = new HashMap<>();
    private DateFormat defaultDateFormater;

    public DateStrBiTci() {
        super(Date.class, String.class);
        defaultConfig("yyyy-MM-dd HH:mm:ss");
        appendConfig("yyyy-MM-dd");
        appendConfig("yyyy/MM/dd HH:mm:ss");
        appendConfig("yyyy/MM/dd");
    }

    @Override
    public String toTarget(Date source) {
        return defaultDateFormater.format(source);
    }

    @Override
    public Date toSource(String source) {
        synchronized (formaterMap) {
            Date value = tryParse(source, defaultDateFormater);
            if (value != null) {
                return value;
            }
            for (DateFormat dateFormat : formaterMap.values()) {
                value = tryParse(source, dateFormat);
                if (value != null) {
                    return value;
                }
            }
        }

        throw new IllegalArgumentException("Failed to parse date from:" + source);
    }

    private Date tryParse(String value, DateFormat dateFormat) {
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    public DateStrBiTci appendConfig(String config) {
        SimpleDateFormat sdf = new SimpleDateFormat(config);
        formaterMap.put(config, sdf);
        return this;
    }

    public DateStrBiTci defaultConfig(String config) {
        if (StringUtils.hasText(config)) {
            SimpleDateFormat sdf = new SimpleDateFormat(config);
            defaultDateFormater = sdf;
        } else {
            defaultDateFormater = null;
        }
        return this;
    }
}
