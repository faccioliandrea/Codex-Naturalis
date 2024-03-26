package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.gold.GoldCard;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfPlayersException;

import java.util.ArrayList;

public class GameModel {
    private String gameId;
    private int currentTurn;
    private int totalTurns;
    private ArrayList<Player> players;
    private ArrayList<StarterCard> starterCardDeck;
    private ArrayList<ResourceCard> resourceCardDeck;
    private ArrayList<GoldCard> goldCardDeck;

    public GameModel(String gameId) {
        this.gameId = gameId;
        this.currentTurn = 0;
        this.totalTurns = 0;

        this.players = new ArrayList<>();
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

    private void initGame() throws InvalidNumberOfPlayersException {
        try {
            assert players.size() >= 2;
            assert players.size() <= 4;
        } catch (AssertionError e) {
            throw new InvalidNumberOfPlayersException();
        }
        // TODO: init decks
    }

    public void startGame() {
        try {
            initGame();
        } catch (InvalidNumberOfPlayersException e) {
            // ...
        }
    }

    public Player getFirstPlayer() {
        return this.players.get(0);
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.currentTurn % this.players.size());
    }

    public void startEndGame() {
        // TODO: calculate max turns
    }

    public GoldCard fetchGoldCard(int index) {
        return index < 2 ? this.goldCardDeck.remove(index) : this.goldCardDeck.remove(2);
    }

    public ResourceCard fetchresourceCard(int index) {
        return index < 2 ? this.resourceCardDeck.remove(index) : this.resourceCardDeck.remove(2);
    }
}
