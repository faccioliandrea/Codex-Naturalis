package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.chat.MessagesQueue;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.connections.data.TurnInfo;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.PlayerColor;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ClientGameData extends Observable {
    private String username;
    private String currentPlayer;
    private String lastPlayer;
    private ArrayList<CardInfo> hand = new ArrayList<>();
    private ArrayList<CardInfo> resourceDeck = new ArrayList<>();
    private ArrayList<CardInfo> goldDeck = new ArrayList<>();
    private ArrayList<Point> availablePositions = new ArrayList<>();
    private int currentTurn = 0;
    private boolean isLastTurn = false;
    private ArrayList<CardInfo> board = new ArrayList<>();
    private Map<String, Integer> leaderboard = new HashMap<>();
    private Map<String, ArrayList<CardInfo>> boards = new HashMap<>();
    private Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
    private ArrayList<GoalInfo> goals = new ArrayList<>();
    private int cardPoints = 0;
    private int goalPoints = 0;
    private Map<CardSymbol, Integer> symbols;
    private boolean gameAborted = false;
    private Map<String, PlayerColor> playerColors;
    private MessagesQueue lastMessages = new MessagesQueue();


    public String getUsername() {
        return username;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
        setChanged();
        notifyObservers();
    }

    public synchronized ArrayList<CardInfo> getHand() {
        return hand;
    }

    public synchronized void removeCardFromHand(String cardId){
        hand.remove(hand.stream().filter(x->x.getId().equals(cardId)).findFirst().get());
        setChanged();
        notifyObservers();
    }

    public synchronized void setHand(ArrayList<CardInfo> hand) {
        this.hand = hand;
        setChanged();
        notifyObservers();
    }

    public synchronized ArrayList<CardInfo> getResourceDeck() {
        return resourceDeck;
    }

    public synchronized void setResourceDeck(ArrayList<CardInfo> resourceDeck) {
        this.resourceDeck = resourceDeck;
        setChanged();
        notifyObservers();
    }

    public synchronized ArrayList<CardInfo> getGoldDeck() {
        return goldDeck;
    }

    public synchronized void setGoldDeck(ArrayList<CardInfo> goldDeck) {
        this.goldDeck = goldDeck;
        setChanged();
        notifyObservers();
    }

    public synchronized ArrayList<Point> getAvailablePositions() {
        return availablePositions;
    }

    public synchronized void setAvailablePositions(ArrayList<Point> availablePositions) {
        this.availablePositions = availablePositions;
        setChanged();
        notifyObservers();
    }

    public synchronized int getCurrentTurn() {
        return currentTurn;
    }

    public synchronized void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
        setChanged();
        notifyObservers();
    }

    public synchronized boolean isLastTurn() {
        return isLastTurn;
    }

    public synchronized void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
        setChanged();
        notifyObservers();
    }

    public synchronized ArrayList<CardInfo> getBoard() {
        return board;
    }

    public synchronized void setBoard(ArrayList<CardInfo> board) {
        this.board = board;
        setChanged();
        notifyObservers();
    }

    public synchronized Map<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public synchronized void setLeaderboard(Map<String, Integer> leaderboard) {
        this.leaderboard = leaderboard;
        setChanged();
        notifyObservers();
    }

    public synchronized Map<String, ArrayList<CardInfo>> getBoards() {
        return boards;
    }

    public synchronized void setBoards(HashMap<String, ArrayList<CardInfo>> boards) {
        this.boards = boards;
        setChanged();
        notifyObservers();
    }

    public synchronized ArrayList<GoalInfo> getGoals() {
        return goals;
    }

    public synchronized void setGoals(ArrayList<GoalInfo> goals) {
        this.goals = (ArrayList<GoalInfo>) goals.stream().distinct().collect(Collectors.toList());
        setChanged();
        notifyObservers();
    }

    public synchronized int getCardPoints() {
        return cardPoints;
    }

    public synchronized void setCardPoints(int cardPoints) {
        this.cardPoints = cardPoints;
        setChanged();
        notifyObservers();
    }

    public synchronized int getGoalPoints() {
        return goalPoints;
    }

    public synchronized void addToList(List<CardInfo> list, CardInfo item) {
        list.add(item);
        setChanged();
        notifyObservers();
    }

    public synchronized void addToList(List<Point> list, Point item) {
        list.add(item);
        setChanged();
        notifyObservers();
    }

    public synchronized void addToList(List<GoalInfo> list, GoalInfo item) {
        list.add(item);
        setChanged();
        notifyObservers();
    }

    public synchronized void setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
        setChanged();
        notifyObservers();
    }

    public synchronized Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    public synchronized void setSymbols(Map<CardSymbol, Integer> symbols) {
        this.symbols = symbols;
        setChanged();
        notifyObservers();
    }

    public synchronized Map<String, ConnectionStatus> getConnectionStatus() {
        return connectionStatus;
    }

    public synchronized void setConnectionStatus(Map<String, ConnectionStatus> connectionStatus) {
        this.connectionStatus = connectionStatus;
        setChanged();
        notifyObservers();
    }

    public synchronized boolean isGameAborted() {
        return gameAborted;
    }

    public synchronized void setGameAborted(boolean gameAborted) {
        this.gameAborted = gameAborted;
        setChanged();
        notifyObservers();
    }

    public synchronized String getCurrentPlayer() {
        return currentPlayer;
    }

    public synchronized void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
        setChanged();
        notifyObservers();
    }

    public synchronized String getLastPlayer() {
        return lastPlayer;
    }

    public synchronized void setLastPlayer(String lastPlayer) {
        this.lastPlayer = lastPlayer;
        setChanged();
        notifyObservers();
    }

    public synchronized void setBoards(Map<String, ArrayList<CardInfo>> boards) {
        this.boards = boards;
        setChanged();
        notifyObservers();
    }

    public synchronized Map<String, PlayerColor> getPlayerColors() {
        return playerColors;
    }

    public synchronized void setPlayerColors(Map<String, PlayerColor> playerColors) {
        this.playerColors = playerColors;
        setChanged();
        notifyObservers();
    }

    public synchronized void fromTurnInfo(TurnInfo turnInfo) {
        this.hand = turnInfo.getHand();
        this.resourceDeck = turnInfo.getResourceDeck();
        this.goldDeck = turnInfo.getGoldDeck();
        this.board = turnInfo.getBoard();
        this.availablePositions = turnInfo.getAvailablePositions();
        this.currentTurn = turnInfo.getCurrentTurn();
        this.isLastTurn = turnInfo.isLastTurn();
        this.symbols = turnInfo.getSymbols();
        setChanged();
        notifyObservers();
    }

    public synchronized void fromGameStateInfo(GameStateInfo gameStateInfo) {
        this.username = gameStateInfo.getUsername();
        this.currentPlayer = gameStateInfo.getCurrentPlayer();
        this.lastPlayer = gameStateInfo.getLastPlayer();
        this.hand = gameStateInfo.getHand();
        this.resourceDeck = gameStateInfo.getResourceDeck();
        this.goldDeck = gameStateInfo.getGoldDeck();
        this.availablePositions = gameStateInfo.getAvailablePositions();
        this.currentTurn = gameStateInfo.getCurrentTurn();
        this.isLastTurn = gameStateInfo.isLastTurn();
        this.board = gameStateInfo.getBoard();
        this.symbols = gameStateInfo.getSymbols();
        this.leaderboard = gameStateInfo.getLeaderboard();
        this.boards = gameStateInfo.getBoards();
        this.connectionStatus = gameStateInfo.getConnectionStatus();
        this.goals = gameStateInfo.getGoals();
        this.cardPoints = gameStateInfo.getCardPoints();
        this.goalPoints = gameStateInfo.getGoalPoints();
        this.gameAborted = gameStateInfo.isGameAborted();
        this.playerColors = gameStateInfo.getPlayerColors();
        setChanged();
        notifyObservers();
    }

    public synchronized Integer replaceEntry(Map<String, Integer> map, String key, Integer value) {
        Integer old = map.replace(key, value);
        setChanged();
        notifyObservers();
        return old;
    }

    public synchronized ConnectionStatus replaceEntry(Map<String, ConnectionStatus> map, String key, ConnectionStatus value) {
        ConnectionStatus old = map.replace(key, value);
        setChanged();
        notifyObservers();
        return old;
    }

    public synchronized ArrayList<CardInfo> putEntry(Map<String, ArrayList<CardInfo>> map, String key, ArrayList<CardInfo> value) {
        ArrayList<CardInfo> old = map.put(key, value);
        setChanged();
        notifyObservers();
        return old;
    }

    public synchronized ConnectionStatus putEntry(Map<String, ConnectionStatus> map, String key, ConnectionStatus value) {
        ConnectionStatus old = map.put(key, value);
        setChanged();
        notifyObservers();
        return old;
    }

    public synchronized void setLastMessages(MessagesQueue lastMessages) {
        this.lastMessages = lastMessages;
        setChanged();
        notifyObservers();
    }

    public synchronized MessagesQueue getLastMessages() {
        return lastMessages;
    }
}
