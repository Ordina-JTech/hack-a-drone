package com.github.nberlijn.hackadrone.gui;

import com.github.nberlijn.hackadrone.CX10;
import com.github.nberlijn.hackadrone.Drone;
import com.github.nberlijn.hackadrone.DroneException;
import com.github.nberlijn.hackadrone.io.Keyboard;
import com.github.nberlijn.hackadrone.utils.ANSI;
import com.github.nberlijn.hackadrone.utils.Color;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public final class MainWindow extends JFrame implements Frame, ActionListener, Window {

    private final Drone cx10 = new CX10();

    private JPanel panel;
    private JButton btnConnect;
    private JButton btnControls;
    private JLabel lblStatus;

    private boolean isConnected = false;
    private boolean isControlled = false;

    MainWindow() {
        init();
    }

    @Override
    public void init() {
        btnConnect.setEnabled(true);
        btnControls.setEnabled(false);
        lblStatus.setEnabled(true);

        btnConnect.addActionListener(this);
        btnControls.addActionListener(this);

        add(panel);
        setTitle("CX-10WD-TX");
        setPreferredSize(new Dimension(400,150));
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

    private void connect() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to establish a new connection..." + ANSI.RESET);

            model = getModel();
            model.setBtnConnectEnabled(false);
            model.setBtnConnectText("Connecting...");
            model.setLblStatusForeground(Color.YELLOW);
            model.setLblStatusText("Trying to establish a new connection...");
            updateModel(model);

            cx10.connect();
            cx10.sendMessages();
            cx10.startHeartbeat();

            model = getModel();
            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Disconnect");
            model.setLblStatusForeground(Color.GREEN);
            model.setLblStatusText("Connection successfully established");
            model.setBtnControlsEnabled(true);
            updateModel(model);

            System.out.println(ANSI.GREEN + "Connection successfully established" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();
            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Connect");
            model.setLblStatusForeground(Color.RED);
            model.setLblStatusText("Connection failed!");
            updateModel(model);

            System.out.println(ANSI.RED + "Connection failed!" + ANSI.RESET);
        }

        isConnected = true;
    }

    private void disconnect() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to disconnect..." + ANSI.RESET);

            model = getModel();
            model.setBtnConnectEnabled(false);
            model.setBtnConnectText("Disconnecting...");
            model.setLblStatusForeground(Color.YELLOW);
            model.setLblStatusText("Trying to disconnect...");
            updateModel(model);

            cx10.stopHeartbeat();
            cx10.disconnect();

            model = getModel();
            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Connect");
            model.setLblStatusForeground(Color.GREEN);
            model.setLblStatusText("Disconnection successful");
            model.setBtnControlsEnabled(false);
            updateModel(model);

            System.out.println(ANSI.GREEN + "Disconnection successful" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();
            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Disconnect");
            model.setLblStatusForeground(Color.RED);
            model.setLblStatusText("Disconnection failed!");
            updateModel(model);

            System.out.println(ANSI.RED + "Disconnection failed!" + ANSI.RESET);
        }

        isConnected = false;
    }

    private void startControls() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to start the controls..." + ANSI.RESET);

            model = getModel();
            model.setBtnControlsEnabled(false);
            model.setBtnControlsText("Starting controls...");
            model.setLblStatusForeground(Color.YELLOW);
            model.setLblStatusText("Trying to start the controls...");
            updateModel(model);

            cx10.startControls(new Keyboard(KeyboardFocusManager.getCurrentKeyboardFocusManager()));

            model = getModel();
            model.setBtnControlsEnabled(true);
            model.setBtnControlsText("Stop Controls");
            model.setLblStatusForeground(Color.GREEN);
            model.setLblStatusText("Controls successfully started");
            updateModel(model);

            System.out.println(ANSI.GREEN + "Controls successfully started" + ANSI.RESET);
        } catch (IOException e) {
            model = getModel();
            model.setBtnControlsEnabled(true);
            model.setBtnControlsText("Start Controls");
            model.setLblStatusForeground(Color.GREEN);
            model.setLblStatusText("Starting the controls failed!");
            updateModel(model);

            System.out.println(ANSI.RED + "Starting the controls failed!" + ANSI.RESET);
        }

        isControlled = true;
    }

    private void stopControls() {
        MainWindowModel model;

        try {
            System.out.println(ANSI.YELLOW + "Trying to stop the controls..." + ANSI.RESET);

            model = getModel();
            model.setBtnConnectEnabled(false);
            model.setBtnConnectText("Stopping controls...");
            model.setLblStatusForeground(Color.YELLOW);
            model.setLblStatusText("Trying to stop the controls...");
            updateModel(model);

            cx10.stopControls();

            model = getModel();
            model.setBtnControlsEnabled(true);
            model.setBtnControlsText("Start Controls");
            model.setLblStatusForeground(Color.GREEN);
            model.setLblStatusText("Controls successfully stopped");
            updateModel(model);

            System.out.println(ANSI.GREEN + "Controls successfully stopped" + ANSI.RESET);
        } catch (DroneException e) {
            model = getModel();
            model.setBtnControlsEnabled(true);
            model.setBtnControlsText("Stop Controls");
            model.setLblStatusForeground(Color.RED);
            model.setLblStatusText("Stopping the controls failed!");
            updateModel(model);

            System.out.println(ANSI.RED + "Stopping the controls failed!" + ANSI.RESET);
        }

        isControlled = false;
    }

    private MainWindowModel getModel() {
        MainWindowModel model = new MainWindowModel();

        model.setBtnConnectEnabled(btnConnect.isEnabled());
        model.setBtnConnectText(btnConnect.getText());

        model.setBtnControlsEnabled(btnControls.isEnabled());
        model.setBtnControlsText(btnControls.getText());

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

            lblStatus.setEnabled(model.isLblStatusEnabled());
            lblStatus.setText(model.getLblStatusText());
            lblStatus.setForeground(model.getLblStatusForeground());
        });
    }

}
