package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ParserResourceTest {
    ResourceCard[] cards;

    @BeforeEach
    void setup() throws FileNotFoundException {
        cards = Parser.parser.initResourceCards();
    }

    @Test
    public void testResourceCardParser() {
        String id = "085";
        CardSymbolKingdom kingdom = CardSymbolKingdom.LEAF;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[2] = new Corner(CardSymbolKingdom.WOLF);
        corners[3] = new Corner(CardSymbolKingdom.LEAF);
        int points = 0;
        ResourceCard resourceCard = new ResourceCard(id, kingdom, corners, points);
        Optional<ResourceCard> resourceCardJsonOpt = Arrays.stream(cards).filter(card -> Objects.equals(card.getId(), id)).findFirst();
        assertTrue(resourceCardJsonOpt.isPresent());
        ResourceCard resourceCardJson = resourceCardJsonOpt.get();
        assertEquals(resourceCard.getId(), resourceCardJson.getId());
        for(int index = 0; index < resourceCardJson.getCorners().length; index++) {
            Corner cornerJson = resourceCardJson.getCorners()[index];
            Corner corner = resourceCard.getCorners()[index];
            if (cornerJson != null) {
                assertNotNull(corner);
                assertEquals(cornerJson.getSymbol(), corner.getSymbol());
            } else {
                assertNull(corner);
            }
        }
        assertEquals(resourceCard.getCardKingdom(), resourceCardJson.getCardKingdom());
        assertEquals(resourceCard.getPoints(), resourceCardJson.getPoints());
    }
}
