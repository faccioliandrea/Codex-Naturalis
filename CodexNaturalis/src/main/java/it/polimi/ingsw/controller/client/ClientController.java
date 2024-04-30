package it.polimi.ingsw.controller.client;


import it.polimi.ingsw.connections.client.ServerConnection;
import it.polimi.ingsw.connections.client.SocketServerConnection;
import it.polimi.ingsw.controller.CardInfo;
import it.polimi.ingsw.view.UserInterface;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientController {
    private ServerConnection serverConnection;

    private UserInterface ui;
    String username;

    public ClientController(UserInterface ui) {
        this.ui = ui;
    }

    public void loginRequest() {
        this.username = ui.askForUsername();
        try {
            serverConnection.loginRequest(this.username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void invalidUsername(){
        ui.printDebug("username taken");
        this.loginRequest();
    }

    public void validUsername() {
        ui.printDebug("username ok");
        this.lobbyRequest();
    }

    public void lobbyRequest() {
        try {
            serverConnection.getLobby(username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void lobbyDoesNotExist() {
        //TODO: comunicare alla view che non esistono lobby
        ui.printDebug("no lobbies");
        int n = ui.askForPlayerNum();
        this.createLobbyRequest(n);
    }

    public void lobbyExists(ArrayList<String> lobbies) {
        //TODO: comunicare alla view le lobbies esistenti
        ui.printDebug(lobbies);
        String id = ui.askForLobbyId();
        this.joinLobbyRequest(id);
    }

    public void joinLobbyRequest(String lobbyId) {
        try {
            serverConnection.joinLobby(username, lobbyId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinLobbySuccess() {
        //TODO: comunicare alla view che è entrato nella lobby
        ui.printDebug("joined lobby");
    }
    public void lobbyFull(){
        //TODO: comunicare alla view che la lobby è piena
        ui.printDebug("lobby full");
    }

    public void playerJoinedLobby(String username) {
        //TODO: comunicare alla view che un giocatore è entrato nella lobby
        ui.printDebug(String.format("%s joined lobby", username));
    }

    public void createLobbyRequest(int numPlayers) {
        try {
            serverConnection.createLobbyAndJoin(username, numPlayers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameStarted(){
        //TODO: comunicare alla view che il gioco è iniziato
        ui.printDebug("game started");
    }

    public void initTurn(ArrayList<CardInfo> hand, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<Point> availablePosition, int currTurn, boolean isLastTurn, ArrayList<CardInfo> board){
        //TODO: comunicare alla view che è il proprio turno
        ui.printDebug("your turn");
    }

    public void initTurnAck(){
        try {
            serverConnection.initTurnAck(username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void placeCardRequest(String cardId, Point position, boolean side){
        try {
            serverConnection.placeCard(username, cardId, position, side);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void placeCardSuccess(int cardsPoints, int goalPoints){
        //TODO: comunicare alla view che la carta è stata posizionata
        ui.printDebug("card placed");
    }

    public void placeCardFailure(){
        //TODO: comunicare alla view che la carta non può essere posizionata
    }

    public void drawResourceRequest(int index){
        try {
            serverConnection.drawResource(username, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void drawGoldRequest(int index){
        try {
            serverConnection.drawGold(username, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawSuccess(ArrayList<CardInfo> hand){
        //TODO: comunicare alla view la nuova hand
    }

    public void endTurn(){
        try {
            serverConnection.endTurn(username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameState(ArrayList<CardInfo> resaourceDeck,ArrayList<CardInfo> golddeck, ArrayList<CardInfo> board, int points){
        //TODO: comunicare alla view lo stato del gioco
    }

    public void choosePrivateGoalRequest(int index){
        try {
            serverConnection.choosePrivateGoal(username, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void privateGoalChosen(){
        //TODO: comunicare alla view che il goal è stato scelto
    }

    public void gameEnd(HashMap<String, Integer> leaderboard){
        //TODO: comunicare alla view che il gioco è finito e passagli la leaderboard

    }

    public void setConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }
}
