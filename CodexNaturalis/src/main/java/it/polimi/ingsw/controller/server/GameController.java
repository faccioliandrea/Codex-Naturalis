package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.gold.GoldCard;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.exceptions.DeckInitializationException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;

import java.awt.Point;
import java.util.*;

/**
 * GameController is a singleton class that manages the game logic.
 * It creates and manages the games, and it provides methods to interact with the game.
 */
public class GameController {
    private static GameController instance;
    private final HashMap<String, Game> games = new HashMap<>();

    /** Default constructor */
    private GameController() { }

    /**
     * Singleton instance getter
     * @return the instance of the GameController
     */
    public static synchronized GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    /**
     * @return the map of the games
     */
    protected HashMap<String, Game> getGames() {
        return games;
    }

    /**
     * Create a new game
     * @param gameId the id of the game
     * @param lobby the lobby
     */
    protected void createGame(String gameId, Lobby lobby) throws DeckInitializationException, InvalidNumberOfPlayersException {
        games.put(gameId, new Game(gameId, lobby));
    }

    /**
     * Start the game
     * @param gameId the id of the game
     */
    protected void startGame(String gameId){
        games.get(gameId).getGameModel().startGame();

    }

    /**
     * let the player choose a private goal
     * @param gameID the id of the game
     * @param username the username of the player
     * @param index the index of the card to choose
     */
    protected void choosePrivateGoal(String gameID,String username, int index){
        games.get(gameID).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst().ifPresent(value -> value.choosePrivateGoal(index));
    }

