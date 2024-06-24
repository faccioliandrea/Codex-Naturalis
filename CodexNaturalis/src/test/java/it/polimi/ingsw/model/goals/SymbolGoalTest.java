package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.ConstructorTest;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;
import it.polimi.ingsw.model.enumeration.CardSymbolObject;
import it.polimi.ingsw.model.enumeration.LDirection;
import it.polimi.ingsw.model.player.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SymbolGoalTest implements ConstructorTest, GoalTest {
    private SymbolGoal symbolGoal;
    private String id;
    private GoalRequirement[] goal;
    private int points;

    private static Board board;

    @BeforeEach
    void setup(){
        ArrayList<Goal> sharedGoals = new ArrayList<>();
        sharedGoals.add(new PatternGoalL("001", 1, LDirection.BOTTOM_LEFT, CardSymbolKingdom.MUSHROOM, CardSymbolKingdom.WOLF));
        sharedGoals.add(new PatternGoalDiagonal("002", 2,true, CardSymbolKingdom.MUSHROOM));
        Goal privateGoal = new PatternGoalL("003", 5, LDirection.BOTTOM_LEFT, CardSymbolKingdom.WOLF, CardSymbolKingdom.LEAF);
        board = new Board(privateGoal, sharedGoals);
        HashMap<CardSymbol, Integer> map = new HashMap<>(7);
        map.put(CardSymbolKingdom.MUSHROOM,    3);
        map.put(CardSymbolKingdom.BUTTERFLY,   6);
        map.put(CardSymbolKingdom.WOLF,        9);
        map.put(CardSymbolKingdom.LEAF,        0);
        map.put(CardSymbolObject.FEATHER,      2);
        map.put(CardSymbolObject.POTION,       2);
        map.put(CardSymbolObject.SCROLL,       3);
        board.setSymbols(map);
    }
    @Test
    public void checkGoal() {
        id = "001";
        goal = new GoalRequirement[1];
        goal[0] = new GoalRequirement(CardSymbolKingdom.WOLF, 3);
        points = 2;
        symbolGoal = new SymbolGoal(id,points,goal);
        int expectedPoints = 6;
        assertEquals(expectedPoints,symbolGoal.checkGoal(board));
    }

    @Test
    public void checkGoalZeroSymbols() {
        id = "001";
        goal = new GoalRequirement[1];
        goal[0] = new GoalRequirement(CardSymbolKingdom.LEAF, 3);
        points = 2;
        symbolGoal = new SymbolGoal(id,points,goal);
        int points = 0;
        assertEquals(points,symbolGoal.checkGoal(board));
    }
    @Test
    public void checkGoalObject() {

        id = "001";
        goal = new GoalRequirement[3];
        goal[0] = new GoalRequirement(CardSymbolObject.SCROLL, 1);
        goal[1] = new GoalRequirement(CardSymbolObject.FEATHER, 1);
        goal[2] = new GoalRequirement(CardSymbolObject.POTION, 1);
        points = 3;
        symbolGoal = new SymbolGoal(id,points,goal);
        int expectedPoints = 6;
        assertEquals(expectedPoints,symbolGoal.checkGoal(board));
    }

    @Test
    public void checkGoalObjectZeroSymbols() {

        id = "001";
        goal = new GoalRequirement[3];
        goal[0] = new GoalRequirement(CardSymbolObject.SCROLL, 1);
        goal[1] = new GoalRequirement(CardSymbolObject.FEATHER, 1);
        goal[2] = new GoalRequirement(CardSymbolObject.POTION, 1);
        points = 3;
        symbolGoal = new SymbolGoal(id,points,goal);
        HashMap<CardSymbol, Integer> map = new HashMap<>(7);
        map.put(CardSymbolKingdom.MUSHROOM,    3);
        map.put(CardSymbolKingdom.BUTTERFLY,   6);
        map.put(CardSymbolKingdom.WOLF,        9);
        map.put(CardSymbolKingdom.LEAF,        0);
        map.put(CardSymbolObject.FEATHER,      2);
        map.put(CardSymbolObject.POTION,       0);
        map.put(CardSymbolObject.SCROLL,       3);
        board.setSymbols(map);
        int expectedPoints = 0;
        assertEquals(expectedPoints,symbolGoal.checkGoal(board));
    }

    @Override
    @Test
    public void testConstructor() {
        id = "001";
        goal = new GoalRequirement[3];
        goal[0] = new GoalRequirement(CardSymbolObject.SCROLL, 1);
        goal[1] = new GoalRequirement(CardSymbolObject.FEATHER, 1);
        goal[2] = new GoalRequirement(CardSymbolObject.POTION, 1);
        points = 3;
        symbolGoal = new SymbolGoal(id,points,goal);
        assertEquals(id,symbolGoal.getId());
        assertEquals(goal, symbolGoal.getSymbolGoalRequirements());
        assertEquals(points, symbolGoal.getPoints());

    }
}