package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.player.Board;

import java.awt.*;

/**
 * Class representing gold cards with points related to the number of a symbol present on the player board.
 */
public class SymbolPointsGoldCard extends GoldCard {

    private CardSymbolObject pointsSymbol;

    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param coord Coordinates of the card (once placed)
     * @param flipped Front/back of the card
     * @param centerSymbol Center Symbol
     * @param frontCorners Corners
     * @param points Points given once the card is placed
     * @param requirements Requirements to place the card
     * @param pointsSymbol Symbol for which the user gets points based on how many are present on the board
     */
    public SymbolPointsGoldCard(String id, Point coord, Boolean flipped, CardSymbolKingdom centerSymbol, Corner[] frontCorners, int points, GoldCardRequirement[] requirements, CardSymbolObject pointsSymbol) {
        super(id, coord, flipped, centerSymbol, frontCorners, points, requirements);
        this.pointsSymbol = pointsSymbol;
    }

    /**
     * Getter for pointsSymbol
     * @return pointsSymbol
     */
    public CardSymbolObject getPointsSymbol() {
        return pointsSymbol;
    }

    @Override
    public int calculatePoints(Board board) {
        if (this.getFlipped()) {
            return 0;
        }
        // TODO: Calculate points based on symbol count of board
        return 0;
    }
}
