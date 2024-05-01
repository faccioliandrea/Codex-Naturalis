package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.controller.CardInfo;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface ClientConnection {
    boolean getStatus();
    void close() throws IOException;
    String getRemoteAddr();

}
