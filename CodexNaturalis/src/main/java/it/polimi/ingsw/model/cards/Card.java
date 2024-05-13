package it.polimi.ingsw.model.cards;

import java.awt.*;

/**
 * Abstract class representing the cards in the game
 */
public abstract class Card {
    private String id;
    private Point coord;
    private boolean flipped;
    private Corner[] frontCorners;
    private Corner[] backCorners;

    /**
     * Default constructor
     * @param id Card ID
     * @param frontCorners Front corners of the card
     * @param backCorners Back corners of the card
     */
    public Card(String id, Corner[] frontCorners, Corner[] backCorners) {
        this.id = id;
        this.coord = null;
        this.flipped = false;
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
    public boolean getFlipped() {
        return flipped;
    }

    /**
     * Getter for corners based on its flip status
     * @return corners
     */
    public Corner[] getCorners() {
        return flipped ? backCorners : frontCorners;
    }

    /**
     * Getter for back corners
     * @return corners
     */
    public Corner[] getBackCorners() {
        return backCorners;
    }

    /**
     * Getter for front corners
     * @return corners
     */
    public Corner[] getFrontCorners() {
        return frontCorners;
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
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    // TODO: Method to cover an angle
}
