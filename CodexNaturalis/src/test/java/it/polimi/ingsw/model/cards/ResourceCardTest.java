package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceCardTest implements ConstructorTest {
    @Override
    @Test
    public void testConstructor() {
        String id = "000";
        Point coord = new Point(1,1);
        boolean flipped = false;
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
        int points = 5;
        ResourceCard resourceCard = new ResourceCard(id, coord, flipped, centerSymbol, corners, points);
        assertEquals(id, resourceCard.getId());
        assertEquals(coord, resourceCard.getCoord());
        assertEquals(flipped, resourceCard.getFlipped());
        assertEquals(corners, resourceCard.getCorners());
        assertEquals(points, resourceCard.getPoints());
        assertEquals(centerSymbol, resourceCard.getCardKingdom());
    }

    @Test
    public void testFlipCard() {
        String id = "000";
        Point coord = new Point(1,1);
        boolean flipped = false;
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
        int points = 5;
        ResourceCard resourceCard = new ResourceCard(id, coord, flipped, centerSymbol, corners, points);
        assertEquals(corners, resourceCard.getCorners());
        assertEquals(centerSymbol, resourceCard.getCardKingdom());
        assertNull(resourceCard.getCenterSymbol());

        resourceCard.setFlipped(true);

        for (Corner corner: resourceCard.getCorners()) {
            assertFalse(corner.isCovered());
            assertNull(corner.getSymbol());
        }
        assertEquals(0, resourceCard.getPoints());
    }
}
