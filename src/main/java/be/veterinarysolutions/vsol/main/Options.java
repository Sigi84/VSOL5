package be.veterinarysolutions.vsol.main;

import com.sun.jna.platform.win32.WinDef;

public class Options {
    public static String START_DIR = "C:/Sandbox/";
    public static final double ROTATION_STEP = 10.0;
    public static final double ZOOM_STEP = 0.1;

    // Imagen Settings
    public static long IMAGEN_PRODUCT_ID = 0x4400;
    public static long IMAGEN_MODE = 0x1;
    public static int IMAGEN_START_TRESHOLD = 440;
    public static int IMAGEN_STOP_TRESHOLD = 437;
    public static double IMAGEN_INTEGRATION_TIME = 100.0;

}
