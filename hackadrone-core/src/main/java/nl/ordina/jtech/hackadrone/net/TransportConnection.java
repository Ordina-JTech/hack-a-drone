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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class representing the transport connection for a drone.
 */
public final class TransportConnection implements Connection, Transport {

    /**
     * The host of the transport connection.
     */
    private final String host;

    /**
     * The port of the transport connection.
     */
    private final int port;

    /**
     * The socket of the transport connection.
     */
    private Socket socket;

    /**
     * A transport connection constructor.
     *
     * @param host the host of the transport connection
     * @param port the port of the transport connection
     */
    public TransportConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Connects the transport connection.
     *
     * @throws IOException if the connection failed
     */
    @Override
    public void connect() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(InetAddress.getByName(host), port);
        } else {
            throw new IOException("Connection failed!");
        }
    }

    /**
     * Disconnects the transport connection.
     *
     * @throws IOException if the disconnection failed
     */
    @Override
    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        } else {
            throw new IOException("Disconnection failed!");
        }
    }

    /**
     * Sends a message.
     *
     * @param bytes the bytes to send
     * @param responseSize the size of the response bytes
     * @throws IOException if sending the message failed
     */
    @Override
    public void sendMessage(byte[] bytes, int responseSize) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.write(bytes);

        byte[] buffer = new byte[responseSize];

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        dataInputStream.readFully(buffer);
    }

}
