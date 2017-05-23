package com.github.ordina.jtech.hackadrone.io;

import com.github.ordina.jtech.hackadrone.utils.ByteUtils;

import java.io.*;
import java.net.Socket;

public final class Heartbeat extends Thread {

    private final String host;
    private final int port;
    private Socket socket;

    public Heartbeat(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public synchronized void start() {
        try {
            socket = new Socket(host, port);
            super.start();
        } catch (IOException e) {
            System.err.println("Connection failed!");
        }
    }
    
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                sendHeartBeat();
                Thread.sleep(5000);
            } catch (IOException e) {
                System.err.println("Unable to send heartbeat");
            } catch (InterruptedException e) {
                System.err.println("Heartbeat interrupted");
            }
        }
    }

    private void sendHeartBeat() throws IOException {
        byte[] heartbeatData = ByteUtils.loadMessageFromFile("bin/heartbeat.bin");
        int start = 0;
        int length = heartbeatData.length;

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        if (length > 0) {
            dataOutputStream.write(heartbeatData, start, length);
        }

        dataOutputStream.flush();

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[106];
        int bytesRead = dataInputStream.read(buf);

        byteArrayOutputStream.write(buf, 0, bytesRead);
    }

}
