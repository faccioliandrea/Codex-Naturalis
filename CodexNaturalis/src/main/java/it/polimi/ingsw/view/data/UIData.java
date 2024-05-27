package it.polimi.ingsw.view.data;


import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.controller.client.ClientGameData;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.PlayerColor;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

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
    private Map<String, Integer> leaderboard = new HashMap<>();
    private Map<String, ArrayList<CardInfo>> boards = new HashMap<>();
    private Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
    private ArrayList<GoalInfo> goals = new ArrayList<>();
    private int cardPoints = 0;
    private int goalPoints = 0;
    private boolean gameAborted = false;
    private Map<String, PlayerColor> playerColors;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<CardInfo> getHand() {
        return hand;
    }

    public void setHand(ArrayList<CardInfo> hand) {
        this.hand = hand;
    }

    public ArrayList<CardInfo> getResourceDeck() {
        return resourceDeck;
    }

    public void setResourceDeck(ArrayList<CardInfo> resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    public ArrayList<CardInfo> getGoldDeck() {
        return goldDeck;
    }

    public void setGoldDeck(ArrayList<CardInfo> goldDeck) {
        this.goldDeck = goldDeck;
    }

    public ArrayList<Point> getAvailablePositions() {
        return availablePositions;
    }

    public void setAvailablePositions(ArrayList<Point> availablePositions) {
        this.availablePositions = availablePositions;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public boolean isLastTurn() {
        return isLastTurn;
    }

    public void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
    }

    public ArrayList<CardInfo> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<CardInfo> board) {
        this.board = board;
    }

    public Map<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(HashMap<String, Integer> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public Map<String, ArrayList<CardInfo>> getBoards() {
        return boards;
    }

    public void setBoards(HashMap<String, ArrayList<CardInfo>> boards) {
        this.boards = boards;
    }

    public ArrayList<GoalInfo> getGoals() {
        return goals;
    }

    public void setGoals(ArrayList<GoalInfo> goals) {
        this.goals = goals;
    }

    public int getCardPoints() {
        return cardPoints;
    }

    public void setCardPoints(int cardPoints) {
        this.cardPoints = cardPoints;
    }

    public int getGoalPoints() {
        return goalPoints;
    }

    public void setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
    }

    public void setLeaderboard(Map<String, Integer> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public void setBoards(Map<String, ArrayList<CardInfo>> boards) {
        this.boards = boards;
    }

    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<CardSymbol, Integer> symbols) {
        this.symbols = symbols;
    }

    public Map<String, ConnectionStatus> getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(Map<String, ConnectionStatus> connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public boolean isGameAborted() {
        return gameAborted;
    }

    public void setGameAborted(boolean gameAborted) {
        this.gameAborted = gameAborted;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getLastPlayer() {
        return lastPlayer;
    }

    public void setLastPlayer(String lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public Map<String, PlayerColor> getPlayerColors() {
        return playerColors;
    }

    public void setPlayerColors(HashMap<String, PlayerColor> playerColors) {
        this.playerColors = playerColors;
    }

    public ArrayList<GoalInfo> getPublicGoals() {
        return new ArrayList<>(this.goals.subList(0, 1));
    }

    public GoalInfo getPrivateGoal() {
        return this.goals.get(2);
    }

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
    }

    public LinkedHashMap<String, Integer> getSortedLeaderboard() {
        return this.getLeaderboard().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new));
    }
}
