package be.veterinarysolutions.vsol.main;

import com.sun.jna.platform.win32.WinDef;

public class Options {
    public static String START_DIR = "C:/Sandbox/";
    public static final double ROTATION_STEP_BIG = 90.0;
    public static final double ROTATION_STEP_SMALL = 15.0;
    public static final double ZOOM_STEP_BIG = 0.2;
    public static final double ZOOM_STEP_SMALL = 0.05;
    public static final double TRANSLATION_STEP = 10.0;

    public static boolean AUTO_DELETE_ON_RETAKE = true;

    // Imagen Settings
    public static boolean IMAGEN_MOCK_CAPTURE = true;
    public static long IMAGEN_PRODUCT_ID = 0x4400;
    public static long IMAGEN_MODE = 0x1;
    public static int IMAGEN_START_TRESHOLD = 440;
    public static int IMAGEN_STOP_TRESHOLD = 437;
    public static double IMAGEN_INTEGRATION_TIME = 100.0;

    // Quadrants Settings
    public static final double QUADRANTS_OUTER_SCALE = 0.75;
    public static final double QUADRANTS_INNER_SCALE = 1.00;
    public static final boolean DRAW_EMPTY_OUTER = false;

}
