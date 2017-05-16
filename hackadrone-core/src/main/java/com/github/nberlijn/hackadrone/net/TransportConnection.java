package com.github.nberlijn.hackadrone.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public final class TransportConnection implements Connection, Transport {

    private final String host;
    private final int port;
    private Socket socket;

    public TransportConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void connect() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(InetAddress.getByName(host), port);
        } else {
            throw new IOException("Connection failed!");
        }
    }

    @Override
    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        } else {
            throw new IOException("Disconnection failed!");
        }
    }

    @Override
    public void sendMessage(byte[] bytes, int responseSize) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.write(bytes);

        byte[] buffer = new byte[responseSize];

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        dataInputStream.readFully(buffer);
    }

}
