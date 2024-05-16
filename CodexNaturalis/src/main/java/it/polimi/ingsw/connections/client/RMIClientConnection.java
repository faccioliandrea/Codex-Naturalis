package it.polimi.ingsw.connections.client;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;
import it.polimi.ingsw.connections.server.ClientConnection;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * This class is the RMI client connection class
 */
public class RMIClientConnection extends UnicastRemoteObject implements RMIClientConnectionInterface {
    private ConnectionBridge connectionBridge;

    /**
     * Constructor for the RMI client connection
     * @throws RemoteException if the connection fails
     */
    public RMIClientConnection() throws RemoteException {
    }


    @Override
    public void close() throws IOException {

    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    /**
     * Method to handle the joining of a player in the lobby
     * @param username the username of the player
     */
    @Override
    public void playerJoined(String username) {
        connectionBridge.playerJoinedLobby(username);
    }

    /**
     * Methot that sets the connection bridge
     * @param connectionBridge the connection bridge
     */
    @Override
    public void setBridge(ConnectionBridge connectionBridge) {
        this.connectionBridge = connectionBridge;
    }

    /**
     * Method to handle the start of the game
     * @param starterData the starter data: the hand, the private goals, the shared goals, the starter card and the users
     * @throws RemoteException if the connection fails
     */
    @Override
    public void gameStarted(StarterData starterData) throws RemoteException {
        connectionBridge.gameStarted(starterData);
    }

    /**
     * Method to communicate the turn of the other player
     * @param currentPlayer the current player
     * @throws RemoteException if the connection fails
     */
    @Override
    public void otherPlayerTurn(String currentPlayer) throws RemoteException {
        connectionBridge.OtherPlayerTurnMessage(currentPlayer);
    }

    /**
     * Method to initialize the turn
     * @param turnInfo the turn info: the player, the hand, the private goals, the shared goals, the round track, the draft pool, the tool cards, the round, the turn, the player's status
     * @throws RemoteException if the connection fails
     */
    @Override
    public void initTurn(TurnInfo turnInfo) throws RemoteException {
        connectionBridge.initTurn(turnInfo);
    }

    /**
     * Method to send the status of the game, recived at the end of each turn
     * @param gameStateInfo the game state info: the round track, the draft pool, the tool cards, the round, the turn, the player's status
     */
    @Override
    public void sendStatus(GameStateInfo gameStateInfo) {
        connectionBridge.gameState(gameStateInfo);
    }

    /**
     * Method to handle the end of the game, showing the leaderboard
     * @param leaderboard the leaderboard of the game
     * @throws RemoteException if the connection fails
     */
    @Override
    public void gameEnded(HashMap<String, Integer> leaderboard) throws RemoteException {
        connectionBridge.gameEnd(leaderboard);
    }

}
