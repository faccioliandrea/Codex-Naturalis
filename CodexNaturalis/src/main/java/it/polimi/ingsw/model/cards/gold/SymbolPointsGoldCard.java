package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.player.Board;

/**
 * Class representing gold cards with points related to the number of a symbol present on the player board.
 */
public class SymbolPointsGoldCard extends GoldCard {

    private final CardSymbolObject pointsSymbol;

    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param centerSymbol Center Symbol
     * @param frontCorners Corners
     * @param points Points given once the card is placed
     * @param requirements Requirements to place the card
     * @param pointsSymbol Symbol for which the user gets points based on how many are present on the board
     */
    public SymbolPointsGoldCard(String id, CardSymbolKingdom centerSymbol, Corner[] frontCorners, int points, GoldCardRequirement[] requirements, CardSymbolObject pointsSymbol) {
        super(id, centerSymbol, frontCorners, points, requirements);
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
        return board.getSymbols().get(this.pointsSymbol) * this.getConditionalPoints();
    }
}
