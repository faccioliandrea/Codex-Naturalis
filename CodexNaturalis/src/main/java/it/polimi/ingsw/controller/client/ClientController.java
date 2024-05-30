package it.polimi.ingsw.controller.client;


import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.chat.ClientChatHandler;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.view.UIManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ClientController {
    private static ClientController instance;
    private UIManager ui;
    private String username;
    private ClientChatHandler chatHandler;

    private TurnInfo currentTurnInfo = new TurnInfo();
    private ClientGameData gameData = new ClientGameData();

    private ClientController() { }

    public static synchronized ClientController getInstance() {
        if (instance == null) {
            instance = new ClientController();
        }
        return instance;
    }

    public void init() {
        this.ui = UIManager.getInstance();
        this.ui.showCommands();
        this.gameData.addObserver(this.ui.getData());
    }

    public String loginRequest() {
        this.username = ui.askForUsername();
        return username;
    }

    public void invalidUsername(){
        ui.invalidUsername(this.username);
        ConnectionBridge.getInstance().loginRequest();
    }

    public void validUsername() {
        ui.welcome(this.username);
        ConnectionBridge.getInstance().lobbyRequest();
    }

    public void lobbyRequest() {
        ConnectionBridge.getInstance().lobbyRequest();
    }

    public void lobbyDoesNotExist() {
        ui.noLobbies();
        int n = ui.askForPlayerNum();
        ConnectionBridge.getInstance().createLobbyRequest(n);
    }

    public void lobbyExists(ArrayList<String> lobbies) {
        String id = ui.askForLobbyId(lobbies);
        if(id.isEmpty()) {
            // TODO: handle in GUI
            int n = ui.askForPlayerNum();
            ConnectionBridge.getInstance().createLobbyRequest(n);
        } else
            ConnectionBridge.getInstance().joinLobbyRequest(id);
    }

    public void joinLobbySuccess(boolean isLastPlayer) {
        chatHandlerSetup();

        if (isLastPlayer) {
            ui.joinedLobbyLast();
            ConnectionBridge.getInstance().createGame();
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

    public void lobbyCreated(String lobbyId) {
        ui.lobbyCreated(lobbyId);
    }

    public void gameStarted(StarterData starterData){
        this.gameData.setHand(starterData.getHand());
        this.gameData.setLeaderboard(starterData.getUsers().stream().collect(Collectors.toMap(s -> s, s -> 0, (x, y) -> x, HashMap::new)));
        this.gameData.setBoards(starterData.getUsers().stream().collect(Collectors.toMap(s -> s, x -> new ArrayList<>(), (x, y) -> x, HashMap::new)));
        this.gameData.setPlayerColors(starterData.getPlayerColors());
        this.gameData.setGoals(starterData.getSharedGoals());
        ui.gameStarted(starterData);

        int chosenGoal = ui.askForPrivateGoal();
        ConnectionBridge.getInstance().choosePrivateGoalRequest(chosenGoal);

        this.gameData.addToList(this.gameData.getGoals(), starterData.getPrivateGoals().get(chosenGoal));
    }

    public void privateGoalChosen(){
        if(!this.gameData.isGameAborted()){
            boolean flipped = ui.askForStarterCardSide();
            ConnectionBridge.getInstance().chooseStarterCardSideRequest(flipped);
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

        CardInfo playedCard = ui.askForPlayCard();
        ConnectionBridge.getInstance().placeCardRequest(playedCard);
    }

    public void placeCardSuccess(PlaceCardSuccessInfo placeCardSuccessInfo) {
        this.gameData.removeCardFromHand(placeCardSuccessInfo.getPlayedCard().getId());
        this.gameData.addToList(this.gameData.getBoard(), placeCardSuccessInfo.getPlayedCard());
        this.gameData.replaceEntry(this.gameData.getLeaderboard(), this.username, placeCardSuccessInfo.getCardsPoint());
        this.gameData.setCardPoints(placeCardSuccessInfo.getCardsPoint());
        this.gameData.setGoalPoints(placeCardSuccessInfo.getGoalsPoint());
        this.gameData.setAvailablePositions(placeCardSuccessInfo.getAvailable());
        this.gameData.setSymbols(placeCardSuccessInfo.getSymbols());
        ui.placeCardSuccess();
        if(!gameData.getResourceDeck().isEmpty() || !gameData.getGoldDeck().isEmpty()){
            int choice = ui.askForDrawCard();
            if(choice / 10 == 1){
                ConnectionBridge.getInstance().drawResourceRequest(choice%10);
            } else {
                ConnectionBridge.getInstance().drawGoldRequest(choice%10);
            }
        } else {
            ConnectionBridge.getInstance().endTurn();
        }
    }

    public void placeCardFailure(){
        ui.placeCardFailure();
        CardInfo playedCard = ui.askForPlayCard();
        ConnectionBridge.getInstance().placeCardRequest(playedCard);
    }

    public void drawSuccess(ArrayList<CardInfo> hand){
        hand.forEach(x -> x.setFlipped(false));
        this.gameData.setHand(hand);
        ui.drawCardSuccess();
        ConnectionBridge.getInstance().endTurn();
    }

    public void gameState(GameStateInfo gameStateInfo){
        this.gameData.fromGameStateInfo(gameStateInfo);
        if(!username.equals(gameStateInfo.getLastPlayer()) && gameStateInfo.getLastPlayer()!=null){
            ui.turnEnded(gameStateInfo);
        }
    }

    public void gameEnd(HashMap<String, Integer> leaderboard){
        this.gameData.setGameAborted(true);
        this.gameData.setLeaderboard(leaderboard);
        ui.gameEnded();
        boolean newGame = ui.askForNewGame();
        if(newGame) {
            this.gameData= new ClientGameData();
            this.gameData.addObserver(this.ui.getData());
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
        chatHandlerSetup();

        this.gameData.fromGameStateInfo(gameStateInfo);
        ui.reconnectionState();
        if(this.gameData.getCurrentPlayer().equals(this.gameData.getUsername())){
            ui.yourTurn(this.gameData.isLastTurn());
            CardInfo playedCard = ui.askForPlayCard();
            ConnectionBridge.getInstance().placeCardRequest(playedCard);
        } else {
            ui.otherPlayerTurn(this.gameData.getCurrentPlayer());
        }
    }

    public String getUsername() {
        return username;
    }

    public ConnectionBridge getConnectionBridge() {
        return ConnectionBridge.getInstance();
    }

    public TurnInfo getCurrentTurnInfo() {
        return currentTurnInfo;
    }

    public void noOtherPlayerConnected() {
        ui.noOtherPlayerConnected();
    }

    private void chatHandlerSetup() {
        new Thread(ClientChatHandler.getInstance()).start();
    }

    public void recvChatMessage(ChatMessageData msg) {
        ClientChatHandler.recvChatMessage(msg);
        ui.messageReceived();
    }

    public ClientGameData getData() {
        return gameData;
    }

    public void serverNotFound() {
        ui.serverNotFound();
    }
}
