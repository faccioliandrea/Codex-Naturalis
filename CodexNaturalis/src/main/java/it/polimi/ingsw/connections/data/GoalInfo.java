package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.connections.data.utils.GoalInfoGenerator;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.goals.Goal;

import java.awt.*;
import java.io.Serializable;

public class GoalInfo implements Serializable {
    private String id;

    private String description;

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
