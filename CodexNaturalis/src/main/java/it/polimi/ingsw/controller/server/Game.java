package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckInitializationException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private GameModel gameModel;

    /**
     * Constructor for the Game class
     * @param gameId unique id of the game
     * @param users List of {@code Player} (must be between 2 and 4)
     * @throws DeckInitializationException if there's an error in initializing the decks from the json files
     * @throws InvalidNumberOfPlayersException if the number of players is not between 2 and 4
     */
    public Game(String gameId, ArrayList<String> users) throws DeckInitializationException, InvalidNumberOfPlayersException {
        PlayerColor[] colors = PlayerColor.values();
        players = new ArrayList<>();
        for(String user : users){
            players.add(new Player(user, colors[players.size()]));
        }
        gameModel = new GameModel(gameId, players);
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

}
