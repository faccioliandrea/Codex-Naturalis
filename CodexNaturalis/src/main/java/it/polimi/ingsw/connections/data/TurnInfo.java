package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.model.enumeration.CardSymbol;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TurnInfo implements Serializable {

    private final ArrayList<CardInfo> hand;
    private final ArrayList<CardInfo> resourceDeck;
    private final ArrayList<CardInfo> goldDeck;
    private final ArrayList<Point> availablePositions;
    private final int currentTurn;
    private final boolean isLastTurn;
    private final ArrayList<CardInfo> board;
    private final Map<CardSymbol, Integer> symbols;

    public TurnInfo(ArrayList<CardInfo> hand, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<Point> availablePositions, int currentTurn, Map<CardSymbol, Integer> symbols, boolean isLastTurn, ArrayList<CardInfo> board) {
        this.hand = hand;
        this.resourceDeck = resourceDeck;
        this.goldDeck = goldDeck;
        this.availablePositions = availablePositions;
        this.currentTurn = currentTurn;
        this.symbols = symbols;
        this.isLastTurn = isLastTurn;
        this.board = board;
    }

    public TurnInfo() {
        this.hand = new ArrayList<>();
        this.resourceDeck = new ArrayList<>();
        this.goldDeck = new ArrayList<>();
        this.availablePositions = new ArrayList<>();
        this.currentTurn = 0;
        this.isLastTurn = false;
        this.board = new ArrayList<>();
        this.symbols = new LinkedHashMap<>();
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

    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }
}
