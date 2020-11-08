package com.happy3w.toolkits.convert;

import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStrBiTci extends AbstractBiTci<Date, String> {
    @Getter
    @Setter
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public DateStrBiTci() {
        super(Date.class, String.class);
    }

    @Override
    public String toTarget(Date source) {
        return dateFormat.format(source);
    }

    @Override
    public Date toSource(String source) {
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse date from:" + source, e);
        }
    }
}
