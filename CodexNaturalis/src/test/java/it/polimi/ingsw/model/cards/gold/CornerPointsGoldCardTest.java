package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CornerPointsGoldCardTest implements ConstructorTest {
    @Override
    @Test
    public void testConstructor() {
        String id = "000";
        Point coord = new Point(1,1);
        Boolean flipped = false;
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
        int points = 5;
        GoldCardRequirement requirement_1 = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        GoldCardRequirement requirement_2 = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 1);
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = requirement_1;
        requirements[1] = requirement_2;
        CornerPointsGoldCard goldCard = new CornerPointsGoldCard(id, coord, flipped, centerSymbol, corners, points, requirements);
        assertEquals(id, goldCard.getId());
        assertEquals(coord, goldCard.getCoord());
        assertEquals(flipped, goldCard.getFlipped());
        assertEquals(centerSymbol, goldCard.getCenterSymbol());
        assertEquals(corners, goldCard.getCorners());
        assertEquals(points, goldCard.getPoints());
        assertEquals(requirements, goldCard.getRequirements());
    }

    @Test
    public void testFlipCard() {
        String id = "000";
        Point coord = new Point(1,1);
        Boolean flipped = false;
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
        int points = 5;
        GoldCardRequirement requirement_1 = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        GoldCardRequirement requirement_2 = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 1);
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = requirement_1;
        requirements[1] = requirement_2;
        CornerPointsGoldCard goldCard = new CornerPointsGoldCard(id, coord, flipped, centerSymbol, corners, points, requirements);

        goldCard.flipCard();
        assertEquals(corners, goldCard.getCorners());

        goldCard.setFlipped(true);
        goldCard.flipCard();

        for (Corner corner: goldCard.getCorners()) {
            assertFalse(corner.isCovered());
            assertNull(corner.getSymbol());
        }
        assertNull(goldCard.getRequirements());
    }
}
