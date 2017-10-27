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

import java.awt.Color;

/**
 * Class representing a main window model for a main window.
 */
final class MainWindowModel {

    /**
     * The status of the connect button.
     */
    private boolean btnConnectEnabled;

    /**
     * The text of the connect button.
     */
    private String btnConnectText;

    /**
     * The status of the controls button.
     */
    private boolean btnControlsEnabled;

    /**
     * The text of the controls button.
     */
    private String btnControlsText;

    /**
     * The status of the camera button.
     */
    private boolean btnCameraEnabled;

    /**
     * The text of the camera button.
     */
    private String btnCameraText;

    /**
     * The status of the recorder button.
     */
    private boolean btnRecorderEnabled;

    /**
     * The text of the recorder button.
     */
    private String btnRecorderText;

    /**
     * The status of the AutoPilot button.
     */
    private boolean btnAutoPilotEnabled;

    /**
     * The text of the AutoPilot button.
     */
    private String btnAutoPilotText;

    /**
     * The status of the DeepLearning button.
     */
    private boolean btnDeepLearningEnabled;

    /**
     * The text of the AutoPilot button.
     */
    private String btnDeepLearningText;

    /**
     * The status of the status label.
     */
    private boolean lblStatusEnabled;

    /**
     * The foreground color of the status label.
     */
    private Color lblStatusForeground;

    /**
     * The text of the status label.
     */
    private String lblStatusText;

    /**
     * A main window model constructor.
     */
    MainWindowModel() {

    }

    /**
     * A main window model constructor.
     *
     * @param btnConnectEnabled the status of the connect button
     * @param btnConnectText the text of the connect button
     * @param btnControlsEnabled the status of the controls button
     * @param btnControlsText the text of the controls button
     * @param btnCameraEnabled the status of the camera button
     * @param btnCameraText the text of the camera button
     * @param btnRecorderEnabled the status of the recorder button
     * @param btnRecorderText the text of the recorder button
     * @param btnAutoPilotEnabled the status of the AutoPilot button
     * @param btnAutoPilotText the text of the AutoPilot button
     * @param btnDeepLearningEnabled the status of the DeepLearning button
     * @param btnDeepLearningText the text of the DeepLearning button
     * @param lblStatusEnabled the status of the status label
     * @param lblStatusForeground the foreground color of the status label
     * @param lblStatusText the text of the status label
     */
    private MainWindowModel(boolean btnConnectEnabled, String btnConnectText, boolean btnControlsEnabled, String btnControlsText, boolean btnCameraEnabled, String btnCameraText,
            boolean btnRecorderEnabled, String btnRecorderText, boolean btnAutoPilotEnabled, String btnAutoPilotText, boolean btnDeepLearningEnabled, String btnDeepLearningText,
            boolean lblStatusEnabled, Color lblStatusForeground, String lblStatusText) {
        this.btnConnectEnabled = btnConnectEnabled;
        this.btnConnectText = btnConnectText;
        this.btnControlsEnabled = btnControlsEnabled;
        this.btnControlsText = btnControlsText;
        this.btnCameraEnabled = btnCameraEnabled;
        this.btnCameraText = btnCameraText;
        this.btnRecorderEnabled = btnRecorderEnabled;
        this.btnRecorderText = btnRecorderText;
        this.btnAutoPilotEnabled = btnAutoPilotEnabled;
        this.btnAutoPilotText = btnAutoPilotText;
        this.btnDeepLearningEnabled = btnDeepLearningEnabled;
        this.btnDeepLearningText = btnDeepLearningText;
        this.lblStatusEnabled = lblStatusEnabled;
        this.lblStatusForeground = lblStatusForeground;
        this.lblStatusText = lblStatusText;
    }

    /**
     * Gets the status of the connect button.
     *
     * @return the status of the connect button
     */
    boolean isBtnConnectEnabled() {
        return btnConnectEnabled;
    }

    /**
     * Sets the status of the connect button.
     *
     * @param btnConnectEnabled the status of the connect button to set
     */
    void setBtnConnectEnabled(boolean btnConnectEnabled) {
        this.btnConnectEnabled = btnConnectEnabled;
    }

    /**
     * Gets the text of the connect button.
     *
     * @return the text of the connect button
     */
    String getBtnConnectText() {
        return btnConnectText;
    }

    /**
     * Sets the text of the connect button.
     *
     * @param btnConnectText the text of the connect button to set
     */
    void setBtnConnectText(String btnConnectText) {
        this.btnConnectText = btnConnectText;
    }

