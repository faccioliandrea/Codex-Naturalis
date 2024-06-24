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
        sharedGoals.add(new PatternGoalL("001", 1, LDirection.BOTTOM_LEFT, CardSymbolKingdom.MUSHROOM, CardSymbolKingdom.WOLF));
        sharedGoals.add(new PatternGoalDiagonal("002", 3, true, CardSymbolKingdom.LEAF));
        privateGoal = new PatternGoalL("003", 5, LDirection.BOTTOM_LEFT, CardSymbolKingdom.WOLF, CardSymbolKingdom.LEAF);
        board = new Board(privateGoal, sharedGoals);
    }

    @Override
    @Test
    public void testConstructor() {
        assertEquals(privateGoal, board.getPrivateGoal());
        assertEquals(sharedGoals, board.getSharedGoals());
    }

    @Test
    public void testAvailablePositionsSingleCard() {
        board.setPlayedCards(new ArrayList<>());
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[2] = new Corner(null);
        ResourceCard c = new ResourceCard("000", CardSymbolKingdom.LEAF, corners, 0);
        c.setCoord(new Point(5, 5));
        board.addPlayedCard(c);
        ArrayList<Point> available = board.availablePositions();
        assert (available.contains(new Point(6, 4)));
    }

    @Test
    public void testAvailablePositionsDoubleCards() {
        board.setPlayedCards(new ArrayList<>());
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[2] = new Corner(null);
        ResourceCard c = new ResourceCard("000", CardSymbolKingdom.LEAF, corners, 0);
        c.setCoord(new Point(5, 5));

        board.addPlayedCard(c);
        ArrayList<Point> available = board.availablePositions();
        assert (available.contains(new Point(6, 4)));
        assert (available.contains(new Point(4, 6)));

        ResourceCard c2 = new ResourceCard("001", CardSymbolKingdom.LEAF, corners, 0);
        c2.setCoord(new Point(4, 6));
        c2.setFlipped(true);

        board.addPlayedCard(c2);
        available = board.availablePositions();
        assert (available.contains(new Point(6, 4)));
        assert (available.contains(new Point(3, 7)));
        assert (available.contains(new Point(3, 5)));
        assert (available.contains(new Point(5, 7)));
        assert (!available.contains(new Point(6, 6)));
        assert (!available.contains(new Point(4, 4)));
    }

    @Test
    public void testAvailablePositionsUnavailableCorner() {
        board.setPlayedCards(new ArrayList<>());
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[2] = new Corner(null);
        ResourceCard c = new ResourceCard("000", CardSymbolKingdom.LEAF, corners, 0);
        c.setCoord(new Point(5, 5));

        board.addPlayedCard(c);

        ResourceCard c2 = new ResourceCard("001", CardSymbolKingdom.LEAF, corners, 0);
        c2.setCoord(new Point(7, 5));
        c2.setFlipped(true);

        board.addPlayedCard(c2);

        ResourceCard c3 = new ResourceCard("002", CardSymbolKingdom.LEAF, corners, 0);
        c3.setCoord(new Point(7, 7));

        board.addPlayedCard(c3);

        ArrayList<Point> available = board.availablePositions();
        assert (!available.contains(new Point(6, 6)));
    }


    @Test
    public void testAddToPlayedCards() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CardSymbolObject.SCROLL);
        corners[2] = new Corner(null);
        ResourceCard c = new ResourceCard("000", CardSymbolKingdom.LEAF, corners, 0);
        c.setCoord(new Point(5, 5));

        ResourceCard c2 = new ResourceCard("001",  CardSymbolKingdom.LEAF, corners, 0);
        c2.setFlipped(true);
        c2.setCoord(new Point(4, 6));

        board.addPlayedCard(c);
        assertEquals(c, board.getPlayedCards().get(board.getPlayedCards().size() - 1));
        assertEquals(1, board.getSymbols().get(CardSymbolObject.SCROLL));

        board.addPlayedCard(c2);
        assertEquals(c2, board.getPlayedCards().get(board.getPlayedCards().size() - 1));
        assertEquals(0, board.getSymbols().get(CardSymbolObject.SCROLL));
        assertEquals(1, board.getSymbols().get(CardSymbolKingdom.LEAF));
        assert c.getCorners()[0].isCovered();
    }
}
