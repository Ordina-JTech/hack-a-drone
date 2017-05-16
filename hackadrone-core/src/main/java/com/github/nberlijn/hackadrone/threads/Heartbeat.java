package com.github.nberlijn.hackadrone.threads;

import com.github.nberlijn.hackadrone.utils.ByteUtils;

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
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.interrupt();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                sendHeartBeat();
                Thread.sleep(5000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendHeartBeat() throws IOException {
        byte[] heartbeatData = ByteUtils.loadMessageFromFile("heartbeat.bin");
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
