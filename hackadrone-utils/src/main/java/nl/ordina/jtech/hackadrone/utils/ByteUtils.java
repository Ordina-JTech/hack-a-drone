package nl.ordina.jtech.hackadrone.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class representing byte utils.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class ByteUtils {

    /**
     * Generates a byte array from numbers.
     *
     * @param values the values containing the numbers
     * @return a byte array containing the numbers
     */
    public static byte[] asUnsigned(int... values) {
        byte[] bytes = new byte[values.length];

        for (int i = 0; i < values.length; i++) {
            int value = values[i];

            if (value > Byte.MAX_VALUE) {
                bytes[i] = (byte) value;
            } else {
                bytes[i] = (byte) (value & 0xff);
            }
        }

        return bytes;
    }

    /**
     * Loads message from a file.
     *
     * @param fileName the file name containing the message
     * @return a bytes array containing the message
     * @throws IOException if loading the message from the file failed
     */
    public static byte[] loadMessageFromFile(String fileName) throws IOException {
        InputStream inputStream = ByteUtils.class.getResourceAsStream("/" + fileName);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int read;
        byte[] data = new byte[4096];

        while ((read = inputStream.read(data, 0, data.length)) > 0) {
            byteArrayOutputStream.write(data, 0, read);
        }

        byteArrayOutputStream.flush();

        return byteArrayOutputStream.toByteArray();
    }

}
