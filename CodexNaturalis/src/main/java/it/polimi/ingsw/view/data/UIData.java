package it.polimi.ingsw.view.data;


import it.polimi.ingsw.chat.MessagesQueue;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.controller.client.ClientGameData;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.PlayerColor;

import java.awt.*;
import java.util.*;

/**
 * Class that contains the data to be displayed in the UI
 */
public class UIData implements Observer {
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
    private Map<CardSymbol, Integer> symbols;
    private Map<String, Integer> leaderboard = new LinkedHashMap<>();
    private Map<String, ArrayList<CardInfo>> boards = new HashMap<>();
    private Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
    private ArrayList<GoalInfo> goals = new ArrayList<>();
    private int cardPoints = 0;
    private int goalPoints = 0;
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
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the hand
     * @return the hand
     */
    public ArrayList<CardInfo> getHand() {
        return hand;
    }

    /**
     * Setter for the hand
     * @param hand the hand
     */
    public void setHand(ArrayList<CardInfo> hand) {
        this.hand = hand;
    }

    /**
     * Getter for the resource deck
     * @return the resource deck
     */
    public ArrayList<CardInfo> getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Setter for the resource deck
     * @param resourceDeck the resource deck
     */
    public void setResourceDeck(ArrayList<CardInfo> resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    /**
     * Getter for the gold deck
     * @return the gold deck
     */
    public ArrayList<CardInfo> getGoldDeck() {
        return goldDeck;
    }

    /**
     * Setter for the gold deck
     * @param goldDeck the gold deck
     */
    public void setGoldDeck(ArrayList<CardInfo> goldDeck) {
        this.goldDeck = goldDeck;
    }

    /**
     * Getter for the available positions
     * @return the available positions
     */
    public ArrayList<Point> getAvailablePositions() {
        return availablePositions;
    }

    /**
     * Setter for the available positions
     * @param availablePositions the available positions
     */
    public void setAvailablePositions(ArrayList<Point> availablePositions) {
        this.availablePositions = availablePositions;
    }

    /**
     * Getter for the current turn
     * @return the current turn
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Setter for the current turn
     * @param currentTurn the current turn
     */
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    /**
     * Getter for the last turn
     * @return the last turn
     */
    public boolean isLastTurn() {
        return isLastTurn;
    }

    /**
     * Setter for the last turn
     * @param lastTurn the last turn
     */
    public void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
    }

    /**
     * Getter for the board
     * @return the board
     */
    public ArrayList<CardInfo> getBoard() {
        return board;
    }

    /**
     * Setter for the board
     * @param board the board
     */
    public void setBoard(ArrayList<CardInfo> board) {
        this.board = board;
    }

    /**
     * Getter for the leaderboard
     * @return the leaderboard
     */
    public Map<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    /**
     * Setter for the leaderboard
     * @param leaderboard the leaderboard
     */
    public void setLeaderboard(Map<String, Integer> leaderboard) {
        this.leaderboard = leaderboard;
    }

    /**
     * Getter for the boards
     * @return the boards
     */
    public Map<String, ArrayList<CardInfo>> getBoards() {
        return boards;
    }

    /**
     * Setter for the boards
     * @param boards the boards
     */
    public void setBoards(HashMap<String, ArrayList<CardInfo>> boards) {
        this.boards = boards;
    }

    /**
     * Getter for the goals
     * @return the goals
     */
    public ArrayList<GoalInfo> getGoals() {
        return goals;
    }

    /**
     * Setter for the goals
     * @param goals the goals
     */
    public void setGoals(ArrayList<GoalInfo> goals) {
        this.goals = goals;
    }

    /**
     * Getter for the card points
     * @return the card points
     */
    public int getCardPoints() {
        return cardPoints;
    }

    /**
     * Setter for the card points
     * @param cardPoints the card points
     */
    public void setCardPoints(int cardPoints) {
        this.cardPoints = cardPoints;
    }

    /**
     * Getter for the goal points
     * @return the goal points
     */
    public int getGoalPoints() {
        return goalPoints;
    }

