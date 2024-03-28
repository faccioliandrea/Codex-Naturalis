package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FixedPointsGoldCardTest implements ConstructorTest, GoldCardTest {
    @Override
    @Test
    public void testConstructor() {
        String id = "000";
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 5;
        GoldCardRequirement requirement_1 = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        GoldCardRequirement requirement_2 = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 1);
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = requirement_1;
        requirements[1] = requirement_2;
        FixedPointsGoldCard goldCard = new FixedPointsGoldCard(id, centerSymbol, corners, points, requirements);
        assertEquals(id, goldCard.getId());
        assertEquals(centerSymbol, goldCard.getCardKingdom());
        assertEquals(corners, goldCard.getCorners());
        assertEquals(points, goldCard.getPoints());
        assertEquals(requirements, goldCard.getRequirements());
    }

    @Override
    @Test
    public void calculatePoints() {
        String id = "000";
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 5;
        GoldCardRequirement requirement_1 = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        GoldCardRequirement requirement_2 = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 1);
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = requirement_1;
        requirements[1] = requirement_2;
        FixedPointsGoldCard goldCard = new FixedPointsGoldCard(id, centerSymbol, corners, points, requirements);
        assertEquals(points, goldCard.calculatePoints(null));
    }

    @Test
    public void testFlipCard() {
        String id = "000";
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 5;
        GoldCardRequirement requirement_1 = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        GoldCardRequirement requirement_2 = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 1);
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = requirement_1;
        requirements[1] = requirement_2;
        FixedPointsGoldCard goldCard = new FixedPointsGoldCard(id, centerSymbol, corners, points, requirements);
        assertEquals(corners, goldCard.getCorners());
        goldCard.setFlipped(true);
        for (Corner corner: goldCard.getCorners()) {
            assertFalse(corner.isCovered());
            assertNull(corner.getSymbol());
        }
        assertNull(goldCard.getRequirements());
    }
}
