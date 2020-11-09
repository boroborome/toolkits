package com.happy3w.toolkits.convert;

import java.math.BigDecimal;

public class StrBigDecimalTci extends AbstractTci<String, BigDecimal> {
    public StrBigDecimalTci() {
        super(String.class, BigDecimal.class);
    }

    @Override
    public BigDecimal toTarget(String source) {
        return new BigDecimal(source);
    }
}
