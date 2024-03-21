package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static it.polimi.ingsw.model.enumeration.CardSymbolKingdom.*;
import static org.junit.jupiter.api.Assertions.*;

class StarterCardTest implements ConstructorTest {

    private String id;
    private Point coord;
    private boolean flipped;
    private CardSymbolKingdom[] centerSymbols;
    private Corner[] frontCorners;
    private Corner[] backCorners;
    private StarterCard starterCard;

    @BeforeEach
    void setup(){
        id = "016";
        coord = new Point(0,0);
        flipped = false;
        centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = LEAF;
        centerSymbols[1] = WOLF;
        centerSymbols[2] = MUSHROOM;
        frontCorners = new Corner[4];
        frontCorners[0] = new Corner(false, null);
        frontCorners[1] = new Corner(false, null);
        backCorners = new Corner[4];
        frontCorners[0] = new Corner(false, MUSHROOM);
        frontCorners[1] = new Corner(false, WOLF);
        frontCorners[2] = new Corner(false, BUTTERFLY);
        frontCorners[3] = new Corner(false, LEAF);
        starterCard = new StarterCard(id, coord, flipped, frontCorners, backCorners, centerSymbols);
    }


    @Override
    @Test
    public void testConstructor(){
        assertEquals(id, starterCard.getId());
        assertEquals(coord, starterCard.getCoord());
        assertEquals(flipped, starterCard.getFlipped());
        assertEquals(frontCorners, starterCard.getCorners());
        assertEquals(centerSymbols, starterCard.getCenterSymbols());
        starterCard.setFlipped(true);
        assertEquals(backCorners, starterCard.getCorners());
        assertNull(starterCard.getCenterSymbols());
    }

}