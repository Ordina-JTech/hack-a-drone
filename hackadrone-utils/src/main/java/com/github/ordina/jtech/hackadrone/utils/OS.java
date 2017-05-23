package com.github.ordina.jtech.hackadrone.utils;

public final class OS {

    public static String getOS() {
        String OS = System.getProperty("os.name").toLowerCase();

        switch (OS) {
            case "nix":
            case "nux":
            case "aix":
                OS = "unix";
                break;
            case "mac":
                OS = "mac";
                break;
            case "win":
                OS = "win";
                break;
            default:
                OS = "unix";
                break;
        }

        return OS;
    }

}
