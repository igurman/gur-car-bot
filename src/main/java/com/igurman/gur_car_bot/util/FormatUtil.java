package com.igurman.gur_car_bot.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@UtilityClass
public class FormatUtil {
    private static final DecimalFormatSymbols otherSymbols;
    @Getter
    private static final DecimalFormat decimalFormat;

    static {
        otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        decimalFormat = new DecimalFormat("##,###", otherSymbols);
    }

    public static String formatPrice(Integer value) {
        return formatPriceOrElse(value, "");
    }

    public static String formatPriceOrElse(Integer value, String def) {
        return (value != null)
                ? "$" + getDecimalFormat().format(value)
                : def;
    }

    public static String formatIntegerOrElse(Integer value, String def) {
        return (value != null)
                ? getDecimalFormat().format(value)
                : def;
    }

    public static String formatInteger(Integer value) {
        return formatIntegerOrElse(value, "");
    }

    public static String formatGrade(Integer i) {
        if (i == null) {
            return "";
        }
        return String.valueOf((float) i / 10);
    }
}
