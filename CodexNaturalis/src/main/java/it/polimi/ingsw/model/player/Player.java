package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.gold.GoldCard;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.goals.Goal;

import java.awt.*;
import java.util.ArrayList;

/** Class representing a player in the game */
public class Player {
    private String username;
    private PlayerColor playerColor;
    private int cardsPoints;
    private int goalsPoints;
    private ArrayList<PlayableCard> hand;
    private StarterCard starterCard;
    private ArrayList<Goal> privateGoals;
    private Board board;

    /**
     * Default constructor
     * @param username Username
     * @param playerColor Player character color
     */
    public Player(String username, PlayerColor playerColor) {
        this.username = username;
        this.playerColor = playerColor;
        this.cardsPoints = 0;
        this.goalsPoints = 0;
        this.hand = new ArrayList<PlayableCard>();
        this.starterCard = null;
        privateGoals = null;
        this.board = new Board();
    }

    /**
     * Getter for Username attribute
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for Player color attribute
     * @return playerColor
     */
    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    /**
     * Getter for Cards points attribute
     * @return CardsPoints
     */
    public int getCardsPoints() {
        return cardsPoints;
    }

    /**
     * Getter for Goals points attribute
     * @return goalsPoints
     */
    public int getGoalPoints() {
        return goalsPoints;
    }

    /**
     * Getter for Player's hand
     * @return hand
     */
    public ArrayList<PlayableCard> getHand() {
        return hand;
    }

    /**
     * Getter for Starter card
     * @return starterCard
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }

    /**
     * Getter for Player's Board
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Getter for Player's private Goals
     * @return privateGoals
     */
    public ArrayList<Goal> getPrivateGoals() {
        return privateGoals;
    }

    /**
     * Setter for Cards points attribute
     */
    public void setCardsPoints(int cardsPoints) {
        this.cardsPoints = cardsPoints;
    }

    /**
     * Setter for Goals points attribute
     */
    public void setGoalsPoints(int goalsPoints) {
        this.goalsPoints = goalsPoints;
    }

    /**
     * Setter for Player's hand attribute
     */
    public void setHand(ArrayList<PlayableCard> hand) {
        this.hand = hand;
    }

    /**
     * Setter for Player's privateGoals attribute
     */
    public void setPrivateGoals(ArrayList<Goal> privateGoals) {
        this.privateGoals = privateGoals;
    }


    /**
     * This method add the drawn Card to player's hand
     */
    public void drawCard(PlayableCard card){
        hand.add(card);
    }

    /**
     * This method handles privateGoal selection
     */
    public void choosePrivateGoal(int index){
        board.setPrivateGoal(privateGoals.get(index));
        privateGoals.clear();
    }
    /**
     * This method handles the player's Card selection
     */
    public void placeCard(Card card, Point coord) throws InvalidPositionException {
       if(!board.availablePositions().contains(coord)) throw new InvalidPositionException();
       card.setCoord(coord);
       board.addPlayedCard(card);
       removeFromHand(card);
       calculatePoints();
    }
    /**
     * This method is called when a Card is placed; it removes the Card from Player's hand
     */
    private void removeFromHand(Card card){
        if(card instanceof StarterCard){
            starterCard = null;
        } else if(card instanceof PlayableCard){
            hand.remove(card);
        }
    }

    /**
     * This method calculates the points scored by the player thanks to the two sharedGoals and the privateGoal
     */
    private void calculatePoints(){
        board.getSharedGoals().forEach((goal -> {
            goalsPoints = goal.checkGoal(board);
        }));
        goalsPoints = board.getPrivateGoal().checkGoal(board);
    }
}
