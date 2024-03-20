package it.polimi.ingsw.model.cards;

import java.awt.*;

/**
 * Abstract class representing the cards in the game
 */
public abstract class Card {
    private String id;
    private Point coord;
    private Boolean flipped;

    private Corner[] frontCorners;
    private Corner[] backCorners;

    /**
     * Default constructor
     * @param id Card ID
     * @param coord Coordinates of the card (once placed)
     * @param flipped Front/back of the card
     * @param frontCorners Front corners of the card
     * @param backCorners Back corners of the card
     */
    public Card(String id, Point coord, Boolean flipped, Corner[] frontCorners, Corner[] backCorners) {
        this.id = id;
        this.coord = coord;
        this.flipped = flipped;
        this.frontCorners = frontCorners;
        this.backCorners = backCorners;
    }

    /**
     * Getter for ID attribute
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for coordinates attribute
     * @return coord
     */
    public Point getCoord() { return coord; }

    /**
     * Getter for flipped attribute
     * @return flipped
     */
    public Boolean getFlipped() {
        return flipped;
    }

    /**
     * Getter for corners
     * @return corners
     */
    public Corner[] getCorners() {
        return flipped ? backCorners : frontCorners;
    }

    /**
     * Setter for coordinates attribute
     */
    public void setCoord(Point coord) {
        this.coord = coord;
    }

    /**
     * Setter for flipped attribute
     */
    public void setFlipped(Boolean flipped) {
        this.flipped = flipped;
    }

    // TODO: Method to cover an angle
}
