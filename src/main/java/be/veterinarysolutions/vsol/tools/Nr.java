package be.veterinarysolutions.vsol.tools;

import java.text.DecimalFormat;

public class Nr {

    public static String perc(double value) { return perc(value, false); }

    public static String perc(double value, boolean showPlus) {
        return (showPlus && value > 0.0 ? "+" : "") + format(value,"0.00") + " %";
    }

    public static String euro(double value) { return "€ " + format(value, "###.00"); }

    public static String fullPrecision(double value) {
        return format(value, "0.0000");
    }

    // PRIVATE

    private static String format(double value, String pattern) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(value);
    }
}
