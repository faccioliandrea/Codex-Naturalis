package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.model.enumeration.CardSymbol;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceCardSuccessInfo implements Serializable {
    final private int cardsPoint;
    final private int goalsPoints;
    final private CardInfo playedCard;
    final private ArrayList<Point> available;
    final private Map<CardSymbol, Integer> symbols;

    public PlaceCardSuccessInfo(int cardsPoint, int goalsPoints, CardInfo playedCard, ArrayList<Point> available, Map<CardSymbol, Integer> symbols) {
        this.cardsPoint = cardsPoint;
        this.goalsPoints = goalsPoints;
        this.playedCard = playedCard;
        this.available = available;
        this.symbols = new LinkedHashMap<>(symbols);
    }

    public int getCardsPoint() {
        return cardsPoint;
    }

    public CardInfo getPlayedCard() {
        return playedCard;
    }

    public ArrayList<Point> getAvailable() {
        return available;
    }

    public Map<CardSymbol, Integer> getSymbols() {
        return symbols;
    }

    public int getGoalsPoint() {return goalsPoints;}
}
