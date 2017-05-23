package com.github.ordina.jtech.hackadrone.net;

import com.github.ordina.jtech.hackadrone.utils.ByteUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public final class DroneDecoder implements Decoder {

    private static byte[] PH = ByteUtils.asUnsigned(
            0x00, 0x00, 0x00, 0x19, 0xD0,
            0x02, 0x40, 0x02, 0x00, 0xBF,
            0x8A, 0x00, 0x01, 0x5D, 0x03,
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00);

    private final String host;
    private final int port;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private boolean savedData = false;
    private boolean initialized = false;

    public DroneDecoder(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }

    @Override
    public void connect() throws IOException {
        InetAddress address = InetAddress.getByName(host);
        socket = new Socket(address, port);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void disconnect() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }

    @Override
    public byte[] read() throws IOException {
        if (!initialized) {
            byte[] bytes = ByteUtils.loadMessageFromFile("bin/video.bin");
            outputStream.write(bytes);

            byte[] response = new byte[106];
            inputStream.read(response);

            initialized = true;
        }

        byte[] nalHeader = readData(10);
        int nalType = nalHeader[3] & 0xFF;

        int headerSize;
        int headerType = nalHeader[7] & 0XFF;

        switch (headerType) {
            case 0x02:
            case 0x03:
                headerSize = 30;
                break;
            case 0x01:
                headerSize = 2;
                break;
            default:
                System.err.println("Unknown header type " + headerType);
                return null;
        }

        int dataLength = ((nalHeader[9] & 0xff) << 8) | (nalHeader[8] & 0xff);
        byte[] fullNalHeader = new byte[headerSize + nalHeader.length];
        System.arraycopy(nalHeader, 0, fullNalHeader, 0, nalHeader.length);

        inputStream.read(fullNalHeader, 10, headerSize);

        if (nalType == 0xA0 && headerType == 0x03) {
            byte[] newHeader = replace(fullNalHeader);
            byte[] data = readData(dataLength);

            return ByteBuffer.allocate(newHeader.length + data.length).put(newHeader).put(data).array();
        } else if (nalType == 0xA1 && headerType == 0x02) {
            PH[8] = fullNalHeader[8];
            PH[9] = fullNalHeader[9];
            PH[10] = fullNalHeader[10];
            PH[11] = fullNalHeader[11];

            PH[16] = fullNalHeader[5];
            PH[17] = fullNalHeader[4];
            PH[18] = fullNalHeader[33];
            PH[19] = fullNalHeader[32];

            savedData = true;

            return readData(dataLength);
        } else if (nalType == 0xA1 && headerType == 0x01) {
            byte[] ret = readData(dataLength);
            byte[] tmp;

            if (savedData) {
                tmp = ByteBuffer.allocate(PH.length + ret.length).put(PH).put(ret).array();
                savedData = false;
            } else {
                tmp = ByteBuffer.allocate(ret.length).put(ret).array();
            }

            return tmp;
        } else {
            return readData(dataLength);
        }
    }

    private byte[] replace(byte[] nalA0) {
        byte[] out = new byte[32];
        byte[] params = ByteUtils.asUnsigned(0x01, 0x00, 0x00, 0x19, 0xD0, 0x02, 0x40, 0x02);

        System.arraycopy(params, 0, out, 0, params.length);
        System.arraycopy(nalA0, 12, out, 8, 8);

        out[16] = nalA0[5];
        out[18] = nalA0[9];
        out[19] = nalA0[8];

        return out;
    }

    private byte[] readData(int length) throws IOException {
        int read = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);

        while (read < length) {
            byte[] buffer = new byte[length - read];
            int lastRead = inputStream.read(buffer);

            byteBuffer.put(buffer, 0, lastRead);
            read += lastRead;
        }

        return byteBuffer.array();
    }

}
