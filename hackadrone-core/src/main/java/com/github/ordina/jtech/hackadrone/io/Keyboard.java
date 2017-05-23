package com.github.ordina.jtech.hackadrone.io;

import com.github.ordina.jtech.hackadrone.models.Command;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;

public final class Keyboard implements Device, KeyEventDispatcher {

    private final KeyboardFocusManager focusManager;
    private CommandListener commandListener;
    private Command command = new Command();

    public Keyboard(KeyboardFocusManager focusManager) {
        this.focusManager = focusManager;
    }

    @Override
    public void start() {
        focusManager.addKeyEventDispatcher(this);
    }

    @Override
    public void stop() {
        focusManager.removeKeyEventDispatcher(this);
    }

    @Override
    public void setListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KEY_PRESSED) {
            onKeyEvent(e, true);
        } else if (e.getID() == KEY_RELEASED) {
            onKeyEvent(e, false);
        }

        return false;
    }

    private void onKeyEvent(KeyEvent keyEvent, boolean isPressed) {
        int value = isPressed ? 127 : 0;
        boolean input = true;

        switch (keyEvent.getKeyCode()) {
            case VK_W:
                command.setPitch(value);
                break;
            case VK_S:
                command.setPitch(-value);
                break;
            case VK_A:
                command.setRoll(-value);
                break;
            case VK_D:
                command.setRoll(value);
                break;
            case VK_Q:
                command.setYaw(-value);
                break;
            case VK_E:
                command.setYaw(value);
                break;
            case VK_LEFT:
                command.setTakeOff(isPressed);
                break;
            case VK_RIGHT:
                command.setLand(isPressed);
                break;
            case VK_UP:
                command.setThrottle(value);
                break;
            case VK_DOWN:
                command.setThrottle(-value);
                break;
            default:
                input = false;
        }

        if (commandListener != null && input) {
            commandListener.onCommandReceived(command);
        }

        keyEvent.consume();
    }

}
