package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.player.Board;

import java.awt.*;

/**
 * Abstract class representing a GoldCard of the game
 */
public abstract class GoldCard extends PlayableCard {
    private GoldCardRequirement[] requirements;

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
    public GoldCard(String id, Point coord, Boolean flipped, CardSymbolKingdom centerSymbol, Corner[] frontCorners, int points, GoldCardRequirement[] requirements) {
        super(id, coord, flipped, centerSymbol, frontCorners, points);
        this.requirements = requirements;
    }

    /**
     * Abstract method to calculate the number of points the user gets when placing the card. Implementation depends on the type of the GoldCard
     * @param board Board of the player
     * @return Number of points the player gets once the card has been placed
     */
    public abstract int calculatePoints(Board board);

    /**
     * Getter for card requirements
     * @return requirements
     */
    public GoldCardRequirement[] getRequirements() {
        return this.getFlipped() ? null : requirements;
    }
}
