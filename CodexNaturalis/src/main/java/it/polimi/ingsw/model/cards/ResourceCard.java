package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

import java.awt.*;

/**
 * Class representing a ResourceCard of the game
 */
public class ResourceCard extends PlayableCard {

    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param coord Coordinates of the card (once placed)
     * @param flipped Front/back of the card
     * @param centerSymbol Center Symbol
     * @param corners Corners
     * @param points Points given once the card is placed
     */
    public ResourceCard(String id, Point coord, Boolean flipped, CardSymbolKingdom centerSymbol, Corner[] corners, int points) {
        super(id, coord, flipped, centerSymbol, corners, points);
    }
}
