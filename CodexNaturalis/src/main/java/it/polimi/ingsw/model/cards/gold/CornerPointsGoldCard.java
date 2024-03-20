package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.player.Board;

import java.awt.*;

/**
 * Class representing gold cards with points related to the number of corners covered when placing
 */
public class CornerPointsGoldCard extends GoldCard {
    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param coord Coordinates of the card (once placed)
     * @param flipped Front/back of the card
     * @param centerSymbol Center Symbol
     * @param frontCorners Corners
     * @param points Points given once the card is placed
     * @param requirements Requirements to place the card
     */
    public CornerPointsGoldCard(String id, Point coord, Boolean flipped, CardSymbolKingdom centerSymbol, Corner[] frontCorners, int points, GoldCardRequirement[] requirements) {
        super(id, coord, flipped, centerSymbol, frontCorners, points, requirements);
    }

    @Override
    public int calculatePoints(Board board) {
        if (this.getFlipped()) {
            return 0;
        }
        // TODO: Calculate points based on placement
        return 0;
    }
}
