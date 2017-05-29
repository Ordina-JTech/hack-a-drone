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
import java.util.function.Consumer;

public final class MainWindow extends JFrame implements Frame, ActionListener, ClickEvent {

    private static final int WIDTH = 750;
    private static final int HEIGHT = 150;

    private final Drone cx10 = new CX10();

    private MainWindowModel model = new MainWindowModel();

    private JPanel panel;
    private JButton btnConnect;
    private JButton btnControls;
    private JButton btnCamera;
    private JButton btnRecorder;
    private JButton btnAi;
    private JLabel lblStatus;

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

        initModel();

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
        try {
            System.out.println(ANSI.YELLOW + "Trying to establish a new connection..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnConnectContext(false, "Connecting...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to establish a new connection...");
            });

            cx10.connect();
            cx10.sendMessages();
            cx10.startHeartbeat();

            isConnected = true;

            updateModel(model -> {
                model.setBtnConnectContext(true, "Disconnect");
                model.setBtnControlsEnabled(true);
                model.setBtnCameraEnabled(true);
                model.setBtnRecorderEnabled(true);
                model.setBtnAiEnabled(true);
                model.setLblStatusContext(SpecialColor.GREEN, "Connection successfully established");
            });

            System.out.println(ANSI.GREEN + "Connection successfully established" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnConnectContext(true, "Connect");
                model.setLblStatusContext(SpecialColor.RED, "Connection failed!");
            });

            System.out.println(ANSI.RED + "Connection failed!" + ANSI.RESET);
        }
    }

    private void disconnect() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to disconnect..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnConnectContext(false, "Disconnecting...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to disconnect...");
            });

            stopRecorder();
            stopCamera();
            stopControls();
            stopAi();

            cx10.stopHeartbeat();
            cx10.disconnect();

            isConnected = false;

            updateModel(model -> {
                model.setBtnConnectContext(true, "Connect");
                model.setBtnConnectContext(false, "Start Controls");
                model.setBtnCameraContext(false, "Start Camera");
                model.setBtnRecorderContext(false, "Start Recorder");
                model.setBtnConnectContext(false, "Start AI");
                model.setLblStatusContext(SpecialColor.GREEN, "Disconnection successful");
            });

            System.out.println(ANSI.GREEN + "Disconnection successful" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnConnectContext(true, "Disconnect");
                model.setLblStatusContext(SpecialColor.RED, "Disconnection failed!");
            });

            System.out.println(ANSI.RED + "Disconnection failed!" + ANSI.RESET);
        }
    }

    private void startControls() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to start the controls..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnControlsContext(false, "Starting controls...");
                model.setBtnAiEnabled(false);
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to start the controls...");
            });

            cx10.startControls(new Keyboard(KeyboardFocusManager.getCurrentKeyboardFocusManager()));

            isControlled = true;

            updateModel(model -> {
                model.setBtnControlsContext(true, "Stop Controls");
                model.setLblStatusContext(SpecialColor.GREEN, "Controls successfully started");
            });

            System.out.println(ANSI.GREEN + "Controls successfully started" + ANSI.RESET);
        } catch (IOException e) {
            updateModel(model -> {
                model.setBtnControlsContext(true, "Start Controls");
                model.setBtnAiEnabled(true);
                model.setLblStatusContext(SpecialColor.RED, "Starting the controls failed!");
            });

            System.out.println(ANSI.RED + "Starting the controls failed!" + ANSI.RESET);
        }
    }

    private void stopControls() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the controls..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnControlsContext(false, "Stopping controls...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to stop the controls...");
            });

            cx10.stopControls();

            isControlled = false;

            updateModel(model -> {
                model.setBtnControlsContext(true, "Start Controls");
                model.setBtnAiEnabled(true);
                model.setLblStatusContext(SpecialColor.GREEN, "Controls successfully stopped");
            });

            System.out.println(ANSI.GREEN + "Controls successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnControlsContext(true, "Stop Controls");
                model.setLblStatusContext(SpecialColor.RED, "Stopping the controls failed!");
            });

            System.out.println(ANSI.RED + "Stopping the controls failed!" + ANSI.RESET);
        }
    }

    private void startCamera() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to start the camera..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnCameraContext(false, "Starting camera...");
                model.setBtnRecorderEnabled(false);
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to start the camera...");
            });

            cx10.startCamera();

            isStreaming = true;

            updateModel(model -> {
                model.setBtnCameraContext(true, "Stop Camera");
                model.setLblStatusContext(SpecialColor.GREEN, "Camera successfully started");
            });

            System.out.println(ANSI.GREEN + "Camera successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnCameraContext(true, "Start Camera");
                model.setBtnRecorderEnabled(true);
                model.setLblStatusContext(SpecialColor.RED, "Starting the camera failed!");
            });

            System.out.println(ANSI.RED + "Starting the camera failed!" + ANSI.RESET);
        }
    }

    private void stopCamera() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the camera..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnCameraContext(false, "Stopping camera...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to stop the camera...");
            });

            cx10.stopCamera();

            isStreaming = false;

            updateModel(model -> {
                model.setBtnCameraContext(true, "Start Camera");
                model.setBtnRecorderEnabled(true);
                model.setLblStatusContext(SpecialColor.GREEN, "Camera successfully stopped");
            });

            System.out.println(ANSI.GREEN + "Camera successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnCameraContext(true, "Stop Camera");
                model.setLblStatusContext(SpecialColor.RED, "Stopping the camera failed!");
            });

            System.out.println(ANSI.RED + "Stopping the camera failed!" + ANSI.RESET);
        }
    }

    private void startRecorder() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to start the recorder.." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnCameraEnabled(false);
                model.setBtnRecorderContext(false, "Starting recorder...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to start the recorder...");
            });

            cx10.startRecorder();

            isRecording = true;

            updateModel(model -> {
                model.setBtnRecorderContext(true, "Stop Recorder");
                model.setLblStatusContext(SpecialColor.GREEN, "Recorder successfully started");
            });

            System.out.println(ANSI.GREEN + "Recorder successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnCameraEnabled(true);
                model.setBtnRecorderContext(true, "Start Recorder");
                model.setLblStatusContext(SpecialColor.RED, "Starting recording failed!");
            });

            System.out.println(ANSI.RED + "Starting recording failed!" + ANSI.RESET);
        }
    }

    private void stopRecorder() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the recorder..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnRecorderContext(false, "Stopping recorder...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to stop the recorder.");
            });

            cx10.stopRecorder();

            isRecording = false;

            updateModel(model -> {
                model.setBtnCameraEnabled(true);
                model.setBtnRecorderContext(true, "Start Recorder");
                model.setLblStatusContext(SpecialColor.GREEN, "Recorder successfully stopped");
            });

            System.out.println(ANSI.GREEN + "Recorder successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnRecorderContext(true, "Stop Recorder");
                model.setLblStatusContext(SpecialColor.RED, "Stopping the recorder failed!");
            });

            System.out.println(ANSI.RED + "Stopping the recorder failed!" + ANSI.RESET);
        }
    }

    private void startAi() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to start the AI..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnControlsEnabled(false);
                model.setBtnAiContext(false, "Starting AI...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to start AI...");
            });

            cx10.startAi();

            isAi = true;

            updateModel(model -> {
                model.setBtnAiContext(true, "Stop AI");
                model.setLblStatusContext(SpecialColor.GREEN, "Starting AI successfully started");
            });

            System.out.println(ANSI.GREEN + "Starting AI successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnControlsEnabled(true);
                model.setBtnAiContext(true, "Start AI");
                model.setLblStatusContext(SpecialColor.RED, "Starting AI failed!");
            });

            System.out.println(ANSI.RED + "Starting AI failed!" + ANSI.RESET);
        }
    }

    private void stopAi() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the AI..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnAiContext(false, "Stopping AI...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to stop the AI.");
            });

            cx10.stopAi();

            isAi = false;

            updateModel(model -> {
                model.setBtnControlsEnabled(true);
                model.setBtnAiContext(true, "Start AI");
                model.setLblStatusContext(SpecialColor.GREEN, "AI successfully stopped");
            });

            System.out.println(ANSI.GREEN + "AI successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnAiContext(true, "Stop AI");
                model.setLblStatusContext(SpecialColor.RED, "Stopping the recorder failed!");
            });

            System.out.println(ANSI.RED + "Stopping the recorder failed!" + ANSI.RESET);
        }
    }

    private void initModel() {
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
    }

    private void updateModel(Consumer<MainWindowModel> modelConsumer){
        MainWindowModel model = this.model.copy();
        modelConsumer.accept(model);
        updateModel(model);
        this.model = model;
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
