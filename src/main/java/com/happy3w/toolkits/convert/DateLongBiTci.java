package com.happy3w.toolkits.convert;

import java.util.Date;

public class DateLongBiTci extends AbstractBiTci<Date, Long> {
    public DateLongBiTci() {
        super(Date.class, Long.class);
    }

    @Override
    public Long toTarget(Date source) {
        return source.getTime();
    }

    @Override
    public Date toSource(Long source) {
        return new Date(source);
    }
}
