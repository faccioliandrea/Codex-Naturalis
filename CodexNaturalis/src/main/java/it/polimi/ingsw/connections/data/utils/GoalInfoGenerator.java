package it.polimi.ingsw.connections.data.utils;

import it.polimi.ingsw.model.goals.*;

/**
 * Class to generate description for goals
 */
final public class GoalInfoGenerator {
    // TODO: Use template in the future
    public static final GoalInfoGenerator generator = new GoalInfoGenerator();

    private GoalInfoGenerator() {}

    /**
     * Creates description of goal
     * @param goal Goal to get info
     * @return Description of goal
     */
    public String getGoalDescription(Goal goal) {
        StringBuilder description = new StringBuilder();
        description.append(String.format("Goal %s", goal.getId()));
        description.append(String.format("\nGives %d points for each placement of ", goal.getPoints()));
        if (goal instanceof PatternGoalL) {
            PatternGoalL patternGoalL = (PatternGoalL) goal;
            description.append(String.format("2x %s kingdom cards in column and 1x %s kingdom card on %s", patternGoalL.getVerticalKingdom().toString(), patternGoalL.getHorizontalKingdom().toString(), patternGoalL.getDirection().toString()));
        } else if (goal instanceof PatternGoalDiagonal) {
            PatternGoalDiagonal patternGoalDiagonal = (PatternGoalDiagonal) goal;
            description.append(String.format("3x %s kingdom cards on %s diagonal", patternGoalDiagonal.getKingdom().toString(), patternGoalDiagonal.isPrimaryDiagonal() ? "primary" : "secondary"));
        } else if (goal instanceof SymbolGoal) {
            SymbolGoal symbolGoal = (SymbolGoal) goal;
            for (GoalRequirement requirement: symbolGoal.getSymbolGoalRequirements()) {
                if (requirement != null) {
                    description.append(String.format("\n\t%dx %s", requirement.getQuantity(), requirement.getRequiredSymbol().toString()));
                }
            }
        }
        return description.toString();
    }
}