package it.polimi.ingsw.controller.client;


import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.chat.ClientChatHandler;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.connections.enums.LogInResponse;
import it.polimi.ingsw.view.UIManager;
import it.polimi.ingsw.view.tui.enums.Decks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that handles the client side of the game
 */
public class ClientController {
    private static ClientController instance;
    private UIManager ui;
    private String username;
    private ClientGameData gameData = new ClientGameData();

    /**
     * Default constructor
     */
    private ClientController() { }

    /**
     * Singleton instance getter
     * @return the instance of the ClientController
     */
    public static synchronized ClientController getInstance() {
        if (instance == null) {
            instance = new ClientController();
        }
        return instance;
    }

    /**
     * Initializes the controller
     */
    public void init() {
        this.ui = UIManager.getInstance();
        this.ui.showCommands();
        this.gameData.addObserver(this.ui.getData());
    }

    /**
     * Handles the login request
     * @return the username
     */
    public String loginRequest() {
        this.username = ui.askForUsername();
        return username;
    }

    /**
     * Handles the login response
     * @param status the login response
     */
    public void invalidUsername(LogInResponse status) {
        ui.invalidUsername(this.username, status);
        ConnectionBridge.getInstance().loginRequest();
    }

    /**
     * Handles the valid username
     */
    public void validUsername() {
        ui.welcome(this.username);
        ConnectionBridge.getInstance().lobbyRequest();
    }

    /**
     * Handles the lobby request
     */
    public void lobbyRequest() {
        ConnectionBridge.getInstance().lobbyRequest();
    }

    /**
     * Handles the lobby response in case of not existing lobbies
     */
    public void lobbyDoesNotExist() {
        ui.noLobbies();
        int n = ui.askForPlayerNum();
        ConnectionBridge.getInstance().createLobbyRequest(n);
    }

    /**
     * Handles the lobby response in case of existing lobbies
     * @param lobbies the list of lobbies
     */
    public void lobbyExists(ArrayList<String> lobbies) {
        String id = ui.askForLobbyId(lobbies);
        if(id.isEmpty()) {
            int n = ui.askForPlayerNum();
            ConnectionBridge.getInstance().createLobbyRequest(n);
        } else
            ConnectionBridge.getInstance().joinLobbyRequest(id);
    }

    /**
     * Handles the join lobby success
     * @param isLastPlayer true if the player is the last one, false otherwise
     */
    public void joinLobbySuccess(boolean isLastPlayer) {
        chatHandlerSetup();

        if (isLastPlayer) {
            ui.joinedLobbyLast();
            ConnectionBridge.getInstance().createGame();
        } else {
            ui.joinedLobby();
        }
    }

    /**
     * Handles the join lobby failure
     */
    public void lobbyFull(){
        ui.lobbyFull();
        ConnectionBridge.getInstance().lobbyRequest();
    }

    /**
     * Handles the PlayerJoinedLobby message
     */
    public void playerJoinedLobby(String username) {
        gameData.putEntry(gameData.getConnectionStatus(), username, ConnectionStatus.ONLINE);
        ui.joinedLobby(username);
    }

    /**
     * Handles the LobbyCreated message
     */
    public void lobbyCreated(String lobbyId) {
        ui.lobbyCreated(lobbyId);
    }

    /**
     * Handles the game started message
     * @param starterData the StarterData
     */
    public void gameStarted(StarterData starterData){
        this.gameData.setHand(starterData.getHand());
        this.gameData.setLeaderboard(starterData.getUsers().stream().collect(Collectors.toMap(s -> s, s -> 0, (x, y) -> x, LinkedHashMap::new)));
        this.gameData.setBoards(starterData.getUsers().stream().collect(Collectors.toMap(s -> s, x -> new ArrayList<>(), (x, y) -> x, HashMap::new)));
        this.gameData.setPlayerColors(starterData.getPlayerColors());
        this.gameData.setGoals(starterData.getSharedGoals());
        ui.gameStarted(starterData);

        int chosenGoal = ui.askForPrivateGoal();
        ConnectionBridge.getInstance().choosePrivateGoalRequest(chosenGoal);

        this.gameData.addToList(this.gameData.getGoals(), starterData.getPrivateGoals().get(chosenGoal));
    }

    /**
     * Handles the private goal chosen message
     */
    public void privateGoalChosen(){
        if(!this.gameData.isGameAborted()){
            boolean flipped = ui.askForStarterCardSide();
            ConnectionBridge.getInstance().chooseStarterCardSideRequest(flipped);
        }
    }

    /**
     * Handles the waiting others starting choice message
     */
    public void waitingOthersStartingChoice() {
        ui.waitingOthersStartingChoice();
    }

