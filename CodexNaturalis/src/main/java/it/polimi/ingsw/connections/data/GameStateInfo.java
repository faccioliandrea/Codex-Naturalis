package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.model.enumeration.CardSymbol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameStateInfo implements Serializable {

    private String username;
    private ArrayList<CardInfo> resourceDeck;
    private ArrayList<CardInfo> goldDeck;
    private ArrayList<CardInfo> board;
    private int cardsPoints;
    private HashMap<String, Integer> leaderboard;
    private Map<CardSymbol, Integer> symbols;

    public GameStateInfo(String username, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<CardInfo> board, int cardsPoints, HashMap<String, Integer> leaderboard, Map<CardSymbol, Integer> symbols) {
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.board = board;
        this.cardsPoints = cardsPoints;
        this.username = username;
        this.leaderboard = leaderboard;
        this.symbols = symbols;
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

    public int getCardsPoints() {
        return cardsPoints;
    }

    public String getUsername() {
        return username;
    }

    public HashMap<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }
}
