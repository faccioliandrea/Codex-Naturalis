package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolGoalTest implements ConstructorTest, GoalTest {
    private SymbolGoal symbolGoal;
    private String id;
    private GoalRequirement[] goal;
    private int points;
    @BeforeEach
    void setup(){
        //viene eseguito prima di ogni test
        id = "001";
        goal = new GoalRequirement[2];
        goal[0] = new GoalRequirement(CardSymbolKingdom.WOLF, 3);
        goal[1] = new GoalRequirement(CardSymbolKingdom.MUSHROOM, 1);
        points = 3;
        symbolGoal = new SymbolGoal(id,points,goal);
    }
    @Test
    public void checkGoal() {
    }

    @Override
    @Test
    public void testConstructor() {
        assertEquals(id,symbolGoal.getId());
        assertEquals(goal, symbolGoal.getSymbolGoalRequirements());
        assertEquals(points, symbolGoal.getPoints());

    }
}