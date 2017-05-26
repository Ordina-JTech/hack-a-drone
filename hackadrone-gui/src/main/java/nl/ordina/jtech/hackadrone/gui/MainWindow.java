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
    private JButton btnCamera;
    private JButton btnRecorder;
    private JLabel lblStatus;
    private JButton btnAi;

    private boolean isConnected = false;
    private boolean isControlled = false;
    private boolean isStreaming = false;
    private boolean isRecording = false;
    private boolean isAi = false;

    MainWindow() {
        init();
    }

    @Override
    public void init() {
        btnConnect.setEnabled(true);
        btnControls.setEnabled(false);
        btnCamera.setEnabled(false);
        btnRecorder.setEnabled(false);
        btnAi.setEnabled(false);

        lblStatus.setEnabled(true);

        btnConnect.addActionListener(this);
        btnControls.addActionListener(this);
        btnCamera.addActionListener(this);
        btnRecorder.addActionListener(this);
        btnAi.addActionListener(this);

        add(panel);
        setTitle(cx10.getName());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
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
            } else if (actionEvent.getSource() == btnCamera) {
                onCameraClicked();
            } else if (actionEvent.getSource() == btnRecorder) {
                onRecorderClicked();
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
    public void onCameraClicked() {
        if (!isStreaming) {
            startCamera();
        } else {
            stopCamera();
        }
    }

    @Override
    public void onRecorderClicked() {
        if (!isRecording) {
            startRecorder();
        } else {
            stopRecorder();
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

            model.setBtnConnectText("Disconnect");
            model.setBtnConnectEnabled(true);

            model.setBtnControlsEnabled(true);

            model.setBtnCameraEnabled(true);

            model.setBtnRecorderEnabled(true);

            model.setBtnAiEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Connection successfully established");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Connection successfully established" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnConnectText("Connect");
            model.setBtnConnectEnabled(true);

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

            stopRecorder();
            stopCamera();
            stopControls();

            cx10.stopHeartbeat();
            cx10.disconnect();

            isConnected = false;

            model = getModel();

            model.setBtnConnectText("Connect");
            model.setBtnConnectEnabled(true);

            model.setBtnControlsText("Start Controls");
            model.setBtnControlsEnabled(false);

            model.setBtnCameraText("Start Camera");
            model.setBtnCameraEnabled(false);

            model.setBtnRecorderText("Start Reader");
            model.setBtnRecorderEnabled(false);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Disconnection successful");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Disconnection successful" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnConnectText("Disconnect");
            model.setBtnConnectEnabled(true);

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

            model.setBtnControlsText("Stop Controls");
            model.setBtnControlsEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Controls successfully started");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Controls successfully started" + ANSI.RESET);
        } catch (IOException e) {
            model = getModel();

            model.setBtnControlsText("Start Controls");
            model.setBtnControlsEnabled(true);

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

            model.setBtnControlsText("Start Controls");
            model.setBtnControlsEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Controls successfully stopped");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Controls successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnControlsText("Stop Controls");
            model.setBtnControlsEnabled(true);

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Stopping the controls failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Stopping the controls failed!" + ANSI.RESET);
        }
    }

    private void startCamera() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to start the camera..." + ANSI.RESET);

            model = getModel();

            model.setBtnCameraEnabled(false);
            model.setBtnCameraText("Starting camera...");

            model.setBtnRecorderEnabled(false);

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to start the camera...");

            updateModel(model);

            cx10.startCamera();

            isStreaming = true;

            model = getModel();

            model.setBtnCameraText("Stop Camera");
            model.setBtnCameraEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Camera successfully started");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Camera successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnCameraText("Start Camera");
            model.setBtnCameraEnabled(true);

            model.setBtnRecorderEnabled(true);

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Starting the camera failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Starting the camera failed!" + ANSI.RESET);
        }
    }

    private void stopCamera() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the camera..." + ANSI.RESET);

            model = getModel();

            model.setBtnCameraEnabled(false);
            model.setBtnCameraText("Stopping camera...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to stop the camera...");

            updateModel(model);

            cx10.stopCamera();

            isStreaming = false;

            model = getModel();

            model.setBtnCameraText("Start Camera");
            model.setBtnCameraEnabled(true);

            model.setBtnRecorderEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Camera successfully stopped");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Camera successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnCameraText("Stop Camera");
            model.setBtnCameraEnabled(true);

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Stopping the camera failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Stopping the camera failed!" + ANSI.RESET);
        }
    }

    private void startRecorder() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to start the recorder.." + ANSI.RESET);

            model = getModel();

            model.setBtnCameraEnabled(false);

            model.setBtnRecorderEnabled(false);
            model.setBtnRecorderText("Starting recorder...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to start the recorder...");

            updateModel(model);

            cx10.startRecorder();

            isRecording = true;

            model = getModel();

            model.setBtnRecorderText("Stop Reader");
            model.setBtnRecorderEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Reader successfully started");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Reader successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnCameraEnabled(true);

            model.setBtnRecorderText("Start Reader");
            model.setBtnRecorderEnabled(true);

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Starting recording failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Starting recording failed!" + ANSI.RESET);
        }
    }

    private void stopRecorder() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the recorder..." + ANSI.RESET);

            model = getModel();

            model.setBtnRecorderEnabled(false);
            model.setBtnRecorderText("Stopping recorder...");

            model.setLblStatusForeground(SpecialColor.YELLOW);
            model.setLblStatusText("Trying to stop the recorder.");

            updateModel(model);

            cx10.stopRecorder();

            isRecording = false;

            model = getModel();

            model.setBtnCameraEnabled(true);

            model.setBtnRecorderText("Start Reader");
            model.setBtnRecorderEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Reader successfully stopped");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Reader successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnRecorderText("Stop Reader");
            model.setBtnRecorderEnabled(true);

            model.setLblStatusForeground(SpecialColor.RED);
            model.setLblStatusText("Stopping the recorder failed!");

            updateModel(model);

            System.out.println(ANSI.RED + "Stopping the recorder failed!" + ANSI.RESET);
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

            model.setBtnAiText("Stop AI");
            model.setBtnAiEnabled(true);

            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Starting AI successfully started");

            updateModel(model);

            System.out.println(ANSI.GREEN + "Starting AI successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();

            model.setBtnControlsEnabled(true);

            model.setBtnAiText("Start AI");
            model.setBtnAiEnabled(true);

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

        model.setBtnCameraEnabled(btnCamera.isEnabled());
        model.setBtnCameraText(btnCamera.getText());

        model.setBtnRecorderEnabled(btnRecorder.isEnabled());
        model.setBtnRecorderText(btnRecorder.getText());

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

            btnCamera.setEnabled(model.isBtnCameraEnabled());
            btnCamera.setText(model.getBtnCameraText());

            btnRecorder.setEnabled(model.isBtnRecorderEnabled());
            btnRecorder.setText(model.getBtnRecorderText());

            btnAi.setEnabled(model.isBtnAiEnabled());
            btnAi.setText(model.getBtnAiText());

            lblStatus.setEnabled(model.isLblStatusEnabled());
            lblStatus.setText(model.getLblStatusText());
            lblStatus.setForeground(model.getLblStatusForeground());
        });
    }

}
