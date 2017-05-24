package nl.ordina.jtech.hackadrone.gui;

import java.awt.*;

class MainWindowModel {

    private boolean btnConnectEnabled;
    private String btnConnectText;
    private boolean btnControlsEnabled;
    private String btnControlsText;
    private boolean btnVideoEnabled;
    private String btnVideoText;
    private boolean btnRecordVideoEnabled;
    private String btnRecordVideoText;
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

    boolean isBtnVideoEnabled() {
        return btnVideoEnabled;
    }

    void setBtnVideoEnabled(boolean btnVideoEnabled) {
        this.btnVideoEnabled = btnVideoEnabled;
    }

    String getBtnVideoText() {
        return btnVideoText;
    }

    void setBtnVideoText(String btnVideoText) {
        this.btnVideoText = btnVideoText;
    }

    boolean isBtnRecordVideoEnabled() {
        return btnRecordVideoEnabled;
    }

    void setBtnRecordVideoEnabled(boolean btnRecordVideoEnabled) {
        this.btnRecordVideoEnabled = btnRecordVideoEnabled;
    }

    String getBtnRecordVideoText() {
        return btnRecordVideoText;
    }

    void setBtnRecordVideoText(String btnRecordVideoText) {
        this.btnRecordVideoText = btnRecordVideoText;
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
