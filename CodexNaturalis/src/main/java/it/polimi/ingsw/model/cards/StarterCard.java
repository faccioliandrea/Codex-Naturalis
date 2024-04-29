package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

/**
 * Class representing a StarterCard of the game
 */
public class StarterCard extends Card{

    private CardSymbolKingdom[] centerSymbols;

    /**
     * Default constructor
     *
     * @param id      Card ID
     * @param frontCorners Front corners
     * @param backCorners Back corners
     * @param centerSymbols Center symbols of the front of the card
     */
    public StarterCard(String id, Corner[] frontCorners, Corner[] backCorners, CardSymbolKingdom[] centerSymbols) {
        super(id, frontCorners, backCorners);
        this.centerSymbols = centerSymbols;
    }

    /**
     * Getter for centerSymbols attribute
     * @return centerSymbols
     */
    public CardSymbolKingdom[] getConditionalCenterSymbols() {
        return this.getFlipped() ? null : centerSymbols;
    }

    public CardSymbolKingdom[] getCenterSymbols() {
        return centerSymbols;
    }



}
