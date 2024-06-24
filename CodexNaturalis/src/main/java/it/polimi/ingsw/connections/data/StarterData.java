package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.model.enumeration.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class StarterData implements Serializable {

    private final ArrayList<CardInfo> hand;
    private final ArrayList<GoalInfo> privateGoals;
    private final ArrayList<GoalInfo> sharedGoals;
    private final CardInfo starterCard;
    private final ArrayList<String> users;
    private final Map<String, PlayerColor> playerColors;

    public StarterData(ArrayList<CardInfo> hand, ArrayList<GoalInfo> privateGoals, ArrayList<GoalInfo> sharedGoals, CardInfo starterCard, ArrayList<String> users, Map<String, PlayerColor> playerColors) {
        this.hand = hand;
        this.privateGoals = privateGoals;
        this.sharedGoals = sharedGoals;
        this.starterCard = starterCard;
        this.users = users;
        this.playerColors = playerColors;
    }

    public ArrayList<CardInfo> getHand() {
        return hand;
    }

    public ArrayList<GoalInfo> getPrivateGoals() {
        return privateGoals;
    }

    public ArrayList<GoalInfo> getSharedGoals() {
        return sharedGoals;
    }

    public CardInfo getStarterCard() {
        return starterCard;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public Map<String, PlayerColor> getPlayerColors() {
        return playerColors;
    }
}
