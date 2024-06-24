package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.connections.data.utils.GoalInfoGenerator;
import it.polimi.ingsw.model.goals.Goal;

import java.io.Serializable;

public class GoalInfo implements Serializable {
    final private String id;

    final private String description;

    public GoalInfo(Goal goal) {
        this.id = goal.getId();
        this.description = GoalInfoGenerator.generator.getGoalDescription(goal);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
