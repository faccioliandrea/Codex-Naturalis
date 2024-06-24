package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.cards.gold.GoldCard;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;
import it.polimi.ingsw.model.goals.Goal;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/** Class representing a player in the game */
public class Player {
    final private String username;
    final private PlayerColor playerColor;
    private int cardsPoints;
    private int goalsPoints;
    private int completedGoals;
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
        this.completedGoals = 0;
        this.hand = new ArrayList<>();
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
     * Setter for Player's starterCard attribute
     */
    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Setter for Player's Board attribute
     */
    public void setBoard(Board board) {
        this.board = board;
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
    public void placeCard(Card card, Point coord) throws InvalidPositionException, RequirementsNotSatisfied {
        if(!board.availablePositions().contains(coord)) throw new InvalidPositionException();
        if(card instanceof GoldCard && !card.getFlipped()){
           if(!Arrays.stream(((GoldCard) card).getConditionalRequirements()).allMatch(x->x.getQuantity()<=board.getSymbols().get(x.getRequiredSymbol()))){
               throw new RequirementsNotSatisfied();
           }
        }
        card.setCoord(coord);
        board.addPlayedCard(card);
        if(card instanceof PlayableCard){
           cardsPoints+=((PlayableCard) card).calculatePoints(board);
        }
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
        goalsPoints = 0;
        completedGoals = 0;
        board.getSharedGoals().forEach((goal -> {
            if(goal.checkGoal(board)!=0){
                goalsPoints += goal.checkGoal(board);
                completedGoals += 1;
            }
        }));
        if(board.getPrivateGoal().checkGoal(board)!=0){
            goalsPoints += board.getPrivateGoal().checkGoal(board);
            completedGoals += 1;
        }
    }

    /**
     * This method returns the number of completed goals
     */
    public int getCompletedGoals() {
        return completedGoals;
    }




}
