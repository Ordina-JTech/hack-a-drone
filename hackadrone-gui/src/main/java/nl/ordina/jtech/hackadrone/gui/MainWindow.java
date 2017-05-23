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

public final class MainWindow extends JFrame implements nl.ordina.jtech.hackadrone.gui.Frame, ActionListener, ClickEvent {

    private final Drone cx10 = new CX10();

    private JPanel panel;
    private JButton btnConnect;
    private JButton btnControls;
    private JButton btnVideo;
    private JLabel lblStatus;

    private boolean isConnected = false;
    private boolean isControlled = false;
    private boolean isVideoStreaming = false;

    MainWindow() {
        init();
    }

    @Override
    public void init() {
        btnConnect.setEnabled(true);
        btnControls.setEnabled(false);
        btnVideo.setEnabled(false);

        lblStatus.setEnabled(true);

        btnConnect.addActionListener(this);
        btnControls.addActionListener(this);
        btnVideo.addActionListener(this);

        add(panel);
        setTitle(cx10.getName());
        setPreferredSize(new Dimension(550,150));
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
            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Connection successfully established");

            model.setBtnControlsEnabled(true);
            model.setBtnVideoEnabled(true);

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

            stopVideo();
            stopControls();

            cx10.stopHeartbeat();
            cx10.disconnect();
            isConnected = false;

            model = getModel();

            model.setBtnVideoEnabled(false);
            model.setBtnVideoText("Start Video");

            model.setBtnControlsEnabled(false);
            model.setBtnControlsText("Start Controls");

            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Connect");
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
            model.setBtnControlsEnabled(true);
            updateModel(model);

            System.out.println(ANSI.GREEN + "Video stream successfully started" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();
            model.setBtnVideoEnabled(true);
            model.setBtnVideoText("Start Video");
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
            model.setLblStatusForeground(SpecialColor.GREEN);
            model.setLblStatusText("Video stream successfully stopped");
            model.setBtnControlsEnabled(true);
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

    private MainWindowModel getModel() {
        MainWindowModel model = new MainWindowModel();

        model.setBtnConnectEnabled(btnConnect.isEnabled());
        model.setBtnConnectText(btnConnect.getText());

        model.setBtnControlsEnabled(btnControls.isEnabled());
        model.setBtnControlsText(btnControls.getText());

        model.setBtnVideoEnabled(btnVideo.isEnabled());
        model.setBtnVideoText(btnVideo.getText());

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

            lblStatus.setEnabled(model.isLblStatusEnabled());
            lblStatus.setText(model.getLblStatusText());
            lblStatus.setForeground(model.getLblStatusForeground());
        });
    }

}
