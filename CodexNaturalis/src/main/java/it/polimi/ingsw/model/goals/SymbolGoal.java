package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.player.Board;

/**
 * Class representing the symbol goal card
 */
public class SymbolGoal extends Goal{
    private GoalRequirement[] symbolGoalRequirements;

    /**
     * Default constructor
     *
     * @param id     Goal ID
     * @param points points given by exceeding the goal
     * @param requirements Goal requirements to score the points
     */
    public SymbolGoal(String id, int points, GoalRequirement[] requirements) {
        super(id, points);
        this.symbolGoalRequirements = requirements;
    }

    /**
     * calculate the points scored by the player with this Goal
     * @param board: the board of the player
     * @return points scored
     */
    @Override
    public int checkGoal(Board board) {
        //TODO: implementare il metodo:
        return 0;
    }

    /**
     * getter for symbolGoalRequirements attribute
     * @return symbolGoalRequirements
     */
    public GoalRequirement[] getSymbolGoalRequirements() {
        return symbolGoalRequirements;
    }
}
