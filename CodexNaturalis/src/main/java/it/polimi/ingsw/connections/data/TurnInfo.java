package it.polimi.ingsw.connections.data;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class TurnInfo implements Serializable {

    private ArrayList<CardInfo> hand;
    private ArrayList<CardInfo> resourceDeck;
    private ArrayList<CardInfo> goldDeck;
    private ArrayList<Point> availablePositions;
    private int currentTurn;
    private boolean isLastTurn;
    private ArrayList<CardInfo> board;

    public TurnInfo(ArrayList<CardInfo> hand, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<Point> availablePositions, int currentTurn, boolean isLastTurn, ArrayList<CardInfo> board) {
        this.hand = hand;
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.availablePositions = availablePositions;
        this.currentTurn = currentTurn;
        this.isLastTurn = isLastTurn;
        this.board = board;
    }


    public ArrayList<CardInfo> getHand() {
        return hand;
    }


    public ArrayList<CardInfo> getResourceDeck() {
        return resourceDeck;
    }

    public ArrayList<CardInfo> getGoldDeck() {
        return goldDeck;
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

    public ArrayList<CardInfo> getBoard() {
        return board;
    }

    public void addCardToBoard(CardInfo card){
        board.add(card);
    }

    public void setAvailablePositions(ArrayList<Point> availablePositions) {
        this.availablePositions = availablePositions;
    }

    public void setHand(ArrayList<CardInfo> hand) {
        this.hand = hand;
    }
}
