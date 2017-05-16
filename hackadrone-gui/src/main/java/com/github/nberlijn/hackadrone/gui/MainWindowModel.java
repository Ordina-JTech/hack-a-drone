package com.github.nberlijn.hackadrone.gui;

import java.awt.*;

class MainWindowModel {

    private boolean btnConnectEnabled;
    private boolean btnControlsEnabled;
    private boolean lblStatusEnabled;
    private String btnConnectText;
    private String btnControlsText;
    private String lblStatusText;
    private Color lblStatusForeground;

    boolean isBtnConnectEnabled() {
        return btnConnectEnabled;
    }

    void setBtnConnectEnabled(boolean btnConnectEnabled) {
        this.btnConnectEnabled = btnConnectEnabled;
    }

    boolean isBtnControlsEnabled() {
        return btnControlsEnabled;
    }

    void setBtnControlsEnabled(boolean btnControlsEnabled) {
        this.btnControlsEnabled = btnControlsEnabled;
    }

    boolean isLblStatusEnabled() {
        return lblStatusEnabled;
    }

    void setLblStatusEnabled(boolean lblStatusEnabled) {
        this.lblStatusEnabled = lblStatusEnabled;
    }

    String getBtnConnectText() {
        return btnConnectText;
    }

    void setBtnConnectText(String btnConnectText) {
        this.btnConnectText = btnConnectText;
    }

    String getBtnControlsText() {
        return btnControlsText;
    }

    void setBtnControlsText(String btnControlsText) {
        this.btnControlsText = btnControlsText;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MainWindowModel that = (MainWindowModel) o;

        if (btnConnectEnabled != that.btnConnectEnabled)
            return false;
        if (btnControlsEnabled != that.btnControlsEnabled)
            return false;
        if (btnConnectText != null ? !btnConnectText.equals(that.btnConnectText) : that.btnConnectText != null)
            return false;
        if (btnControlsText != null ? !btnControlsText.equals(that.btnControlsText) : that.btnControlsText != null)
            return false;

        return lblStatusText != null ? lblStatusText.equals(that.lblStatusText) : that.lblStatusText == null;
    }

    @Override
    public int hashCode() {
        int result = (btnConnectEnabled ? 1 : 0);

        result = 31 * result + (btnControlsEnabled ? 1 : 0);
        result = 31 * result + (btnConnectText != null ? btnConnectText.hashCode() : 0);
        result = 31 * result + (btnControlsText != null ? btnControlsText.hashCode() : 0);
        result = 31 * result + (lblStatusText != null ? lblStatusText.hashCode() : 0);

        return result;
    }

}
