package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.gold.GoldCard;
import it.polimi.ingsw.model.exceptions.DeckInitializationException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameController {
    private ArrayList<String> users;
    private HashMap<String, Game> games;

    public GameController(){
        users = new ArrayList<>();
        games = new HashMap<>();
    }

    protected HashMap<String, Game> getGames() {
        return games;
    }


    private void addPlayer(String user){
        if(!users.contains(user))
            users.add(user);
    }

    protected void createGame(String gameId, ArrayList<String> users) throws DeckInitializationException, InvalidNumberOfPlayersException {
        for(String user : users)
            addPlayer(user);
        games.put(gameId, new Game(gameId, users));
    }


    protected void startGame(String gameId){
        games.get(gameId).getGameModel().startGame();

    }


    /**
     * let the player choose a private goal
     * @param gameID the id of the game
     * @param index the index of the card to choose
     */
    protected void choosePrivateGoal(String gameID,String username, int index){
        //TODO: devo passagli anche il riferimento al client?
        games.get(gameID).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst().get().choosePrivateGoal(index);

    }

    protected void chooseStarterCardSide(String gameID, String username, boolean flipped) {

        Player player = games.get(gameID).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst().get();
        StarterCard starterCard= player.getStarterCard();
        starterCard.setFlipped(flipped);
        try {
            player.placeCard(player.getStarterCard(), new Point(0,0));
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        } catch (RequirementsNotSatisfied e) {
            throw new RuntimeException(e);
        }

    }



    protected void placeCard(String gameId,Card card, Point position) throws RequirementsNotSatisfied, InvalidPositionException {
        games.get(gameId).getGameModel().getCurrentPlayer().placeCard(card, position);
        if (games.get(gameId).getGameModel().getCurrentPlayer().getCardsPoints() >= 20)
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
        for (Card card : games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst().get().getHand())
            hand.add(new CardInfo(card));
        return hand;
    }

    /**
     * @param gameId the id of the game
     * @param username the username of the player
     * @return the private goals of the player
     */
    protected ArrayList<GoalInfo> getPrivateGoals(String gameId, String username) {
        ArrayList<GoalInfo> privateGoals = new ArrayList<>();
        for (Goal card : games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst().get().getPrivateGoals())
            privateGoals.add(new GoalInfo(card));
        return privateGoals;
    }

    /**
     * @param gameId the id of the game
     * @return the shared goals of the game
     */
    protected ArrayList<GoalInfo> getSharedGoals(String gameId) {
        ArrayList<GoalInfo> sharedGoals = new ArrayList<>();
        for (Goal card : games.get(gameId).getGameModel().getPlayers().get(0).getBoard().getSharedGoals())
            sharedGoals.add(new GoalInfo(card));
        return sharedGoals;
    }

    protected CardInfo getStarterCard(String gameId, String username) {
        return new CardInfo(games.get(gameId).getGameModel().getPlayers().stream().filter(x->x.getUsername().equals(username)).findFirst().get().getStarterCard());

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
        for (ResourceCard card : games.get(gameId).getGameModel().getResourceCardDeck().subList(0,2))
            resourceDeck.add(new CardInfo(card));
        return resourceDeck;
    }

    /**
     * @param gameId the id of the game
     * @return the list of the first 3 cards of the gold deck
     */
    protected ArrayList<CardInfo> getGoldDeck(String gameId) {
        ArrayList<CardInfo> goldDeck = new ArrayList<>();
        for (GoldCard card : games.get(gameId).getGameModel().getGoldCardDeck().subList(0,2))
            goldDeck.add(new CardInfo(card));
        return goldDeck;
    }

    /**
     * @param gameId the id of the game
     * @return the played cards of the current player, not all the board
     */
    protected ArrayList<CardInfo> getUserBoard(String gameId) {
        ArrayList<CardInfo> board = new ArrayList<>();
        for (Card card : games.get(gameId).getGameModel().getCurrentPlayer().getBoard().getPlayedCards())
            board.add(new CardInfo(card));
        return board;
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
     * @return the points of the current player withouth the cards points
     */
    protected int getUserGoalsPoints(String gameId){
        return games.get(gameId).getGameModel().getCurrentPlayer().getGoalPoints();
    }

    /**
     * @param gameId the id of the game
     * @return true if the game is finished
     */
    protected boolean isGameFinished(String gameId) {
        return games.get(gameId).getGameModel().getCurrentTurn() == games.get(gameId).getGameModel().getTotalTurns();
    }

    /**
     * called when the game is finished, return all the point to the players to generete the leaderboard
     */
    protected HashMap<String, Integer> endGame(String gameId){
        HashMap<String, Integer> leaderboard = new HashMap<>();
        for( int i = 0; i < games.get(gameId).getPlayers().size(); i++)
            leaderboard.put(games.get(gameId).getPlayers().get(i).getUsername(), games.get(gameId).getPlayers().get(i).getCardsPoints() + games.get(gameId).getPlayers().get(i).getGoalPoints());
        return leaderboard;
    }

    protected void controller(){
        //cicla fino a che non finisce la partita e chiama i metodi:
        // e gestisce la partita
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
    protected ArrayList<Point> getAvailablePositions(String gameId) {

        return new ArrayList<>(games.get(gameId).getGameModel().getCurrentPlayer().getBoard().availablePositions());
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
        return games.get(gameId).getGameModel().getTotalTurns() - games.get(gameId).getGameModel().getCurrentTurn() / games.get(gameId).getPlayers().size() == 0;
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
    public Card getCard(String gameId, String cardId){
        return games.get(gameId).getGameModel().getCurrentPlayer().getHand().stream().filter(x -> x.getId().equals(cardId)).findFirst().orElse(null);
    }


}
