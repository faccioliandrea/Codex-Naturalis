package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

import java.awt.*;

/**
 * Class representing a StarterCard of the game
 */
public class StarterCard extends Card{

    private CardSymbolKingdom[] centerSymbols;

    /**
     * Default constructor
     *
     * @param id      Card ID
     * @param coord   Coordinates of the card (once placed)
     * @param flipped Front/back of the card
     * @param frontCorners Front corners
     * @param backCorners Back corners
     * @param centerSymbols Center symbols of the front of the card
     */
    public StarterCard(String id, Point coord, Boolean flipped, Corner[] frontCorners, Corner[] backCorners, CardSymbolKingdom[] centerSymbols) {
        super(id, coord, flipped, frontCorners, backCorners);
        this.centerSymbols = centerSymbols;
    }

    /**
     * Getter for centerSymbols attribute
     * @return centerSymbols
     */
    public CardSymbolKingdom[] getCenterSymbols() {
        return this.getFlipped() ? null : centerSymbols;
    }



}
