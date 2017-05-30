package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.utils.ByteUtils;

import java.io.*;
import java.net.Socket;

/**
 * Class representing the heartbeat of a drone.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class Heartbeat extends Thread {

    /**
     * The host of the drone.
     */
    private final String host;

    /**
     * The port of the drone.
     */
    private final int port;

    /**
     * The socket to open with the drone.
     */
    private Socket socket;

    /**
     * A heartbeat constructor.
     *
     * @param host the host of the drone
     * @param port the port of the drone
     */
    public Heartbeat(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Starts the heartbeat as a thread.
     */
    @Override
    public synchronized void start() {
        try {
            socket = new Socket(host, port);
            super.start();
        } catch (IOException e) {
            System.err.println("Connection failed!");
        }
    }

    /**
     * Starts running the heartbeat.
     */
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

    /**
     * Sends a heartbeat to the drone.
     *
     * @throws IOException if sending the heartbeat to the drone failed
     */
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
