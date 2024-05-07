package it.polimi.ingsw.connections.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameStateInfo implements Serializable {

    private String username;
    private ArrayList<CardInfo> resourceDeck;
    private ArrayList<CardInfo> goldDeck;
    private ArrayList<CardInfo> board;
    private int cardsPoints;
    private HashMap<String, Integer> leaderboard;

    public GameStateInfo(String username, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<CardInfo> board, int cardsPoints, HashMap<String, Integer> leaderboard) {
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.board = board;
        this.cardsPoints = cardsPoints;
        this.username = username;
        this.leaderboard = leaderboard;
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
}
