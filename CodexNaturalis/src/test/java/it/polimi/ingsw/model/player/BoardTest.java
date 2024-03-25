package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.goals.PatternGoalDiagonal;
import it.polimi.ingsw.model.goals.PatternGoalL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest implements ConstructorTest {
    private static ArrayList<Goal> sharedGoals;
    private static Goal privateGoal;
    private static Board board;

    @BeforeEach
    void setup() {
        sharedGoals = new ArrayList<>();
        sharedGoals.add(new PatternGoalL("001", 1, LDirection.BOTTOMLEFT, CardSymbolKingdom.MUSHROOM, CardSymbolKingdom.WOLF));
        sharedGoals.add(new PatternGoalDiagonal("002", 3, true, CardSymbolKingdom.LEAF));
        privateGoal = new PatternGoalL("003", 5, LDirection.BOTTOMLEFT, CardSymbolKingdom.WOLF, CardSymbolKingdom.LEAF);
        board = new Board(privateGoal, sharedGoals);
    }

    @Override
    @Test
    public void testConstructor() {
        assertEquals(privateGoal, board.getPrivateGoal());
        assertEquals(sharedGoals, board.getSharedGoals());
    }

    @Test
    public void testAvailablePositions() {
        board.setPlayedCards(new ArrayList<>());
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[2] = new Corner(false, null);
        ResourceCard c = new ResourceCard("000", new Point(5, 5), false, CardSymbolKingdom.LEAF, corners, 0);
        board.addPlayedCard(c);
        ArrayList<Point> available = board.availablePositions();
        assert (available.contains(new Point(6, 4)));
    }


    @Test
    public void testAddToPlayedCards() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(true, CardSymbolObject.SCROLL);
        corners[2] = new Corner(false, null);
        ResourceCard c = new ResourceCard("000", new Point(5, 5), false, CardSymbolKingdom.LEAF, corners, 0);
        ResourceCard c2 = new ResourceCard("001", new Point(5, 5), true, CardSymbolKingdom.LEAF, corners, 0);
        board.addPlayedCard(c);
        board.addPlayedCard(c2);
        assertEquals(c, board.getPlayedCards().get(board.getPlayedCards().size() - 2));
        assertEquals(c2, board.getPlayedCards().get(board.getPlayedCards().size() - 1));
        assertEquals(1, board.getSymbols().get(CardSymbolObject.SCROLL));
        assertEquals(1, board.getSymbols().get(CardSymbolKingdom.LEAF));
    }
}
