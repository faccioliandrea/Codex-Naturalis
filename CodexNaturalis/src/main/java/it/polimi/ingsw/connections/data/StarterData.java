package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.model.enumeration.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class contains the data to be sent to the client at the beginning of the game
 */
public class StarterData implements Serializable {
    private final ArrayList<CardInfo> hand;
    private final ArrayList<GoalInfo> privateGoals;
    private final ArrayList<GoalInfo> sharedGoals;
    private final CardInfo starterCard;
    private final ArrayList<String> users;
    private final Map<String, PlayerColor> playerColors;

    /** Constrctor:
     * @param hand is the list of cards in the player's hand
     * @param privateGoals is the list of private goals that the player has to choose one from
     * @param sharedGoals is the list of shared goals
     * @param starterCard is the starter card
     * @param users is the list of users in the game
     * @param playerColors is the map that associates each user with a color
     */
    public StarterData(ArrayList<CardInfo> hand, ArrayList<GoalInfo> privateGoals, ArrayList<GoalInfo> sharedGoals, CardInfo starterCard, ArrayList<String> users, Map<String, PlayerColor> playerColors) {
        this.hand = hand;
        this.privateGoals = privateGoals;
        this.sharedGoals = sharedGoals;
        this.starterCard = starterCard;
        this.users = users;
        this.playerColors = playerColors;
    }

    /**
     * This method is used to get the list of cards in the player's hand
     * @return the hand
     */
    public ArrayList<CardInfo> getHand() {
        return hand;
    }

    /**
     * This method is used to get the list of private goals that the player has to choose one from
     * @return the private goals
     */
    public ArrayList<GoalInfo> getPrivateGoals() {
        return privateGoals;
    }

    /**
     * This method is used to get the list of shared goals
     * @return the shared goals
     */
    public ArrayList<GoalInfo> getSharedGoals() {
        return sharedGoals;
    }

    /**
     * This method is used to get the starter card
     * @return the starter card
     */
    public CardInfo getStarterCard() {
        return starterCard;
    }

    /**
     * This method is used to get the list of users in the game
     * @return the users
     */
    public ArrayList<String> getUsers() {
        return users;
    }

    /**
     * This method is used to get the map that associates each user with a color
     * @return the player colors
     */
    public Map<String, PlayerColor> getPlayerColors() {
        return playerColors;
    }
}
