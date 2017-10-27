/*
 * Copyright (C) 2017 Ordina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.ordina.jtech.hackadrone.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class representing byte utils.
 */
public final class ByteUtils {

    /**
     * Generates bytes from numbers.
     *
     * @param values the values containing the numbers
     * @return bytes containing the numbers
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
