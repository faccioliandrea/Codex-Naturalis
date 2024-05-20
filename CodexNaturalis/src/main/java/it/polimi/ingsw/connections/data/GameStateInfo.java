package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.model.enumeration.CardSymbol;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameStateInfo implements Serializable {

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
    private Map<String, Integer> leaderboard = new HashMap<>();
    private Map<String, ArrayList<CardInfo>> boards = new HashMap<>();
    private Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
    private ArrayList<GoalInfo> goals = new ArrayList<>();
    private int cardPoints = 0;
    private int goalPoints = 0;
    private boolean gameAborted = false;

    public GameStateInfo(
            String username,
            String currentPlayer,
            String lastPlayer,
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




    public ArrayList<CardInfo> getResourceDeck() {
        return resourceDeck;
    }

    public ArrayList<CardInfo> getGoldDeck() {
        return goldDeck;
    }

    public ArrayList<CardInfo> getBoard() {
        return board;
    }

    public int getCardPoints() {
        return cardPoints;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getLastPlayer() {
        return lastPlayer;
    }

    public ArrayList<CardInfo> getHand() {
        return hand;
    }

    public ArrayList<Point> getAvailablePositions() {
        return availablePositions;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public boolean isLastTurn() {
        return isLastTurn;
    }

    public Map<String, ArrayList<CardInfo>> getBoards() {
        return boards;
    }

    public Map<String, ConnectionStatus> getConnectionStatus() {
        return connectionStatus;
    }

    public ArrayList<GoalInfo> getGoals() {
        return goals;
    }

    public int getGoalPoints() {
        return goalPoints;
    }

    public boolean isGameAborted() {
        return gameAborted;
    }
}
