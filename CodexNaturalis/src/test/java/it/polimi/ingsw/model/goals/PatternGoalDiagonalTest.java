package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternGoalDiagonalTest implements GoalTest, ConstructorTest {
    private static String id;
    private static int points;
    private static boolean isPrimaryDiagonal;
    private static CardSymbolKingdom kingdom;
    private static PatternGoalDiagonal goal;

    @BeforeAll
    static void setup(){
        id = "003";
        points = 2;
        isPrimaryDiagonal = false;
        kingdom = CardSymbolKingdom.BUTTERFLY;
        goal = new PatternGoalDiagonal(id, points, isPrimaryDiagonal, kingdom);
    }

    @Override
    @Test
    public void testConstructor() {
        assertEquals(id, goal.getId());
        assertEquals(points, goal.getPoints());
        assertEquals(isPrimaryDiagonal, goal.isPrimaryDiagonal());
        assertEquals(kingdom, goal.getKingdom());
    }

    @Override
    @Test
    public void checkGoal() {
        //TODO: finire prima il metodo nella classe
    }
}