package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.goals.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ParserGoalTest {
    Goal[] cards;

    @BeforeEach
    void setup() throws FileNotFoundException {
        cards = Parser.parser.initGoals();
    }

    @Test
    public void testGoalPatternLParser() {
        String id = "010";
        CardSymbolKingdom horizontalKingdom = CardSymbolKingdom.BUTTERFLY;
        CardSymbolKingdom verticalKingdom = CardSymbolKingdom.LEAF;
        int points = 3;
        LDirection direction = LDirection.BOTTOM_LEFT;
        PatternGoalL goalCard = new PatternGoalL(id, points, direction, verticalKingdom, horizontalKingdom);
        Optional<Goal> goalCardJsonOpt = Arrays.stream(cards).filter(card -> Objects.equals(card.getId(), id)).findFirst();
        assertTrue(goalCardJsonOpt.isPresent());
        assertInstanceOf(PatternGoalL.class, goalCardJsonOpt.get());
        PatternGoalL goalCardJson = (PatternGoalL) goalCardJsonOpt.get();
        assertEquals(goalCard.getId(), goalCardJson.getId());
        assertEquals(goalCard.getPoints(), goalCardJson.getPoints());
        assertEquals(goalCard.getDirection(), goalCardJson.getDirection());
        assertEquals(goalCard.getVerticalKingdom(), goalCardJson.getVerticalKingdom());
        assertEquals(goalCard.getHorizontalKingdom(), goalCardJson.getHorizontalKingdom());
    }

    @Test
    public void testGoalPatternDiagParser() {
        String id = "013";
        CardSymbolKingdom kingdom = CardSymbolKingdom.WOLF;
        int points = 2;
        boolean isPrimary = false;
        PatternGoalDiagonal goalCard = new PatternGoalDiagonal(id, points, isPrimary, kingdom);
        Optional<Goal> goalCardJsonOpt = Arrays.stream(cards).filter(card -> Objects.equals(card.getId(), id)).findFirst();
        assertTrue(goalCardJsonOpt.isPresent());
        assertInstanceOf(PatternGoalDiagonal.class, goalCardJsonOpt.get());
        PatternGoalDiagonal goalCardJson = (PatternGoalDiagonal) goalCardJsonOpt.get();
        assertEquals(goalCard.getId(), goalCardJson.getId());
        assertEquals(goalCard.getPoints(), goalCardJson.getPoints());
        assertEquals(goalCard.isPrimaryDiagonal(), goalCardJson.isPrimaryDiagonal());
        assertEquals(goalCard.getKingdom(), goalCardJson.getKingdom());
    }

    @Test
    public void testGoalSymbolParser() {
        String id = "004";
        int points = 2;
        GoalRequirement[] requirements = new GoalRequirement[3];
        requirements[0] = new GoalRequirement(CardSymbolKingdom.BUTTERFLY, 3);
        SymbolGoal goalCard = new SymbolGoal(id, points, requirements);
        Optional<Goal> goalCardJsonOpt = Arrays.stream(cards).filter(card -> Objects.equals(card.getId(), id)).findFirst();
        assertTrue(goalCardJsonOpt.isPresent());
        assertInstanceOf(SymbolGoal.class, goalCardJsonOpt.get());
        SymbolGoal goalCardJson = (SymbolGoal) goalCardJsonOpt.get();
        assertEquals(goalCard.getId(), goalCardJson.getId());
        assertEquals(goalCard.getPoints(), goalCardJson.getPoints());
        for(int index = 0; index < goalCardJson.getSymbolGoalRequirements().length; index++) {
            GoalRequirement requirementJson = goalCardJson.getSymbolGoalRequirements()[index];
            GoalRequirement requirement = goalCard.getSymbolGoalRequirements()[index];
            if (requirementJson != null) {
                assertNotNull(requirement);
                assertEquals(requirementJson.getRequiredSymbol(), requirement.getRequiredSymbol());
                assertEquals(requirementJson.getQuantity(), requirement.getQuantity());
            } else {
                assertNull(requirement);
            }
        }
    }
}
