package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.player.Board;

/**
 * Class representing gold cards with fixed points when placed
 */
public class FixedPointsGoldCard extends GoldCard {

    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param centerSymbol Center Symbol
     * @param frontCorners Corners
     * @param points Points given once the card is placed
     * @param requirements Requirements to place the card
     */
    public FixedPointsGoldCard(String id, CardSymbolKingdom centerSymbol, Corner[] frontCorners, int points, GoldCardRequirement[] requirements) {
        super(id, centerSymbol, frontCorners, points, requirements);
    }

    @Override
    public int calculatePoints(Board board) {
        return this.getConditionalPoints();
    }
}
