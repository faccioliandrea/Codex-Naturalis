package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.connections.data.utils.CardInfoGenerator;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

import java.awt.*;
import java.io.Serializable;

public class CardInfo implements Serializable {
    private final String id;
    private final Point coord;
    private final boolean flipped;
    private final String description;
    private final String color;

    public CardInfo(String id, Point coord, boolean flipped, String description, CardSymbolKingdom kingdom) {
        this.id = id;
        this.coord = coord;
        this.flipped = flipped;
        this.description = description;
        this.color = kingdom!=null ? kingdom.toString() : "STARTER";
    }

    public CardInfo(Card card) {
        this.id = card.getId();
        this.coord = card.getCoord();
        this.flipped = card.getFlipped();
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
}
