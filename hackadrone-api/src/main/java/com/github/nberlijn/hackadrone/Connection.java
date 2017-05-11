package com.github.nberlijn.hackadrone;

public interface Connection {

    void connect() throws Exception;

    void disconnect() throws Exception;

}