    /**
     * Sets the context of the connect button
     *
     * @param btnConnectEnabled the status of the connect button to set
     * @param btnConnectText the text of the connect button to set
     */
    void setBtnConnectContext(boolean btnConnectEnabled, String btnConnectText) {
        this.btnConnectEnabled = btnConnectEnabled;
        this.btnConnectText = btnConnectText;
    }

    /**
     * Gets the status of the controls button.
     *
     * @return the status of the controls button
     */
    boolean isBtnControlsEnabled() {
        return btnControlsEnabled;
    }

    /**
     * Sets the status of the controls button.
     *
     * @param btnControlsEnabled the status of the controls button to set
     */
    void setBtnControlsEnabled(boolean btnControlsEnabled) {
        this.btnControlsEnabled = btnControlsEnabled;
    }

    /**
     * Gets the text of the controls button.
     *
     * @return the text of the controls button
     */
    String getBtnControlsText() {
        return btnControlsText;
    }

    /**
     * Sets the text of the controls button.
     *
     * @param btnControlsText the text of the controls button to set
     */
    void setBtnControlsText(String btnControlsText) {
        this.btnControlsText = btnControlsText;
    }

    /**
     * Sets the context of the controls button
     *
     * @param btnControlsEnabled the status of the controls button to set
     * @param btnControlsText the text of the controls button to set
     */
    void setBtnControlsContext(boolean btnControlsEnabled, String btnControlsText) {
        this.btnControlsEnabled = btnControlsEnabled;
        this.btnControlsText = btnControlsText;
    }

    /**
     * Gets the status of the camera button.
     *
     * @return the status of the camera button
     */
    boolean isBtnCameraEnabled() {
        return btnCameraEnabled;
    }

    /**
     * Sets the status of the camera button.
     *
     * @param btnCameraEnabled the status of the camera button to set
     */
    void setBtnCameraEnabled(boolean btnCameraEnabled) {
        this.btnCameraEnabled = btnCameraEnabled;
    }

    /**
     * Gets the text of the camera button.
     *
     * @return the text of the camera button
     */
    String getBtnCameraText() {
        return btnCameraText;
    }

    /**
     * Sets the text of the camera button.
     *
     * @param btnCameraText the text of the camera button to set
     */
    void setBtnCameraText(String btnCameraText) {
        this.btnCameraText = btnCameraText;
    }

    /**
     * Sets the context of the camera button.
     *
     * @param btnCameraEnabled the status of the camera button to set
     * @param btnCameraText the text of the camera button to set
     */
    void setBtnCameraContext(boolean btnCameraEnabled, String btnCameraText) {
        this.btnCameraEnabled = btnCameraEnabled;
        this.btnCameraText = btnCameraText;
    }

    /**
     * Gets the status of the recorder button.
     *
     * @return the status of the recorder button
     */
    boolean isBtnRecorderEnabled() {
        return btnRecorderEnabled;
    }

    /**
     * Sets the status of the recorder button.
     *
     * @param btnRecorderEnabled the status of the recorder button to set
     */
    void setBtnRecorderEnabled(boolean btnRecorderEnabled) {
        this.btnRecorderEnabled = btnRecorderEnabled;
    }

    /**
     * Gets the text of the recorder button.
     *
     * @return the text of the recorder button
     */
    String getBtnRecorderText() {
        return btnRecorderText;
    }

    /**
     * Sets the text of the recorder button.
     *
     * @param btnRecorderText the text of the recorder text to set
     */
    void setBtnRecorderText(String btnRecorderText) {
        this.btnRecorderText = btnRecorderText;
    }

    /**
     * Sets the context of the recorder button.
     *
     * @param btnRecorderEnabled the status of the recorder button to set
     * @param btnRecorderText the text of the recorder button to set
     */
    void setBtnRecorderContext(boolean btnRecorderEnabled, String btnRecorderText) {
        this.btnRecorderEnabled = btnRecorderEnabled;
        this.btnRecorderText = btnRecorderText;
    }

    /**
     * Gets the status of the AutoPilot button.
     *
     * @return the status of the AutoPilot button
     */
    boolean isBtnAutoPilotEnabled() {
        return btnAutoPilotEnabled;
    }

    /**
     * Sets the status of the AutoPilot button.
     *
     * @param btnAutoPilotEnabled the status of the AutoPilot button to set
     */
    void setBtnAutoPilotEnabled(boolean btnAutoPilotEnabled) {
        this.btnAutoPilotEnabled = btnAutoPilotEnabled;
    }

