package it.polimi.ingsw.model;

import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.cards.PlayableCard;
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

    public GameModel(String gameId, ArrayList<Player> players) {
        this.gameId = gameId;
        this.players = players;
        this.currentTurn = 0;
        this.totalTurns = 0;
        this.starterCardDeck = new ArrayList<>();
        this.resourceCardDeck = new ArrayList<>();
        this.goldCardDeck = new ArrayList<>();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public void setTotalTurns(int totalTurns) {
        this.totalTurns = totalTurns;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<StarterCard> getStarterCardDeck() {
        return starterCardDeck;
    }

    public void setStarterCardDeck(ArrayList<StarterCard> starterCardDeck) {
        this.starterCardDeck = starterCardDeck;
    }

    public ArrayList<ResourceCard> getResourceCardDeck() {
        return resourceCardDeck;
    }

    public void setResourceCardDeck(ArrayList<ResourceCard> resourceCardDeck) {
        this.resourceCardDeck = resourceCardDeck;
    }

    public ArrayList<GoldCard> getGoldCardDeck() {
        return goldCardDeck;
    }

    public void setGoldCardDeck(ArrayList<GoldCard> goldCardDeck) {
        this.goldCardDeck = goldCardDeck;
    }

    public ArrayList<Goal> getGoalsDeck() {
        return goalsDeck;
    }

    public void setGoalsDeck(ArrayList<Goal> goalsDeck) {
        this.goalsDeck = goalsDeck;
    }

    private void initGame() throws InvalidNumberOfPlayersException {
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
        } catch (IllegalArgumentException e) {
            // TODO: notify controller
        } catch (FileNotFoundException e) {
            // TODO: notify controller
        } catch (JsonParseException e) {
            // TODO: notify controller
        }

        Collections.shuffle(starterCardDeck);
        Collections.shuffle(resourceCardDeck);
        Collections.shuffle(goldCardDeck);
        Collections.shuffle(goalsDeck);

        // MARK: total cards - hand for each player + 1 turn for each player (last turn if deck ends) + additional turns to come back to first player
        this.totalTurns = (this.goldCardDeck.size() + this.resourceCardDeck.size()) - (2 * this.players.size()) + (this.players.size() % 2);
    }


    // TODO: extra turn (not counted) where players select starter card side + private goal
    public void startGame() {
        try {
            initGame();
        } catch (InvalidNumberOfPlayersException e) {
            // TODO: notify controller
        }

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

    public Player getFirstPlayer() {
        return this.players.get(0);
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.currentTurn % this.players.size());
    }

    public void startEndGame() {
        // current + final turn + additional turns to come back to first player
        totalTurns = currentTurn + this.players.size() + (this.players.size() - currentTurn % players.size());
    }

    public GoldCard fetchGoldCard(int index) {
        return index < 2 ? this.goldCardDeck.remove(index) : this.goldCardDeck.remove(2);
    }

    public ResourceCard fetchResourceCard(int index) {
        return index < 2 ? this.resourceCardDeck.remove(index) : this.resourceCardDeck.remove(2);
    }
}
