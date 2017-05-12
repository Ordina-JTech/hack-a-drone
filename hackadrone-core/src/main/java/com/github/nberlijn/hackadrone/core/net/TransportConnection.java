package com.github.nberlijn.hackadrone.core.net;

import com.github.nberlijn.hackadrone.api.Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public final class TransportConnection implements Connection {

    private final String host;
    private final int port;
    private Socket socket;

    public TransportConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(InetAddress.getByName(host), port);
        } else {
            throw new IOException("Connection failed!");
        }
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("hoi");
        } else {
            throw new IOException("Disconnection failed!");
        }
    }

    public void sendMessage(byte[] bytes, int responseSize) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.write(bytes);

        byte[] buffer = new byte[responseSize];

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        dataInputStream.readFully(buffer);
    }

}
