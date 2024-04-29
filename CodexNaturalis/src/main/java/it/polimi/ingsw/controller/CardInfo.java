package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cards.Card;

import java.awt.*;

public class CardInfo {
    private String id;
    private Point coord;
    private boolean flipped;
    private String description;

    public CardInfo(String id, Point coord, boolean flipped, String description) {
        this.id = id;
        this.coord = coord;
        this.flipped = flipped;
        this.description = description;
    }

    public CardInfo(Card card) {
        this.id = card.getId();
        this.coord = card.getCoord();
        this.flipped = card.getFlipped();
        this.description = "";
    }

    public String getId() {
        return id;
    }

    public Point getCoord() {
        return coord;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public String getDescription() {
        return description;
    }
}
