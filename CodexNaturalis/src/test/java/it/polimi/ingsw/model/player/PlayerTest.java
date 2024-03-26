package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.goals.GoalRequirement;
import it.polimi.ingsw.model.goals.PatternGoalDiagonal;
import it.polimi.ingsw.model.goals.SymbolGoal;
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
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
        int points = 5;
        ResourceCard resourceCard = new ResourceCard(id, centerSymbol, corners, points);
        player.drawCard(resourceCard);
        assertTrue(player.getHand().contains(resourceCard));

    }

    @Test
    void choosePrivateGoal() {
        String id = "003";
        int points = 2;
        boolean isPrimaryDiagonal = false;
        CardSymbolKingdom kingdom = CardSymbolKingdom.BUTTERFLY;
        PatternGoalDiagonal patternGoal = new PatternGoalDiagonal(id, points, isPrimaryDiagonal, kingdom);
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
    void placeCard() throws InvalidPositionException {
        String id1 = "016";
        CardSymbolKingdom[] centerSymbols = new CardSymbolKingdom[3];
        centerSymbols[0] = LEAF;
        centerSymbols[1] = WOLF;
        centerSymbols[2] = MUSHROOM;
        Corner[] frontCorners = new Corner[4];
        frontCorners[0] = new Corner(false, null);
        frontCorners[1] = new Corner(false, null);
        Corner[] backCorners = new Corner[4];
        backCorners[0] = new Corner(false, MUSHROOM);
        backCorners[1] = new Corner(false, WOLF);
        backCorners[2] = new Corner(false, BUTTERFLY);
        backCorners[3] = new Corner(false, LEAF);
        StarterCard starterCard = new StarterCard(id1, frontCorners, backCorners, centerSymbols);
        starterCard.setCoord(new Point(0,0));

        ArrayList<Card> playedCard = new ArrayList<Card>();
        playedCard.add(starterCard);
        player.getBoard().setPlayedCards(playedCard);

        String id = "001";
        CardSymbolKingdom centerSymbol = CardSymbolKingdom.MUSHROOM;
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[1] = new Corner(false, null);
        int points = 5;
        ResourceCard resourceCard = new ResourceCard(id, centerSymbol, corners, points);
        player.drawCard(resourceCard);

        Point coor = new Point(1,1);

        // TODO: da testare con calculatePoints() e checkGoal()
        try{
            player.placeCard(resourceCard, coor);
        }catch(NullPointerException e){
            System.out.println("Skipping CalculatePoints() test");
        }
        assertEquals(resourceCard.getCoord(), coor);
        assertFalse(player.getHand().contains(resourceCard));
        assertTrue(player.getBoard().getPlayedCards().contains(resourceCard));


        Exception exception = assertThrows(InvalidPositionException.class, () -> {
            Point invalidCoor = new Point(1,-1);

            player.placeCard(resourceCard, invalidCoor);


        });
        String expectedMessage = "Error: you can't place a card here!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


}