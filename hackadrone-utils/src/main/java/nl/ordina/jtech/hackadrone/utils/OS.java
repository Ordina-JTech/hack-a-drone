package nl.ordina.jtech.hackadrone.utils;

public final class OS {

    public static String getOS() {
        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("win")) {
            OS = "win";
        } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            OS = "unix";
        } else if (OS.contains("osx") || OS.contains("mac")) {
            OS = "osx";
        }

        return OS;
    }

}
