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

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.imageio.ImageIO;

import nl.ordina.jtech.hackadrone.net.DroneDecoder;
import nl.ordina.jtech.hackadrone.utils.OS;

/**
 * Class representing the camera for a drone.
 */
public final class Camera implements Handler {

    /**
     * Frame rate used by FFMPEG might need to be adjusted based on the available system resources
     */
    private static final int NR_OF_FRAMES_PER_SECOND = 10;

    private static final int NUMBER_OF_FRAMES_PER_RECOGNITION = NR_OF_FRAMES_PER_SECOND * 6;

    /**
     * The video resource path.
     */
    private static final String VIDEO_PATH = System.getProperty("user.dir") + "/video";

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
     * Image file format spec. Last two bytes should have the following values: FF D9
     */
    private static final int JPEG_FILE_EOF_NEXT_TO_LAST = 255; //FF
    /**
     * Image file format spec. Last two bytes should have the following values: FF D9
     */
    private static final int JPEG_FILE_EOF_LAST = 217;//D9
    /**
     * The video decoder.
     */
    private DroneDecoder decoder;
    /**
     * Flag for the video display thread.
     */
    private boolean stopVideoThread;
    /**
     * Video frame.
     */
    private VideoFrame videoFrame;
    /**
     * Deep learning reference
     */
    private DeepLearning deepLearning;

    /**
     * A command constructor.
     *
     * @param droneHost the host of the drone
     * @param dronePort the port of the drone
     * @param cameraHost the host of the camera
     * @param cameraPort the port of the camera
     */
    public Camera(String droneHost, int dronePort, String cameraHost, int cameraPort, VideoFrame videoFrame) {
        this.droneHost = droneHost;
        this.dronePort = dronePort;
        this.cameraHost = cameraHost;
        this.cameraPort = cameraPort;
        this.videoFrame = videoFrame;
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

            stopVideoThread = false;
            startVideoCamera();

            Thread.sleep(1000);

            videoSocket = new Socket(InetAddress.getByName(cameraHost), cameraPort);
            videoOutputStream = new BufferedOutputStream(videoSocket.getOutputStream());

            startVideo();

            videoFrame.showFrame();
        } catch (IOException | InterruptedException e) {
            System.err.println("Unable to start the video player");
            System.err.println(e);
        }
    }

    /**
     * Stops the video camera.
     */
    @Override
    public void stop() {
        videoFrame.hideFrame();

        if (videoPlayer != null) {
            videoPlayer.destroy();
            videoPlayer = null;
        }

        stopVideoThread = true;

        if (videoOutputStream != null && videoSocket != null && decoder != null) {
            try {
                videoOutputStream.close();
                videoSocket.close();
                decoder.requestStop();
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
                videoPlayer =
                        new ProcessBuilder("cmd", "/c", VIDEO_PATH + "/win/ffmpeg.exe", "-i", output, "-r", String.valueOf(NR_OF_FRAMES_PER_SECOND), "-f", "image2pipe", "pipe:1")
                                .start();
                break;
            case "unix":
                videoPlayer = new ProcessBuilder(VIDEO_PATH + "/unix/ffmpeg", "-i", output, "-r", String.valueOf(NR_OF_FRAMES_PER_SECOND), "-f", "image2pipe", "pipe:1").start();
                break;
            case "osx":
                videoPlayer = new ProcessBuilder(VIDEO_PATH + "/osx/ffmpeg", "-i", output, "-r", String.valueOf(NR_OF_FRAMES_PER_SECOND), "-f", "image2pipe", "pipe:1").start();
                break;
        }
    }

    /**
     * Starts the video.
     *
     * @throws IOException if starting the video failed
     */
    private void startVideo() throws IOException {
        if (decoder == null) {
            decoder = new DroneDecoder(droneHost, dronePort, videoOutputStream);
        }

        Thread decoderThread = new Thread(decoder);
        decoderThread.start();

        final Thread frameWatcherThread = new Thread(() -> {
            InputStream is = new BufferedInputStream(videoPlayer.getInputStream());
            int index = NUMBER_OF_FRAMES_PER_RECOGNITION;
            int currentByte;
            int preLastByte = -2;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //Grab all of the bytes until we see the end of a jpeg file
            try {
                while ((currentByte = is.read()) != -1 && !stopVideoThread) {
                    out.write(currentByte);

                    if (preLastByte == JPEG_FILE_EOF_NEXT_TO_LAST && currentByte == JPEG_FILE_EOF_LAST) {
                        // no in output you get the image as a binary array. You can efficiently save it in the database, memcached or file
                        InputStream inputStream = new ByteArrayInputStream(out.toByteArray());
                        BufferedImage bImageFromConvert = ImageIO.read(inputStream);
                        videoFrame.updateVideoFrame(bImageFromConvert);
                        if (deepLearning != null && index % 60 == 0) {
                            deepLearning.setVideoFrame(videoFrame);
                            deepLearning.updateLabels(bImageFromConvert);
                            index = 0;
                        }
                        out.reset();
                    }
                    preLastByte = currentByte;
                    index++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        frameWatcherThread.start();

        final Thread errorThread = new Thread(() -> {
            InputStream errorInputStream = new BufferedInputStream(videoPlayer.getErrorStream());
            int currentByte;
            try {
                while ((currentByte = errorInputStream.read()) != -1) {
                    // FFmpeg output
                    System.err.print((char) currentByte);
                }
            } catch (IOException e) {
                if (!stopVideoThread) {
                    e.printStackTrace();
                }
            }
        });
        errorThread.start();

    }

    public DeepLearning getDeepLearning() {
        return deepLearning;
    }

    public void setDeepLearning(DeepLearning deepLearning) {
        this.deepLearning = deepLearning;
    }
}
