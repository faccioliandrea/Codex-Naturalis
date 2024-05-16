package it.polimi.ingsw.controller.client;


import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.view.UIManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientController {

    private UIManager ui;

    private String username;
    private ConnectionBridge connectionBridge;

    private TurnInfo currentTurnInfo = new TurnInfo();
    private final ClientGameData gameData = new ClientGameData();

    public ClientController(UIManager ui) {
        this.ui = ui;
        connectionBridge = new ConnectionBridge(this);
        this.ui.showCommands();
        this.gameData.addObserver(this.ui.getData());
    }

    public String loginRequest() {
        this.username = ui.askForUsername();
        return username;
    }

    public void invalidUsername(){
        ui.invalidUsername(this.username);
        connectionBridge.loginRequest();
    }

    public void validUsername() {
        ui.welcome(this.username);
        connectionBridge.lobbyRequest();
    }

    public void lobbyRequest() {
        connectionBridge.lobbyRequest();
    }


    public void lobbyDoesNotExist() {
        ui.noLobbies();
        int n = ui.askForPlayerNum();
        connectionBridge.createLobbyRequest(n);
    }

    public void lobbyExists(ArrayList<String> lobbies) {
        String id = ui.askForLobbyId(lobbies);
        connectionBridge.joinLobbyRequest(id);
    }


    public void joinLobbySuccess(boolean isLastPlayer) {

        if (isLastPlayer) {
            ui.joinedLobbyLast();
            connectionBridge.createGame();

        } else {
            ui.joinedLobby();
        }
    }
    public void lobbyFull(){
        ui.lobbyFull();
    }

    public void playerJoinedLobby(String username) {
        gameData.putEntry(gameData.getConnectionStatus(), username, ConnectionStatus.ONLINE);
        ui.joinedLobby(username);
    }

    public void lobbyIsReady() {

    }

    public void gameStarted(StarterData starterData){
        ui.gameStarted(starterData);
        this.gameData.setHand(starterData.getHand());

        int chosenGoal = ui.askForPrivateGoal();
        connectionBridge.choosePrivateGoalRequest(chosenGoal);
        this.gameData.setLeaderboard(starterData.getUsers().stream().collect(Collectors.toMap(s -> s, s -> 0, (x, y) -> x, HashMap::new)));
        this.gameData.setBoards(starterData.getUsers().stream().collect(Collectors.toMap(s -> s, x -> new ArrayList<>(), (x, y) -> x, HashMap::new)));
        // TODO: move this in privateGoalChosen?
        this.gameData.setGoals(starterData.getSharedGoals());
        this.gameData.addToList(this.gameData.getGoals(), starterData.getPrivateGoals().get(chosenGoal));
    }

    public void privateGoalChosen(){
        if(!this.gameData.isGameAborted()){
            boolean flipped = ui.askForStarterCardSide();
            connectionBridge.chooseStarterCardSideRequest(flipped);
        }
    }

    public void waitingOthersStartingChoice() {
        ui.waitingOthersStartingChoice();
    }

    public void otherPlayerTurnMessage(String currentPlayer) {
        ui.otherPlayerTurn(currentPlayer);
    }

    public void initTurn(TurnInfo turnInfo){
        this.currentTurnInfo = turnInfo;
        this.gameData.fromTurnInfo(turnInfo);
        ui.yourTurn(turnInfo.isLastTurn());

        CardInfo playedCard = ui.askForPlayCard(turnInfo.getHand(), turnInfo.getAvailablePositions());
        connectionBridge.placeCardRequest(playedCard);
    }

    public void placeCardSuccess(PlaceCardSuccessInfo placeCardSuccessInfo) {
        this.gameData.addToList(this.gameData.getBoard(), placeCardSuccessInfo.getPlayedCard());
        this.gameData.replaceEntry(this.gameData.getLeaderboard(), this.username, placeCardSuccessInfo.getCardsPoint());
        this.gameData.setCardPoints(placeCardSuccessInfo.getCardsPoint());
        this.gameData.setGoalPoints(placeCardSuccessInfo.getGoalsPoint());
        this.gameData.setAvailablePositions(placeCardSuccessInfo.getAvailable());
        this.gameData.setSymbols(placeCardSuccessInfo.getSymbols());
        ui.placeCardSuccess();

        int choice = ui.askForDrawCard(currentTurnInfo);
        if(choice / 10 == 1){
            connectionBridge.drawResourceRequest(choice%10);
        } else {
            connectionBridge.drawGoldRequest(choice%10);
        }
    }

    public void placeCardFailure(){
        ui.placeCardFailure();
        CardInfo playedCard = ui.askForPlayCard(this.currentTurnInfo.getHand(), this.currentTurnInfo.getAvailablePositions());
        connectionBridge.placeCardRequest(playedCard);
    }

    public void drawSuccess(ArrayList<CardInfo> hand){
        hand.forEach(x -> x.setFlipped(false));
        this.gameData.setHand(hand);
        ui.drawCardSuccess();
        connectionBridge.endTurn();
    }

    public void gameState(GameStateInfo gameStateInfo){
        this.gameData.fromGameStateInfo(gameStateInfo);
        //this.gameData.setLeaderboard(gameStateInfo.getLeaderboard());
        //this.gameData.putEntry(this.gameData.getBoards(), gameStateInfo.getUsername(), gameStateInfo.getBoard());

        ui.turnEnded(gameStateInfo);
    }

    public void gameEnd(HashMap<String, Integer> leaderboard){
        this.gameData.setGameAborted(true);
        this.gameData.setLeaderboard(leaderboard);
        ui.gameEnded();
        boolean newGame = ui.askForNewGame();
        if(newGame) {
            this.lobbyRequest();
        } else {
            ui.goodbye();
            // TODO: quit
        }
    }

    public void playerDisconnected(String username, boolean gameStarted) {
        this.gameData.replaceEntry(this.gameData.getConnectionStatus(), username, ConnectionStatus.OFFLINE);
        ui.playerDisconnected(username, gameStarted);
    }

    public void playerReconnected(String username) {
        this.gameData.replaceEntry(this.gameData.getConnectionStatus(), username, ConnectionStatus.ONLINE);
        ui.playerReconnected(username);
    }

    public void reconnectionState(GameStateInfo gameStateInfo) {
        this.gameData.fromGameStateInfo(gameStateInfo);
        ui.reconnectionState();
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


}
