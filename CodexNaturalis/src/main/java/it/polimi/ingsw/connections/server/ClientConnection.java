package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.controller.CardInfo;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface ClientConnection extends Runnable{
    public void threadExceptionCallback(Exception e);
    public void close() throws IOException;
    public boolean getStatus();
    public String getRemoteAddr();

    public void validUsername() throws IOException;
    public void invalidUsername() throws IOException;
    public void lobbyExists(ArrayList<String> idList) throws IOException;
    public void joinLobbySuccess() throws IOException;
    public void lobbyFull() throws IOException;
    public void lobbyDoesNotExist() throws IOException;
    // TODO: playerNumInvalid() throws IOException
    public void playerJoined(String username) throws IOException;
    public void gameStarted() throws IOException;
    public void privateGoalChosen() throws IOException;
    public void initTurn(ArrayList<CardInfo> hand,
                         ArrayList<CardInfo> resourceDeck,
                         ArrayList<CardInfo> goldDeck,
                         ArrayList<Point> availablePositions,
                         int currTurn,
                         boolean isLastTurn,
                         ArrayList<CardInfo> board
    ) throws IOException;
    public void placeCardSuccess(int cardsPoints, int goalsPoints) throws IOException;
    public void placeCardFailure() throws IOException;
    public void drawSuccess(ArrayList<CardInfo> hand) throws IOException;
    public void sendStatus(ArrayList<CardInfo> resourceDeck,
                           ArrayList<CardInfo> goldDeck,
                           ArrayList<CardInfo> board,
                           int cardsPoints
    ) throws IOException;
    public void gameEnded(HashMap<String, Integer> leaderboard) throws IOException;
}
