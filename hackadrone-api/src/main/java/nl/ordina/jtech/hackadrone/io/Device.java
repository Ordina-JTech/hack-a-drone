package nl.ordina.jtech.hackadrone.io;

public interface Device extends Handler {

    void setListener(CommandListener commandListener);

}
