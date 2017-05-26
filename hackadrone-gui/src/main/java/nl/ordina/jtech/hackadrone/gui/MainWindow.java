package nl.ordina.jtech.hackadrone.gui;

import nl.ordina.jtech.hackadrone.CX10;
import nl.ordina.jtech.hackadrone.Drone;
import nl.ordina.jtech.hackadrone.DroneException;
import nl.ordina.jtech.hackadrone.io.Keyboard;
import nl.ordina.jtech.hackadrone.utils.ANSI;
import nl.ordina.jtech.hackadrone.utils.SpecialColor;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public final class MainWindow extends JFrame implements Frame, ActionListener, ClickEvent {

    private static final int WIDTH = 750;
    private static final int HEIGHT = 150;

    private final Drone cx10 = new CX10();

    private JPanel panel;
    private JButton btnConnect;
    private JButton btnControls;
    private JButton btnVideo;
    private JButton btnRecordVideo;
    private JLabel lblStatus;
    private JButton btnAi;

    private boolean isConnected = false;
    private boolean isControlled = false;
    private boolean isVideoStreaming = false;
    private boolean isVideoRecording = false;
    private boolean isAi = false;

    MainWindow() {
        init();
    }

    @Override
    public void init() {
        btnConnect.setEnabled(true);
        btnControls.setEnabled(false);
        btnVideo.setEnabled(false);
        btnRecordVideo.setEnabled(false);
        btnAi.setEnabled(false);

        lblStatus.setEnabled(true);

        btnConnect.addActionListener(this);
        btnControls.addActionListener(this);
        btnVideo.addActionListener(this);
        btnRecordVideo.addActionListener(this);
        btnAi.addActionListener(this);

        add(panel);
        setTitle(cx10.getName());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setLocationByPlatform(true);
        panel.setVisible(true);
        setVisible(true);
        pack();

        System.out.println(ANSI.BLUE + "Welcome to the " + cx10.getName() + " Graphical User Interface (GUI)" + ANSI.RESET);
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        new Thread(() -> {
            if (actionEvent.getSource() == btnConnect) {
                onConnectClicked();
            } else if (actionEvent.getSource() == btnControls) {
                onControlsClicked();
            } else if (actionEvent.getSource() == btnVideo) {
                onVideoClicked();
            } else if (actionEvent.getSource() == btnRecordVideo) {
                onRecordVideoClicked();
            } else if (actionEvent.getSource() == btnAi) {
                onAiClicked();
            }
        }).start();
    }

    @Override
    public void onConnectClicked() {
        if (!isConnected) {
            connect();
        } else {
            disconnect();
        }
    }

    @Override
    public void onControlsClicked() {
        if (!isControlled) {
            startControls();
        } else {
            stopControls();
        }
    }

    @Override
    public void onVideoClicked() {
        if (!isVideoStreaming) {
            startVideo();
        } else {
            stopVideo();
        }
    }

    @Override
    public void onRecordVideoClicked() {
        if (!isVideoRecording) {
            startRecordVideo();
        } else {
            stopRecordVideo();
        }
    }

    @Override
    public void onAiClicked() {
        if (!isAi) {
            startAi();
        } else {
            stopAi();
        }
    }

    private void connect() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to establish a new connection..." + ANSI.RESET);

            model = getModel();

            model.setBtnConnectEnabled(false);
            model.setBtnConnectText("Connecting...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to establish a new connection...");

            updateModel(model);

            cx10.connect();
            cx10.sendMessages();
            cx10.startHeartbeat();

            isConnected = true;

            model = getModel();

            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Disconnect");

            model.setBtnControlsEnabled(true);

            model.setBtnVideoEnabled(true);

            model.setBtnRecordVideoEnabled(true);

            model.setBtnAiEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Connection successfully established");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Connection successfully established" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Connect");

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Connection failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Connection failed!" + ANSI.RESET);
        }
    }

    private void disconnect() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to disconnect..." + ANSI.RESET);

            model = getModel();

            model.setBtnConnectEnabled(false);
            model.setBtnConnectText("Disconnecting...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to disconnect...");

            updateModel(model);

            stopRecordVideo();
            stopVideo();
            stopControls();

            cx10.stopHeartbeat();
            cx10.disconnect();

            isConnected = false;

            model = getModel();

            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Connect");

            model.setBtnControlsEnabled(false);
            model.setBtnControlsText("Start Controls");

            model.setBtnVideoEnabled(false);
            model.setBtnVideoText("Start Video");

            model.setBtnRecordVideoEnabled(false);
            model.setBtnRecordVideoText("Record Video");

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Disconnection successful");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Disconnection successful" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Disconnect");

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Disconnection failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Disconnection failed!" + ANSI.RESET);
        }
    }

    private void startControls() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to start the controls..." + ANSI.RESET);

            model = getModel();

            model.setBtnControlsEnabled(false);
            model.setBtnControlsText("Starting controls...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to start the controls...");

            updateModel(model);

            cx10.startControls(new Keyboard(KeyboardFocusManager.getCurrentKeyboardFocusManager()));

            isControlled = true;

            model = getModel();

            model.setBtnControlsEnabled(true);
            model.setBtnControlsText("Stop Controls");

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Controls successfully started");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Controls successfully started" + ANSI.RESET);
        } catch (IOException e) {
            model = getModel();

            model.setBtnControlsEnabled(true);
            model.setBtnControlsText("Start Controls");

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Starting the controls failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Starting the controls failed!" + ANSI.RESET);
        }
    }

    private void stopControls() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the controls..." + ANSI.RESET);

            model = getModel();

            model.setBtnControlsEnabled(false);
            model.setBtnControlsText("Stopping controls...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to stop the controls...");

            updateModel(model);

            cx10.stopControls();

            isControlled = false;

            model = getModel();

            model.setBtnControlsEnabled(true);
            model.setBtnControlsText("Start Controls");

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Controls successfully stopped");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Controls successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnControlsEnabled(true);
            model.setBtnControlsText("Stop Controls");

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Stopping the controls failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Stopping the controls failed!" + ANSI.RESET);
        }
    }

    private void startVideo() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to start the video stream..." + ANSI.RESET);

            model = getModel();

            model.setBtnVideoEnabled(false);
            model.setBtnVideoText("Starting video...");

            model.setBtnRecordVideoEnabled(false);

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to start the video stream...");

            updateModel(model);

            cx10.startVideoStream();

            isVideoStreaming = true;

            model = getModel();

            model.setBtnVideoEnabled(true);
            model.setBtnVideoText("Stop Video");

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Video stream successfully started");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Video stream successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnVideoEnabled(true);
            model.setBtnVideoText("Start Video");

            model.setBtnRecordVideoEnabled(true);

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Starting the video stream failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Starting the video stream failed!" + ANSI.RESET);
        }
    }

    private void stopVideo() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the video stream..." + ANSI.RESET);

            model = getModel();

            model.setBtnVideoEnabled(false);
            model.setBtnVideoText("Stopping video stream...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to stop the video stream...");

            updateModel(model);

            cx10.stopVideoStream();

            isVideoStreaming = false;

            model = getModel();

            model.setBtnVideoEnabled(true);
            model.setBtnVideoText("Start Video");

            model.setBtnRecordVideoEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Video stream successfully stopped");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Video stream successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnVideoEnabled(true);
            model.setBtnVideoText("Stop Video");

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Stopping the video stream failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Stopping the video stream failed!" + ANSI.RESET);
        }
    }

    private void startRecordVideo() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to start recording a video..." + ANSI.RESET);

            model = getModel();

            model.setBtnVideoEnabled(false);

            model.setBtnRecordVideoEnabled(false);
            model.setBtnRecordVideoText("Starting recording video...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to start recording a video...");

            updateModel(model);

            cx10.startVideoRecord();

            isVideoRecording = true;

            model = getModel();

            model.setBtnRecordVideoEnabled(true);
            model.setBtnRecordVideoText("Stop Record");

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Recording a video successfully started");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Recording a video successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnVideoEnabled(true);

            model.setBtnRecordVideoEnabled(true);
            model.setBtnRecordVideoText("Record Video");

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Starting recording a video failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Starting recording a video failed!" + ANSI.RESET);
        }
    }

    private void stopRecordVideo() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to stop recording a video..." + ANSI.RESET);

            model = getModel();

            model.setBtnRecordVideoEnabled(false);
            model.setBtnRecordVideoText("Stopping recording a video...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to stop recording a video..");

            updateModel(model);

            cx10.stopVideoRecord();

            isVideoRecording = false;

            model = getModel();

            model.setBtnVideoEnabled(true);

            model.setBtnRecordVideoEnabled(true);
            model.setBtnRecordVideoText("Record Video");

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Recording a video successfully stopped");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Recording a video successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnRecordVideoEnabled(true);
            model.setBtnRecordVideoText("Stop Record");

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Stopping recording a video failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Stopping recording a video failed!" + ANSI.RESET);
        }
    }

    private void startAi() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to start the AI..." + ANSI.RESET);

            model = getModel();

            model.setBtnControlsEnabled(false);

            model.setBtnAiEnabled(false);
            model.setBtnAiText("Starting AI...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to start AI...");

            updateModel(model);

            cx10.startAi();

            isAi = true;

            model = getModel();

            model.setBtnAiEnabled(true);
            model.setBtnAiText("Stop AI");

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Starting AI successfully started");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Starting AI successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnControlsEnabled(true);

            model.setBtnAiEnabled(true);
            model.setBtnRecordVideoText("Start AI");

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Starting AI failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Starting AI failed!" + ANSI.RESET);
        }
    }

    private void stopAi() {

    }

    private MainWindowModel getModel() {
        MainWindowModel model = new MainWindowModel();

        model.setBtnConnectEnabled(btnConnect.isEnabled());
        model.setBtnConnectText(btnConnect.getText());

        model.setBtnControlsEnabled(btnControls.isEnabled());
        model.setBtnControlsText(btnControls.getText());

        model.setBtnVideoEnabled(btnVideo.isEnabled());
        model.setBtnVideoText(btnVideo.getText());

        model.setBtnRecordVideoEnabled(btnRecordVideo.isEnabled());
        model.setBtnRecordVideoText(btnRecordVideo.getText());

        model.setBtnAiEnabled(btnAi.isEnabled());
        model.setBtnAiText(btnAi.getText());

        model.setLblStatusEnabled(lblStatus.isEnabled());
        model.setLblStatusText(lblStatus.getText());
        model.setLblStatusForeground(lblStatus.getForeground());

        return model;
    }

    private void updateModel(final MainWindowModel model) {
        SwingUtilities.invokeLater(() -> {
            btnConnect.setEnabled(model.isBtnConnectEnabled());
            btnConnect.setText(model.getBtnConnectText());

            btnControls.setEnabled(model.isBtnControlsEnabled());
            btnControls.setText(model.getBtnControlsText());

            btnVideo.setEnabled(model.isBtnVideoEnabled());
            btnVideo.setText(model.getBtnVideoText());

            btnRecordVideo.setEnabled(model.isBtnRecordVideoEnabled());
            btnRecordVideo.setText(model.getBtnRecordVideoText());

            btnAi.setEnabled(model.isBtnAiEnabled());
            btnAi.setText(model.getBtnAiText());

            lblStatus.setEnabled(model.isLblStatusEnabled());
            lblStatus.setText(model.getLblStatusText());
            lblStatus.setForeground(model.getLblStatusForeground());
        });
    }

}
