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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import nl.ordina.jtech.hackadrone.CX10;
import nl.ordina.jtech.hackadrone.Drone;
import nl.ordina.jtech.hackadrone.DroneException;
import nl.ordina.jtech.hackadrone.io.Keyboard;
import nl.ordina.jtech.hackadrone.utils.ANSI;
import nl.ordina.jtech.hackadrone.utils.SpecialColor;

/**
 * Class representing a main window that exists within a Graphical User Interface (GUI).
 */
final class MainWindow extends JFrame implements Frame, ActionListener, ClickEvent {

    /**
     * The width of the main window.
     */
    private static final int WIDTH = 1000;

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
     * The AutoPilot button.
     */
    private JButton btnAutoPilot = new JButton("Start AutoPilot");

    /**
     * The DeepLearning button.
     */
    private JButton btnDeepLearning = new JButton("Start DeepLearning");

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
     * The AutoPilot status.
     */
    private boolean isAutoPilotOn = false;

    /**
     * The DeepLearning status.
     */
    private boolean isDeepLearningOn = false;

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

        btnAutoPilot.setPreferredSize(new Dimension(125, 25));
        btnAutoPilot.setEnabled(false);
        btnAutoPilot.addActionListener(this);

        btnDeepLearning.setPreferredSize(new Dimension(150, 25));
        btnDeepLearning.setEnabled(false);
        btnDeepLearning.addActionListener(this);

        lblStatus.setEnabled(true);

