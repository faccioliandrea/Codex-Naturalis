package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.connections.data.utils.CardInfoGenerator;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

import java.awt.*;
import java.io.Serializable;

public class CardInfo implements Serializable {
    private final String id;
    private Point coord;

    private boolean flipped;
    private final String frontDescription;
    private final String backDescription;
    private final String description;
    private final String color;

    public CardInfo(String id, Point coord, boolean flipped, String description, String backDescription, String frontDescription, CardSymbolKingdom kingdom) {
        this.id = id;
        this.coord = coord;
        this.flipped = flipped;
        this.backDescription = backDescription;
        this.frontDescription = frontDescription;
        this.description = description;
        this.color = kingdom!=null ? kingdom.toString() : "STARTER";
    }

    public CardInfo(Card card) {
        this.id = card.getId();
        this.coord = card.getCoord();
        this.flipped = card.getFlipped();
        this.backDescription = CardInfoGenerator.generator.getCardFrontDescription(card);
        this.frontDescription = CardInfoGenerator.generator.getCardBackDescription(card);
        this.description = CardInfoGenerator.generator.getCardDescription(card);
        this.color = card instanceof PlayableCard ? ((PlayableCard) card).getCardKingdom().toString() : "STARTER";

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

    public String getColor() {return color;}

    public void setCoord(Point coord) {
        this.coord = coord;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public String getFrontDescription() {
        return frontDescription;
    }

    public String getBackDescription() {
        return backDescription;
    }
}
