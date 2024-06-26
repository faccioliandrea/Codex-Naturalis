package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.model.enumeration.CardSymbol;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class contains the information to be sent to the client after a successful placement of a card
 */
public class PlaceCardSuccessInfo implements Serializable {
    final private int cardsPoints;
    final private int goalsPoints;
    final private CardInfo playedCard;
    final private ArrayList<Point> available;
    final private Map<CardSymbol, Integer> symbols;

    /** Constructor
     * @param cardsPoints the updated cards points
     * @param goalsPoints the points obtained by completing goals
     * @param playedCard the card that has been placed
     * @param available the available positions to place the card
     * @param symbols the symbols obtained by placing the card
     */
    public PlaceCardSuccessInfo(int cardsPoints, int goalsPoints, CardInfo playedCard, ArrayList<Point> available, Map<CardSymbol, Integer> symbols) {
        this.cardsPoints = cardsPoints;
        this.goalsPoints = goalsPoints;
        this.playedCard = playedCard;
        this.available = available;
        this.symbols = new LinkedHashMap<>(symbols);
    }

    /**
     * Method that returns the updated cards points
     * @return the updated cards points
     */
    public int getCardsPoints() {
        return cardsPoints;
    }

    /**
     * Method that returns the card that has been placed
     * @return the card that has been placed
     */
    public CardInfo getPlayedCard() {
        return playedCard;
    }

    /**
     * Method that returns the updated available positions
     * @return the updated available positions
     */
    public ArrayList<Point> getAvailable() {
        return available;
    }

    /**
     * Method that returns the symbols in the board
     * @return the symbols
     */
    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    /**
     * Method that returns the updated goals points
     * @return the updated goals points
     */
    public int getGoalsPoint() {return goalsPoints;}
}
