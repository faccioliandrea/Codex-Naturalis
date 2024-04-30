package it.polimi.ingsw.connections.client;

import java.awt.*;
import java.io.IOException;

public interface  ServerConnection extends Runnable {
    public void threadExceptionCallback(Exception e);
    public void close() throws IOException;

    // messages
    public void loginRequest(String username) throws IOException;
    public void getLobby(String username) throws IOException;
    public void joinLobby(String username, String id) throws IOException;
    public void createLobbyAndJoin(String username, int playerNum) throws IOException;
    public void choosePrivateGoal(String username, int index) throws IOException;
    public void initTurnAck(String username) throws IOException;
    public void placeCard(String username, String cardId, Point pos, boolean side) throws IOException;
    public void drawResource(String username, int index) throws IOException;
    public void drawGold(String username, int index) throws IOException;
    public void endTurn(String username) throws IOException;
}