    /**
     * Gets the text of the AutoPilot button.
     *
     * @return the text of the AutoPilot button
     */
    String getBtnAutoPilotText() {
        return btnAutoPilotText;
    }

    /**
     * Sets the text of the AutoPilot button.
     *
     * @param btnAutoPilotText the text of the AutoPilot button to set
     */
    void setBtnAutoPilotText(String btnAutoPilotText) {
        this.btnAutoPilotText = btnAutoPilotText;
    }

    /**
     * Sets the context of the AutoPilot button.
     *
     * @param btnAutoPilotEnabled the status of the AutoPilot button to set
     * @param btnAutoPilotText the text of the AutoPilot button to set
     */
    void setBtnAutoPilotContext(boolean btnAutoPilotEnabled, String btnAutoPilotText) {
        this.btnAutoPilotEnabled = btnAutoPilotEnabled;
        this.btnAutoPilotText = btnAutoPilotText;
    }

    /**
     * Get the status of the DeepLearning button.
     *
     * @return the status of the DeepLearning button.
     */
    boolean isBtnDeepLearningEnabled() {
        return btnDeepLearningEnabled;
    }

    /**
     * Sets the status of the DeepLearning button.
     *
     * @param btnDeepLearningEnabled the status of the DeepLearning button to set.
     */
    void setBtnDeepLearningEnabled(boolean btnDeepLearningEnabled) {
        this.btnDeepLearningEnabled = btnDeepLearningEnabled;
    }

    /**
     * Get the text of the DeepLearning button.
     *
     * @return the text of the DeepLearning button.
     */
    String getBtnDeepLearningText() {
        return btnDeepLearningText;
    }

    /**
     * Sets the text of the DeepLearning button.
     *
     * @param btnDeepLearningText the text of the DeepLearning button to set.
     */
    void setBtnDeepLearningText(String btnDeepLearningText) {
        this.btnDeepLearningText = btnDeepLearningText;
    }

    /**
     * Sets the context of the DeepLearning button.
     *
     * @param btnDeepLearningEnabled the status of the DeepLearning button to set.
     * @param btnDeepLearningText    the text of the DeepLearning button to set.
     */
    void setBtnDeepLearningContext(boolean btnDeepLearningEnabled, String btnDeepLearningText) {
        this.btnDeepLearningEnabled = btnDeepLearningEnabled;
        this.btnDeepLearningText = btnDeepLearningText;
    }

    /**
     * Gets the status of the status label.
     *
     * @return the status of the status label
     */
    boolean isLblStatusEnabled() {
        return lblStatusEnabled;
    }

    /**
     * Sets the status of the status label.
     *
     * @param lblStatusEnabled the status of the status label to set
     */
    void setLblStatusEnabled(boolean lblStatusEnabled) {
        this.lblStatusEnabled = lblStatusEnabled;
    }

    /**
     * Sets the foreground color of the status label.
     *
     * @return the foreground color of the status label
     */
    Color getLblStatusForeground() {
        return lblStatusForeground;
    }

    /**
     * Sets the the foreground color of the status label.
     *
     * @param lblStatusForeground the foreground color of the status label to set
     */
    void setLblStatusForeground(Color lblStatusForeground) {
        this.lblStatusForeground = lblStatusForeground;
    }

    /**
     * Gets the text of the status label.
     *
     * @return the text of the status label
     */
    String getLblStatusText() {
        return lblStatusText;
    }

    /**
     * Sets the text of the status label.
     *
     * @param lblStatusText the text of the status label to set
     */
    void setLblStatusText(String lblStatusText) {
        this.lblStatusText = lblStatusText;
    }

    /**
     * Sets the context of the status label.
     *
     * @param lblStatusForeground the status of the status label to set
     * @param lblStatusText the text of the status label to set
     */
    void setLblStatusContext(Color lblStatusForeground, String lblStatusText) {
        this.lblStatusForeground = lblStatusForeground;
        this.lblStatusText = lblStatusText;
    }

    /**
     * Copies the current model to a new model.
     *
     * @return a fresh copy of the current model
     */
    MainWindowModel copy() {
        return new MainWindowModel(
                btnConnectEnabled,
                btnConnectText,
                btnControlsEnabled,
                btnControlsText,
                btnCameraEnabled,
                btnCameraText,
                btnRecorderEnabled,
                btnRecorderText, btnAutoPilotEnabled, btnAutoPilotText, btnDeepLearningEnabled, btnDeepLearningText,
                lblStatusEnabled,
                lblStatusForeground,
                lblStatusText
        );
    }

}
