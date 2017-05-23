package com.github.ordina.jtech.hackadrone.net;

import com.github.ordina.jtech.hackadrone.models.Command;
import com.github.ordina.jtech.hackadrone.utils.ByteUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public final class CommandConnection implements Execute {

    private final DatagramSocket socket = new DatagramSocket();
    private final InetAddress host;
    private final int port;

    public CommandConnection(String host, int port) throws IOException {
        this.host = InetAddress.getByName(host);
        this.port = port;
    }

    @Override
    public void sendCommand(Command command) throws IOException {
        byte[] data = asByteArray(command);
        DatagramPacket packet = new DatagramPacket(data, 0, data.length, host, port);
        socket.send(packet);
    }

    private byte[] asByteArray(Command command) {
        int pitch = command.getPitch() + 128;
        int yaw = command.getYaw() + 128;
        int roll = command.getRoll() + 128;
        int throttle = command.getThrottle() + 128;
        boolean takeOff = command.isTakeOff();
        boolean land = command.isLand();

        byte[] data = new byte[8];
        data[0] = (byte) 0xCC;
        data[1] = (byte) roll;
        data[2] = (byte) pitch;
        data[3] = (byte) throttle;
        data[4] = (byte) yaw;

        if (takeOff) {
            data[5] = (byte) 0x01;
        } else if (land) {
            data[5] = (byte) 0x02;
        } else {
            data[5] = (byte) 0x00;
        }

        data[6] = checksum(ByteUtils.asUnsigned(data[1], data[2], data[3], data[4], data[5]));
        data[7] = (byte) 0x33;

        return data;
    }

    private static byte checksum(byte[] bytes) {
        byte sum = 0;

        for (byte b : bytes) {
            sum ^= b;
        }

        return sum;
    }

}
