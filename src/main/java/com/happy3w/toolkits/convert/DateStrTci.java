package com.happy3w.toolkits.convert;

import java.util.Date;

public class DateStrTci extends AbstractTci<Date, String> {
    public DateStrTci() {
        super(Date.class, String.class);
    }

    @Override
    public String convert(Date source) {
        return String.valueOf(source.getTime());
    }
}
