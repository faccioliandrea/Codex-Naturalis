package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.PlayerColor;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class contains the information about the current state of the game. It is used to send the information of the game to the client.
 */
public class GameStateInfo implements Serializable {
    final private String username;
    final private String currentPlayer;
    final private String lastPlayer;
    final private Map<String, PlayerColor> playerColors;
    private ArrayList<CardInfo> hand;
    private ArrayList<CardInfo> resourceDeck;
    private ArrayList<CardInfo> goldDeck;
    private ArrayList<Point> availablePositions;
    private int currentTurn;
    private boolean isLastTurn;
    private ArrayList<CardInfo> board;
    final private Map<CardSymbol, Integer> symbols;
    private Map<String, Integer> leaderboard;
    private Map<String, ArrayList<CardInfo>> boards;
    private Map<String, ConnectionStatus> connectionStatus;
    private ArrayList<GoalInfo> goals;
    private int cardPoints;
    private int goalPoints;
    private boolean gameAborted;

    /** Constructor for the game state info
     * @param username the username of the player
     * @param currentPlayer the current player
     * @param lastPlayer the last player
     * @param playerColors the colors of the players
     * @param hand the hand of the player
     * @param resourceDeck the resource deck
     * @param goldDeck the gold deck
     * @param availablePositions the available positions
     * @param currentTurn the current turn
     * @param isLastTurn if it is the last turn
     * @param board the board
     * @param symbols the symbols
     * @param leaderboard the leaderboard
     * @param boards the boards
     * @param connectionStatus the connection status
     * @param sharedGoals the shared goals
     * @param privateGoal the private goal
     * @param cardPoints the card points
     * @param goalPoints the goal points
     * @param gameAborted if the game is aborted
     */
    public GameStateInfo(
            String username,
            String currentPlayer,
            String lastPlayer,
            Map<String, PlayerColor> playerColors,
            ArrayList<CardInfo> hand,
            ArrayList<CardInfo> resourceDeck,
            ArrayList<CardInfo> goldDeck,
            ArrayList<Point> availablePositions,
            int currentTurn,
            boolean isLastTurn,
            ArrayList<CardInfo> board,
            Map<CardSymbol, Integer> symbols,
            Map<String, Integer> leaderboard,
            Map<String, ArrayList<CardInfo>> boards,
            Map<String, ConnectionStatus> connectionStatus,
            ArrayList<GoalInfo> sharedGoals,
            GoalInfo privateGoal,
            int cardPoints,
            int goalPoints,
            boolean gameAborted
    ) {
        this.username = username;
        this.currentPlayer = currentPlayer;
        this.lastPlayer = lastPlayer;
        this.playerColors = playerColors;
        this.hand = hand;
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.availablePositions = availablePositions;
        this.currentTurn = currentTurn;
        this.isLastTurn = isLastTurn;
        this.board = board;
        this.symbols = symbols;
        this.leaderboard = leaderboard;
        this.boards = boards;
        this.connectionStatus = connectionStatus;
        this.goals = sharedGoals;
        this.goals.add(privateGoal);
        this.cardPoints = cardPoints;
        this.goalPoints = goalPoints;
        this.gameAborted = gameAborted;
    }

    /**
     * Method to get the resource deck
     * @return the resource deck
     */
    public ArrayList<CardInfo> getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Method to get the gold deck
     * @return the gold deck
     */
    public ArrayList<CardInfo> getGoldDeck() {
        return goldDeck;
    }

    /**
     * Method to get the board
     * @return the board
     */
    public ArrayList<CardInfo> getBoard() {
        return board;
    }

    /**
     * Method to get the card points
     * @return the card points
     */
    public int getCardPoints() {
        return cardPoints;
    }

    /**
     * Method to get the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method to get the leaderboard
     * @return the leaderboard
     */
    public Map<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    /**
     * Method to get the symbols on the board
     * @return the symbols
     */
    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    /**
     * Method to get the current player
     * @return the current player
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method to get the last player
     * @return the last player
     */
    public String getLastPlayer() {
        return lastPlayer;
    }

    /**
     * Method to get the hand of the player
     * @return the hand of the player
     */
    public ArrayList<CardInfo> getHand() {
        return hand;
    }

    /**
     * Method to get the available positions
     * @return the available positions
     */
    public ArrayList<Point> getAvailablePositions() {
        return availablePositions;
    }

    /**
     * Method to get the current turn
     * @return the current turn
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Method to check if it is the last turn
     * @return true if it is the last turn, false otherwise
     */
    public boolean isLastTurn() {
        return isLastTurn;
    }

    /**
     * Method to get the boards of the players
     * @return the boards
     */
    public Map<String, ArrayList<CardInfo>> getBoards() {
        return boards;
    }

    /**
     * Method to get the connection status of the players
     * @return the connection status
     */
    public Map<String, ConnectionStatus> getConnectionStatus() {
        return connectionStatus;
    }

    /**
     * Method to get the goals
     * @return the goals
     */
    public ArrayList<GoalInfo> getGoals() {
        return goals;
    }

    /**
     * Method to get the goal points
     * @return the goal points
     */
    public int getGoalPoints() {
        return goalPoints;
    }

    /**
     * Method to check if the game is aborted
     * @return true if the game is aborted, false otherwise
     */
    public boolean isGameAborted() {
        return gameAborted;
    }

    /**
     * Method to get the colors of the players
     * @return the colors of the players
     */
    public Map<String, PlayerColor> getPlayerColors() {
        return playerColors;
    }
}
