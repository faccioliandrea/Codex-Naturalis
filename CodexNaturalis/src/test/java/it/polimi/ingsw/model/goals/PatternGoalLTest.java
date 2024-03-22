package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.LDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternGoalLTest implements GoalTest, ConstructorTest {
    private String id;
    private int points;

    private LDirection direction;
    private CardSymbolKingdom verticalKingdom;
    private CardSymbolKingdom horizontalKingdom;
    private PatternGoalL goal;
    @BeforeEach
    void setup(){
        id = "002";
        points = 3;
        direction = LDirection.BOTTOMRIGHT;
        verticalKingdom = CardSymbolKingdom.MUSHROOM;
        horizontalKingdom = CardSymbolKingdom.LEAF;
        goal = new PatternGoalL(id,points,direction,verticalKingdom,horizontalKingdom);
    }
    @Test
    public void checkGoal() {
        //TODO: finire prima il metodo poi posso testarlo
    }

    @Override
    @Test
    public void testConstructor() {
        assertEquals(id,goal.getId());
        assertEquals(points,goal.getPoints());
        assertEquals(direction,goal.getDirection());
        assertEquals(verticalKingdom,goal.getVerticalKingdom());
        assertEquals(horizontalKingdom,goal.getHorizontalKingdom());
    }
}