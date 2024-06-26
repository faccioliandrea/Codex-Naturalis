package it.polimi.ingsw.connections.data;

import it.polimi.ingsw.connections.data.utils.CardInfoGenerator;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

import java.awt.*;
import java.io.Serializable;

/**
 * This class contains the information of a card. It is used to send the information of a card to the client.
*/
public class CardInfo implements Serializable {
    private final String id;
    private Point coord;
    private boolean flipped;
    private final String frontDescription;
    private final String backDescription;
    private final String description;
    private final String color;

    /** Constructor for the card info
     * @param id the id of the card
     * @param coord the coordinates of the card
     * @param flipped if the card is flipped
     * @param description the description of the card
     * @param backDescription the back description of the card
     * @param frontDescription the front description of the card
     * @param kingdom the kingdom of the card
     */
    public CardInfo(String id, Point coord, boolean flipped, String description, String backDescription, String frontDescription, CardSymbolKingdom kingdom) {
        this.id = id;
        this.coord = coord;
        this.flipped = flipped;
        this.backDescription = backDescription;
        this.frontDescription = frontDescription;
        this.description = description;
        this.color = kingdom!=null ? kingdom.toString() : "STARTER";
    }

/**
     * Constructor for the card info
     * @param card the card to get the information from
     */
    public CardInfo(Card card) {
        this.id = card.getId();
        this.coord = card.getCoord();
        this.flipped = card.getFlipped();
        this.backDescription = CardInfoGenerator.generator.getCardFrontDescription(card);
        this.frontDescription = CardInfoGenerator.generator.getCardBackDescription(card);
        this.description = CardInfoGenerator.generator.getCardDescription(card);
        this.color = card instanceof PlayableCard ? ((PlayableCard) card).getCardKingdom().toString() : "STARTER";

    }

    /**
     * Method to get the id of the card
     * @return the id of the card
     */
    public String getId() {
        return id;
    }

    /**
     * Method to get the coordinates of the card
     * @return the coordinates of the card
     */
    public Point getCoord() {
        return coord;
    }

    /**
     * Method to check if the card is flipped
     * @return true if the card is flipped, false otherwise
     */
    public boolean isFlipped() {
        return flipped;
    }

    /**
     * Method to get the description of the card
     * @return the description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to get the color of the card
     * @return the color of the card
     */
    public String getColor() {return color;}

    /**
     * Method to set the coordinates of the card
     * @param coord the coordinates of the card
     */
    public void setCoord(Point coord) {
        this.coord = coord;
    }

    /**
     * Method to set if the card is flipped
     * @param flipped true if the card is flipped, false otherwise
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    /**
     * Method to get the front description of the card
     * @return the front description of the card
     */
    public String getFrontDescription() {
        return frontDescription;
    }

    /**
     * Method to get the back description of the card
     * @return the back description of the card
     */
    public String getBackDescription() {
        return backDescription;
    }
}
