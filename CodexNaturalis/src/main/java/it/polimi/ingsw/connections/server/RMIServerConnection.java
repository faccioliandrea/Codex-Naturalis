package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.PlaceCardSuccessInfo;
import it.polimi.ingsw.connections.enums.AddPlayerToLobbyresponse;
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
    private final ConnectionBridge connectionBridge;
    private ConnectionStatus connectionStatus = ConnectionStatus.INITIALIZING;

    /**
     * Constructor for the RMI server connection
     * @param connectionBridge the connection bridge
     * @throws RemoteException if the connection fails
     */
    public RMIServerConnection(ConnectionBridge connectionBridge) throws RemoteException {
        this.connectionBridge = connectionBridge;
    }

    /**
     * Method to handle the login request
     * @param username the username of the player
     * @param client the client connection
     * @return the login response
     * @throws IOException if the connection fails
     */
    @Override
    public LogInResponse loginRequest(String username, ClientConnection client) throws IOException {
        return connectionBridge.addConnection(client, username);
    }

    /**
     * Method to get the lobbies
     * @param username the username of the player
     * @return the list of lobbies
     * @throws RemoteException if the connection fails
     */
    @Override
    public ArrayList<String> getLobby(String username) throws RemoteException {
        return connectionBridge.getLobbies(username);
    }

    /**
     * Method to add a player to a lobby
     * @param username the username of the player
     * @param lobbyId the id of the lobby
     * @return the response of adding the player to the lobby
     * @throws RemoteException if the connection fails
     */
    @Override
    public AddPlayerToLobbyresponse addPlayerToLobby(String username, String lobbyId) throws RemoteException {
        return connectionBridge.addPlayerToLobby(username, lobbyId);
    }

    /**
     * Method to choose a private goal
     * @param username the username of the player
     * @param index the index of the private goal
     * @return
     * @throws RemoteException if the connection fails
     */
    @Override
    public int choosePrivateGoal(String username, int index) throws RemoteException {
        return connectionBridge.choosePrivateGoal(username, index);
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
        return connectionBridge.chooseStarterCardSide(username, flipped);
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
        return connectionBridge.placeCard(username, cardId, position, flipped);
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
        return connectionBridge.drawResource(username, index);
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
        return connectionBridge.drawGold(username, index);
    }

    /**
     * Method to end the player's turn
     * @param username the username of the player
     * @throws RemoteException if the connection fails
     */
    @Override
    public void endTurn(String username) throws RemoteException {
        connectionBridge.endTurn(username);
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
        return connectionBridge.createLobby(username, numPlayers);
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

    /**
     * Method to create a game
     * @param username the username of the player
     * @throws RemoteException if the connection fails
     */
    @Override
    public void createGame(String username) throws RemoteException {
        connectionBridge.createGame(username);
    }
}
