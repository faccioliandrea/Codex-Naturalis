package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.client.RMIClientConnectionInterface;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * This class is used to handle the RMI connection from the server side to the client.
 * It implements the ClientConnection interface to manage the connection status and the methods to handle the connection.
 */
public class RMIConnection implements ClientConnection{

    private ConnectionStatus connectionStatus;

    final private RMIClientConnectionInterface rmiClientConnection;


    public RMIConnection(RMIClientConnectionInterface rmiClientConnection) {
        this.rmiClientConnection = rmiClientConnection;
        connectionStatus = ConnectionStatus.ONLINE;
    }

    @Override
    public void close(){
        connectionStatus = ConnectionStatus.CLOSED;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public void noOtherPlayerConnected() throws IOException {
        rmiClientConnection.noOtherPlayerConnected();

    }

    @Override
    public void setOffline() {
        connectionStatus = ConnectionStatus.OFFLINE;
    }

    @Override
    public ConnectionStatus getStatus() {
        return connectionStatus;
    }

    @Override
    public void playerJoined(String username) throws IOException {
        rmiClientConnection.playerJoined(username);
    }

    @Override
    public void gameStarted(StarterData starterData) throws RemoteException{
        rmiClientConnection.gameStarted(starterData);
    }

    @Override
    public void otherPlayerTurnMessage(String currentPlayer) throws RemoteException{
        rmiClientConnection.otherPlayerTurn(currentPlayer);
    }

    @Override
    public void initTurn(TurnInfo turnInfo) throws RemoteException{
        rmiClientConnection.initTurn(turnInfo);
    }

    @Override
    public void sendStatus(GameStateInfo gameStateInfo) throws RemoteException{
        rmiClientConnection.sendStatus(gameStateInfo);
    }

    @Override
    public void gameEnded(Map<String, Integer> leaderboard) throws RemoteException{
        rmiClientConnection.gameEnded(leaderboard);
    }

    @Override
    public void playerDisconnected(String username, boolean gameStarted) throws RemoteException {
        rmiClientConnection.playerDisconnected(username, gameStarted);
    }
    @Override
    public void playerReconnected(String username)throws RemoteException {
        rmiClientConnection.playerReconnected(username);
    }

    @Override
    public void reconnectionState(GameStateInfo gameStateInfo)throws RemoteException {
        rmiClientConnection.reconnectionState(gameStateInfo);
    }

    @Override
    public void sendChatMessage(ChatMessageData msg) throws RemoteException {
        rmiClientConnection.recvChatMessage(msg);
    }

    public void ping() throws RemoteException {
        rmiClientConnection.ping();
    }
}
