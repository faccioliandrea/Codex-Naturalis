package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.player.Board;

/**
 * Class representing gold cards with points related to the number of corners covered when placing
 */
public class CornerPointsGoldCard extends GoldCard {
    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param centerSymbol Center Symbol
     * @param frontCorners Corners
     * @param points Points given once the card is placed
     * @param requirements Requirements to place the card
     */
    public CornerPointsGoldCard(String id,CardSymbolKingdom centerSymbol, Corner[] frontCorners, int points, GoldCardRequirement[] requirements) {
        super(id, centerSymbol, frontCorners, points, requirements);
    }

    @Override
    public int calculatePoints(Board board) {
        if (this.getFlipped()) {
            return 0;
        }
        return (int) (board.getPlayedCards().stream().filter(card -> card.getCoord().distanceSq(this.getCoord()) == 2).count() * this.getPoints());
    }
}
