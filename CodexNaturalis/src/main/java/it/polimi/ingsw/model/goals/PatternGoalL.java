package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.player.Board;

/**
 * Class representing the LGoal Card
 */
public class PatternGoalL extends Goal{

    private LDirection direction;
    private CardSymbolKingdom verticalKingdom;
    private CardSymbolKingdom horizontalKingdom;

    /**
     * Default constructor
     *
     * @param id : Goal ID
     * @param points : points given by exceeding the goal
     * @param verticalKingdom : kingdom of the 2 vertical cards
     * @param horizontalKingdom : kingdom of the horizontal card
     */
    public PatternGoalL(String id, int points, LDirection direction, CardSymbolKingdom verticalKingdom, CardSymbolKingdom horizontalKingdom) {
        super(id, points);
        this.direction = direction;
        this.verticalKingdom = verticalKingdom;
        this.horizontalKingdom = horizontalKingdom;
    }

    /**
     * calculate the points scored by the player with this Goal
     * @param board : board of the current player
     * @return points
     */
    @Override
    public int checkGoal(Board board) {
        //TODO: implementare il metodo
        return 0;
    }

    /**
     *  Getter for direction attribute
     * @return direction
     */
    public LDirection getDirection() {
        return direction;
    }

    /**
     * Getter for verticalKingdom attribute
     * @return verticalKingdom
     */
    public CardSymbolKingdom getVerticalKingdom() {
        return verticalKingdom;
    }

    /**
     * Getter for horizontalKingdom
     * @return horizontalKingdom
     */
    public CardSymbolKingdom getHorizontalKingdom() {
        return horizontalKingdom;
    }
}
