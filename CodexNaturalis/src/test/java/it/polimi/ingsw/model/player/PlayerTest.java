package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.gold.FixedPointsGoldCard;
import it.polimi.ingsw.model.cards.gold.GoldCardRequirement;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;
import it.polimi.ingsw.model.goals.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static it.polimi.ingsw.model.enumeration.CardSymbolKingdom.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest implements ConstructorTest {

    private String username;
    private PlayerColor playerColor;
    private Player player;

    @BeforeEach
    void setUp() {
        username = "playerUsername";
        playerColor = PlayerColor.BLUE;
        player = new Player(username, playerColor);
    }

    @Override
    @Test
    public void testConstructor() {
        assertEquals(username, player.getUsername());
        assertEquals(playerColor, player.getPlayerColor());
    }

    @Test
    void drawCard() {
        String id = "000";
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 5;
        ResourceCard resourceCard = new ResourceCard(id, CardSymbolKingdom.MUSHROOM, corners, points);
        player.drawCard(resourceCard);
        assertTrue(player.getHand().contains(resourceCard));

    }

    @Test
    void choosePrivateGoal() {
        String id = "003";
        int points = 2;
        boolean isPrimaryDiagonal = false;
        PatternGoalDiagonal patternGoal = new PatternGoalDiagonal(id, points, isPrimaryDiagonal, CardSymbolKingdom.BUTTERFLY);
        String id2 = "001";
        GoalRequirement[] goal2 = new GoalRequirement[2];
        goal2[0] = new GoalRequirement(CardSymbolKingdom.WOLF, 3);
        goal2[1] = new GoalRequirement(CardSymbolKingdom.MUSHROOM, 1);
        int points2 = 3;
        SymbolGoal symbolGoal = new SymbolGoal(id2,points2,goal2);
        ArrayList<Goal> privateGoals = new ArrayList<>();
        privateGoals.add(patternGoal);
        privateGoals.add(symbolGoal);
        player.setPrivateGoals(privateGoals);
        player.choosePrivateGoal(0);
        assertTrue(player.getPrivateGoals().isEmpty() );
        assertEquals(player.getBoard().getPrivateGoal(), patternGoal);


    }

    @Test
    void successfulPlaceCard() throws InvalidPositionException, RequirementsNotSatisfied {
        ArrayList<Goal> sharedGoals = new ArrayList<>();
        sharedGoals.add(new PatternGoalL("001", 1, LDirection.BOTTOMLEFT, CardSymbolKingdom.MUSHROOM, CardSymbolKingdom.WOLF));
        sharedGoals.add(new PatternGoalDiagonal("002", 3, true, CardSymbolKingdom.LEAF));
        Goal privateGoal = new PatternGoalL("003", 5, LDirection.BOTTOMLEFT, CardSymbolKingdom.WOLF, CardSymbolKingdom.LEAF);
        Board board = new Board(privateGoal, sharedGoals);
        player.setBoard(board);

        String id1 = "016";
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = LEAF;
        centerSymbols[1] = WOLF;
        centerSymbols[2] = MUSHROOM;
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(null);
        frontCorners[1] = new Corner(null);
        Corner[] backCorners = new Corner[4];
        backCorners[0] = new Corner(MUSHROOM);
        backCorners[1] = new Corner(WOLF);
        backCorners[2] = new Corner(BUTTERFLY);
        backCorners[3] = new Corner(LEAF);
        StarterCard starterCard = new StarterCard(id1, frontCorners, backCorners, centerSymbols);
        starterCard.setCoord(new Point(0,0));

        ArrayList<Card> playedCard = new ArrayList<>();
        playedCard.add(starterCard);
        player.getBoard().setPlayedCards(playedCard);

        String id = "001";
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 5;
        ResourceCard resourceCard = new ResourceCard(id, CardSymbolKingdom.MUSHROOM, corners, points);
        player.drawCard(resourceCard);

        Point coor = new Point(1,1);

        player.placeCard(resourceCard, coor);

        assertEquals(resourceCard.getCoord(), coor);
        assertFalse(player.getHand().contains(resourceCard));
        assertTrue(player.getBoard().getPlayedCards().contains(resourceCard));

    }

    @Test
    void exceptionalPlaceGoldCard() throws InvalidPositionException, RequirementsNotSatisfied {
        ArrayList<Goal> sharedGoals = new ArrayList<>();
        sharedGoals.add(new PatternGoalL("001", 1, LDirection.BOTTOMLEFT, CardSymbolKingdom.MUSHROOM, CardSymbolKingdom.WOLF));
        sharedGoals.add(new PatternGoalDiagonal("002", 3, true, CardSymbolKingdom.LEAF));
        Goal privateGoal = new PatternGoalL("003", 5, LDirection.BOTTOMLEFT, CardSymbolKingdom.WOLF, CardSymbolKingdom.LEAF);
        Board board = new Board(privateGoal, sharedGoals);
        player.setBoard(board);

        String id1 = "016";
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = MUSHROOM;
        centerSymbols[1] = LEAF;
        centerSymbols[2] = MUSHROOM;
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(null);
        frontCorners[1] = new Corner(null);
        Corner[] backCorners = new Corner[4];
        backCorners[0] = new Corner(MUSHROOM);
        backCorners[1] = new Corner(LEAF);
        backCorners[2] = new Corner(BUTTERFLY);
        backCorners[3] = new Corner(LEAF);
        StarterCard starterCard = new StarterCard(id1, frontCorners, backCorners, centerSymbols);

        player.placeCard(starterCard, new Point(0,0));

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
        player.drawCard(goldCard);

        Point coor = new Point(1,1);

        Exception exception = assertThrows(RequirementsNotSatisfied.class, () -> {
            player.placeCard(goldCard, coor);
        });
        String expectedMessage = "Error: you don't satisfy the requirements!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void exceptionalPlaceCard() {
        String id1 = "016";
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = LEAF;
        centerSymbols[1] = WOLF;
        centerSymbols[2] = MUSHROOM;
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(null);
        frontCorners[1] = new Corner(null);
        Corner[] backCorners = new Corner[4];
        backCorners[0] = new Corner(MUSHROOM);
        backCorners[1] = new Corner(WOLF);
        backCorners[2] = new Corner(BUTTERFLY);
        backCorners[3] = new Corner(LEAF);
        StarterCard starterCard = new StarterCard(id1, frontCorners, backCorners, centerSymbols);
        starterCard.setCoord(new Point(0,0));

        ArrayList<Card> playedCard = new ArrayList<>();
        playedCard.add(starterCard);
        player.getBoard().setPlayedCards(playedCard);

        String id = "001";
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[1] = new Corner(null);
        int points = 5;
        ResourceCard resourceCard = new ResourceCard(id, CardSymbolKingdom.MUSHROOM, corners, points);
        player.drawCard(resourceCard);

        Exception exception = assertThrows(InvalidPositionException.class, () -> {
            Point invalidCoor = new Point(1,-1);
            player.placeCard(resourceCard, invalidCoor);
        });
        String expectedMessage = "Error: you can't place a card here!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }



}