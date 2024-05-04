package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.connections.data.utils.CardInfoGenerator;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

import java.awt.*;
import java.io.Serializable;

public class CardInfo implements Serializable {
    private String id;
    private Point coord;
    private boolean flipped;
    private String description;
    private CardSymbolKingdom cardSymbolKingdom;

    public CardInfo(String id, Point coord, boolean flipped, String description, CardSymbolKingdom cardSymbolKingdom) {
        this.id = id;
        this.coord = coord;
        this.flipped = flipped;
        this.description = description;
    }

    public CardInfo(Card card) {
        this.id = card.getId();
        this.coord = card.getCoord();
        this.flipped = card.getFlipped();
        this.description = CardInfoGenerator.generator.getCardDescription(card);
        if(card instanceof PlayableCard)
            this.cardSymbolKingdom = ((PlayableCard) card).getCardKingdom();

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

    public CardSymbolKingdom getCardSymbolKingdom() {return cardSymbolKingdom;}
}
