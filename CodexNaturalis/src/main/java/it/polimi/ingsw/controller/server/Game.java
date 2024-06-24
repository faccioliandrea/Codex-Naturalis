package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.chat.ServerChatHandler;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckInitializationException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

/**
 * Class that represents a game
 */
public class Game {
    final private ArrayList<Player> players;
    final private GameModel gameModel;
    final private ServerChatHandler chatHandler;

    /**
     * Constructor for the Game class
     * @param gameId unique id of the game
     * @param lobby Lobby object that contains players
     * @throws DeckInitializationException if there's an error in initializing the decks from the json files
     * @throws InvalidNumberOfPlayersException if the number of players is not between 2 and 4
     */

    public Game(String gameId, Lobby lobby) throws DeckInitializationException, InvalidNumberOfPlayersException {
        PlayerColor[] colors = PlayerColor.values();
        players = new ArrayList<>();
        for(String user : lobby.getUsers()){
            players.add(new Player(user, colors[players.size()]));
        }
        gameModel = new GameModel(gameId, players);
        chatHandler = lobby.getChatHandler();
    }

    /**
     * @return the game model
     */
    protected GameModel getGameModel() {
        return gameModel;
    }

    /**
     * @return the list of players
     */
    protected ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @return the chat handler
     */
    protected ServerChatHandler getChatHandler() {
        return chatHandler;
    }
}
