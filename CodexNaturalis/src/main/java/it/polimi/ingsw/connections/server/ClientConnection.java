package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.ConnectionStatus;

import java.io.IOException;

public interface ClientConnection {
    void close() throws IOException;
    String getRemoteAddr() throws IOException;

}
