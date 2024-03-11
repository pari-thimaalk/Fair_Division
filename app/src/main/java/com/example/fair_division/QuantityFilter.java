package com.example.fair_division;

import android.text.InputFilter;
import android.text.Spanned;

public class QuantityFilter implements InputFilter {
    private int minimumValue;
    private int maximumValue;
    public QuantityFilter(int minimumValue, int maximumValue) {
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.subSequence(0, dstart).toString() + charSequence + dest.subSequence(dend, dest.length()));
            if (isInRange(minimumValue, maximumValue, input))
                return null;
        }
        catch (NumberFormatException nfe) {
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
