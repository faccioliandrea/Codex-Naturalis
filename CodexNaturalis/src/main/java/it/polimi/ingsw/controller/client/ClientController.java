package it.polimi.ingsw.controller.client;


import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.view.TUIColors;
import it.polimi.ingsw.view.UserInterface;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientController {

    private UserInterface ui;
    private String username;
    private ConnectionBridge connectionBridge;
    private TurnInfo currentTurnInfo = new TurnInfo();
    private HashMap<String, Integer> leaderboard = new HashMap<>();
    private HashMap<String, ArrayList<CardInfo>> boards = new HashMap<>();
    private ArrayList<GoalInfo> goals = new ArrayList<>();

    public ClientController(UserInterface ui) {
        this.ui = ui;
        connectionBridge = new ConnectionBridge(this);
        ui.setController(this);
        ui.printColorDebug(TUIColors.YELLOW, "Type :help to see all the available commands.");
    }

    public String loginRequest() {
        this.username = ui.askForUsername();
        return username;
    }

    public void invalidUsername(){
        ui.printColorDebug(TUIColors.RED, this.username + " is already taken. Please select a new one");
        connectionBridge.loginRequest();
    }

    public void validUsername() {
        ui.printDebug("Welcome " + username + "!");
        connectionBridge.lobbyRequest();
    }

    public void lobbyRequest() {
        connectionBridge.lobbyRequest();
    }


    public void lobbyDoesNotExist() {
        //TODO: comunicare alla view che non esistono lobby
        ui.printDebug("There are no lobbies available. Let's create one!");
        int n = ui.askForPlayerNum();
        connectionBridge.createLobbyRequest(n);
    }

    public void lobbyExists(ArrayList<String> lobbies) {
        //TODO: comunicare alla view le lobbies esistenti
        String id = ui.askForLobbyId(lobbies);
        connectionBridge.joinLobbyRequest(id);
    }


    public void joinLobbySuccess(boolean isLastPlayer) {
        //TODO: comunicare alla view che è entrato nella lobby

        if (isLastPlayer) {
            ui.printDebug("You joined the lobby. The game will start soon!");
            connectionBridge.createGame();

        } else {
            ui.printDebug("You joined the lobby. Waiting for other players to join...");
        }
    }
    public void lobbyFull(){
        //TODO: comunicare alla view che la lobby è piena
        ui.printDebug("lobby full");
    }

    public void playerJoinedLobby(String username) {
        //TODO: comunicare alla view che un giocatore è entrato nella lobby
        ui.printDebug(String.format("%s joined lobby", username));
    }

    public void lobbyIsReady() {

    }

    public void gameStarted(StarterData starterData){
        ui.printDebug("Game started");
        currentTurnInfo.setHand(starterData.getHand());
        ui.printColorDebug(TUIColors.CYAN, "Your current hand:");
        starterData.getHand().forEach(x->ui.printDebug(String.format("Card id: %s", x.getId())));
        ui.printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
        ui.printColorDebug(TUIColors.CYAN, "Game shared goals:");
        starterData.getSharedGoals().forEach(ui::printGoalInfo);
        ui.printColorDebug(TUIColors.CYAN,"You have to choose one of the following private goals:");
        starterData.getPrivateGoals().forEach(ui::printGoalInfo);
        ui.printColorDebug(TUIColors.CYAN,"You have to choose your starter card side:");
        ui.printCardInfo(starterData.getStarterCard());
        int chosenGoal = ui.askForPrivateGoal();
        connectionBridge.choosePrivateGoalRequest(chosenGoal);
        this.leaderboard = starterData.getUsers().stream().collect(Collectors.toMap(s -> s, s -> 0, (x, y) -> x, HashMap::new));
        this.boards = starterData.getUsers().stream().collect(Collectors.toMap(s -> s, x -> new ArrayList<>(), (x, y) -> x, HashMap::new));
        // TODO: move this in privateGoalChosen?
        this.goals = starterData.getSharedGoals();
        this.goals.add(starterData.getPrivateGoals().get(chosenGoal));
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
        ui.displayBoard(turnInfo.getBoard(), turnInfo.getAvailablePositions(), turnInfo.getSymbols());
        ui.printColorDebug(TUIColors.CYAN, "Your current hand:");
        turnInfo.getHand().forEach(x->ui.printDebug(String.format("Card id: %s", x.getId())));
        ui.printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
        CardInfo playedCard = ui.askForPlayCard(turnInfo.getHand(), turnInfo.getAvailablePositions());
        connectionBridge.placeCardRequest(playedCard);
    }


    public void placeCardSuccess(PlaceCardSuccessInfo placeCardSuccessInfo){
        currentTurnInfo.addCardToBoard(placeCardSuccessInfo.getPlayedCard());
        currentTurnInfo.setSymbols(placeCardSuccessInfo.getSymbols());
        //TODO: comunicare alla view che la carta è stata posizionata
        ui.printDebug("Card placed!");
        ui.printDebug("You currently have " + placeCardSuccessInfo.getCardsPoint() + " points and you will score " + placeCardSuccessInfo.getGoalsPoints() + " points from the goals!");
        this.leaderboard.replace(this.username, placeCardSuccessInfo.getCardsPoint());
        ui.printColorDebug(TUIColors.CYAN,"This is your board:");
        ui.displayBoard(currentTurnInfo.getBoard(), null, placeCardSuccessInfo.getSymbols());
        ui.printColorDebug(TUIColors.CYAN,"Resource deck:");
        currentTurnInfo.getResourceDeck().forEach(x->ui.printDebug(String.format("Card id: %s", x.getId())));
        ui.printColorDebug(TUIColors.CYAN,"Gold deck:");
        currentTurnInfo.getGoldDeck().forEach(x->ui.printDebug(String.format("Card id: %s", x.getId())));
        ui.printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
        this.currentTurnInfo.setAvailablePositions(placeCardSuccessInfo.getAvailable());
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
        hand.forEach(x -> x.setFlipped(false));
        this.currentTurnInfo.setHand(hand);
        hand.forEach(x->ui.printDebug(String.format("Card id: %s", x.getId())));
        ui.printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
        connectionBridge.endTurn();
    }


    public void gameState(GameStateInfo gameStateInfo){
        ui.printDebug(gameStateInfo.getUsername() + " has ended his turn.");
        ui.printColorDebug(TUIColors.CYAN, "This is his current board:");
        ui.displayBoard(gameStateInfo.getBoard(), null, gameStateInfo.getSymbols());
        ui.printDebug("He currently has " + gameStateInfo.getCardsPoints() + " points");
        ui.printColorDebug(TUIColors.CYAN,"Resource deck:");
        gameStateInfo.getResourceDeck().forEach(x->ui.printDebug(String.format("Card id: %s", x.getId())));
        ui.printColorDebug(TUIColors.CYAN,"Gold deck:");
        gameStateInfo.getResourceDeck().forEach(x->ui.printDebug(String.format("Card id: %s", x.getId())));
        ui.printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
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
        boolean newGame = ui.askForNewGame();
        if(newGame) {
            this.lobbyRequest();
        } else {
            ui.printColorDebug(TUIColors.BLUE, "See you next time!");
            // TODO: quit
        }
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

    public ArrayList<GoalInfo> getGoals() {
        return goals;
    }
}
