package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.net.Decoder;
import nl.ordina.jtech.hackadrone.net.DroneDecoder;
import nl.ordina.jtech.hackadrone.utils.OS;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public final class VideoPlayer implements Player {

    private static final String VIDEO_PATH = System.getProperty("user.dir") + "/hackadrone-persistence/target/classes/video";

    private final String droneHost;
    private final int dronePort;
    private final String videoHost;
    private final int videoPort;

    private Process videoPlayer;
    private Socket videoSocket;
    private OutputStream videoOutputStream;
    private Decoder decoder;

    public VideoPlayer(String droneHost, int dronePort, String videoHost, int videoPort) {
        this.droneHost = droneHost;
        this.dronePort = dronePort;
        this.videoHost = videoHost;
        this.videoPort = videoPort;
    }

    @Override
    public void start() {
        try {
            if (videoPlayer != null) {
                stop();
            }

            startVideoPlayer();

            Thread.sleep(1000);

            videoSocket = new Socket(InetAddress.getByName(videoHost), videoPort);
            videoOutputStream = new BufferedOutputStream(videoSocket.getOutputStream());

            startVideo();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Unable to start the video player");
        }
    }

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

    private void startVideoPlayer() throws IOException {
        String output = "tcp://" + videoHost + ":" + videoPort + "?listen";

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
