package nl.ordina.jtech.hackadrone.gui;

import java.awt.*;

class MainWindowModel {

    private boolean btnConnectEnabled;
    private String btnConnectText;
    private boolean btnControlsEnabled;
    private String btnControlsText;
    private boolean btnCameraEnabled;
    private String btnCameraText;
    private boolean btnRecorderEnabled;
    private String btnRecorderText;
    private boolean btnAiEnabled;
    private String btnAiText;
    private boolean lblStatusEnabled;
    private String lblStatusText;
    private Color lblStatusForeground;

    boolean isBtnConnectEnabled() {
        return btnConnectEnabled;
    }

    void setBtnConnectEnabled(boolean btnConnectEnabled) {
        this.btnConnectEnabled = btnConnectEnabled;
    }

    String getBtnConnectText() {
        return btnConnectText;
    }

    void setBtnConnectText(String btnConnectText) {
        this.btnConnectText = btnConnectText;
    }

    boolean isBtnControlsEnabled() {
        return btnControlsEnabled;
    }

    void setBtnControlsEnabled(boolean btnControlsEnabled) {
        this.btnControlsEnabled = btnControlsEnabled;
    }

    String getBtnControlsText() {
        return btnControlsText;
    }

    void setBtnControlsText(String btnControlsText) {
        this.btnControlsText = btnControlsText;
    }

    boolean isBtnCameraEnabled() {
        return btnCameraEnabled;
    }

    void setBtnCameraEnabled(boolean btnCameraEnabled) {
        this.btnCameraEnabled = btnCameraEnabled;
    }

    String getBtnCameraText() {
        return btnCameraText;
    }

    void setBtnCameraText(String btnCameraText) {
        this.btnCameraText = btnCameraText;
    }

    boolean isBtnRecorderEnabled() {
        return btnRecorderEnabled;
    }

    void setBtnRecorderEnabled(boolean btnRecorderEnabled) {
        this.btnRecorderEnabled = btnRecorderEnabled;
    }

    String getBtnRecorderText() {
        return btnRecorderText;
    }

    void setBtnRecorderText(String btnRecorderText) {
        this.btnRecorderText = btnRecorderText;
    }

    boolean isBtnAiEnabled() {
        return btnAiEnabled;
    }

    void setBtnAiEnabled(boolean btnAiEnabled) {
        this.btnAiEnabled = btnAiEnabled;
    }

    String getBtnAiText() {
        return btnAiText;
    }

    void setBtnAiText(String btnAiText) {
        this.btnAiText = btnAiText;
    }

    boolean isLblStatusEnabled() {
        return lblStatusEnabled;
    }

    void setLblStatusEnabled(boolean lblStatusEnabled) {
        this.lblStatusEnabled = lblStatusEnabled;
    }

    String getLblStatusText() {
        return lblStatusText;
    }

    void setLblStatusText(String lblStatusText) {
        this.lblStatusText = lblStatusText;
    }

    Color getLblStatusForeground() {
        return lblStatusForeground;
    }

    void setLblStatusForeground(Color lblStatusForeground) {
        this.lblStatusForeground = lblStatusForeground;
    }

}
