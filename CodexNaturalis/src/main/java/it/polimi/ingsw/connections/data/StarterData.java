package it.polimi.ingsw.connections.data;

import java.io.Serializable;
import java.util.ArrayList;

public class StarterData implements Serializable {

    private ArrayList<CardInfo> hand;
    private ArrayList<GoalInfo> privateGoals;
    private ArrayList<GoalInfo> sharedGoals;
    private CardInfo starterCard;
    private ArrayList<String> users;

    public StarterData(ArrayList<CardInfo> hand, ArrayList<GoalInfo> privateGoals, ArrayList<GoalInfo> sharedGoals, CardInfo starterCard, ArrayList<String> users) {
        this.hand = hand;
        this.privateGoals = privateGoals;
        this.sharedGoals = sharedGoals;
        this.starterCard = starterCard;
        this.users = users;
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
}
