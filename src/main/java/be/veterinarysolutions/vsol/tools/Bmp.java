package be.veterinarysolutions.vsol.tools;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Bmp {

    public static void createBmp(String filename, int width, int height, byte[] pixels) {
        File file = new File(filename);

        double min = Bytes.MAX_16;
        double max = 0.0;

//        short[] shorts = new short[pixels.length / 2];
//        ByteBuffer.wrap(pixels).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);

        int[] values = Bytes.get16BitBuffer(pixels, Bytes.LITTLE_ENDIAN);
        for (int value : values) {
            if (value < min) min = value;
            if (value > max) max = value;
        }

        System.out.println("MIN = " + min);
        System.out.println("MAX = " + max);

        byte[] rgb = new byte[values.length * 3];
        for (int i = 0; i < values.length; i++) {
            double fraction = (values[i] - min) / (max - min);
            byte current = (byte) ( fraction * 255 );

            rgb[(i * 3) + 0] = current;
            rgb[(i * 3) + 1] = current;
            rgb[(i * 3) + 2] = current;
        }

        // BITMAPFILEHEADER
        byte[] FileType = { 0x42, 0x4D }; // BM
        byte[] FileSize = getByteArray(pixels.length + 54);
        byte[] Reserved = { 0x00, 0x00, 0x00, 0x00 };
        byte[] PixelDataOffset = getByteArray(54);

        // BITMAPINFOHEADER
        byte[] HeaderSize = getByteArray(40);
        byte[] ImageWidth = getByteArray(width);
        byte[] ImageHeight = getByteArray(height);
        byte[] Planes = getByteArray((short) 1);
        byte[] BitsPerPixel = getByteArray((short) 24);
        byte[] Compression = getByteArray(0);
        byte[] ImageSize = getByteArray(0);
        byte[] XpixelsPerMeter = getByteArray(0);
        byte[] YpixelsPerMeter = getByteArray(0);
        byte[] TotalColors = getByteArray(0);
        byte[] ImportantColors = getByteArray(0);

        try {
            FileOutputStream out = new FileOutputStream(file);

            out.write(FileType);
            out.write(FileSize);
            out.write(Reserved);
            out.write(PixelDataOffset);

            out.write(HeaderSize);
            out.write(ImageWidth);
            out.write(ImageHeight);
            out.write(Planes);
            out.write(BitsPerPixel);
            out.write(Compression);
            out.write(ImageSize);
            out.write(XpixelsPerMeter);
            out.write(YpixelsPerMeter);
            out.write(TotalColors);
            out.write(ImportantColors);

            out.write(rgb);

            out.close();

//            FileOutputStream log = new FileOutputStream(filename + "_log.txt");
//            log.write(pixels);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] read(String filename) {

        File file = new File(filename);
        long length = file.length();

        try {
            FileInputStream in = new FileInputStream(filename);

            byte[] result = new byte[(int) length];

            in.read(result);

            in.close();

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static int getInt(byte[] byteArray) {
        final ByteBuffer bb = ByteBuffer.wrap(byteArray);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }

    private static byte[] getByteArray(int number) {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(number);
        return bb.array();
    }

    private static byte[] getByteArray(short number) {
        final ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(number);
        return bb.array();
    }



}
