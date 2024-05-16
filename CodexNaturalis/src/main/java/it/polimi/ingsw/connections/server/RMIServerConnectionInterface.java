package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.client.ServerConnection;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.PlaceCardSuccessInfo;

import it.polimi.ingsw.connections.enums.AddPlayerToLobbyresponse;
import it.polimi.ingsw.connections.enums.ChooseStarterCardSideResponse;
import it.polimi.ingsw.connections.enums.LogInResponse;

import java.awt.*;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This class is the RMI server connection interface
 */
public interface RMIServerConnectionInterface extends Remote, ServerConnection {
    LogInResponse loginRequest(String username, ClientConnection client) throws IOException;
    ArrayList<String> getLobby(String username) throws RemoteException;
    AddPlayerToLobbyresponse addPlayerToLobby(String username, String lobbyId) throws RemoteException;
    int choosePrivateGoal(String username, int index) throws RemoteException;
    ChooseStarterCardSideResponse chooseStarterCardSide(String username, boolean flipped) throws RemoteException;
    PlaceCardSuccessInfo placeCard(String username, String cardId, Point position, boolean flipped) throws RemoteException;
    ArrayList<CardInfo> drawResource(String username, int index) throws RemoteException;
    ArrayList<CardInfo> drawGold(String username, int index) throws RemoteException;
    void endTurn(String username) throws RemoteException;
    String createLobbyAndJoin(String username, int numPlayers) throws RemoteException;
    void threadExceptionCallback(String exceptionMessage) throws IOException;
    void close() throws IOException;
    ConnectionStatus getStatus() throws IOException;

    void createGame(String username) throws RemoteException;

}
