package it.polimi.ingsw.connections.data;

import java.awt.*;
import java.util.ArrayList;

public class PlaceCardSuccessInfo {
    private int cardsPoint;
    private int goalsPoints;
    private CardInfo playedCard;
    private ArrayList<Point> available;

    public PlaceCardSuccessInfo(int cardsPoint, int goalsPoints, CardInfo playedCard, ArrayList<Point> available) {
        this.cardsPoint = cardsPoint;
        this.goalsPoints = goalsPoints;
        this.playedCard = playedCard;
        this.available = available;
    }

    public int getCardsPoint() {
        return cardsPoint;
    }

    public int getGoalsPoints() {
        return goalsPoints;
    }

    public CardInfo getPlayedCard() {
        return playedCard;
    }

    public ArrayList<Point> getAvailable() {
        return available;
    }
}
