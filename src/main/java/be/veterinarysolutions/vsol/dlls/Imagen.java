package be.veterinarysolutions.vsol.dlls;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Imagen {
    private Logger logger = LogManager.getLogger();

    private CMOS_USB dll;
    private int deviceHandle = -1;

    public interface CMOS_USB extends Library {
        CMOS_USB INSTANCE = (CMOS_USB) Native.load("CMOS_USB", CMOS_USB.class);

        /*  1 */ int USB_OpenDevice(short ProductID);
        /*  2 */ int USB_OpenTargetDevice(short ProductID, short SensorNo);
        /*  3 */ int USB_OpenPipe(int DeviceHandle);
        /*  4 */ void USB_CloseDevice(int DeviceHandle);
        /*  5 */ long USB_SuspendDevice(int DeviceHandle, int SuspendDelay);
        /*  6 */ long USB_ResumeDevice(int DeviceHandle);
        /*  7 */ long HPK_GetXrayImage(int DeviceHandle, String Buffer, long BufferLength, String pParam);
        /*  8 */ void HPK_GetXrayCorrectionImage();
        /*  9 */ void HPK_AbortBulkPipe();
        /* 10 */ void HPK_ForceTrigAndGetDummy();
        /* 11 */ void HPK_GetTrigPdData();
        /* 12 */ void HPK_StopTrigPdData();
        /* 13 */ void HPK_GetSensorInformation();
    }

    public Imagen() {
        dll = CMOS_USB.INSTANCE;
        init();
    }

    public void init() {
        deviceHandle = dll.USB_OpenDevice((short) 0x4400);
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



}
