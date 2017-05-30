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

package nl.ordina.jtech.hackadrone.net;

import nl.ordina.jtech.hackadrone.models.Command;
import nl.ordina.jtech.hackadrone.utils.ByteUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Class representing the command connection for a drone.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class CommandConnection implements Execute {

    /**
     * The socket of the command connection.
     */
    private final DatagramSocket socket = new DatagramSocket();

    /**
     * The host of the command connection.
     */
    private final InetAddress host;

    /**
     * The port of the command connection.
     */
    private final int port;

    /**
     * A command connection constructor.
     *
     * @param host the host of the command connection
     * @param port the port of the command connection
     * @throws IOException if the connection failed
     */
    public CommandConnection(String host, int port) throws IOException {
        this.host = InetAddress.getByName(host);
        this.port = port;
    }

    /**
     * Sends a command.
     *
     * @param command the command to send
     * @throws IOException if sending the command failed
     */
    @Override
    public void sendCommand(Command command) throws IOException {
        byte[] data = asByteArray(command);
        DatagramPacket packet = new DatagramPacket(data, 0, data.length, host, port);
        socket.send(packet);
    }

    /**
     * Converts a command into bytes.
     *
     * @param command the command to convert into bytes
     * @return bytes containing the command
     */
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

    /**
     * Generates a digit representing the sum of the correct digits in a piece of transmitted digital data.
     *
     * @param bytes the bytes to check
     * @return the sum
     */
    private static byte checksum(byte[] bytes) {
        byte sum = 0;

        for (byte b : bytes) {
            sum ^= b;
        }

        return sum;
    }

}
