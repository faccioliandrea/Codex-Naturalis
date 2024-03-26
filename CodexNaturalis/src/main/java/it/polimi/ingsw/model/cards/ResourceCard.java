package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.player.Board;

import java.awt.*;

/**
 * Class representing a ResourceCard of the game
 */
public class ResourceCard extends PlayableCard {

    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param centerSymbol Center Symbol
     * @param frontCorners Corners
     * @param points Points given once the card is placed
     */
    public ResourceCard(String id,CardSymbolKingdom centerSymbol, Corner[] frontCorners, int points) {
        super(id, centerSymbol, frontCorners, points);
    }

    @Override
    public int calculatePoints(Board board) {
        return this.getPoints();
    }
}
