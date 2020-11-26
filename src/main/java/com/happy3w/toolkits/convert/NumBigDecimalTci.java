package com.happy3w.toolkits.convert;

import java.math.BigDecimal;

public class NumBigDecimalTci extends AbstractTci<Number, BigDecimal> {
    public NumBigDecimalTci() {
        super(Number.class, BigDecimal.class);
    }

    @Override
    public BigDecimal toTarget(Number source) {
        return BigDecimal.valueOf(source.doubleValue());
    }
}