    /**
     * let the player choose the starter card side
     * @param gameID the id of the game
     * @param username the username of the player
     * @param flipped the side of the card
     */
    protected void chooseStarterCardSide(String gameID, String username, boolean flipped) {

        Optional<Player> optionalPlayer = games.get(gameID).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        StarterCard starterCard= player.getStarterCard();
        starterCard.setFlipped(flipped);
        try {
            player.placeCard(player.getStarterCard(), new Point(0,0));
        } catch (InvalidPositionException | RequirementsNotSatisfied e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Handles placement of a card on the board
     * @param gameId the id of the game
     * @param card the card to place
     * @param position the position where to place the card
     * @throws RequirementsNotSatisfied if the requirements are not satisfied
     * @throws InvalidPositionException if the position is invalid
     */
    protected void placeCard(String gameId,Card card, Point position) throws RequirementsNotSatisfied, InvalidPositionException {
        int initialPoints = games.get(gameId).getGameModel().getCurrentPlayer().getCardsPoints();
        games.get(gameId).getGameModel().getCurrentPlayer().placeCard(card, position);
        if (games.get(gameId).getGameModel().getCurrentPlayer().getCardsPoints() >= 20 && initialPoints < 20)
            games.get(gameId).getGameModel().startEndGame();
    }

    /**
     * Draw a card from the ResourceDeck
     * @param gameId the id of the game
     * @param index the index of the card to draw from the ResourceDeck
     */
    protected void drawResource(String gameId,int index){
        PlayableCard card = games.get(gameId).getGameModel().fetchResourceCard(index);
        games.get(gameId).getGameModel().getCurrentPlayer().drawCard(card);
    }

    /**
     * Draw a card from the GoldDeck
     * @param gameId the id of the game
     * @param index the index of the card to draw from the GoldDeck
     */
    protected void drawGold( String gameId, int index){
        PlayableCard card = games.get(gameId).getGameModel().fetchGoldCard(index);
        games.get(gameId).getGameModel().getCurrentPlayer().drawCard(card);
    }

    /**
     * @param gameId the id of the game
     * @param username the username of the player
     * @return the hand of the player
     */
    protected ArrayList<CardInfo> getHand(String gameId, String username) {
        ArrayList<CardInfo> hand = new ArrayList<>();
        Optional<Player> optionalPlayer = games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        for (Card card : player.getHand())
            hand.add(new CardInfo(card));
        return hand;
    }

    /**
     * Get the private goals of the player
     * @param gameId the id of the game
     * @param username the username of the player
     * @return the private goals of the player
     */
    protected ArrayList<GoalInfo> getPrivateGoals(String gameId, String username) {
        ArrayList<GoalInfo> privateGoals = new ArrayList<>();
        Optional<Player> optionalPlayer = games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        for (Goal card : player.getPrivateGoals())
            privateGoals.add(new GoalInfo(card));
        return privateGoals;
    }

    /**
     * Get the chosen private goal of the player
     * @param gameId the id of the game
     * @param username the username of the player
     * @return the private goals of the player
     */
    protected GoalInfo getPrivateGoal(String gameId, String username) {
        Optional<Player> optionalPlayer = games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        return new GoalInfo(player.getBoard().getPrivateGoal());
    }

    /**
     * Get the shared goals of the game
     * @param gameId the id of the game
     * @return the shared goals of the game
     */
    protected ArrayList<GoalInfo> getSharedGoals(String gameId) {
        ArrayList<GoalInfo> sharedGoals = new ArrayList<>();
        for (Goal card : games.get(gameId).getGameModel().getPlayers().get(0).getBoard().getSharedGoals())
            sharedGoals.add(new GoalInfo(card));
        return sharedGoals;
    }

    /**
     * Get the starter card of the player
     * @param gameId the id of the game
     * @param username the username of the player
     * @return the starter card of the player
     */
    protected CardInfo getStarterCard(String gameId, String username) {
        Optional<Player> optionalPlayer = games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        return new CardInfo(player.getStarterCard());

    }

    /**
     * end the turn of the current player and increment the currentTurn value
     * @param gameId the id of the game
     */
    protected void endTurn(String gameId){
        games.get(gameId).getGameModel().setCurrentTurn(games.get(gameId).getGameModel().getCurrentTurn()+1);
    }

    /**
     * @param gameId the id of the game
     * @return the list of the first 3 cards of the resource deck
     */
    protected ArrayList<CardInfo> getResourceDeck(String gameId) {
        ArrayList<CardInfo> resourceDeck = new ArrayList<>();
        int sizeDeck = games.get(gameId).getGameModel().getResourceCardDeck().size();
        List<ResourceCard> subDeck = new ArrayList<>(games.get(gameId).getGameModel().getResourceCardDeck().subList(0, Math.min(sizeDeck, 3)));
        for (ResourceCard card : subDeck) {
            card.setFlipped(subDeck.indexOf(card) == 2);
            resourceDeck.add(new CardInfo(card));
            card.setFlipped(false);
        }
        return resourceDeck;
    }

    /**
     * @param gameId the id of the game
     * @return the list of the first 3 cards of the gold deck
     */
    protected ArrayList<CardInfo> getGoldDeck(String gameId) {
        ArrayList<CardInfo> goldDeck = new ArrayList<>();
        int sizeDeck = games.get(gameId).getGameModel().getGoldCardDeck().size();
        List<GoldCard> subDeck = games.get(gameId).getGameModel().getGoldCardDeck().subList(0,Math.min(sizeDeck, 3));
        for (GoldCard card : subDeck) {
            card.setFlipped(subDeck.indexOf(card) == 2);
            goldDeck.add(new CardInfo(card));
            card.setFlipped(false);
        }
        return goldDeck;
    }

    /**
     * @param gameId the id of the game
     * @return the played cards of the current player, not all the board
     */
    protected ArrayList<CardInfo> getUserBoard(String gameId) {
        ArrayList<CardInfo> board = new ArrayList<>();
        if(games.get(gameId) != null) {
            for (Card card : games.get(gameId).getGameModel().getCurrentPlayer().getBoard().getPlayedCards())
                board.add(new CardInfo(card));
        }
        return board;
    }

    /**
     *
     * @param gameId the id of the game
     * @param username the username of the player
     * @return the user Board
     */
    protected ArrayList<CardInfo> getUserBoardByUsername(String gameId, String username) {
        ArrayList<CardInfo> board = new ArrayList<>();
        Optional<Player> optionalPlayer = games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        if(games.get(gameId) != null) {
            for (Card card : player.getBoard().getPlayedCards())
                board.add(new CardInfo(card));
        }
        return board;
    }

    /**
     * @param gameId the id of the game
     * @return the boards of all the players
     */
    protected  Map<String, ArrayList<CardInfo>> getBoards(String gameId){
        Map<String, ArrayList<CardInfo>> boards = new HashMap<>();
        for(Player player : games.get(gameId).getGameModel().getPlayers()){
            ArrayList<CardInfo> board = new ArrayList<>();
            for (Card card : player.getBoard().getPlayedCards())
                board.add(new CardInfo(card));
            boards.put(player.getUsername(), board);
        }
        return boards;
    }


    /**
     * @param gameId the id of the game
     * @return the points of the current player withouth the goals points
     */
    protected int getUserCardsPoints(String gameId) {
        return games.get(gameId).getGameModel().getCurrentPlayer().getCardsPoints();
    }

    /**
     * @param gameId the id of the game
     * @param username the username of the player
     * @return the points of the player withouth the goals points
     */
    protected int getUserCardsPoints(String gameId, String username) {
        Optional<Player> optionalPlayer = games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        return player.getCardsPoints();
    }

    /**
     * @param gameId the id of the game
     * @return the symbols of the current player
     */
    protected Map<CardSymbol, Integer> getUserSymbols(String gameId) {
        return games.get(gameId).getGameModel().getCurrentPlayer().getBoard().getSymbols();
    }

    /**
     * @param gameId the id of the game
     * @return the symbols of the player
     */
    protected Map<CardSymbol, Integer> getUserSymbols(String gameId, String username) {
        Optional<Player> optionalPlayer = games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        return player.getBoard().getSymbols();
    }

    /**
     * @param gameId the id of the game
     * @return the points of the current player withouth the cards points
     */
    protected int getUserGoalsPoints(String gameId){
        return games.get(gameId).getGameModel().getCurrentPlayer().getGoalPoints();
    }

    /**
     * @param gameId the id of the game
     * @return the points of the player withouth the cards points
     */
    protected int getUserGoalsPoints(String gameId, String username){
        Optional<Player> optionalPlayer = games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        return player.getGoalPoints();
    }

    /**
     * @param gameId the id of the game
     * @return true if the game is finished
     */
    protected boolean isGameFinished(String gameId) {
        return games.get(gameId).getGameModel().getCurrentTurn() == games.get(gameId).getGameModel().getTotalTurns();
    }

    /**
     * called when the game is finished, return all the cards points to the players to generete the leaderboard
     */
    protected HashMap<String, Integer> getLeaderboard(String gameId){
        HashMap<String, Integer> leaderboard = new HashMap<>();
        for( int i = 0; i < games.get(gameId).getPlayers().size(); i++)
            leaderboard.put(games.get(gameId).getPlayers().get(i).getUsername(), games.get(gameId).getPlayers().get(i).getCardsPoints());
        return leaderboard;
    }

    /**
     * called when the game is finished, return all the points to the players to generete the leaderboard
     */
    protected HashMap<String, Integer> getFullLeaderboard(String gameId){
        HashMap<String, Integer> leaderboard = new HashMap<>();
        for( int i = 0; i < games.get(gameId).getPlayers().size(); i++)
            leaderboard.put(games.get(gameId).getPlayers().get(i).getUsername(), games.get(gameId).getPlayers().get(i).getCardsPoints() + games.get(gameId).getPlayers().get(i).getGoalPoints());
        return leaderboard;
    }


    /**
     * @return the list of users
     */
    protected ArrayList<Player> getGamePlayers(String gameId) {
        return new ArrayList<>(games.get(gameId).getPlayers());
    }


    /**
     * @param gameId the id of the game
     * @return the current player positions where he can place a card
     */
    protected ArrayList<Point> getAvailablePositions(String gameId, String username) {
        Optional<Player> optionalPlayer = games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst();
        if(!optionalPlayer.isPresent()){
            throw new RuntimeException();
        }
        Player player = optionalPlayer.get();
        return new ArrayList<>(player.getBoard().availablePositions());
    }

    /**
     * @param gameId the id of the game
     * @return the current turn
     */
    protected int getCurrentTurn(String gameId) {
        return games.get(gameId).getGameModel().getCurrentTurn();
    }

    /**
     * @param gameId the id of the game
     * @return the total turns of the game
     */
    protected int getTotalTurns(String gameId) {
        return games.get(gameId).getGameModel().getTotalTurns();
    }

    /**
     * @param gameId the id of the game
     * @return if is the last turn of the game for the current player
     */
    public boolean isLast(String gameId) {
        return ((games.get(gameId).getGameModel().getTotalTurns() - games.get(gameId).getGameModel().getCurrentTurn()) / games.get(gameId).getPlayers().size()) < 1;
    }

    /**
     * @param gameId the id of the game
     * @return the username of the current player
     */
    public String getCurrentPlayer(String gameId) {
        return games.get(gameId).getGameModel().getCurrentPlayer().getUsername();
    }

    /**
     * @param gameId the id of the game
     * @param cardId the id of the card
     * @return the card with the id cardId
     */
    public Card getCardFromHand(String gameId, String cardId){
        return games.get(gameId).getGameModel().getCurrentPlayer().getHand().stream().filter(x -> x.getId().equals(cardId)).findFirst().orElse(null);
    }

}