    /**
     * Setter for the goal points
     * @param goalPoints the goal points
     */
    public void setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
    }

    /**
     * Setter for all players boards
     * @param boards the boards
     */
    public void setBoards(Map<String, ArrayList<CardInfo>> boards) {
        this.boards = boards;
    }

    /**
     * Getter for the symbols
     * @return the symbols
     */
    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    /**
     * Setter for the symbols
     * @param symbols the symbols
     */
    public void setSymbols(Map<CardSymbol, Integer> symbols) {
        this.symbols = symbols;
    }

    /**
     * Getter for the connection status
     * @return the connection status
     */
    public Map<String, ConnectionStatus> getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * Setter for the connection status
     * @param connectionStatus the connection status
     */
    public void setConnectionStatus(Map<String, ConnectionStatus> connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    /**
     * Getter for the game aborted status
     * @return the game aborted status
     */
    public boolean isGameAborted() {
        return gameAborted;
    }

    /**
     * Setter for the game aborted status
     * @param gameAborted the game aborted status
     */
    public void setGameAborted(boolean gameAborted) {
        this.gameAborted = gameAborted;
    }

    /**
     * Getter for the current player
     * @return the current player
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Setter for the current player
     * @param currentPlayer the current player
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Getter for the last player
     * @return the last player
     */
    public String getLastPlayer() {
        return lastPlayer;
    }

    /**
     * Setter for the last player
     * @param lastPlayer the last player
     */
    public void setLastPlayer(String lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    /**
     * Getter for the player colors
     * @return the player colors
     */
    public Map<String, PlayerColor> getPlayerColors() {
        return playerColors;
    }

    /**
     * Setter for the player colors
     * @param playerColors the player colors
     */
    public void setPlayerColors(HashMap<String, PlayerColor> playerColors) {
        this.playerColors = playerColors;
    }

    /**
     * Getter for the public goals
     * @return the public goals
     */
    public ArrayList<GoalInfo> getPublicGoals() {
        return new ArrayList<>(this.goals.subList(0, 2));
    }

    /**
     * Getter for the private goal
     * @return the private goal
     */
    public GoalInfo getPrivateGoal() {
        return this.goals.get(2);
    }

    /**
     * Getter for the last messages
     * @return the last messages
     */
    public MessagesQueue getLastMessages() {
        return lastMessages;
    }

    /**
     * Getter for the last messages
     * @param limit the limit of messages to get
     * @return the last messages
     */
    public MessagesQueue getLastMessages(int limit) {
        return new MessagesQueue(lastMessages, limit);
    }

    /**
     * Setter for the last messages
     * @param lastMessages the last messages
     */
    public void setLastMessages(MessagesQueue lastMessages) {
        this.lastMessages = lastMessages;
    }

    /**
     * Update the data with the new data
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers} method.
     */
    @Override
    public void update(Observable o, Object arg) {
        this.username = ((ClientGameData)o).getUsername();
        this.currentPlayer = ((ClientGameData)o).getCurrentPlayer();
        this.lastPlayer = ((ClientGameData)o).getLastPlayer();
        this.hand = ((ClientGameData)o).getHand();
        this.resourceDeck = ((ClientGameData)o).getResourceDeck();
        this.goldDeck = ((ClientGameData)o).getGoldDeck();
        this.availablePositions = ((ClientGameData)o).getAvailablePositions();
        this.currentTurn = ((ClientGameData)o).getCurrentTurn();
        this.isLastTurn = ((ClientGameData)o).isLastTurn();
        this.board = ((ClientGameData)o).getBoard();
        this.boards = ((ClientGameData)o).getBoards();
        this.connectionStatus = ((ClientGameData)o).getConnectionStatus();
        this.goals = ((ClientGameData)o).getGoals();
        this.cardPoints = ((ClientGameData)o).getCardPoints();
        this.goalPoints = ((ClientGameData)o).getGoalPoints();
        this.symbols = ((ClientGameData)o).getSymbols();
        this.leaderboard = ((ClientGameData)o).getLeaderboard();
        this.gameAborted = ((ClientGameData)o).isGameAborted();
        this.playerColors = ((ClientGameData)o).getPlayerColors();
        this.lastMessages = ((ClientGameData)o).getLastMessages();
    }

    /**
     * Utility to check no other players are connected
     * @return no other player are connected
     */
    public boolean noOtherPlayerConnected() {
        return connectionStatus.keySet().stream().filter(k -> !k.equals(username)).allMatch(k -> connectionStatus.get(k) == ConnectionStatus.OFFLINE);
    }

    /**
     * Getter for the sorted leaderboard
     * @return the sorted leaderboard
     */
    public Map<String, Integer> getSortedLeaderboard() {
        return this.getLeaderboard();
    }
}
