package com.github.nberlijn.hackadrone.gui;

import com.github.nberlijn.hackadrone.CX10;
import com.github.nberlijn.hackadrone.DroneException;
import com.github.nberlijn.hackadrone.io.Keyboard;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainWindow extends JFrame implements ActionListener {

    private final CX10 cx10;

    private JPanel panel;
    private JButton btnConnect;
    private JButton btnControls;
    private JLabel lblStatus;

    private boolean isConnected = false;
    private boolean isControlled = false;

    MainWindow() {
        this.cx10 = new CX10();

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
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        new Thread(() -> {
            if (actionEvent.getSource() == btnConnect) {
                onConnectClicked();
            }

            if (actionEvent.getSource() == btnControls) {
                onControlsClicked();
            }
        }).start();
    }

    private void onConnectClicked() {
        MainWindowModel model;

        if (!isConnected) {
            try {
                model = getModel();
                model.setBtnConnectEnabled(false);
                model.setBtnConnectText("Connecting...");
                model.setLblStatusForeground(Color.ORANGE);
                model.setLblStatusText("Trying to establish a new connection...");
                setModel(model);

                cx10.connect();
                cx10.sendMessages();
                cx10.startHeartbeat();

                isConnected = true;

                model = getModel();
                model.setBtnConnectEnabled(true);
                model.setBtnConnectText("Disconnect");
                model.setLblStatusForeground(Color.GREEN);
                model.setLblStatusText("Connection successfully established");
                model.setBtnControlsEnabled(true);
                setModel(model);
            } catch (DroneException e) {
                model = getModel();
                model.setBtnConnectEnabled(true);
                model.setBtnConnectText("Connect");
                model.setLblStatusForeground(Color.RED);
                model.setLblStatusText("Connection failed!");
                setModel(model);
            }
        } else {
            try {
                model = getModel();
                model.setBtnConnectEnabled(false);
                model.setBtnConnectText("Disconnecting...");
                model.setLblStatusForeground(Color.ORANGE);
                model.setLblStatusText("Trying to disconnect...");
                setModel(model);

                cx10.stopHeartbeat();
                cx10.disconnect();

                isConnected = false;

                model = getModel();
                model.setBtnConnectEnabled(true);
                model.setBtnConnectText("Connect");
                model.setLblStatusForeground(Color.GREEN);
                model.setLblStatusText("Disconnection successful");
                model.setBtnControlsEnabled(false);
                setModel(model);
            } catch (DroneException e) {
                model = getModel();
                model.setBtnConnectEnabled(true);
                model.setBtnConnectText("Disconnect");
                model.setLblStatusForeground(Color.RED);
                model.setLblStatusText("Disconnection failed!");
                setModel(model);
            }
        }
    }

    private void onControlsClicked() {
        MainWindowModel model;

        if (!isControlled) {
            try {
                cx10.startControls(new Keyboard(KeyboardFocusManager.getCurrentKeyboardFocusManager()));
                isControlled = true;
                model = getModel();
                setModel(model);
            } catch (IOException e) {
                model = getModel();
                setModel(model);
            }
        } else {
            try {
                cx10.stopControls();
            } catch (DroneException e) {
                e.printStackTrace();
            }

            isControlled = false;
            model = getModel();
            setModel(model);
        }
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

    private void setModel(final MainWindowModel model) {
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
