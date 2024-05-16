package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.ConnectionStatus;

import java.io.IOException;

public interface ClientConnection {
    ConnectionStatus getStatus();
    void close() throws IOException;
    String getRemoteAddr();

    void setStatus(ConnectionStatus connectionStatus);
}
