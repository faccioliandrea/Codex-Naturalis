package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

import java.awt.*;
import java.util.Collections;

/**
 * Abstract class for Playable Cards (Resource and Gold cards)
 */
public abstract class PlayableCard extends Card {
    private CardSymbolKingdom centerSymbol;
    private Corner[] corners;
    private int points;

    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param coord Coordinates of the card (once placed)
     * @param flipped Front/back of the card
     * @param centerSymbol Center Symbol
     * @param corners Corners
     * @param points Points given once the card is placed
     */
    public PlayableCard(String id, Point coord, Boolean flipped, CardSymbolKingdom centerSymbol, Corner[] corners, int points) {
        super(id, coord, flipped);
        this.centerSymbol = centerSymbol;
        this.corners = corners;
        this.points = points;
    }

    /**
     * Getter for points attribute
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for centerSymbol attribute
     * @return centerSymbol
     */
    public CardSymbolKingdom getCenterSymbol() {
        return centerSymbol;
    }

    /**
     * Getter for corners attribute
     * @return corners
     */
    public Corner[] getCorners() {
        return corners;
    }

    /**
     * Method to manage the state of the card when flipped and placed
     * Sets the card corners to not covered and w/o symbol if the attribute flipped is true
     */
    @Override
    public void flipCard() {
        if(this.getFlipped()) {
            Corner availableCorner = new Corner(false, null);
            this.corners = Collections.nCopies(4, availableCorner).toArray(new Corner[4]);
        }
    }
}