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
     * @return scoredPoints
     */
    @Override
    public int checkGoal(Board board) {

        int scoredPoints;
        if(getSymbolGoalRequirements().length == 1)
            scoredPoints = (board.getSymbols().get(getSymbolGoalRequirements()[0].getRequiredSymbol()) / getSymbolGoalRequirements()[0].getQuantity()) * getPoints();
        else{
            int min = board.getSymbols().get(getSymbolGoalRequirements()[0].getRequiredSymbol());
            for(GoalRequirement x : getSymbolGoalRequirements()){
                if (board.getSymbols().get(x.getRequiredSymbol()) < min){
                    min = board.getSymbols().get(x.getRequiredSymbol());
                }
            }
            scoredPoints = min * getPoints();
        }

        return scoredPoints;
    }

    /**
     * getter for symbolGoalRequirements attribute
     * @return symbolGoalRequirements
     */
    public GoalRequirement[] getSymbolGoalRequirements() {
        return symbolGoalRequirements;
    }
}
