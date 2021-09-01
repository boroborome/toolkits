package com.happy3w.toolkits.convert;

import com.happy3w.java.ext.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DateStrBiTci extends AbstractBiTci<Date, String> {

    private Map<String, ConfigItem> formatterMap = new HashMap<>();
    private List<ConfigItem> configItems = new ArrayList<>();
    private DateFormat defaultDateFormatter;

    public DateStrBiTci() {
        super(Date.class, String.class);
        defaultConfig("yyyy-MM-dd HH:mm:ss");
        appendConfig("yyyy-MM-dd");
        appendConfig("yyyy/MM/dd HH:mm:ss");
        appendConfig("yyyy/MM/dd");
    }

    public DateStrBiTci(String defaultFormat) {
        super(Date.class, String.class);
        defaultConfig(defaultFormat);
    }

    @Override
    public String toTarget(Date source) {
        return defaultDateFormatter.format(source);
    }

    @Override
    public Date toSource(String source) {
        synchronized (formatterMap) {
            ParsePosition position = new ParsePosition(0);
            int longestIndex = 0;
            Date longestValue = null;
            int targetIndex = source.length();

            for (ConfigItem item : configItems) {
                position.setErrorIndex(-1);
                position.setIndex(0);

                Date value = item.format.parse(source, position);
                if (position.getIndex() == targetIndex) {
                    return value;
                }
                if (position.getIndex() > longestIndex) {
                    longestIndex = position.getIndex();
                    longestValue = value;
                }
            }
            return longestValue;
        }
    }

    public DateStrBiTci appendConfig(String config) {
        if (!formatterMap.containsKey(config)) {
            SimpleDateFormat sdf = new SimpleDateFormat(config);
            ConfigItem configItem = new ConfigItem(config, sdf);
            formatterMap.put(config, configItem);
            configItems.add(configItem);
        }

        return this;
    }

    public DateStrBiTci removeConfig(String config) {
        ConfigItem item = formatterMap.get(config);
        if (item != null) {
            formatterMap.remove(config);
            configItems.remove(item);
            if (defaultDateFormatter == item.format) {
                if (formatterMap.isEmpty()) {
                    throw new UnsupportedOperationException("DateStrBiTci has at least one config.");
                }
                defaultDateFormatter = formatterMap.values().iterator().next().format;
            }
        }
        return this;
    }

    public DateStrBiTci defaultConfig(String config) {
        if (StringUtils.hasText(config)) {
            appendConfig(config);
            defaultDateFormatter = formatterMap.get(config).format;
        } else {
            throw new UnsupportedOperationException("defaultConfig of DateStrBiTci can not be empty.");
        }
        return this;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class ConfigItem {
        private String textFormat;
        private DateFormat format;
    }
}
