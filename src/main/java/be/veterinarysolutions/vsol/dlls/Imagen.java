package be.veterinarysolutions.vsol.dlls;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class Imagen {
    private Logger logger = LogManager.getLogger();

    private CMOS_USB dll;
    private int deviceHandle = -1;
    private boolean suspended = false;

    public interface CMOS_USB extends Library {
        CMOS_USB INSTANCE = (CMOS_USB) Native.load("CMOS_USB", CMOS_USB.class);

        /*  1 */ int USB_OpenDevice(WinDef.USHORT ProductID);
        /*  2 */ int USB_OpenTargetDevice(WinDef.USHORT ProductID, WinDef.USHORT SensorNo);
        /*  3 */ int USB_OpenPipe(int DeviceHandle);
        /*  4 */ void USB_CloseDevice(int DeviceHandle);
        /*  5 */ long USB_SuspendDevice(int DeviceHandle, int SuspendDelay);
        /*  6 */ long USB_ResumeDevice(int DeviceHandle);
        /*  7 */ long HPK_GetXrayImage(int DeviceHandle, Pointer Buffer, Pointer BufferLength, /* Pointer */ UnitXrayImage pParam);
        /*  8 */ void HPK_GetXrayCorrectionImage();
        /*  9 */ void HPK_AbortBulkPipe();
        /* 10 */ void HPK_ForceTrigAndGetDummy();
        /* 11 */ void HPK_GetTrigPdData();
        /* 12 */ void HPK_StopTrigPdData();
        /* 13 */ void HPK_GetSensorInformation();
    }

    public static class UnitXrayImage extends Structure {
        public WinDef.UCHAR Mode = new WinDef.UCHAR(0x3);
        public UnitIntegrationParameter IntegParam = new UnitIntegrationParameter();

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("Mode", "IntegParam");
        }
    }

    public static class UnitIntegrationParameter extends Structure {
        public WinDef.USHORT Xray_Start_Treshold = new WinDef.USHORT(440);
        public WinDef.USHORT Integration_Stop_Treshold = new WinDef.USHORT(437);
        public double Integration_Time = 100.0;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("Xray_Start_Treshold", "Integration_Stop_Treshold", "Integration_Time");
        }
    }

    public Imagen() {
        dll = CMOS_USB.INSTANCE;
        init();
    }

    public void init() {
        deviceHandle = dll.USB_OpenDevice(new WinDef.USHORT(0x4400));
        logger.debug("Imagen Device Handle: " + deviceHandle);
        if (deviceHandle == -1) {
            logger.error("Couldn't open Imagen device.");
            return;
        }
        logger.info("Imagen device opened.");

        int pipe = dll.USB_OpenPipe(deviceHandle);
        if (pipe == -1) {
            logger.error("Couldn't open Imagen pipe.");
        }
    }

    Pointer buffer;
    Pointer bufferLength;

    public void xray() {
        try {
            System.out.println("a");
            UnitXrayImage pParam = new UnitXrayImage();
            System.out.println("b");

            buffer = new Memory(4460000);
            bufferLength = new Memory(Long.SIZE);

//            (new Thread(() -> {
                long result = dll.HPK_GetXrayImage(deviceHandle, buffer, bufferLength, pParam);
                if (result == 0) {
                    System.out.println(bufferLength.getLong(0));
                }

//            })).start();

        } catch (Exception e) {
            e.printStackTrace();
            close();
            System.exit(-1);
        }
    }

    public void suspend() {
        System.out.println( bufferLength.getLong(0) );

//        if (suspended) {
//            long result = dll.USB_ResumeDevice(deviceHandle);
//            if (result == 0) {
//                suspended = false;
//                logger.info("Imagen device resumed.");
//            } else {
//                logger.info("Imagen device failed to resume.");
//            }
//        } else {
//            long result = dll.USB_SuspendDevice(deviceHandle, 1000);
//            if (result == 0) {
//                suspended = true;
//                logger.info("Imagen device suspended.");
//            } else {
//                logger.info("Imagen device failed to suspend.");
//            }
//        }
    }

    public void close() {
        if (deviceHandle != -1) {
            dll.USB_CloseDevice(deviceHandle);
            logger.debug("Imagen device closed.");
        }
    }

    // GETTERS

    public boolean isOpen() {
        return deviceHandle != -1;
    }

    public boolean isSuspended() {
        return suspended;
    }
}
