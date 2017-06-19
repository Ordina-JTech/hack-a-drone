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

package nl.ordina.jtech.hackadrone.gui;

import nl.ordina.jtech.hackadrone.CX10;
import nl.ordina.jtech.hackadrone.Drone;
import nl.ordina.jtech.hackadrone.DroneException;
import nl.ordina.jtech.hackadrone.io.Keyboard;
import nl.ordina.jtech.hackadrone.utils.ANSI;
import nl.ordina.jtech.hackadrone.utils.SpecialColor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Class representing a main window that exists within a Graphical User Interface (GUI).
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
final class MainWindow extends JFrame implements Frame, ActionListener, ClickEvent {

    /**
     * The width of the main window.
     */
    private static final int WIDTH = 750;

    /**
     * The height of the main window.
     */
    private static final int HEIGHT = 150;

    /**
     * The core code of the CX10 drone.
     */
    private final Drone cx10 = new CX10();

    /**
     * The main window model.
     */
    private MainWindowModel model = new MainWindowModel();

    /**
     * The main panel.
     */
    private JPanel mainPanel = new JPanel();

    /**
     * The buttons panel.
     */
    private JPanel buttonsPanel = new JPanel();

    /**
     * The status panel.
     */
    private JPanel statusPanel = new JPanel();

    /**
     * The connect button.
     */
    private JButton btnConnect = new JButton("Connect");

    /**
     * The controls button.
     */
    private JButton btnControls = new JButton("Start Controls");

    /**
     * The camera button.
     */
    private JButton btnCamera = new JButton("Start Camera");

    /**
     * The recorder button.
     */
    private JButton btnRecorder = new JButton("Start Recorder");

    /**
     * The AI button.
     */
    private JButton btnAi = new JButton("Start AI");

    /**
     * The status label.
     */
    private JLabel lblStatus = new JLabel();

    /**
     * The connected status.
     */
    private boolean isConnected = false;

    /**
     * The controlled status.
     */
    private boolean isControlled = false;

    /**
     * The streaming status.
     */
    private boolean isStreaming = false;

    /**
     * The recording status.
     */
    private boolean isRecording = false;

    /**
     * The AI status.
     */
    private boolean isAi = false;

    /**
     * A main window constructor.
     */
    MainWindow() {
        init();
    }

    /**
     * Initializes the main window.
     */
    @Override
    public void init() {
        setTitle(cx10.getName());
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setAlwaysOnTop(true);
        setResizable(false);
        setContentPane(mainPanel);

        btnConnect.setPreferredSize(new Dimension(125, 25));
        btnConnect.setEnabled(true);
        btnConnect.addActionListener(this);

        btnControls.setPreferredSize(new Dimension(125, 25));
        btnControls.setEnabled(false);
        btnControls.addActionListener(this);

        btnCamera.setPreferredSize(new Dimension(125, 25));
        btnCamera.setEnabled(false);
        btnCamera.addActionListener(this);

        btnRecorder.setPreferredSize(new Dimension(125, 25));
        btnRecorder.setEnabled(false);
        btnRecorder.addActionListener(this);

        btnAi.setPreferredSize(new Dimension(125, 25));
        btnAi.setEnabled(false);
        btnAi.addActionListener(this);

        lblStatus.setEnabled(true);

        buttonsPanel.add(btnConnect);
        buttonsPanel.add(btnControls);
        buttonsPanel.add(btnCamera);
        buttonsPanel.add(btnRecorder);
        buttonsPanel.add(btnAi);

        statusPanel.add(lblStatus);

        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.add(buttonsPanel);
        mainPanel.add(statusPanel);

        initModel();

        setVisible(true);

        System.out.println(ANSI.BLUE + "Welcome to the " + cx10.getName() + " Graphical User Interface (GUI)" + ANSI.RESET);
    }

    /**
     * Handles a performed action when a source is triggered.
     */
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

    /**
     * Handles the connect button.
     */
    @Override
    public void onConnectClicked() {
        if (!isConnected) {
            connect();
        } else {
            disconnect();
        }
    }

    /**
     * Handles the controls button.
     */
    @Override
    public void onControlsClicked() {
        if (!isControlled) {
            startControls();
        } else {
            stopControls();
        }
    }

    /**
     * Handles the camera button.
     */
    @Override
    public void onCameraClicked() {
        if (!isStreaming) {
            startCamera();
        } else {
            stopCamera();
        }
    }

    /**
     * Handles the recorder button.
     */
    @Override
    public void onRecorderClicked() {
        if (!isRecording) {
            startRecorder();
        } else {
            stopRecorder();
        }
    }

    /**
     * Handles AI button.
     */
    @Override
    public void onAiClicked() {
        if (!isAi) {
            startAi();
        } else {
            stopAi();
        }
    }

    /**
     * Connects.
     */
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

    /**
     * Disconnects.
     */
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
                model.setBtnControlsContext(false, "Start Controls");
                model.setBtnCameraContext(false, "Start Camera");
                model.setBtnRecorderContext(false, "Start Recorder");
                model.setBtnAiContext(false, "Start AI");
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

    /**
     * Starts the controls.
     */
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

    /**
     * Stops the controls.
     */
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

    /**
     * Starts the camera.
     */
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

    /**
     * Stops the camera.
     */
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

    /**
     * Starts the recorder.
     */
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

    /**
     * Stops the recorder.
     */
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

    /**
     * Starts the AI.
     */
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

    /**
     * Stops the AI.
     */
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
                model.setLblStatusContext(SpecialColor.RED, "Stopping the AI failed!");
            });

            System.out.println(ANSI.RED + "Stopping the AI failed!" + ANSI.RESET);
        }
    }

    /**
     * Initializes the default model.
     */
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

    /**
     * Updates the model.
     *
     * @param modelConsumer the model consumer
     */
    private void updateModel(Consumer<MainWindowModel> modelConsumer){
        MainWindowModel model = this.model.copy();
        modelConsumer.accept(model);
        updateModel(model);
        this.model = model;
    }

    /**
     * Updates the model to the view.
     *
     * @param model the model
     */
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
