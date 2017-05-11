package com.github.nberlijn.hackadrone.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ByteUtils {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        StringBuilder output = new StringBuilder();

        for (byte aByte : bytes) {
            int value = aByte & 0xFF;
            output.append(hexArray[value >>> 4]);
            output.append(hexArray[value & 0x0F]);
            output.append(" ");
        }

        return output.toString();
    }

    public static byte[] loadMessageFromFile(String fileName) throws IOException {
        InputStream resourceAsStream = ByteUtils.class.getResourceAsStream("/" + fileName);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[4096];

        while ((nRead = resourceAsStream.read(data, 0, data.length)) > 0) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

}
