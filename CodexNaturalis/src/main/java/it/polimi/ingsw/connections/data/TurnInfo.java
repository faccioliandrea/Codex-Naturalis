package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.model.enumeration.CardSymbol;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class contains the information to be sent to the players at the beginning of their turn.
 */
public class TurnInfo implements Serializable {
    private final ArrayList<CardInfo> hand;
    private final ArrayList<CardInfo> resourceDeck;
    private final ArrayList<CardInfo> goldDeck;
    private final ArrayList<Point> availablePositions;
    private final int currentTurn;
    private final boolean isLastTurn;
    private final ArrayList<CardInfo> board;
    private final Map<CardSymbol, Integer> symbols;

    /** Constructor:
     * @param hand the hand of the player
     * @param resourceDeck the resource deck
     * @param goldDeck the gold deck
     * @param availablePositions the available positions
     * @param currentTurn the current turn
     * @param symbols the symbols
     * @param isLastTurn true if it is the last turn
     * @param board the board
     */
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

    /**
     * This method returns the hand of the player.
     * @return the hand of the player
     */
    public ArrayList<CardInfo> getHand() {
        return hand;
    }

    /**
     * This method returns the resource deck.
     * @return the resource deck
     */
    public ArrayList<CardInfo> getResourceDeck() {
        return resourceDeck;
    }

    /**
     * This method returns the gold deck.
     * @return the gold deck
     */
    public ArrayList<CardInfo> getGoldDeck() {
        return goldDeck;
    }

    /**
     * This method returns the available positions.
     * @return the available positions
     */
    public ArrayList<Point> getAvailablePositions() {
        return availablePositions;
    }

    /**
     * This method returns the current turn.
     * @return the current turn
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * This method returns true if it is the last turn.
     * @return true if it is the last turn
     */
    public boolean isLastTurn() {
        return isLastTurn;
    }

    /**
     * This method returns the player board.
     * @return the board
     */
    public ArrayList<CardInfo> getBoard() {
        return board;
    }

    /**
     * This method returns the symbols.
     * @return the symbols
     */
    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }
}
