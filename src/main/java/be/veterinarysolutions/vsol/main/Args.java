package be.veterinarysolutions.vsol.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Args {

    private static final Logger logger = LogManager.getLogger();

    public static Integer
            x = null,  y = null,
            width = 1200, height = 800;
    public static Boolean
            maximize = false;


    public static void load(String[] commandlineArgs) {
        for (String commandlineArg : commandlineArgs) {
            String[] args = commandlineArg.split(" ");
            for (String arg : args) {
                String[] multi = arg.split(" ");

                String[] subs = arg.split("=", -1);

                if (subs.length == 1) {
                    String sub = subs[0];
                    if (sub.equals("maximize")) {
                        maximize = true;
                    }
                } else if (subs.length == 2) {
                    String key = subs[0];
                    String value = subs[1];

                    if (key.equals("x")) {
                        x = getInt(value);
                    } else if (key.equals("y")) {
                        y = getInt(value);
                    } else if (key.equals("width")) {
                        width = getInt(value);
                    } else if (key.equals("height")) {
                        height = getInt(value);
                    }
                }
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
