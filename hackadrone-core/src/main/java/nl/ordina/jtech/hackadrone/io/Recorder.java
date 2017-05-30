package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.net.Decoder;
import nl.ordina.jtech.hackadrone.net.DroneDecoder;
import nl.ordina.jtech.hackadrone.utils.OS;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class representing the video recorder of a drone.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class Recorder implements Handler {

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
     * The host of the recorder.
     */
    private final String recorderHost;

    /**
     * The port of the recorder.
     */
    private final int recorderPort;

    /**
     * The recorder.
     */
    private Process recorder;

    /**
     * The recorder socket.
     */
    private Socket recorderSocket;

    /**
     * The recorder output stream.
     */
    private OutputStream recorderOutputStream;

    /**
     * The decoder.
     */
    private Decoder decoder;

    /**
     * A recorder constructor.
     *
     * @param droneHost the host of the drone
     * @param dronePort the port of the drone
     * @param recorderHost the recorder of the drone
     * @param recorderPort the recorder of the drone
     */
    public Recorder(String droneHost, int dronePort, String recorderHost, int recorderPort) {
        this.droneHost = droneHost;
        this.dronePort = dronePort;
        this.recorderHost = recorderHost;
        this.recorderPort = recorderPort;
    }

    /**
     * Starts the video recorder.
     */
    @Override
    public void start() {
        try {
            if (recorder != null) {
                stop();
            }

            startVideoRecorder();

            Thread.sleep(1000);

            recorderSocket = new Socket(InetAddress.getByName(recorderHost), recorderPort);
            recorderOutputStream = new BufferedOutputStream(recorderSocket.getOutputStream());

            startRecorder();
        } catch (IOException | InterruptedException e) {
            System.err.println("Unable to start recording a video");
        }
    }

    /**
     * Stops the video recorder.
     */
    @Override
    public void stop() {
        if (recorder != null) {
            recorder.destroy();
            recorder = null;
        }

        if (recorderOutputStream != null && recorderSocket != null) {
            try {
                recorderOutputStream.close();
                recorderSocket.close();
            } catch (IOException e) {
                System.err.println("Unable to stop recording a video");
            }

            recorderOutputStream = null;
            recorderSocket = null;
        }
    }

    /**
     * Starts the video recorder.
     *
     * @throws IOException if starting the video recorder failed
     */
    private void startVideoRecorder() throws IOException {
        String output = "tcp://" + recorderHost + ":" + recorderPort + "?listen";
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String fileName = ("record-" + timestamp + ".mp4");

        switch (OS.getOS()) {
            case "win":
                recorder = new ProcessBuilder("cmd", "/c", "start", VIDEO_PATH + "/win/ffmpeg.exe", "-f", "h264", "-i", output, "-vcodec", "copy", "-r", "25", fileName).start();
                break;
            case "unix":
                recorder = new ProcessBuilder(VIDEO_PATH + "/unix/ffmpeg", "-f", "h264", "-i", output, "-vcodec", "copy", "-r", "25", fileName).start();
                break;
            case "osx":
                recorder = new ProcessBuilder(VIDEO_PATH + "/osx/ffmpeg", "-f", "h264", "-i", output, "-vcodec", "copy", "-r", "25", fileName).start();
                break;
        }
    }

    /**
     * Start the recorder.
     *
     * @throws IOException if starting the recorder failed
     */
    private void startRecorder() throws IOException {
        if (decoder != null) {
            throw new IOException("Starting recording a video failed!");
        }

        decoder = new DroneDecoder(droneHost, dronePort);
        decoder.connect();

        final Thread thread = new Thread(() -> {
            byte[] data = null;

            do {
                try {
                    data = decoder.read();

                    if (recorderOutputStream != null) {
                        recorderOutputStream.write(data);
                    }

                    if (recorderOutputStream == null) {
                        decoder.disconnect();
                        break;
                    }
                } catch (IOException e) {
                    System.err.println("Unable to read record output stream");
                }
            } while (data != null);

            decoder = null;
        });

        thread.start();
    }

}
