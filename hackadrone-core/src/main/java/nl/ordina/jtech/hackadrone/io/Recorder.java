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

package nl.ordina.jtech.hackadrone.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.ordina.jtech.hackadrone.net.DroneDecoder;
import nl.ordina.jtech.hackadrone.utils.OS;

/**
 * Class representing the video recorder for a drone.
 */
public final class Recorder implements Handler {

    /**
     * The video resource path.
     */
    private static final String VIDEO_PATH = "hackadrone-core/target/classes/video";

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
    private DroneDecoder decoder;

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
            System.err.println("Unable to start recording a video" + e);
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

        decoder = new DroneDecoder(droneHost, dronePort, recorderOutputStream);

        Thread decoderThread = new Thread(decoder);
        decoderThread.start();

    }

}
