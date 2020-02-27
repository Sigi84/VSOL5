package be.veterinarysolutions.vsol.main;

import com.sun.jna.platform.win32.WinDef;

public class Options {
    public static String START_DIR = "C:/Sandbox/";
    public static final double ROTATION_STEP_BIG = 90.0;
    public static final double ROTATION_STEP_SMALL = 15.0;
    public static final double ZOOM_STEP_BIG = 0.2;
    public static final double ZOOM_STEP_SMALL = 0.05;
    public static final double TRANSLATION_STEP = 10.0;

    // Imagen Settings
    public static long IMAGEN_PRODUCT_ID = 0x4400;
    public static long IMAGEN_MODE = 0x3;
    public static int IMAGEN_START_TRESHOLD = 440;
    public static int IMAGEN_STOP_TRESHOLD = 437;
    public static double IMAGEN_INTEGRATION_TIME = 100.0;

}
