package com.happy3w.toolkits.convert;

public class NumStrTci extends AbstractTci<Number, String> {
    public NumStrTci() {
        super(Number.class, String.class);
    }

    @Override
    public String toTarget(Number source) {
        String result = source.toString();
        int index = result.indexOf('.');
        if (index > 0 && isAllZereBehind(result, index)) {
            result = result.substring(0, index);
        }
        return result;
    }

    private boolean isAllZereBehind(String result, int index) {
        for (int end = result.length(), cur = index + 1;
            cur < end; cur++) {
            if (result.charAt(cur) != '0') {
                return false;
            }
        }
        return true;
    }
}
