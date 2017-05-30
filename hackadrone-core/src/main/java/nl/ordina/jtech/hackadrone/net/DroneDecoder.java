package nl.ordina.jtech.hackadrone.net;

import nl.ordina.jtech.hackadrone.utils.ByteUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Class representing the drone decoder for a drone.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class DroneDecoder implements Decoder {

    /**
     * The ph containing unsigned bytes.
     */
    private static byte[] PH = ByteUtils.asUnsigned(
            0x00, 0x00, 0x00, 0x19, 0xD0,
            0x02, 0x40, 0x02, 0x00, 0xBF,
            0x8A, 0x00, 0x01, 0x5D, 0x03,
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00);

    /**
     * The host of the drone decoder.
     */
    private final String host;

    /**
     * The port of the drone decoder.
     */
    private final int port;

    /**
     * The input stream of the drone decoder.
     */
    private InputStream inputStream;

    /**
     * The output stream of the drone decoder.
     */
    private OutputStream outputStream;

    /**
     * The socket of the drone decoder.
     */
    private Socket socket;

    /**
     * If the data is saved.
     */
    private boolean savedData = false;

    /**
     * If initialized.
     */
    private boolean initialized = false;

    /**
     * A drone decoder constructor,.
     *
     * @param host the host of the drone decoder
     * @param port the port of the drone decoder
     */
    public DroneDecoder(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Connects the drone decoder.
     *
     * @throws IOException if the connection failed
     */
    @Override
    public void connect() throws IOException {
        InetAddress address = InetAddress.getByName(host);
        socket = new Socket(address, port);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
    }

    /**
     * Disconnects the drone decoder.
     *
     * @throws IOException if the disconnection failed
     */
    @Override
    public void disconnect() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }

    /**
     * Reads bytes.
     *
     * @return the read bytes
     * @throws IOException if reading the bytes failed
     */
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

    /**
     * Replaces bytes to the correct bytes.
     *
     * @param nalA0 the bytes to replace
     * @return replaced bytes
     */
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

    /**
     * Reads data from the input stream.
     *
     * @param length the length to read
     * @return buffered bytes
     * @throws IOException if reading the data failed
     */
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
