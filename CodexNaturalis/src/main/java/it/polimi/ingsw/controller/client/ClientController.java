package it.polimi.ingsw.controller.client;


import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;
import it.polimi.ingsw.view.TUIColors;
import it.polimi.ingsw.view.UserInterface;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ClientController {

    private UserInterface ui;

    private String username;

    private ConnectionBridge connectionBridge;


    private TurnInfo currentTurnInfo;
    private HashMap<String, Integer> leaderboard = new HashMap<>();
    private HashMap<String, ArrayList<CardInfo>> boards = new HashMap<>();

    public ClientController(UserInterface ui) {
        this.ui = ui;
        connectionBridge = new ConnectionBridge(this);
        ui.setController(this);
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
        ui.printDebug("Game started");
        ui.printColorDebug(TUIColors.CYAN, "Your current hand:");
        starterData.getHand().forEach(x->ui.printCardInfo(x));
        ui.printColorDebug(TUIColors.CYAN, "Game shared goals:");
        starterData.getSharedGoals().forEach(x->ui.printDebug(x.getDescription()));
        ui.printColorDebug(TUIColors.CYAN,"You have to choose one of the following private goals:");
        starterData.getPrivateGoals().forEach(x->ui.printDebug(x.getDescription()));
        ui.printColorDebug(TUIColors.CYAN,"You have to choose your starter card side:");
        ui.printCardInfo(starterData.getStarterCard());
        int chosenGoal = ui.askForPrivateGoal();
        connectionBridge.choosePrivateGoalRequest(chosenGoal);
        this.leaderboard = starterData.getUsers().stream().collect(Collectors.toMap(s -> s, s -> 0, (x, y) -> x, HashMap::new));
        this.boards = starterData.getUsers().stream().collect(Collectors.toMap(s -> s, x -> new ArrayList<>(), (x, y) -> x, HashMap::new));
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
        this.currentTurnInfo = turnInfo;
        ui.printDebug("It's your turn!");
        if(turnInfo.isLastTurn()){
            ui.printColorDebug(TUIColors.RED, "This is your last turn.");
        }
        ui.printColorDebug(TUIColors.CYAN,"This is your board:");
        ui.displayBoard(turnInfo.getBoard(), turnInfo.getAvailablePositions());
        ui.printColorDebug(TUIColors.CYAN, "Your current hand:");
        turnInfo.getHand().forEach(x->ui.printCardInfo(x));
        CardInfo playedCard = ui.askForPlayCard(turnInfo.getHand(), turnInfo.getAvailablePositions());
        connectionBridge.placeCardRequest(playedCard);
    }


    public void placeCardSuccess(int cardsPoints, int goalPoints, CardInfo placedCard, ArrayList<Point> newAvailable){
        currentTurnInfo.addCardToBoard(placedCard);
        //TODO: comunicare alla view che la carta è stata posizionata
        ui.printDebug("Card placed!");
        ui.printDebug("You currently have " + cardsPoints + " points and you will score " + goalPoints + " points from the goals!");
        ui.printColorDebug(TUIColors.CYAN,"This is your board:");
        ui.displayBoard(currentTurnInfo.getBoard(), null);
        ui.printColorDebug(TUIColors.CYAN,"These are the first two cards of the resource deck:");
        ui.printCardInfo(currentTurnInfo.getResourceDeck().get(0));
        ui.printCardInfo(currentTurnInfo.getResourceDeck().get(1));
        ui.printDebug("The kingdom of the first covered card of the resource deck is " + currentTurnInfo.getResourceDeck().get(2).getColor());
        ui.printColorDebug(TUIColors.CYAN,"These are the first two cards of the gold deck:");
        ui.printCardInfo(currentTurnInfo.getGoldDeck().get(0));
        ui.printCardInfo(currentTurnInfo.getGoldDeck().get(1));
        ui.printDebug("The kingdom of the first covered card of the gold deck is " + currentTurnInfo.getGoldDeck().get(2).getColor());
        this.currentTurnInfo.setAvailablePositions(newAvailable);
        int choice = ui.askForDrawCard(currentTurnInfo);
        if(choice / 10 == 1){
            connectionBridge.drawResourceRequest(choice%10);
        } else {
            connectionBridge.drawGoldRequest(choice%10);
        }
    }

    public void placeCardFailure(){
        ui.printColorDebug(TUIColors.RED, "You don't have the requirements to place the card!");
        CardInfo playedCard = ui.askForPlayCard(this.currentTurnInfo.getHand(), this.currentTurnInfo.getAvailablePositions());
        connectionBridge.placeCardRequest(playedCard);

        //TODO: comunicare alla view che la carta non può essere posizionata
    }


    public void drawSuccess(ArrayList<CardInfo> hand){
        ui.printDebug("Card drawn!");
        ui.printColorDebug(TUIColors.CYAN, "This is your updated hand:");
        hand.forEach(x->ui.printCardInfo(x));
        connectionBridge.endTurn();
        //TODO: comunicare alla view la nuova hand
    }


    public void gameState(GameStateInfo gameStateInfo){
        ui.printDebug(gameStateInfo.getUsername() + " has ended his turn.");
        ui.printColorDebug(TUIColors.CYAN, "This is his current board:");
        ui.displayBoard(gameStateInfo.getBoard(), null);
        ui.printDebug("He currently has " + gameStateInfo.getCardsPoints() + " points");
        ui.printColorDebug(TUIColors.CYAN,"These are the first two cards of the resource deck:");
        ui.printCardInfo(gameStateInfo.getResourceDeck().get(0));
        ui.printCardInfo(gameStateInfo.getResourceDeck().get(1));
        ui.printDebug("The kingdom of the first covered card of the resource deck is " + gameStateInfo.getResourceDeck().get(2).getColor());
        ui.printColorDebug(TUIColors.CYAN,"These are the first two cards of the gold deck:");
        ui.printCardInfo(gameStateInfo.getGoldDeck().get(0));
        ui.printCardInfo(gameStateInfo.getGoldDeck().get(1));
        ui.printDebug("The kingdom of the first covered card of the gold deck is " + gameStateInfo.getGoldDeck().get(2).getColor());
        ui.printLeaderboard(gameStateInfo.getLeaderboard());

        this.leaderboard = gameStateInfo.getLeaderboard();
        ArrayList<CardInfo> prev = this.boards.replace(gameStateInfo.getUsername(), gameStateInfo.getBoard());
        if (prev == null) {
            this.boards.put(gameStateInfo.getUsername(), gameStateInfo.getBoard());
        }

        //TODO: comunicare alla view lo stato del gioco
    }





    public void gameEnd(HashMap<String, Integer> leaderboard){
        ui.printDebug("Game ended!");
        ui.printLeaderboard(leaderboard);
    }

    public String getUsername() {
        return username;
    }

    public ConnectionBridge getConnectionBridge() {
        return connectionBridge;
    }


    public TurnInfo getCurrentTurnInfo() {
        return currentTurnInfo;
    }

    public HashMap<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public HashMap<String, ArrayList<CardInfo>> getBoards() {
        return boards;
    }
}
