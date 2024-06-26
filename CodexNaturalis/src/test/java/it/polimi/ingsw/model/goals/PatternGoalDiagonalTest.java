package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.player.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static it.polimi.ingsw.model.enumeration.CardSymbolKingdom.*;
import static org.junit.jupiter.api.Assertions.*;

class PatternGoalDiagonalTest implements GoalTest, ConstructorTest {
    private static String id;
    private static int points;
    private static boolean isPrimaryDiagonal;
    private static CardSymbolKingdom kingdom;
    private static PatternGoalDiagonal goal;

    private static Board board;

    @BeforeAll
    static void setup(){
        id = "003";
        points = 2;
        isPrimaryDiagonal = true;
        kingdom = BUTTERFLY;
        goal = new PatternGoalDiagonal(id, points, true, kingdom);
        ArrayList<Goal> sharedGoals = new ArrayList<>();
        sharedGoals.add(new PatternGoalDiagonal("002", 2, false, WOLF));
        sharedGoals.add(new PatternGoalDiagonal("003", 2, true, BUTTERFLY));
        Goal privateGoal = new PatternGoalL("003", 5, LDirection.BOTTOM_LEFT, CardSymbolKingdom.WOLF, CardSymbolKingdom.LEAF);
        board = new Board(privateGoal, sharedGoals);
    }

    @Override
    @Test
    public void testConstructor() {
        assertEquals(id, goal.getId());
        assertEquals(points, goal.getPoints());
        assertEquals(isPrimaryDiagonal, goal.isPrimaryDiagonal());
        assertEquals(kingdom, goal.getKingdom());
    }

    @Override
    @Test
    public void checkGoal() {
        ArrayList<Card> playedCards = new ArrayList<>();
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
        playedCards.add(starterCard);
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        ResourceCard resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(0,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(0,4));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-2,0));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(1,5));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(2,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(2,4));
        playedCards.add(resourceCard);
        board.setPlayedCards(playedCards);
        assertEquals(4,board.getSharedGoals().get(1).checkGoal(board));
    }

    @Test
    public void checkGoalNoSameSymbol() {
        ArrayList<Card> playedCards = new ArrayList<>();
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
        playedCards.add(starterCard);
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        ResourceCard resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(0,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(0,4));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-2,0));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(1,5));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(2,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(2,4));
        playedCards.add(resourceCard);
        board.setPlayedCards(playedCards);
        assertEquals(0,board.getSharedGoals().get(0).checkGoal(board));
    }

    @Test
    public void checkGoalStarterInDiagonal() {
        ArrayList<Card> playedCards = new ArrayList<>();
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
        playedCards.add(starterCard);
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        ResourceCard resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(0,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(-1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(0,4));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(-1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(-2,0));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(1,5));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(2,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(2,4));
        playedCards.add(resourceCard);
        board.setPlayedCards(playedCards);
        assertEquals(0,board.getSharedGoals().get(1).checkGoal(board));
    }
}