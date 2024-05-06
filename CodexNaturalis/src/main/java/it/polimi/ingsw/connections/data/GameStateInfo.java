package it.polimi.ingsw.connections.data;

import java.io.Serializable;
import java.util.ArrayList;

public class GameStateInfo implements Serializable {

    private String username;
    private ArrayList<CardInfo> resourceDeck;
    private ArrayList<CardInfo> goldDeck;
    private ArrayList<CardInfo> board;
    private int cardsPoints;

    public GameStateInfo(String username, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<CardInfo> board, int cardsPoints) {
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.board = board;
        this.cardsPoints = cardsPoints;
        this.username = username;
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
}
