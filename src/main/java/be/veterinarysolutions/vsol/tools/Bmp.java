package be.veterinarysolutions.vsol.tools;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Bmp {

    public static void createBmp(String filename, int width, int height, byte[] pixels) {
        File file = new File(filename);

//        System.out.println(pixels.length);

        System.out.println(1);
        short min = (short) 0;
        short max = (short) (256 * 256);

        short[] shorts = new short[pixels.length / 2];
        ByteBuffer.wrap(pixels).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);

        for (short s : shorts) {
            if (s < min) min = s;
            if (s > max) max = s;


        }


        byte[] reduced = new byte[pixels.length / 2];
        for (int i = 0; i < reduced.length; i++) {
            byte temp = pixels[i*2+1];
            reduced[i] = temp;
            if (temp < min) min = temp;
            if (temp > max) max = temp;
        }

        System.out.println(2);
        byte[] rgb = new byte[reduced.length * 3];
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = reduced[i / 3];
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

        System.out.println(3);

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

            System.out.println(4);

            out.write(rgb);

            System.out.println(5);

            out.close();

            FileOutputStream log = new FileOutputStream(filename + "_log.txt");
            log.write(pixels);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(4);
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
