package it.polimi.ingsw.connections.server;

import java.io.IOException;

public interface ClientConnection {
    boolean getStatus();
    void close() throws IOException;
    String getRemoteAddr();

}
