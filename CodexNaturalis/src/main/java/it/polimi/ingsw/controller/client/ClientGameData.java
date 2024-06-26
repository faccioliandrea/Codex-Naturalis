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

/**
 * Class that represents the game data on the client side
 */
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
    private Map<String, Integer> leaderboard = new LinkedHashMap<>();
    private Map<String, ArrayList<CardInfo>> boards = new HashMap<>();
    private Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
    private ArrayList<GoalInfo> goals = new ArrayList<>();
    private int cardPoints = 0;
    private int goalPoints = 0;
    private Map<CardSymbol, Integer> symbols;
    private boolean gameAborted = false;
    private Map<String, PlayerColor> playerColors;
    private MessagesQueue lastMessages = new MessagesQueue();

    /**
     * Getter for the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username
     * @param username: the username
     */
    public synchronized void setUsername(String username) {
        this.username = username;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the hand
     * @return the hand
     */
    public synchronized ArrayList<CardInfo> getHand() {
        return hand;
    }

    public synchronized void removeCardFromHand(String cardId){
        hand.remove(hand.stream().filter(x->x.getId().equals(cardId)).findFirst().orElse(null));
        setChanged();
        notifyObservers();
    }

    /**
     * Setter for the hand
     * @param hand: the hand
     */
    public synchronized void setHand(ArrayList<CardInfo> hand) {
        this.hand = hand;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the resource deck
     * @return the resource deck
     */
    public synchronized ArrayList<CardInfo> getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Setter for the resource deck
     * @param resourceDeck: the resource deck
     */
    public synchronized void setResourceDeck(ArrayList<CardInfo> resourceDeck) {
        this.resourceDeck = resourceDeck;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the gold deck
     * @return the gold deck
     */
    public synchronized ArrayList<CardInfo> getGoldDeck() {
        return goldDeck;
    }

    /**
     * Setter for the gold deck
     * @param goldDeck: the gold deck
     */
    public synchronized void setGoldDeck(ArrayList<CardInfo> goldDeck) {
        this.goldDeck = goldDeck;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the available positions
     * @return the available positions
     */
    public synchronized ArrayList<Point> getAvailablePositions() {
        return availablePositions;
    }

    /**
     * Setter for the available positions
     * @param availablePositions: the available positions
     */
    public synchronized void setAvailablePositions(ArrayList<Point> availablePositions) {
        this.availablePositions = availablePositions;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the current turn
     * @return the current turn
     */
    public synchronized int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Setter for the current turn
     * @param currentTurn: the current turn
     */
    public synchronized void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the last turn
     * @return true if it is the last turn, false otherwise
     */
    public synchronized boolean isLastTurn() {
        return isLastTurn;
    }

    /**
     * Setter for the last turn
     * @param lastTurn: true if it is the last turn, false otherwise
     */
    public synchronized void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the board
     * @return the board
     */
    public synchronized ArrayList<CardInfo> getBoard() {
        return board;
    }

    /**
     * Setter for the board
     * @param board: the board
     */
    public synchronized void setBoard(ArrayList<CardInfo> board) {
        this.board = board;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the leaderboard
     * @return the leaderboard
     */
    public synchronized Map<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    /**
     * Setter for the leaderboard
     * @param leaderboard: the leaderboard
     */
    public synchronized void setLeaderboard(Map<String, Integer> leaderboard) {
        this.leaderboard = leaderboard;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the other player's boards
     * @return the boards
     */
    public synchronized Map<String, ArrayList<CardInfo>> getBoards() {
        return boards;
    }

    /**
     * Setter for the other player's boards
     * @param boards: the boards
     */
    public synchronized void setBoards(HashMap<String, ArrayList<CardInfo>> boards) {
        this.boards = boards;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the goals
     * @return the goals
     */
    public synchronized ArrayList<GoalInfo> getGoals() {
        return goals;
    }

    /**
     * Setter for the goals
     * @param goals: the goals
     */
    public synchronized void setGoals(ArrayList<GoalInfo> goals) {
        this.goals = (ArrayList<GoalInfo>) goals.stream().distinct().collect(Collectors.toList());
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the card points
     * @return the card points
     */
    public synchronized int getCardPoints() {
        return cardPoints;
    }

    /**
     * Setter for the card points
     * @param cardPoints: the card points
     */
    public synchronized void setCardPoints(int cardPoints) {
        this.cardPoints = cardPoints;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the goal points
     * @return the goal points
     */
    public synchronized int getGoalPoints() {
        return goalPoints;
    }

    /**
     * Add a card to a list
     * @param list the list
     * @param item the item to add
     */
    public synchronized void addToList(List<CardInfo> list, CardInfo item) {
        list.add(item);
        setChanged();
        notifyObservers();
    }

    /**
     * Add a point to a list
     * @param list the list
     * @param item the item to add
     */
    public synchronized void addToList(List<Point> list, Point item) {
        list.add(item);
        setChanged();
        notifyObservers();
    }

    /**
     * Add a goal to a list
     * @param list the list
     * @param item the item to add
     */
    public synchronized void addToList(List<GoalInfo> list, GoalInfo item) {
        list.add(item);
        setChanged();
        notifyObservers();
    }

    /**
     * Setter for the goal points
     * @param goalPoints: the goal points
     */
    public synchronized void setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the symbols
     * @return the symbols
     */
    public synchronized Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    /**
     * Setter for the symbols
     * @param symbols: the symbols
     */
    public synchronized void setSymbols(Map<CardSymbol, Integer> symbols) {
        this.symbols = symbols;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the connection status
     * @return the connection status
     */
    public synchronized Map<String, ConnectionStatus> getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * Setter for the connection status
     * @param connectionStatus: the connection status
     */
    public synchronized void setConnectionStatus(Map<String, ConnectionStatus> connectionStatus) {
        this.connectionStatus = connectionStatus;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the game status
     * @return true if the game is aborted, false otherwise
     */
    public synchronized boolean isGameAborted() {
        return gameAborted;
    }

    /**
     * Setter for the game status
     * @param gameAborted: true if the game is aborted, false otherwise
     */
    public synchronized void setGameAborted(boolean gameAborted) {
        this.gameAborted = gameAborted;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the current player
     * @return the current player
     */
    public synchronized String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Setter for the current player
     * @param currentPlayer: the current player
     */
    public synchronized void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the last player
     * @return the last player
     */
    public synchronized String getLastPlayer() {
        return lastPlayer;
    }

    /**
     * Setter for the last player
     * @param lastPlayer: the last player
     */
    public synchronized void setLastPlayer(String lastPlayer) {
        this.lastPlayer = lastPlayer;
        setChanged();
        notifyObservers();
    }

    /**
     * Setter for all the player's boards
     * @param boards the boards
     */
    public synchronized void setBoards(Map<String, ArrayList<CardInfo>> boards) {
        this.boards = boards;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the player colors
     * @return the player colors
     */
    public synchronized Map<String, PlayerColor> getPlayerColors() {
        return playerColors;
    }

    /**
     * Setter for the player colors
     * @param playerColors: the player colors
     */
    public synchronized void setPlayerColors(Map<String, PlayerColor> playerColors) {
        this.playerColors = playerColors;
        setChanged();
        notifyObservers();
    }

    /**
     * Updates the game data with the information from the turn state
     * @param turnInfo: the turn information
     */
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

    /**
     * Updates the game data with the information from the game state
     * @param gameStateInfo: the game state information
     */
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

    /**
     * Replace an entry in a map
     * @param map the map
     * @param key the key
     * @param value the value
     * @return the old value
     */
    public synchronized Integer replaceEntry(Map<String, Integer> map, String key, Integer value) {
        Integer old = map.replace(key, value);
        setChanged();
        notifyObservers();
        return old;
    }

    /**
     * Replace an entry in a map
     * @param map the map
     * @param key the key
     * @param value the value
     * @return the old value
     */
    public synchronized ConnectionStatus replaceEntry(Map<String, ConnectionStatus> map, String key, ConnectionStatus value) {
        ConnectionStatus old = map.replace(key, value);
        setChanged();
        notifyObservers();
        return old;
    }

    /**
     * Put an entry in a map
     * @param map the map
     * @param key the key
     * @param value the value
     * @return the old value
     */
    public synchronized ArrayList<CardInfo> putEntry(Map<String, ArrayList<CardInfo>> map, String key, ArrayList<CardInfo> value) {
        ArrayList<CardInfo> old = map.put(key, value);
        setChanged();
        notifyObservers();
        return old;
    }

    /**
     * Put an entry in a map
     * @param map the map
     * @param key the key
     * @param value the value
     * @return the old value
     */
    public synchronized ConnectionStatus putEntry(Map<String, ConnectionStatus> map, String key, ConnectionStatus value) {
        ConnectionStatus old = map.put(key, value);
        setChanged();
        notifyObservers();
        return old;
    }

    /**
     * Setter for the last messages
     * @param lastMessages queue of messages
     */
    public synchronized void setLastMessages(MessagesQueue lastMessages) {
        this.lastMessages = lastMessages;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for the last messages
     * @return messages queue
     */
    public synchronized MessagesQueue getLastMessages() {
        return lastMessages;
    }
}
