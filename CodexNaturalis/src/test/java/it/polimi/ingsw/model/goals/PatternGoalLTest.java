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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static it.polimi.ingsw.model.enumeration.CardSymbolKingdom.*;
import static org.junit.jupiter.api.Assertions.*;

class PatternGoalLTest implements GoalTest, ConstructorTest {
    private String id;
    private int points;

    private LDirection direction;
    private CardSymbolKingdom verticalKingdom;
    private CardSymbolKingdom horizontalKingdom;
    private PatternGoalL goal;

    private static Board board;
    @BeforeEach
    void setup(){
        id = "002";
        points = 3;
        direction = LDirection.TOPLEFT;
        verticalKingdom = MUSHROOM;
        horizontalKingdom = LEAF;
        goal = new PatternGoalL(id,points,direction,verticalKingdom,horizontalKingdom);

        ArrayList<Goal> sharedGoals = new ArrayList<>();
        sharedGoals.add(new PatternGoalL("002", 3, LDirection.BOTTOMLEFT, WOLF,MUSHROOM));
        sharedGoals.add(new PatternGoalL("003", 3, LDirection.TOPRIGHT, BUTTERFLY, LEAF));
        PatternGoalL privateGoal = new PatternGoalL("003", 3, LDirection.BOTTOMRIGHT, WOLF, LEAF);
        board = new Board(privateGoal, sharedGoals);
    }
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
        frontCorners[0] = new Corner(false, null);
        frontCorners[1] = new Corner(false, null);
        Corner[] backCorners = new Corner[4];
        frontCorners[0] = new Corner(false, MUSHROOM);
        frontCorners[1] = new Corner(false, WOLF);
        frontCorners[2] = new Corner(false, BUTTERFLY);
        frontCorners[3] = new Corner(false, LEAF);
        StarterCard starterCard = new StarterCard(id,frontCorners,backCorners,centerSymbols);
        starterCard.setCoord(coord);
        playedCards.add(starterCard);
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
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
        resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(1,5));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(2,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(2,4));
        playedCards.add(resourceCard);
        board.setPlayedCards(playedCards);
        assertEquals(6,board.getSharedGoals().get(1).checkGoal(board));
    }
    @Test
    public void checkGoalBL() {
        ArrayList<Card> playedCards = new ArrayList<>();
        String id = "016";
        Point coord = new Point(0,0);
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = LEAF;
        centerSymbols[1] = WOLF;
        centerSymbols[2] = MUSHROOM;
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(false, null);
        frontCorners[1] = new Corner(false, null);
        Corner[] backCorners = new Corner[4];
        frontCorners[0] = new Corner(false, MUSHROOM);
        frontCorners[1] = new Corner(false, WOLF);
        frontCorners[2] = new Corner(false, BUTTERFLY);
        frontCorners[3] = new Corner(false, LEAF);
        StarterCard starterCard = new StarterCard(id,frontCorners,backCorners,centerSymbols);
        starterCard.setCoord(coord);
        playedCards.add(starterCard);
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
        ResourceCard resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(0,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(0,4));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(-1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-2,0));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(1,5));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(2,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(2,4));
        playedCards.add(resourceCard);
        board.setPlayedCards(playedCards);
        assertEquals(6,board.getSharedGoals().get(0).checkGoal(board));
    }

    @Test
    public void checkGoalBR() {
        ArrayList<Card> playedCards = new ArrayList<>();
        String id = "016";
        Point coord = new Point(0,0);
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = LEAF;
        centerSymbols[1] = WOLF;
        centerSymbols[2] = MUSHROOM;
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(false, null);
        frontCorners[1] = new Corner(false, null);
        Corner[] backCorners = new Corner[4];
        frontCorners[0] = new Corner(false, MUSHROOM);
        frontCorners[1] = new Corner(false, WOLF);
        frontCorners[2] = new Corner(false, BUTTERFLY);
        frontCorners[3] = new Corner(false, LEAF);
        StarterCard starterCard = new StarterCard(id,frontCorners,backCorners,centerSymbols);
        starterCard.setCoord(coord);
        playedCards.add(starterCard);
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
        ResourceCard resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(0,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(0,4));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(-1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-2,0));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(1,5));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(2,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(2,4));
        playedCards.add(resourceCard);
        board.setPlayedCards(playedCards);
        assertEquals(6,board.getPrivateGoal().checkGoal(board));
    }

    @Test
    public void checkGoalTL() {
        ArrayList<Card> playedCards = new ArrayList<>();
        String id = "016";
        Point coord = new Point(0,0);
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = LEAF;
        centerSymbols[1] = WOLF;
        centerSymbols[2] = MUSHROOM;
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(false, null);
        frontCorners[1] = new Corner(false, null);
        Corner[] backCorners = new Corner[4];
        frontCorners[0] = new Corner(false, MUSHROOM);
        frontCorners[1] = new Corner(false, WOLF);
        frontCorners[2] = new Corner(false, BUTTERFLY);
        frontCorners[3] = new Corner(false, LEAF);
        StarterCard starterCard = new StarterCard(id,frontCorners,backCorners,centerSymbols);
        starterCard.setCoord(coord);
        playedCards.add(starterCard);
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
        ResourceCard resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", WOLF, corners, 0);
        resourceCard.setCoord(new Point(0,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(0,4));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(-1,1));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", BUTTERFLY, corners, 0);
        resourceCard.setCoord(new Point(-2,0));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", LEAF, corners, 0);
        resourceCard.setCoord(new Point(1,5));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(1,3));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(2,2));
        playedCards.add(resourceCard);
        resourceCard = new ResourceCard("000", MUSHROOM, corners, 0);
        resourceCard.setCoord(new Point(2,4));
        playedCards.add(resourceCard);
        board.setPlayedCards(playedCards);
        assertEquals(6,goal.checkGoal(board));
    }

    @Override
    @Test
    public void testConstructor() {
        assertEquals(id,goal.getId());
        assertEquals(points,goal.getPoints());
        assertEquals(direction,goal.getDirection());
        assertEquals(verticalKingdom,goal.getVerticalKingdom());
        assertEquals(horizontalKingdom,goal.getHorizontalKingdom());
    }
}