package it.polimi.ingsw.model.cards.gold;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.goals.PatternGoalDiagonal;
import it.polimi.ingsw.model.goals.PatternGoalL;
import it.polimi.ingsw.model.player.Board;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SymbolPointsGoldCardTest implements ConstructorTest, GoldCardTest {
    @Override
    @Test
    public void testConstructor() {
        String id = "000";
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 5;
        CardSymbolObject pointsSymbol = CardSymbolObject.SCROLL;
        GoldCardRequirement requirement_1 = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        GoldCardRequirement requirement_2 = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 1);
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = requirement_1;
        requirements[1] = requirement_2;
        SymbolPointsGoldCard goldCard = new SymbolPointsGoldCard(id, centerSymbol, corners, points, requirements, pointsSymbol);
        assertEquals(id, goldCard.getId());
        assertEquals(centerSymbol, goldCard.getCardKingdom());
        assertEquals(corners, goldCard.getCorners());
        assertEquals(points, goldCard.getPoints());
        assertEquals(pointsSymbol, goldCard.getPointsSymbol());
        assertEquals(requirements, goldCard.getRequirements());
    }

    @Test
    public void testFlipCard() {
        String id = "000";
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 5;
        CardSymbolObject pointsSymbol = CardSymbolObject.SCROLL;
        GoldCardRequirement requirement_1 = new GoldCardRequirement(CardSymbolKingdom.MUSHROOM, 2);
        GoldCardRequirement requirement_2 = new GoldCardRequirement(CardSymbolKingdom.BUTTERFLY, 1);
        GoldCardRequirement[] requirements = new GoldCardRequirement[2];
        requirements[0] = requirement_1;
        requirements[1] = requirement_2;
        SymbolPointsGoldCard goldCard = new SymbolPointsGoldCard(id, centerSymbol, corners, points, requirements, pointsSymbol);
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

    @Override
    @Test
    public void calculatePoints() {
        Player player = new Player("TEST", PlayerColor.BLUE);
        Board board;
        ArrayList<Goal> sharedGoals = new ArrayList<>();
        sharedGoals.add(new PatternGoalDiagonal("002", 2, false, CardSymbolKingdom.WOLF));
        sharedGoals.add(new PatternGoalDiagonal("003", 2, true, CardSymbolKingdom.BUTTERFLY));
        Goal privateGoal = new PatternGoalL("003", 5, LDirection.BOTTOMLEFT, CardSymbolKingdom.WOLF, CardSymbolKingdom.LEAF);
        board = new Board(privateGoal, sharedGoals);

        ArrayList<Card> playedCard = new ArrayList<>();
        playedCard.add(starterCard());
        player.setBoard(board);
        player.getBoard().setPlayedCards(playedCard);

        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 1;
        SymbolPointsGoldCard goldCard = new SymbolPointsGoldCard("00", CardSymbolKingdom.BUTTERFLY, corners, points, null, CardSymbolObject.SCROLL);

        //assertDoesNotThrow( () -> player.placeCard(starterCard(), new Point(0,0)), "Invalid position");
        assertDoesNotThrow( () -> player.placeCard(playableCardEmpty(), new Point(-1,1)), "Invalid position");
        assertDoesNotThrow( () -> player.placeCard(playableCard2(), new Point(0,2)), "Invalid position");
        assertDoesNotThrow( () -> player.placeCard(playableCardEmpty(), new Point(1,3)), "Invalid position");
        assertDoesNotThrow( () -> player.placeCard(playableCard1(), new Point(2,2)), "Invalid position");
        assertDoesNotThrow( () -> player.placeCard(playableCardEmpty(), new Point(3,1)), "Invalid position");
        assertDoesNotThrow( () -> player.placeCard(playableCard3(), new Point(2,0)), "Invalid position");
        assertDoesNotThrow( () -> player.placeCard(goldCard, new Point(1,1)), "Invalid position");

        assertEquals(3, goldCard.calculatePoints(board));
    }

    private StarterCard starterCard() {
        String id = "016";
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = CardSymbolKingdom.LEAF;
        centerSymbols[1] = CardSymbolKingdom.WOLF;
        centerSymbols[2] = CardSymbolKingdom.MUSHROOM;
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(null);
        frontCorners[1] = new Corner(null);
        Corner[] backCorners = new Corner[4];
        frontCorners[0] = new Corner(CardSymbolKingdom.MUSHROOM);
        frontCorners[1] = new Corner(CardSymbolKingdom.WOLF);
        frontCorners[2] = new Corner(CardSymbolKingdom.BUTTERFLY);
        frontCorners[3] = new Corner(CardSymbolKingdom.LEAF);
        StarterCard starterCard = new StarterCard(id,frontCorners,backCorners,centerSymbols);
        starterCard.setCoord(new Point(0,0));
        return starterCard;
    }

    private PlayableCard playableCardEmpty() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(null);
        corners[1] = new Corner(null);
        corners[2] = new Corner(null);
        corners[3] = new Corner(null);
        return new ResourceCard("000", CardSymbolKingdom.BUTTERFLY, corners, 0);
    }
    private PlayableCard playableCard1() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        corners[2] = new Corner(CardSymbolObject.SCROLL);
        corners[3] = new Corner(CardSymbolObject.SCROLL);
        return new ResourceCard("000", CardSymbolKingdom.BUTTERFLY, corners, 0);
    }

    private PlayableCard playableCard2() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(CardSymbolObject.POTION);
        corners[2] = new Corner(CardSymbolObject.SCROLL);
        corners[3] = new Corner(null);
        return new ResourceCard("000", CardSymbolKingdom.BUTTERFLY, corners, 0);
    }

    private PlayableCard playableCard3() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        corners[2] = new Corner(null);
        corners[3] = new Corner(null);
        return new ResourceCard("000", CardSymbolKingdom.BUTTERFLY, corners, 0);
    }
}
