package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.client.RMIClientConnectionInterface;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.PlaceCardSuccessInfo;
import it.polimi.ingsw.connections.enums.AddPlayerToLobbyResponse;
import it.polimi.ingsw.connections.enums.ChooseStarterCardSideResponse;
import it.polimi.ingsw.connections.enums.LogInResponse;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * This class is the RMI server connection class
 */
public class RMIServerConnection extends UnicastRemoteObject implements RMIServerConnectionInterface {
    private ConnectionStatus connectionStatus = ConnectionStatus.INITIALIZING;
    private final Object lock = new Object();

    /**
     * Constructor for the RMI server connection
     * @throws RemoteException if the connection fails
     */
    public RMIServerConnection() throws RemoteException { }

    /**
     * Method to handle the login request
     * @param username the username of the player
     * @param client the client connection
     * @return the login response
     * @throws IOException if the connection fails
     */
    @Override
    public LogInResponse loginRequest(String username, RMIClientConnectionInterface client) throws IOException {
        RMIConnection rmiConnection = new RMIConnection(client);
        new Thread(()->{

            while (true){
                try {
                    rmiConnection.ping();
                    synchronized (lock){
                        lock.wait(3000);
                    }
                } catch (RemoteException | InterruptedException e) {
                    new Thread(()->{
                        connectionStatus = ConnectionStatus.OFFLINE;
                        ConnectionBridge.getInstance().onClientDisconnect(rmiConnection);
                    }).start();

                    break;
                }
            }
        }).start();

        return ConnectionBridge.getInstance().addConnection(rmiConnection, username);
    }

    /**
     * Method to get the lobbies
     * @param username the username of the player
     * @return the list of lobbies
     * @throws RemoteException if the connection fails
     */
    @Override
    public ArrayList<String> getLobby(String username) throws RemoteException {
        return ConnectionBridge.getInstance().getLobbies(username);
    }

    /**
     * Method to add a player to a lobby
     * @param username the username of the player
     * @param lobbyId the id of the lobby
     * @return the response of adding the player to the lobby
     * @throws RemoteException if the connection fails
     */
    @Override
    public AddPlayerToLobbyResponse addPlayerToLobby(String username, String lobbyId) throws RemoteException {
        return ConnectionBridge.getInstance().addPlayerToLobby(username, lobbyId);
    }

    /**
     * Method to choose a private goal
     * @param username the username of the player
     * @param index the index of the private goal
     * @throws RemoteException if the connection fails
     */
    @Override
    public void choosePrivateGoal(String username, int index) throws RemoteException {
        ConnectionBridge.getInstance().choosePrivateGoal(username, index);
    }

    /**
     * Method to choose the side of the starter card
     * @param username the username of the player
     * @param flipped the side of the starter card
     * @return the response of choosing the starter card side
     * @throws RemoteException if the connection fails
     */
    @Override
    public ChooseStarterCardSideResponse chooseStarterCardSide(String username, boolean flipped) throws RemoteException {
        return ConnectionBridge.getInstance().chooseStarterCardSide(username, flipped);
    }

    /**
     * Method to place a card
     * @param username the username of the player
     * @param cardId the id of the card
     * @param position the position where the player wants to place the card
     * @param flipped true if the card is flipped, false otherwise
     * @return the response of placing the card
     * @throws RemoteException if the connection fails
     */
    @Override
    public PlaceCardSuccessInfo placeCard(String username, String cardId, Point position, boolean flipped) throws RemoteException {
        return ConnectionBridge.getInstance().placeCard(username, cardId, position, flipped);
    }

    /**
     * Method to draw a resource card
     * @param username the username of the player
     * @param index the index of the resource card in the deck
     * @return the new player's hand
     * @throws RemoteException if the connection fails
     */
    @Override
    public ArrayList<CardInfo> drawResource(String username, int index) throws RemoteException {
        return ConnectionBridge.getInstance().drawResource(username, index);
    }

    /**
     * Method to draw a gold card
     * @param username the username of the player
     * @param index the index of the gold card in the deck
     * @return the new player's hand
     * @throws RemoteException if the connection fails
     */
    @Override
    public ArrayList<CardInfo> drawGold(String username, int index) throws RemoteException {
        return ConnectionBridge.getInstance().drawGold(username, index);
    }

    /**
     * Method to end the player's turn
     * @param username the username of the player
     * @throws RemoteException if the connection fails
     */
    @Override
    public void endTurn(String username) throws RemoteException {
        ConnectionBridge.getInstance().endTurn(username);
    }

    /**
     * Method to create a lobby and join it
     * @param username the username of the player
     * @param numPlayers the number of players in the lobby
     * @return the id of the lobby
     * @throws RemoteException if the connection fails
     */
    @Override
    public String createLobbyAndJoin(String username, int numPlayers) throws RemoteException {
        return ConnectionBridge.getInstance().createLobby(username, numPlayers);
    }

    @Override
    public void threadExceptionCallback(String exceptionMessage) {

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public ConnectionStatus getStatus() throws RemoteException {
        return this.connectionStatus;
    }

    @Override
    public void sendChatMessage(ChatMessageData msg) throws RemoteException {
        ConnectionBridge.getInstance().recvChatMessage(msg);
    }

    /**
     * Method to create a game
     * @param username the username of the player
     * @throws RemoteException if the connection fails
     */
    @Override
    public void createGame(String username) throws RemoteException {
        ConnectionBridge.getInstance().createGame(username);
    }

    @Override
    public void ping() throws RemoteException {

    }
}
