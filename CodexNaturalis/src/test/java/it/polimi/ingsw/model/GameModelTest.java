package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.gold.GoldCard;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        gameModel.startGame();
        assertEquals(72, gameModel.getTotalTurns());
        gameModel.getPlayers().forEach(x -> {
            assertEquals(3, x.getHand().size());
            assertEquals(2, x.getPrivateGoals().size());
            assertEquals(2, x.getBoard().getSharedGoals().size());
            assertNotNull(x.getStarterCard());
        });
    }

    @Test
    void startEndGame() {
        gameModel.startGame();
        gameModel.setCurrentTurn(37);
        gameModel.startEndGame();
        assertEquals(44, gameModel.getTotalTurns());
    }

    @Test
    void fetchGoldCard() {
        gameModel.startGame();
        int deckSize = gameModel.getGoldCardDeck().size();
        GoldCard toBeDrawn = gameModel.getGoldCardDeck().get(0);
        assertEquals(toBeDrawn, gameModel.fetchGoldCard(0));
        toBeDrawn = gameModel.getGoldCardDeck().get(1);
        assertEquals(toBeDrawn, gameModel.fetchGoldCard(1));
        toBeDrawn = gameModel.getGoldCardDeck().get(2);
        assertEquals(toBeDrawn, gameModel.fetchGoldCard(2));
        assertEquals(deckSize-3, gameModel.getGoldCardDeck().size());
    }

    @Test
    void fetchResourceCard() {
        gameModel.startGame();
        int deckSize = gameModel.getResourceCardDeck().size();
        ResourceCard toBeDrawn = gameModel.getResourceCardDeck().get(0);
        assertEquals(toBeDrawn, gameModel.fetchResourceCard(0));
        toBeDrawn = gameModel.getResourceCardDeck().get(1);
        assertEquals(toBeDrawn, gameModel.fetchResourceCard(1));
        toBeDrawn = gameModel.getResourceCardDeck().get(2);
        assertEquals(toBeDrawn, gameModel.fetchResourceCard(2));
        assertEquals(deckSize-3, gameModel.getResourceCardDeck().size());
    }


}