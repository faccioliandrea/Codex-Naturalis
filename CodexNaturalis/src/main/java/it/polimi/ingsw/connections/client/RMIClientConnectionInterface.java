package it.polimi.ingsw.connections.client;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * This class is the RMI client connection interface
 */
public interface RMIClientConnectionInterface extends Remote {
    void playerJoined(String username) throws RemoteException;

     void setBridge(ConnectionBridge connectionBridge) throws RemoteException;

    void gameStarted(StarterData starterData) throws RemoteException;

    void otherPlayerTurn(String currentPlayer) throws RemoteException;

    void initTurn(TurnInfo turnInfo) throws RemoteException;

    void sendStatus(GameStateInfo gameStateInfo) throws RemoteException;

    void gameEnded(HashMap<String, Integer> leaderboard) throws RemoteException;

    void playerDisconnected(String username, boolean gameStarted) throws RemoteException;

    void playerReconnected(String username)  throws RemoteException;

    void reconnectionState(GameStateInfo gameStateInfo)throws RemoteException;

    void ping()throws RemoteException;
}
