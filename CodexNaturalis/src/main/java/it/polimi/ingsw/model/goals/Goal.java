package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.player.Board;
/**
 * Abstract class representing the goals in the game
 */
public abstract class Goal {
    final private String id;
    final private int points;

    /**
     * Default constructor
     * @param id Goal ID
     * @param points points given by exceeding the goal
     */
    public Goal(String id, int points){
        this.id = id;
        this.points = points;
    }

    /**
     * Abstract method to calculate the points scored by the player with this Goal
     * @param board player board
     */
    public abstract int checkGoal(Board board);

    /**
     * Getter for id attribute
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for points attribute
     * @return points
     */
    public int getPoints() {
        return points;
    }
}
