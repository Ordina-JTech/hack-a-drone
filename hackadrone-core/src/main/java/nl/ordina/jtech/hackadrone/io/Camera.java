package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.net.Decoder;
import nl.ordina.jtech.hackadrone.net.DroneDecoder;
import nl.ordina.jtech.hackadrone.utils.OS;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class representing the camera of a drone.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class Camera implements Handler {

    /**
     * The video resource path.
     */
    private static final String VIDEO_PATH = System.getProperty("user.dir") + "/hackadrone-persistence/target/classes/video";

    /**
     * The host of the drone.
     */
    private final String droneHost;

    /**
     * The port of the drone.
     */
    private final int dronePort;

    /**
     * The host of the camera.
     */
    private final String cameraHost;

    /**
     * The port of the camera.
     */
    private final int cameraPort;

    /**
     * The video player.
     */
    private Process videoPlayer;

    /**
     * The video socket.
     */
    private Socket videoSocket;

    /**
     * The video output stream.
     */
    private OutputStream videoOutputStream;

    /**
     * The video decoder.
     */
    private Decoder decoder;

    /**
     * A command constructor.
     *
     * @param droneHost the host of the drone
     * @param dronePort the port of the drone
     * @param cameraHost the host of the camera
     * @param cameraPort the port of the camera
     */
    public Camera(String droneHost, int dronePort, String cameraHost, int cameraPort) {
        this.droneHost = droneHost;
        this.dronePort = dronePort;
        this.cameraHost = cameraHost;
        this.cameraPort = cameraPort;
    }

    /**
     * Starts the video camera.
     */
    @Override
    public void start() {
        try {
            if (videoPlayer != null) {
                stop();
            }

            startVideoCamera();

            Thread.sleep(1000);

            videoSocket = new Socket(InetAddress.getByName(cameraHost), cameraPort);
            videoOutputStream = new BufferedOutputStream(videoSocket.getOutputStream());

            startVideo();
        } catch (IOException | InterruptedException e) {
            System.err.println("Unable to start the video player");
        }
    }

    /**
     * Stops the video camera.
     */
    @Override
    public void stop() {
        if (videoPlayer != null) {
            videoPlayer.destroy();
            videoPlayer = null;
        }

        if (videoOutputStream != null && videoSocket != null) {
            try {
                videoOutputStream.close();
                videoSocket.close();
            } catch (IOException e) {
                System.err.println("Unable to stop the video player");
            }

            videoOutputStream = null;
            videoSocket = null;
        }
    }

    /**
     * Starts the video camera.
     *
     * @throws IOException if starting the video camera failed
     */
    private void startVideoCamera() throws IOException {
        String output = "tcp://" + cameraHost + ":" + cameraPort + "?listen";

        switch (OS.getOS()) {
            case "win":
                videoPlayer = new ProcessBuilder("cmd", "/c", "start", VIDEO_PATH + "/win/ffplay.exe", "-probesize", "64", "-sync", "ext", output).start();
                break;
            case "unix":
                videoPlayer = new ProcessBuilder(VIDEO_PATH + "/unix/ffplay", "-fflags", "nobuffer", output).start();
                break;
            case "osx":
                videoPlayer = new ProcessBuilder(VIDEO_PATH + "/osx/ffplay", "-fflags", "nobuffer", output).start();
                break;
        }
    }

    /**
     * Starts the video.
     *
     * @throws IOException if starting the video failed
     */
    private void startVideo() throws IOException {
        if (decoder != null) {
            throw new IOException("Starting the video stream failed!");
        }

        decoder = new DroneDecoder(droneHost, dronePort);
        decoder.connect();

        final Thread thread = new Thread(() -> {
            byte[] data = null;

            do {
                try {
                    data = decoder.read();

                    if (videoOutputStream != null) {
                        videoOutputStream.write(data);
                    }

                    if (videoOutputStream == null) {
                        decoder.disconnect();
                        break;
                    }
                } catch (IOException e) {
                    System.err.println("Unable to read video output stream");
                }
            } while (data != null);

            decoder = null;
        });

        thread.start();
    }

}