    /**
     * Handles the other player turn message
     */
    public void otherPlayerTurnMessage(String currentPlayer) {
        ui.otherPlayerTurn(currentPlayer);
    }

    /**
     * Handles the init turn message
     */
    public void initTurn(TurnInfo turnInfo){
        this.gameData.fromTurnInfo(turnInfo);
        ui.yourTurn(turnInfo.isLastTurn());

        CardInfo playedCard = ui.askForPlayCard();
        ConnectionBridge.getInstance().placeCardRequest(playedCard);
    }

    /**
     * Handles the place card success message
     * @param placeCardSuccessInfo the PlaceCardSuccessInfo
     */
    public void placeCardSuccess(PlaceCardSuccessInfo placeCardSuccessInfo) {
        this.gameData.removeCardFromHand(placeCardSuccessInfo.getPlayedCard().getId());
        this.gameData.addToList(this.gameData.getBoard(), placeCardSuccessInfo.getPlayedCard());
        this.gameData.replaceEntry(this.gameData.getLeaderboard(), this.username, placeCardSuccessInfo.getCardsPoints());
        this.gameData.setCardPoints(placeCardSuccessInfo.getCardsPoints());
        this.gameData.setGoalPoints(placeCardSuccessInfo.getGoalsPoint());
        this.gameData.setAvailablePositions(placeCardSuccessInfo.getAvailable());
        this.gameData.setSymbols(placeCardSuccessInfo.getSymbols());
        ui.placeCardSuccess();
        if(!gameData.getResourceDeck().isEmpty() || !gameData.getGoldDeck().isEmpty()){
            Decks choice = ui.askForDrawCard();
            if(choice.isResourceDeck()){
                ConnectionBridge.getInstance().drawResourceRequest(choice.getCard());
            } else {
                ConnectionBridge.getInstance().drawGoldRequest(choice.getCard());
            }
        } else {
            ConnectionBridge.getInstance().endTurn();
        }
    }

    /**
     * Handles the place card failure message
     */
    public void placeCardFailure(){
        ui.placeCardFailure();
        CardInfo playedCard = ui.askForPlayCard();
        ConnectionBridge.getInstance().placeCardRequest(playedCard);
    }

    /**
     * Handles the draw card success message
     * @param hand the updated hand
     */
    public void drawSuccess(ArrayList<CardInfo> hand){
        hand.forEach(x -> x.setFlipped(false));
        this.gameData.setHand(hand);
        ui.drawCardSuccess();
        ConnectionBridge.getInstance().endTurn();
    }

    /**
     * Handles the game state message
     */
    public void gameState(GameStateInfo gameStateInfo){
        this.gameData.fromGameStateInfo(gameStateInfo);
        if(!username.equals(gameStateInfo.getLastPlayer()) && gameStateInfo.getLastPlayer()!=null){
            ui.turnEnded(gameStateInfo);
        }
    }

    /**
     * Handles the game end message
     * @param leaderboard the leaderboard
     */
    public void gameEnd(Map<String, Integer> leaderboard){
        this.gameData.setGameAborted(true);
        this.gameData.setLeaderboard(leaderboard);
        ClientChatHandler.reset();
        ui.gameEnded();
        boolean newGame = ui.askForNewGame();
        if(newGame) {
            this.gameData= new ClientGameData();
            this.gameData.addObserver(this.ui.getData());
            this.lobbyRequest();
        } else {
            ui.goodbye();
        }
    }

    /**
     * Handles the player disconnected message
     */
    public void playerDisconnected(String username, boolean gameStarted) {
        this.gameData.replaceEntry(this.gameData.getConnectionStatus(), username, ConnectionStatus.OFFLINE);
        ui.playerDisconnected(username, gameStarted);
    }

    /**
     * Handles the player reconnected message
     */
    public void playerReconnected(String username) {
        this.gameData.replaceEntry(this.gameData.getConnectionStatus(), username, ConnectionStatus.ONLINE);
        ui.playerReconnected(username);
    }

    /**
     * Handles the reconnection state message
     */
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

    /**
     * getter for the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Handles the no other player connected message
     */
    public void noOtherPlayerConnected() {
        ui.noOtherPlayerConnected();
    }

    /** method to set up the chat handler */
    private void chatHandlerSetup() {
        new Thread(ClientChatHandler.getInstance()).start();
    }

    /** handle a received chat message
     * @param msg the chat message
     */
    public void recvChatMessage(ChatMessageData msg) {
        ClientChatHandler.recvChatMessage(msg);
        ui.messageReceived();
    }

    /**
     * getter for the game data
     * @return the game data
     */
    public ClientGameData getData() {
        return gameData;
    }

    /**
     * Handles the server not found message
     */
    public void serverNotFound() {
        ui.serverNotFound();
    }
}
