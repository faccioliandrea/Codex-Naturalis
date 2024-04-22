package it.polimi.ingsw.model;

import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.exceptions.DeckInitializationException;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.gold.GoldCard;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.parser.Parser;

import java.io.FileNotFoundException;
import java.util.*;

public class GameModel {
    private String gameId;
    private int currentTurn;
    private int totalTurns;
    private ArrayList<Player> players;
    private ArrayList<StarterCard> starterCardDeck;
    private ArrayList<ResourceCard> resourceCardDeck;
    private ArrayList<GoldCard> goldCardDeck;
    private ArrayList<Goal> goalsDeck;

    /**
     * Constructor
     * @param gameId unique id of the game
     * @param players List of {@code Player} (must be between 2 and 4)
     * @throws InvalidNumberOfPlayersException if the number of players is not between 2 and 4
     * @throws DeckInitializationException if there's an error in initializing the decks from the json files
     */
    public GameModel(String gameId, ArrayList<Player> players) throws InvalidNumberOfPlayersException, DeckInitializationException  {
        this.gameId = gameId;
        this.players = players;
        this.currentTurn = 0;
        this.totalTurns = 0;

        initGame();
    }

    /**
     * Gets the gameId attribute
     * @return gameId value
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Sets the gameId attribute
     * @param gameId value
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * Gets the currentTurn attribute
     * @return currentTurn value
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Sets the currentTurn attribute
     * @param currentTurn value
     */
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    /**
     * Gets the totalTurns attribute
     * @return totalTurns value
     */
    public int getTotalTurns() {
        return totalTurns;
    }

    /**
     * Sets the totalTurns attribute
     * @param totalTurns value
     */
    public void setTotalTurns(int totalTurns) {
        this.totalTurns = totalTurns;
    }

    /**
     * Gets the players attribute
     * @return players value
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the players attribute
     * @param players value
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Gets the starterCardDeck attribute
     * @return starterCardDeck value
     */
    public ArrayList<StarterCard> getStarterCardDeck() {
        return starterCardDeck;
    }

    /**
     * Sets the starterCardDeck attribute
     * @param starterCardDeck value
     */
    public void setStarterCardDeck(ArrayList<StarterCard> starterCardDeck) {
        this.starterCardDeck = starterCardDeck;
    }

    /**
     * Gets the resourceCardDeck attribute
     * @return resourceCardDeck value
     */
    public ArrayList<ResourceCard> getResourceCardDeck() {
        return resourceCardDeck;
    }

    /**
     * Sets the resourceCardDeck attribute
     * @param resourceCardDeck value
     */
    public void setResourceCardDeck(ArrayList<ResourceCard> resourceCardDeck) {
        this.resourceCardDeck = resourceCardDeck;
    }

    /**
     * Gets the goldCardDeck attribute
     * @return goldCardDeck value
     */
    public ArrayList<GoldCard> getGoldCardDeck() {
        return goldCardDeck;
    }

    /**
     * Sets the goldCardDeck attribute
     * @param goldCardDeck value
     */
    public void setGoldCardDeck(ArrayList<GoldCard> goldCardDeck) {
        this.goldCardDeck = goldCardDeck;
    }

    /**
     * Gets the goalsDeck attribute
     * @return goalsDeck value
     */
    public ArrayList<Goal> getGoalsDeck() {
        return goalsDeck;
    }

    /**
     * Sets the goalsDeck attribute
     * @param goalsDeck value
     */
    public void setGoalsDeck(ArrayList<Goal> goalsDeck) {
        this.goalsDeck = goalsDeck;
    }

    /**
     * Initializes the game
     * @throws InvalidNumberOfPlayersException if the number of players is not between 2 and 4
     * @throws DeckInitializationException if there's an error in initializing the decks from the json files
     */
    private void initGame() throws InvalidNumberOfPlayersException, DeckInitializationException {
        try {
            assert players.size() >= 2;
            assert players.size() <= 4;
        } catch (AssertionError e) {
            throw new InvalidNumberOfPlayersException();
        }

        try {
            starterCardDeck = new ArrayList<>(Arrays.asList(Parser.parser.initStarterCards()));
            resourceCardDeck = new ArrayList<>(Arrays.asList(Parser.parser.initResourceCards()));
            goldCardDeck = new ArrayList<>(Arrays.asList(Parser.parser.initGoldCards()));
            goalsDeck = new ArrayList<>(Arrays.asList(Parser.parser.initGoals()));
        } catch (IllegalArgumentException | FileNotFoundException | JsonParseException e) {
            throw new DeckInitializationException(e);
        }

        Collections.shuffle(starterCardDeck);
        Collections.shuffle(resourceCardDeck);
        Collections.shuffle(goldCardDeck);
        Collections.shuffle(goalsDeck);

        // MARK: total cards - hand for each player + 1 turn for each player (last turn if deck ends) + additional turns to come back to first player
        this.totalTurns = (this.goldCardDeck.size() + this.resourceCardDeck.size()) - (2 * this.players.size()) + (this.players.size() % 2);
    }


    /**
     * Starts the game
     */
    public void startGame() {

        // sets starter card for each player
        this.players.forEach(x -> x.setStarterCard(this.starterCardDeck.remove(0)));

        // sets same shared goals for all players
        this.players.forEach(x -> x.getBoard().setSharedGoals(new ArrayList<Goal>(goalsDeck.subList(0,2))));
        // sets private goals for each player
        for (int i=0, j=0; i<this.players.size() && j<goalsDeck.size(); i++, j += 2) {
            this.players.get(i).setPrivateGoals(new ArrayList<Goal>(goalsDeck.subList(j,j+2)));
        }

        // sets hand
        for (int i=0, j=0; i<this.players.size(); i++, j += 2) {
            Collection<ResourceCard> pair = this.resourceCardDeck.subList(j, j+2);
            ArrayList<PlayableCard> tmp = new ArrayList<>(pair);
            this.resourceCardDeck.removeAll(pair);
            tmp.add(this.goldCardDeck.remove(0));
            this.players.get(i).setHand(tmp);
        }
    }

    /**
     * Returns the first player of the game (the one which played the first turn)
     * @return The first player
     */
    public Player getFirstPlayer() {
        return this.players.get(0);
    }

    /**
     * Returns the current player
     * @return The current Player
     */
    public Player getCurrentPlayer() {
        return this.players.get(this.currentTurn % this.players.size());
    }

    /**
     * Signals the start of the final stages of the game
     */
    public void startEndGame() {
        // current + final turn + additional turns to come back to first player
        totalTurns = currentTurn + this.players.size() + (this.players.size() - currentTurn % players.size());
    }

    /**
     * Returns a Gold Card based on the index
     * @param index if 0 or 1 selects one of the two uncovered cards, if 2 selects the first covered card from the deck
     * @return Selected Gold Card
     */
    public GoldCard fetchGoldCard(int index) {
        return index < 2 ? this.goldCardDeck.remove(index) : this.goldCardDeck.remove(2);
    }

    /**
     * Returns a Resource Card based on the index
     * @param index if 0 or 1 selects one of the two uncovered cards, if 2 selects the first covered card from the deck
     * @return Selected Resource Card
     */
    public ResourceCard fetchResourceCard(int index) {
        return index < 2 ? this.resourceCardDeck.remove(index) : this.resourceCardDeck.remove(2);
    }
}
