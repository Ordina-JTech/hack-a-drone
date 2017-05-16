package com.github.nberlijn.hackadrone.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ByteUtils {

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
