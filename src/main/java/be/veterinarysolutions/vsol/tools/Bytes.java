package be.veterinarysolutions.vsol.tools;

public class Bytes {

    public static byte BIG_ENDIAN = 0x00, LITTLE_ENDIAN = 0x01;
    public static int MAX_16 = 256 * 256;

    public static int[] get16BitBuffer(byte[] bytes, byte endian) {
        int[] result = new int[bytes.length / 2];

        for (int i = 0; i < result.length; i++) {
            short  first = (short) (bytes[(i * 2) + 0] & 0xFF);
            short second = (short) (bytes[(i * 2) + 1] & 0xFF);

            if (endian == BIG_ENDIAN) {
                result[i] = (first * 256) + second;
            } else if (endian == LITTLE_ENDIAN) {
                result[i] = (second * 256) + first;
            }
        }

        return result;
    }

}
