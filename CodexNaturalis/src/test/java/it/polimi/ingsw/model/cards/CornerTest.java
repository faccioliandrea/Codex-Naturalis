package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CornerTest implements ConstructorTest {
    @Override
    @Test
    public void testConstructor() {
        CardSymbol cardSymbol = CardSymbolKingdom.WOLF;
        Corner corner = new Corner(cardSymbol);
        assertEquals(cardSymbol, corner.getSymbol());
    }
}
