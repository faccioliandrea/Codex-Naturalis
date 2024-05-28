package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;

import java.io.IOException;
import java.util.HashMap;

public interface ClientConnection {
    void close() throws IOException;
    String getRemoteAddr();
    void noOtherPlayerConnected() throws IOException;
    void setOffline();
    ConnectionStatus getStatus();

    void playerJoined(String username) throws IOException;

    void initTurn(TurnInfo turnInfo) throws IOException;

    void otherPlayerTurnMessage(String currentPlayer)throws IOException;

    void gameEnded(HashMap<String, Integer> leaderboard)throws IOException;

    void gameStarted(StarterData starterData) throws IOException;

    void sendStatus(GameStateInfo gameStateInfo)throws IOException;

    void playerDisconnected(String username, boolean gameStarted)throws IOException;

    void playerReconnected(String username)throws IOException;

    void reconnectionState(GameStateInfo gameStateInfo)throws IOException;
    void sendChatMessage(ChatMessageData msg) throws IOException;
}
