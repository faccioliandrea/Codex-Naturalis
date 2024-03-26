package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.gold.*;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ParserGoldTest {

    GoldCard[] cards;

    @BeforeEach
    void setup() throws FileNotFoundException {
        cards = Parser.parser.initGoldCards();
    }

    @Test
    public void testGoldCardFixedParser() {
        String id = "023";
        CardSymbolKingdom kingdom = CardSymbolKingdom.BUTTERFLY;
        Corner[] corners = new Corner[4];
        corners[2] = new Corner(null);
        corners[3] = new Corner(CardSymbolObject.FEATHER);
        int points = 3;
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 3);
        FixedPointsGoldCard goldCard = new FixedPointsGoldCard(id, kingdom, corners, points, requirements);
        Optional<GoldCard> goldCardJsonOpt = Arrays.stream(cards).filter(card -> Objects.equals(card.getId(), id)).findFirst();
        assertTrue(goldCardJsonOpt.isPresent());
        GoldCard goldCardJson = goldCardJsonOpt.get();
        assertEquals(goldCard.getId(), goldCardJson.getId());
        for(int index = 0; index < goldCardJson.getCorners().length; index++) {
            Corner cornerJson = goldCardJson.getCorners()[index];
            Corner corner = goldCard.getCorners()[index];
            if (cornerJson != null) {
                assertNotNull(corner);
                assertEquals(cornerJson.getSymbol(), corner.getSymbol());
            } else {
                assertNull(corner);
            }
        }
        assertEquals(goldCard.getCardKingdom(), goldCardJson.getCardKingdom());
        assertEquals(goldCard.getPoints(), goldCardJson.getPoints());
        for(int index = 0; index < goldCardJson.getRequirements().length; index++) {
            GoldCardRequirement requirementJson = goldCardJson.getRequirements()[index];
            GoldCardRequirement requirement = goldCard.getRequirements()[index];
            if (requirementJson != null) {
                assertNotNull(requirement);
                assertEquals(requirementJson.getRequiredSymbol(), requirement.getRequiredSymbol());
                assertEquals(requirementJson.getQuantity(), requirement.getQuantity());
            } else {
                assertNull(requirement);
            }
        }
    }

    @Test
    public void testGoldCardCornerParser() {
        String id = "027";
        CardSymbolKingdom kingdom = CardSymbolKingdom.BUTTERFLY;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(null);
        corners[1] = new Corner(null);
        corners[3] = new Corner(null);
        int points = 2;
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 3);
        requirements[1] = new GoldCardRequirement(CardSymbolKingdom.LEAF, 1);
        CornerPointsGoldCard goldCard = new CornerPointsGoldCard(id, kingdom, corners, points, requirements);
        Optional<GoldCard> goldCardJsonOpt = Arrays.stream(cards).filter(card -> Objects.equals(card.getId(), id)).findFirst();
        assertTrue(goldCardJsonOpt.isPresent());
        GoldCard goldCardJson = goldCardJsonOpt.get();
        assertEquals(goldCard.getId(), goldCardJson.getId());
        for(int index = 0; index < goldCardJson.getCorners().length; index++) {
            Corner cornerJson = goldCardJson.getCorners()[index];
            Corner corner = goldCard.getCorners()[index];
            if (cornerJson != null) {
                assertNotNull(corner);
                assertEquals(cornerJson.getSymbol(), corner.getSymbol());
            } else {
                assertNull(corner);
            }
        }
        assertEquals(goldCard.getCardKingdom(), goldCardJson.getCardKingdom());
        assertEquals(goldCard.getPoints(), goldCardJson.getPoints());
        for(int index = 0; index < goldCardJson.getRequirements().length; index++) {
            GoldCardRequirement requirementJson = goldCardJson.getRequirements()[index];
            GoldCardRequirement requirement = goldCard.getRequirements()[index];
            if (requirementJson != null) {
                assertNotNull(requirement);
                assertEquals(requirementJson.getRequiredSymbol(), requirement.getRequiredSymbol());
                assertEquals(requirementJson.getQuantity(), requirement.getQuantity());
            } else {
                assertNull(requirement);
            }
        }
    }

    @Test
    public void testGoldCardSymbolParser() {
        String id = "061";
        CardSymbolKingdom kingdom = CardSymbolKingdom.MUSHROOM;
        CardSymbolObject pointsSymbol = CardSymbolObject.FEATHER;
        Corner[] corners = new Corner[4];
        corners[1] = new Corner(null);
        corners[2] = new Corner(CardSymbolObject.FEATHER);
        corners[3] = new Corner(null);
        int points = 1;
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        requirements[1] = new GoldCardRequirement(CardSymbolKingdom.WOLF, 1);
        SymbolPointsGoldCard goldCard = new SymbolPointsGoldCard(id, kingdom, corners, points, requirements, pointsSymbol);
        Optional<GoldCard> goldCardJsonOpt = Arrays.stream(cards).filter(card -> Objects.equals(card.getId(), id)).findFirst();
        assertTrue(goldCardJsonOpt.isPresent());
        GoldCard goldCardJson = goldCardJsonOpt.get();
        assertEquals(goldCard.getId(), goldCardJson.getId());
        for(int index = 0; index < goldCardJson.getCorners().length; index++) {
            Corner cornerJson = goldCardJson.getCorners()[index];
            Corner corner = goldCard.getCorners()[index];
            if (cornerJson != null) {
                assertNotNull(corner);
                assertEquals(cornerJson.getSymbol(), corner.getSymbol());
            } else {
                assertNull(corner);
            }
        }
        assertEquals(goldCard.getCardKingdom(), goldCardJson.getCardKingdom());
        assertEquals(goldCard.getPoints(), goldCardJson.getPoints());
        for(int index = 0; index < goldCardJson.getRequirements().length; index++) {
            GoldCardRequirement requirementJson = goldCardJson.getRequirements()[index];
            GoldCardRequirement requirement = goldCard.getRequirements()[index];
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
