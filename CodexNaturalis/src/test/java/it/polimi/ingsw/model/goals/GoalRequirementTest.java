package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoalRequirementTest implements ConstructorTest {
    private CardSymbol requiredSymbol;
    private int quantity;
    private GoalRequirement goalRequirement;
    @BeforeEach
    void setup(){
        requiredSymbol = CardSymbolObject.SCROLL;
        quantity = 1;
        goalRequirement = new GoalRequirement(requiredSymbol, quantity);
    }
    @Override
    @Test
    public void testConstructor() {
        assertEquals(requiredSymbol, goalRequirement.getRequiredSymbol());
        assertEquals(quantity, goalRequirement.getQuantity());
    }
}