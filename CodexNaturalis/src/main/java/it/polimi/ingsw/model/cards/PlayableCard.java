package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.player.Board;

/**
 * Abstract class for Playable Cards (Resource and Gold cards)
 */
public abstract class PlayableCard extends Card {
    private CardSymbolKingdom kingdomSymbol;

    private int points;

    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param kingdomSymbol Kingdom Symbol
     * @param frontCorners Front corners
     * @param points Points given once the card is placed
     */
    public PlayableCard(String id, CardSymbolKingdom kingdomSymbol, Corner[] frontCorners, int points) {
        super(id, frontCorners, new Corner[]{new Corner(null), new Corner(null), new Corner(null), new Corner(null)});
        this.kingdomSymbol = kingdomSymbol;
        this.points = points;
    }

    /**
     * Getter for points attribute based on flipped status
     * @return points
     */
    public int getConditionalPoints() {
        return this.getFlipped() ? 0 : points;
    }

    /**
     * Getter for points attribute
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for centerSymbol attribute based on its flip status
     * @return centerSymbol
     */
    public CardSymbolKingdom getCenterSymbol() {
        return this.getFlipped() ? kingdomSymbol : null;
    }

    /**
     * Getter for card kingdom attribute
     * @return card kingdom
     */
    public CardSymbolKingdom getCardKingdom() { return kingdomSymbol; }

    /**
     * Abstract method to calculate the number of points the user gets when placing the card. Implementation depends on the type of the GoldCard
     * @param board Board of the player
     * @return Number of points the player gets once the card has been placed
     */
    public abstract int calculatePoints(Board board);
}