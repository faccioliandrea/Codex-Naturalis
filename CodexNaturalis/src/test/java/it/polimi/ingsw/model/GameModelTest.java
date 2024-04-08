package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest implements ConstructorTest{

    private GameModel gameModel;
    private String gameId;
    private ArrayList<Player> players;

    @BeforeEach
    void setUp() {
        gameId = "xxxxx";
        Player player1 = new Player("foo", PlayerColor.BLUE);
        Player player2 = new Player("bar", PlayerColor.RED);
        Player player3 = new Player("baz", PlayerColor.GREEN);
        Player player4 = new Player("qux", PlayerColor.YELLOW);
        players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        gameModel = new GameModel(gameId, players);
    }

    @Test
    @Override
    public void testConstructor() {
        assertEquals(gameModel.getGameId(), gameId);
        assertEquals(gameModel.getPlayers(), players);
    }

    @Test
    void startGame() {
    }

    @Test
    void startEndGame() {
    }

    @Test
    void fetchGoldCard() {
    }

    @Test
    void fetchResourceCard() {
    }


}