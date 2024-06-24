package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Interface for the connection to the client
 */
public interface ClientConnection {
    /**
    * Close the connection
    * @throws IOException if an error occurs while closing the connection
    */
    void close() throws IOException;

    /**
     * Get the IP address of the client
     */
    String getRemoteAddr();
    
    /**
     * Notify the client that no other player is connected
     * @throws IOException if an error occurs while sending the message
     */
    void noOtherPlayerConnected() throws IOException;

    /**
     * Sets the connection status to offline
     */
    void setOffline();

    /**
     * Get the connection status
     * @return the connection status
     */
    ConnectionStatus getStatus();

    /**
     * Notify the client that a player has joined the game
     * @param username the username of the player that joined the game
     * @throws IOException if an error occurs while sending the message
     */
    void playerJoined(String username) throws IOException;

    /**
     * Send the client the information about the current turn
     * @param turnInfo the information about the current turn
     * @throws IOException if an error occurs while sending the message
     */
    void initTurn(TurnInfo turnInfo) throws IOException;

    /**
     * Notify the client that another player's turn has started
     * @param currentPlayer the username of the player that is playing
     * @throws IOException if an error occurs while sending the message
     */
    void otherPlayerTurnMessage(String currentPlayer)throws IOException;

    /**
     * Notify the client that the game has ended
     * @param leaderboard the leaderboard of the game
     * @throws IOException if an error occurs while sending the message
     */
    void gameEnded(Map<String, Integer> leaderboard)throws IOException;

    /**
     * Notify the client that the game has started
     * @param starterData the data needed to start the game
     * @throws IOException if an error occurs while sending the message
     */
    void gameStarted(StarterData starterData) throws IOException;

    /**
     * Notify the client that the game has been paused
     * @throws IOException if an error occurs while sending the message
     */
    void sendStatus(GameStateInfo gameStateInfo)throws IOException;

    /**
     * Notify the client that a player has disconnected
     * @param username the username of the player that disconnected
     * @param gameStarted true if the game has started, false otherwise
     * @throws IOException if an error occurs while sending the message
     */
    void playerDisconnected(String username, boolean gameStarted)throws IOException;

    /**
     * Notify the client that a player has reconnected
     * @param username the username of the player that reconnected
     * @throws IOException if an error occurs while sending the message
     */
    void playerReconnected(String username)throws IOException;

    /**
     * Sends the new reconnected client the current game state
     * @throws IOException if an error occurs while sending the message
     */
    void reconnectionState(GameStateInfo gameStateInfo)throws IOException;

    /**
     * Sends a chat message to the client
     * @param msg the message to send
     * @throws IOException if an error occurs while sending the message
     */
    void sendChatMessage(ChatMessageData msg) throws IOException;
}
