package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SymbolPointsGoldCardTest implements ConstructorTest {
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
        CardSymbolObject pointsSymbol = CardSymbolObject.SCROLL;
        GoldCardRequirement requirement_1 = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        GoldCardRequirement requirement_2 = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 1);
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = requirement_1;
        requirements[1] = requirement_2;
        SymbolPointsGoldCard goldCard = new SymbolPointsGoldCard(id, coord, flipped, centerSymbol, corners, points, requirements, pointsSymbol);
        assertEquals(id, goldCard.getId());
        assertEquals(coord, goldCard.getCoord());
        assertEquals(flipped, goldCard.getFlipped());
        assertEquals(centerSymbol, goldCard.getCardKingdom());
        assertEquals(corners, goldCard.getCorners());
        assertEquals(points, goldCard.getPoints());
        assertEquals(pointsSymbol, goldCard.getPointsSymbol());
        assertEquals(requirements, goldCard.getRequirements());
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
        CardSymbolObject pointsSymbol = CardSymbolObject.SCROLL;
        GoldCardRequirement requirement_1 = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        GoldCardRequirement requirement_2 = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 1);
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = requirement_1;
        requirements[1] = requirement_2;
        SymbolPointsGoldCard goldCard = new SymbolPointsGoldCard(id, coord, flipped, centerSymbol, corners, points, requirements, pointsSymbol);
        assertEquals(corners, goldCard.getCorners());
        assertEquals(centerSymbol, goldCard.getCardKingdom());
        assertNull(goldCard.getCenterSymbol());

        goldCard.setFlipped(true);

        for (Corner corner: goldCard.getCorners()) {
            assertFalse(corner.isCovered());
            assertNull(corner.getSymbol());
        }
        assertEquals(0, goldCard.getPoints());
        assertNull(goldCard.getRequirements());
    }
}
