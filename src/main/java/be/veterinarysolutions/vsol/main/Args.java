package be.veterinarysolutions.vsol.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Args {

    private static final Logger logger = LogManager.getLogger();

    public static Integer
            x = null,
            y = null;

    public static void load(String[] args) {
        for (String arg : args) {
            String[] subs = arg.split("=", -1);
            if (subs.length != 2) break;
            String key = subs[0];
            String value = subs[1];

            if (key.equals("x")) {
                x = getInt(value);
            } else if (key.equals("y")) {
                y = getInt(value);
            }
        }
    }

    public static Integer getInt(String string) {
        Integer result = null;
        try {
            result = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            logger.error(e);
        }
        return result;
    }

}
