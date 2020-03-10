package be.veterinarysolutions.vsol.tools;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Cal {


    public static long getId() {
        return new GregorianCalendar().getTimeInMillis();
    }
}
