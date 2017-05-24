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

public final class VideoRecorder implements Recorder {

    private static final String VIDEO_PATH = System.getProperty("user.dir") + "/hackadrone-persistence/target/classes/video";

    private final String droneHost;
    private final int dronePort;
    private final String recordHost;
    private final int recordPort;

    private Process recorder;
    private Socket recordSocket;
    private OutputStream recordOutputStream;
    private Decoder decoder;

    public VideoRecorder(String droneHost, int dronePort, String recordHost, int recordPort) {
        this.droneHost = droneHost;
        this.dronePort = dronePort;
        this.recordHost = recordHost;
        this.recordPort = recordPort;
    }

    @Override
    public void start() {
        try {
            if (recorder != null) {
                stop();
            }

            startRecordVideo();

            Thread.sleep(1000);

            recordSocket = new Socket(InetAddress.getByName(recordHost), recordPort);
            recordOutputStream = new BufferedOutputStream(recordSocket.getOutputStream());

            startRecord();
        } catch (IOException | InterruptedException e) {
            System.err.println("Unable to start recording a video");
        }
    }

    @Override
    public void stop() {
        if (recorder != null) {
            recorder.destroy();
            recorder = null;
        }

        if (recordOutputStream != null && recordSocket != null) {
            try {
                recordOutputStream.close();
                recordSocket.close();
            } catch (IOException e) {
                System.err.println("Unable to stop recording a video");
            }

            recordOutputStream = null;
            recordSocket = null;
        }
    }

    private void startRecordVideo() throws IOException {
        String output = "tcp://" + recordHost + ":" + recordPort + "?listen";
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String fileName = ("output-" + timestamp + ".mp4");

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

    private void startRecord() throws IOException {
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

                    if (recordOutputStream != null) {
                        recordOutputStream.write(data);
                    }

                    if (recordOutputStream == null) {
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
