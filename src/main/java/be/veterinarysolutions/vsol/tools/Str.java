package be.veterinarysolutions.vsol.tools;

import java.util.Collection;
import java.util.TreeSet;

public class Str {

    public static String getList(TreeSet<? extends Object> objects) {
        String result = "";
        for (Object object : objects) {
            result += object.toString() + ", ";
        }
        return Str.cutoff(result, ", ");
    }

    public static String cutoff(String string, String suffix) {
        if (string.endsWith(suffix)) {
            string = string.substring(0, string.length() - suffix.length());
        }
        return string;
    }

}
