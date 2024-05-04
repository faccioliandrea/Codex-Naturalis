package it.polimi.ingsw.controller.client;


import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;
import it.polimi.ingsw.view.UserInterface;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientController {

    private UserInterface ui;

    private String username;

    private ConnectionBridge connectionBridge;

    public ClientController(UserInterface ui) {
        this.ui = ui;
        connectionBridge = new ConnectionBridge(this);
    }

    public String loginRequest() {
        this.username = ui.askForUsername();
        return username;
    }

    public void invalidUsername(){
        ui.printDebug("username taken");
        connectionBridge.loginRequest();
    }

    public void validUsername() {
        ui.printDebug("username ok");
        connectionBridge.lobbyRequest();
    }


    public void lobbyDoesNotExist() {
        //TODO: comunicare alla view che non esistono lobby
        ui.printDebug("no lobbies");
        int n = ui.askForPlayerNum();
        connectionBridge.createLobbyRequest(n);
    }

    public void lobbyExists(ArrayList<String> lobbies) {
        //TODO: comunicare alla view le lobbies esistenti
        ui.printDebug(lobbies);
        String id = ui.askForLobbyId();
        connectionBridge.joinLobbyRequest(id);
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


    public void gameStarted(StarterData starterData){
        //TODO: comunicare alla view che il gioco è iniziato
        ui.printDebug("Game started");
        ui.printColorDebug(UserInterface.ANSI_CYAN, "Your current hand:");
        starterData.getHand().forEach(x->ui.printDebug(x.getDescription()));
        ui.printColorDebug(UserInterface.ANSI_CYAN, "Game shared goals:");
        starterData.getSharedGoals().forEach(x->ui.printDebug(x.getDescription()));
        ui.printColorDebug(UserInterface.ANSI_CYAN,"You have to choose one of the following private goals:");
        starterData.getPrivateGoals().forEach(x->ui.printDebug(x.getDescription()));
        ui.printColorDebug(UserInterface.ANSI_CYAN,"You have to choose your starter card side:");
        ui.printDebug(starterData.getStarterCard().getDescription());
        int chosenGoal = ui.askForPrivateGoal();
        connectionBridge.choosePrivateGoalRequest(chosenGoal);
    }

    public void privateGoalChosen(){
        boolean flipped = ui.askForStarterCardSide();
        connectionBridge.chooseStarterCardSideRequest(flipped);

        //TODO: comunicare alla view che il goal è stato scelto
    }

    public void waitingOthersStartingChoice() {
        ui.printDebug("Awesome! Now wait for the other players to choose their private goals and starter cards");
    }

    public void otherPlayerTurnMessage(String currentPlayer) {
        ui.printDebug("It's " + currentPlayer + "'s turn! Please wait for your turn!");
    }

    public void initTurn(TurnInfo turnInfo){
        //TODO: comunicare alla view che è il proprio turno
        ui.printDebug("your turn");
    }


    public void placeCardSuccess(int cardsPoints, int goalPoints){
        //TODO: comunicare alla view che la carta è stata posizionata
        ui.printDebug("card placed");
    }

    public void placeCardFailure(){
        //TODO: comunicare alla view che la carta non può essere posizionata
    }


    public void drawSuccess(ArrayList<CardInfo> hand){
        //TODO: comunicare alla view la nuova hand
    }


    public void gameState(ArrayList<CardInfo> resaourceDeck,ArrayList<CardInfo> golddeck, ArrayList<CardInfo> board, int points){
        //TODO: comunicare alla view lo stato del gioco
    }





    public void gameEnd(HashMap<String, Integer> leaderboard){
        //TODO: comunicare alla view che il gioco è finito e passagli la leaderboard

    }

    public String getUsername() {
        return username;
    }

    public ConnectionBridge getConnectionBridge() {
        return connectionBridge;
    }


}
