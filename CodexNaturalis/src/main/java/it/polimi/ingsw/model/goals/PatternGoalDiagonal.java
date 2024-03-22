package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.player.Board;

/**
 * Class representing  the diagonal Goal card
 */
public class PatternGoalDiagonal extends Goal{
    private boolean isPrimaryDiagonal;
    private CardSymbolKingdom kingdom;

    /**
     * Default constructor
     *
     * @param id     Goal ID
     * @param points : points given by exceeding the goal
     * @param isPrimaryDiagonal : direction of the pattern
     * @param kingdom : kingdom of the cards
     */
    public PatternGoalDiagonal(String id, int points, boolean isPrimaryDiagonal, CardSymbolKingdom kingdom) {
        super(id, points);
        this.isPrimaryDiagonal = isPrimaryDiagonal;
        this.kingdom = kingdom;
    }

    /**
     * calculate the points scored by the player with this Goal
     * @param board : board of the current player
     * @return
     */
    @Override
    public int checkGoal(Board board) {
        //TODO: implementare il metodo
        return 0;
    }

    /**
     * Getter for isPrimaryDiagonal attribute
     * @return isPrimaryDiagonal
     */
    public boolean isPrimaryDiagonal() {
        return isPrimaryDiagonal;
    }

    /**
     * Getter for kingdom attribute
     * @return kingdom
     */
    public CardSymbolKingdom getKingdom() {
        return kingdom;
    }
}
