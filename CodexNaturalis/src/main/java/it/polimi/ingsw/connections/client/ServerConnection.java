package it.polimi.ingsw.connections.client;

import java.awt.*;
import java.io.IOException;

public interface  ServerConnection {
    public void threadExceptionCallback(Exception e);
    public void close() throws IOException;


}
