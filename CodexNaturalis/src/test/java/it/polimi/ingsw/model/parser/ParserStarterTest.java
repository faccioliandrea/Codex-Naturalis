package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ParserStarterTest {

    StarterCard[] cards;

    @BeforeEach
    void setup() throws FileNotFoundException {
        cards = Parser.parser.initStarterCards();
    }

    @Test
    public void testResourceCardParser() {
        String id = "017";
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(null);
        frontCorners[1] = new Corner(null);
        Corner[] backCorners = new Corner[4];
        backCorners[0] = new Corner(CardSymbolKingdom.BUTTERFLY);
        backCorners[1] = new Corner(CardSymbolKingdom.MUSHROOM);
        backCorners[2] = new Corner(CardSymbolKingdom.WOLF);
        backCorners[3] = new Corner(CardSymbolKingdom.LEAF);
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = CardSymbolKingdom.WOLF;
        centerSymbols[1] = CardSymbolKingdom.BUTTERFLY;
        centerSymbols[2] = CardSymbolKingdom.LEAF;
        StarterCard starterCard = new StarterCard(id, frontCorners, backCorners, centerSymbols);
        Optional<StarterCard> starterCardJsonOpt = Arrays.stream(cards).filter(card -> Objects.equals(card.getId(), id)).findFirst();
        assertTrue(starterCardJsonOpt.isPresent());
        StarterCard starterCardJson = starterCardJsonOpt.get();
        assertEquals(starterCard.getId(), starterCardJson.getId());
        for(int index = 0; index < starterCardJson.getCorners().length; index++) {
            Corner cornerJson = starterCardJson.getCorners()[index];
            Corner corner = starterCard.getCorners()[index];
            if (cornerJson != null) {
                assertNotNull(corner);
                assertEquals(cornerJson.getSymbol(), corner.getSymbol());
            } else {
                assertNull(corner);
            }
        }
        for(int index = 0; index < starterCardJson.getConditionalCenterSymbols().length; index++) {
            CardSymbolKingdom centerSymbolJson = starterCardJson.getConditionalCenterSymbols()[index];
            CardSymbolKingdom centerSymbol = starterCard.getConditionalCenterSymbols()[index];
            if (centerSymbolJson != null) {
                assertNotNull(centerSymbol);
                assertEquals(centerSymbolJson, centerSymbol);
            } else {
                assertNull(centerSymbol);
            }
        }
        starterCardJson.setFlipped(true);
        starterCard.setFlipped(true);
        for(int index = 0; index < starterCardJson.getCorners().length; index++) {
            Corner cornerJson = starterCardJson.getCorners()[index];
            Corner corner = starterCard.getCorners()[index];
            if (cornerJson != null) {
                assertNotNull(corner);
                assertEquals(cornerJson.getSymbol(), corner.getSymbol());
            } else {
                assertNull(corner);
            }
        }
    }
}
