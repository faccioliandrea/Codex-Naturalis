package it.polimi.ingsw.controller.client;


import it.polimi.ingsw.controller.CardInfo;
import sun.jvm.hotspot.debugger.linux.LinuxDebugger;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientController {
    private ServerConnection serverConnection;


    String username;

    public ClientController(ServerConnection serverConnection) {
        serverConnection = this.serverConnection;
    }

    public void loginRequest(String username) {
        this.username = username;
        serverConnection.addConnection(username);
    }

    public void invalidUsername(){
        //TODO: comunicare alla view che il nome utente è già in uso
    }public void validUsername(){
        //TODO: comunicare alla view che è stato aggiunto
    }

    public void lobbyRequest() {
        serverConnection.getLobbiew(username);

    }
    public void lobbyDoesNotExist() {
        //TODO: comunicare alla view che non esistono lobby
    }

    public void lobbyExists(ArrayList<String> lobbies) {
        //TODO: comunicare alla view le lobbies esistenti
    }

    public void joinLobbyRequest(String lobbyId) {
        serverConnection.joinLobby(username, lobbyId);
    }

    public void joinLobbySuccess() {
        //TODO: comunicare alla view che è entrato nella lobby
    }
    public void lobbyFull(){
        //TODO: comunicare alla view che la lobby è piena
    }

    public void playerJoinedLobby(String username) {
        //TODO: comunicare alla view che un giocatore è entrato nella lobby
    }

    public void createLobbyRequest(int numPlayers) {
        serverConnection.createLobby(username, numPlayers);
    }

    public void gameStarted(){
        //TODO: comunicare alla view che il gioco è iniziato
    }

    public void initTurn(ArrayList<CardInfo> hand, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<Point> availablePosition, int currTurn, boolean isLastTurn, ArrayList<CardInfo> board){
        //TODO: comunicare alla view che è il proprio turno
    }

    public void initTurnAck(){
        serverConnection.initTurnAck(username);
    }


    public void placeCardRequest(CardInfo card, Point position, boolean side){
        serverConnection.placeCard(username, card, position, side);
    }

    public void placeCardSuccess(int cardsPoints, int goalPoints){
        //TODO: comunicare alla view che la carta è stata posizionata
    }

    public void placeCardFailure(){
        //TODO: comunicare alla view che la carta non può essere posizionata
    }

    public void drawResourceRequest(int index){
        serverConnection.drawResource(username, index);
    }
    public void drawGoldRequest(int index){
        serverConnection.drawGold(username, index);
    }

    public void drawSuccess(ArrayList<CardInfo> hand){
        //TODO: comunicare alla view la nuova hand
    }

    public void endTurn(){
        serverConnection.endTurn(username);
    }

    public void gameState(ArrayList<CardInfo> resaourceDeck,ArrayList<CardInfo> golddeck, ArrayList<CardInfo> board, int points){
        //TODO: comunicare alla view lo stato del gioco
    }

    public void choosePrivateGoalRequest(int index){
        serverConnection.choosePrivateGoal(username, index);
    }

    public void choosePrivateGoalSuccess(){
        //TODO: comunicare alla view che il goal è stato scelto
    }

    public void gameEnd(HashMap<String, Integer> leaderboard){
        //TODO: comunicare alla view che il gioco è finito e passagli la leaderboard

    }
}
