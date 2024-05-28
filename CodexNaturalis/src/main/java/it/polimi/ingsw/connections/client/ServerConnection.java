package it.polimi.ingsw.connections.client;

import it.polimi.ingsw.connections.ConnectionStatus;

import java.awt.*;
import java.io.IOException;

public interface  ServerConnection {
    public void threadExceptionCallback(String e) throws IOException;
    public void close() throws IOException;
    public ConnectionStatus getStatus() throws IOException;
}
