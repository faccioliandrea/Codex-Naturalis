package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

import java.awt.*;
import java.util.Collections;

/**
 * Abstract class for Playable Cards (Resource and Gold cards)
 */
public abstract class PlayableCard extends Card {
    private CardSymbolKingdom centerSymbol;

    private int points;

    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param coord Coordinates of the card (once placed)
     * @param flipped Front/back of the card
     * @param centerSymbol Center Symbol
     * @param frontCorners Front corners
     * @param points Points given once the card is placed
     */
    public PlayableCard(String id, Point coord, Boolean flipped, CardSymbolKingdom centerSymbol, Corner[] frontCorners, int points) {
        super(id, coord, flipped, frontCorners, Collections.nCopies(4, new Corner(false, null)).toArray(new Corner[4]));
        this.centerSymbol = centerSymbol;
        this.points = points;
    }

    /**
     * Getter for points attribute
     * @return points
     */
    public int getPoints() {
        return this.getFlipped() ? 0 : points;
    }

    /**
     * Getter for centerSymbol attribute
     * @return centerSymbol
     */
    public CardSymbolKingdom getCenterSymbol() {
        return this.getFlipped() ? null : centerSymbol;
    }
}