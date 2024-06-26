package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.connections.data.utils.GoalInfoGenerator;
import it.polimi.ingsw.model.goals.Goal;

import java.io.Serializable;

/**
 * This class contains the information of a goal.
 */
public class GoalInfo implements Serializable {
    final private String id;
    final private String description;

    /** Constructor:
     * @param goal the goal to get the information from
     */
    public GoalInfo(Goal goal) {
        this.id = goal.getId();
        this.description = GoalInfoGenerator.generator.getGoalDescription(goal);
    }

    /**
     * Method to get the id of the goal
     * @return the id of the goal
     */
    public String getId() {
        return id;
    }

    /**
     * Method to get the description of the goal
     * @return the description of the goal
     */
    public String getDescription() {
        return description;
    }
}