        buttonsPanel.add(btnConnect);
        buttonsPanel.add(btnControls);
        buttonsPanel.add(btnCamera);
        buttonsPanel.add(btnRecorder);
        buttonsPanel.add(btnAutoPilot);
        buttonsPanel.add(btnDeepLearning);

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
            } else if (actionEvent.getSource() == btnAutoPilot) {
                onAutoPilotBtnClicked();
            } else if (actionEvent.getSource() == btnDeepLearning) {
                onDeepLearningBtnClicked();
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
     * Handles AutoPilot button.
     */
    @Override
    public void onAutoPilotBtnClicked() {
        if (!isAutoPilotOn) {
            startAutoPilot();
        } else {
            stopAutoPilot();
        }
    }

    /**
     * Handles the DeepLearning button.
     */
    private void onDeepLearningBtnClicked() {
        if (!isDeepLearningOn) {
            startDeepLearning();
        } else {
            stopDeepLearning();
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
                model.setBtnAutoPilotEnabled(true);
                model.setBtnDeepLearningEnabled(true);
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
            stopAutoPilot();
            stopDeepLearning();

            cx10.stopHeartbeat();
            cx10.disconnect();

            isConnected = false;

            updateModel(model -> {
                model.setBtnConnectContext(true, "Connect");
                model.setBtnControlsContext(false, "Start Controls");
                model.setBtnCameraContext(false, "Start Camera");
                model.setBtnRecorderContext(false, "Start Recorder");
                model.setBtnAutoPilotContext(false, "Start AutoPilot");
                model.setBtnDeepLearningContext(false, "Start DeepLearning");
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
                model.setBtnAutoPilotEnabled(false);
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
                model.setBtnAutoPilotEnabled(true);
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
                model.setBtnAutoPilotEnabled(true);
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
     * Starts the AutoPilot.
     */
    private void startAutoPilot() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to start the AutoPilot..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnControlsEnabled(false);
                model.setBtnAutoPilotContext(false, "Starting AutoPilot...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to start AutoPilot...");
            });

            cx10.startAutoPilot();

            isAutoPilotOn = true;

            updateModel(model -> {
                model.setBtnAutoPilotContext(true, "Stop AutoPilot");
                model.setLblStatusContext(SpecialColor.GREEN, "Starting AutoPilot successfully started");
            });

            System.out.println(ANSI.GREEN + "Starting AutoPilot successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnControlsEnabled(true);
                model.setBtnAutoPilotContext(true, "Start AutoPilot");
                model.setLblStatusContext(SpecialColor.RED, "Starting AutoPilot failed!");
            });

            System.out.println(ANSI.RED + "Starting AutoPilot failed!" + ANSI.RESET);
        }
    }

    /**
     * Stops the AutoPilot.
     */
    private void stopAutoPilot() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the AutoPilot..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnAutoPilotContext(false, "Stopping AutoPilot...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to stop the AutoPilot.");
            });

            cx10.stopAutoPilot();

            isAutoPilotOn = false;

            updateModel(model -> {
                model.setBtnControlsEnabled(true);
                model.setBtnAutoPilotContext(true, "Start AutoPilot");
                model.setLblStatusContext(SpecialColor.GREEN, "AutoPilot successfully stopped");
            });

            System.out.println(ANSI.GREEN + "AutoPilot successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnAutoPilotContext(true, "Stop AutoPilot");
                model.setLblStatusContext(SpecialColor.RED, "Stopping the AutoPilot failed!");
            });

            System.out.println(ANSI.RED + "Stopping the AutoPilot failed!" + ANSI.RESET);
        }
    }

    /**
     * Starts DeepLearning.
     */
    private void startDeepLearning() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to start DeepLearning..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnControlsEnabled(false);
                model.setBtnDeepLearningContext(false, "Starting DeepLearning..");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to start DeepLearning...");
            });

            cx10.startDeepLearning();

            isDeepLearningOn = true;

            updateModel(model -> {
                model.setBtnDeepLearningContext(true, "Stop DeepLearning");
                model.setLblStatusContext(SpecialColor.GREEN, "DeepLearning successfully started");
            });

            System.out.println(ANSI.GREEN + "DeepLearning successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnControlsEnabled(true);
                model.setBtnDeepLearningContext(true, "Start DeepLearning");
                model.setLblStatusContext(SpecialColor.RED, "Starting DeepLearning failed!");
            });

            System.out.println(ANSI.RED + "Starting DeepLearning failed!" + ANSI.RESET);
        }
    }

    /**
     * Stops DeepLearning.
     */
    private void stopDeepLearning() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the DeepLearning..." + ANSI.RESET);

            updateModel(model -> {
                model.setBtnDeepLearningContext(false, "Stopping DeepLearning...");
                model.setLblStatusContext(SpecialColor.YELLOW, "Trying to stop DeepLearning.");
            });

            cx10.stopDeepLearning();

            isDeepLearningOn = false;

            updateModel(model -> {
                model.setBtnControlsEnabled(true);
                model.setBtnDeepLearningContext(true, "Start DeepLearning");
                model.setLblStatusContext(SpecialColor.GREEN, "DeepLearning successfully stopped");
            });

            System.out.println(ANSI.GREEN + "DeepLearning successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            updateModel(model -> {
                model.setBtnDeepLearningContext(true, "Stop DeepLearning");
                model.setLblStatusContext(SpecialColor.RED, "Stopping DeepLearning failed!");
            });

            System.out.println(ANSI.RED + "Stopping DeepLearning failed!" + ANSI.RESET);
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

        model.setBtnAutoPilotEnabled(btnAutoPilot.isEnabled());
        model.setBtnAutoPilotText(btnAutoPilot.getText());

        model.setBtnDeepLearningEnabled(btnDeepLearning.isEnabled());
        model.setBtnDeepLearningText(btnDeepLearning.getText());

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

            btnAutoPilot.setEnabled(model.isBtnAutoPilotEnabled());
            btnAutoPilot.setText(model.getBtnAutoPilotText());

            btnDeepLearning.setEnabled(model.isBtnDeepLearningEnabled());
            btnDeepLearning.setText(model.getBtnDeepLearningText());

            lblStatus.setEnabled(model.isLblStatusEnabled());
            lblStatus.setText(model.getLblStatusText());
            lblStatus.setForeground(model.getLblStatusForeground());
        });
    }

}
