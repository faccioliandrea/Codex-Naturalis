package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.connections.data.TurnInfo;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ClientGameData extends Observable {
    private String username;
    private ArrayList<CardInfo> hand = new ArrayList<>();
    private ArrayList<CardInfo> resourceDeck = new ArrayList<>();
    private ArrayList<CardInfo> goldDeck = new ArrayList<>();
    private ArrayList<Point> availablePositions = new ArrayList<>();
    private int currentTurn = 0;
    private boolean isLastTurn = false;
    private ArrayList<CardInfo> board = new ArrayList<>();
    private Map<String, Integer> leaderboard = new HashMap<>();
    private Map<String, ArrayList<CardInfo>> boards = new HashMap<>();
    private ArrayList<GoalInfo> goals = new ArrayList<>();
    private int cardPoints = 0;
    private int goalPoints = 0;
    private Map<CardSymbol, Integer> symbols;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        setChanged();
        notifyObservers();
    }

    public ArrayList<CardInfo> getHand() {
        return hand;
    }

    public void setHand(ArrayList<CardInfo> hand) {
        this.hand = hand;
        setChanged();
        notifyObservers();
    }

    public ArrayList<CardInfo> getResourceDeck() {
        return resourceDeck;
    }

    public void setResourceDeck(ArrayList<CardInfo> resourceDeck) {
        this.resourceDeck = resourceDeck;
        setChanged();
        notifyObservers();
    }

    public ArrayList<CardInfo> getGoldDeck() {
        return goldDeck;
    }

    public void setGoldDeck(ArrayList<CardInfo> goldDeck) {
        this.goldDeck = goldDeck;
        setChanged();
        notifyObservers();
    }

    public ArrayList<Point> getAvailablePositions() {
        return availablePositions;
    }

    public void setAvailablePositions(ArrayList<Point> availablePositions) {
        this.availablePositions = availablePositions;
        setChanged();
        notifyObservers();
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
        setChanged();
        notifyObservers();
    }

    public boolean isLastTurn() {
        return isLastTurn;
    }

    public void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
        setChanged();
        notifyObservers();
    }

    public ArrayList<CardInfo> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<CardInfo> board) {
        this.board = board;
        setChanged();
        notifyObservers();
    }

    public Map<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(HashMap<String, Integer> leaderboard) {
        this.leaderboard = leaderboard;
        setChanged();
        notifyObservers();
    }

    public Map<String, ArrayList<CardInfo>> getBoards() {
        return boards;
    }

    public void setBoards(HashMap<String, ArrayList<CardInfo>> boards) {
        this.boards = boards;
        setChanged();
        notifyObservers();
    }

    public ArrayList<GoalInfo> getGoals() {
        return goals;
    }

    public void setGoals(ArrayList<GoalInfo> goals) {
        this.goals = goals;
        setChanged();
        notifyObservers();
    }

    public int getCardPoints() {
        return cardPoints;
    }

    public void setCardPoints(int cardPoints) {
        this.cardPoints = cardPoints;
        setChanged();
        notifyObservers();
    }

    public int getGoalPoints() {
        return goalPoints;
    }

    public void addToList(List<CardInfo> list, CardInfo item) {
        list.add(item);
        setChanged();
        notifyObservers();
    }

    public void addToList(List<Point> list, Point item) {
        list.add(item);
        setChanged();
        notifyObservers();
    }

    public void addToList(List<GoalInfo> list, GoalInfo item) {
        list.add(item);
        setChanged();
        notifyObservers();
    }

    public void setGoalPoints(int goalPoints) {
        this.goalPoints = goalPoints;
        setChanged();
        notifyObservers();
    }

    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<CardSymbol, Integer> symbols) {
        this.symbols = symbols;
        setChanged();
        notifyObservers();
    }

    public void fromTurnInfo(TurnInfo turnInfo) {
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

    public Integer replaceEntry(Map<String, Integer> map, String key, Integer value) {
        Integer old = map.replace(key, value);
        setChanged();
        notifyObservers();
        return old;
    }

    public ArrayList<CardInfo> putEntry(Map<String, ArrayList<CardInfo>> map, String key, ArrayList<CardInfo> value) {
        ArrayList<CardInfo> old = map.put(key, value);
        setChanged();
        notifyObservers();
        return old;
    }
}
