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
    private Color lblStatusForeground;
    private String lblStatusText;

    MainWindowModel() {

    }

    private MainWindowModel(boolean btnConnectEnabled, String btnConnectText, boolean btnControlsEnabled, String btnControlsText, boolean btnCameraEnabled, String btnCameraText, boolean btnRecorderEnabled, String btnRecorderText, boolean btnAiEnabled, String btnAiText, boolean lblStatusEnabled, Color lblStatusForeground, String lblStatusText) {
        this.btnConnectEnabled = btnConnectEnabled;
        this.btnConnectText = btnConnectText;
        this.btnControlsEnabled = btnControlsEnabled;
        this.btnControlsText = btnControlsText;
        this.btnCameraEnabled = btnCameraEnabled;
        this.btnCameraText = btnCameraText;
        this.btnRecorderEnabled = btnRecorderEnabled;
        this.btnRecorderText = btnRecorderText;
        this.btnAiEnabled = btnAiEnabled;
        this.btnAiText = btnAiText;
        this.lblStatusEnabled = lblStatusEnabled;
        this.lblStatusForeground = lblStatusForeground;
        this.lblStatusText = lblStatusText;
    }

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

    void setBtnConnectContext(boolean btnConnectEnabled, String btnConnectText) {
        this.btnConnectEnabled = btnConnectEnabled;
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

    void setBtnControlsContext(boolean btnControlsEnabled, String btnControlsText) {
        this.btnControlsEnabled = btnControlsEnabled;
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

    void setBtnCameraContext(boolean btnCameraEnabled, String btnCameraText) {
        this.btnCameraEnabled = btnCameraEnabled;
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

    void setBtnRecorderContext(boolean btnRecorderEnabled, String btnRecorderText) {
        this.btnRecorderEnabled = btnRecorderEnabled;
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

    void setBtnAiContext(boolean btnAiEnabled, String btnAiText) {
        this.btnAiEnabled = btnAiEnabled;
        this.btnAiText = btnAiText;
    }

    boolean isLblStatusEnabled() {
        return lblStatusEnabled;
    }

    void setLblStatusEnabled(boolean lblStatusEnabled) {
        this.lblStatusEnabled = lblStatusEnabled;
    }

    Color getLblStatusForeground() {
        return lblStatusForeground;
    }

    void setLblStatusForeground(Color lblStatusForeground) {
        this.lblStatusForeground = lblStatusForeground;
    }

    String getLblStatusText() {
        return lblStatusText;
    }

    void setLblStatusText(String lblStatusText) {
        this.lblStatusText = lblStatusText;
    }

    void setLblStatusContext(Color lblStatusForeground, String lblStatusText) {
        this.lblStatusForeground = lblStatusForeground;
        this.lblStatusText = lblStatusText;
    }

    MainWindowModel copy() {
        return new MainWindowModel(
                btnConnectEnabled,
                btnConnectText,
                btnControlsEnabled,
                btnControlsText,
                btnCameraEnabled,
                btnCameraText,
                btnRecorderEnabled,
                btnRecorderText,
                btnAiEnabled,
                btnAiText,
                lblStatusEnabled,
                lblStatusForeground,
                lblStatusText
        );
    }

}
