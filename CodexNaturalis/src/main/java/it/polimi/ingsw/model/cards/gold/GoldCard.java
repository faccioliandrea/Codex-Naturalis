package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

/**
 * Abstract class representing a GoldCard of the game
 */
public abstract class GoldCard extends PlayableCard {
    final private GoldCardRequirement[] requirements;

    /**
     * Default constructor
     * @param id PlayableCard ID
     * @param centerSymbol Center Symbol
     * @param frontCorners Corners
     * @param points Points given once the card is placed
     * @param requirements Requirements to place the card
     */
    public GoldCard(String id, CardSymbolKingdom centerSymbol, Corner[] frontCorners, int points, GoldCardRequirement[] requirements) {
        super(id, centerSymbol, frontCorners, points);
        this.requirements = requirements;
    }

    /**
     * Getter for card requirements
     * @return requirements
     */
    public GoldCardRequirement[] getRequirements() {
        return requirements;
    }

    /**
     * Getter for card requirements based on its flip status
     * @return requirements
     */
    public GoldCardRequirement[] getConditionalRequirements() {
        return this.getFlipped() ? null : requirements;
    }
}
