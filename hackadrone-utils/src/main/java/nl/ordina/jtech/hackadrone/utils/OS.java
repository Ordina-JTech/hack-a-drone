package nl.ordina.jtech.hackadrone.utils;

/**
 * Class representing OS utils.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class OS {

    /**
     * Gets the used OS.
     *
     * @return the used os
     */
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
