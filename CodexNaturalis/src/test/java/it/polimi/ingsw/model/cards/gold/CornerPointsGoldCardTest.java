package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.goals.PatternGoalDiagonal;
import it.polimi.ingsw.model.goals.PatternGoalL;
import it.polimi.ingsw.model.player.Board;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static it.polimi.ingsw.model.enumeration.CardSymbolKingdom.*;
import static org.junit.jupiter.api.Assertions.*;

public class CornerPointsGoldCardTest implements ConstructorTest, GoldCardTest {

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
        CornerPointsGoldCard goldCard = new CornerPointsGoldCard(id, centerSymbol, corners, points, requirements);
        assertEquals(id, goldCard.getId());
        assertEquals(centerSymbol, goldCard.getCardKingdom());
        assertEquals(corners, goldCard.getCorners());
        assertEquals(points, goldCard.getConditionalPoints());
        assertEquals(requirements, goldCard.getConditionalRequirements());
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
        CornerPointsGoldCard goldCard = new CornerPointsGoldCard(id, centerSymbol, corners, points, requirements);
        assertEquals(corners, goldCard.getCorners());
        assertEquals(centerSymbol, goldCard.getCardKingdom());
        assertNull(goldCard.getCenterSymbol());

        goldCard.setFlipped(true);

        for (Corner corner: goldCard.getCorners()) {
            assertFalse(corner.isCovered());
            assertNull(corner.getSymbol());
        }
        assertEquals(0, goldCard.getConditionalPoints());
        assertNull(goldCard.getConditionalRequirements());
    }

    @Override
    @Test
    public void calculatePoints() {
        ArrayList<Goal> sharedGoals = new ArrayList<>();
        sharedGoals.add(new PatternGoalDiagonal("002", 2, false, WOLF));
        sharedGoals.add(new PatternGoalDiagonal("003", 2, true, BUTTERFLY));
        Goal privateGoal = new PatternGoalL("003", 5, LDirection.BOTTOM_LEFT, CardSymbolKingdom.WOLF, CardSymbolKingdom.LEAF);
        Board board = new Board(privateGoal, sharedGoals);
        ArrayList<Card> playedCards = new ArrayList<>();
        playedCards.add(starterCard());
        playedCards.add(playableCard1());
        playedCards.add(playableCard2());
        playedCards.add(playableCard3());

        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 2;
        CornerPointsGoldCard goldCard = new CornerPointsGoldCard("00", WOLF, corners, points, null);
        goldCard.setCoord(new Point(1,1));
        playedCards.add(goldCard);

        board.setPlayedCards(playedCards);

        assertEquals(8, goldCard.calculatePoints(board));
    }

    private StarterCard starterCard() {
        String id = "016";
        Point coord = new Point(0,0);
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = LEAF;
        centerSymbols[1] = WOLF;
        centerSymbols[2] = MUSHROOM;
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(null);
        frontCorners[1] = new Corner(null);
        Corner[] backCorners = new Corner[4];
        frontCorners[0] = new Corner(MUSHROOM);
        frontCorners[1] = new Corner(WOLF);
        frontCorners[2] = new Corner(BUTTERFLY);
        frontCorners[3] = new Corner(LEAF);
        StarterCard starterCard = new StarterCard(id,frontCorners,backCorners,centerSymbols);
        starterCard.setCoord(coord);
        return starterCard;
    }

    private PlayableCard playableCard1() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        corners[2] = new Corner(null);
        corners[3] = new Corner(null);
        PlayableCard resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(2,2));
        return resourceCard;
    }

    private PlayableCard playableCard2() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        corners[2] = new Corner(null);
        corners[3] = new Corner(null);
        PlayableCard resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(0,2));
        return resourceCard;
    }

    private PlayableCard playableCard3() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        corners[2] = new Corner(null);
        corners[3] = new Corner(null);
        PlayableCard resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(2,0));
        return resourceCard;
    }
}
