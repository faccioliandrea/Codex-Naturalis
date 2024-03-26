package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumeration.CardSymbol;

/**
 * Class representing the corner of a Card
 */
public class Corner {

    private boolean covered;
    private CardSymbol symbol;

    /**
     * Default constructor
     * @param covered Attribute for covered corner
     * @param symbol Symbol on the corner
     */
    public Corner(boolean covered, CardSymbol symbol) {
        this.covered = covered;
        this.symbol = symbol;
    }

    /**
     * Constructor
     * @param symbol Symbol on the corner
     */
    public Corner(CardSymbol symbol) {
        this.covered = false;
        this.symbol = symbol;
    }

    /**
     * Getter for covered attribute
     * @return covered
     */
    public boolean isCovered() {
        return covered;
    }

    /**
     * Getter for Symbol attribute
     * @return symbol
     */
    public CardSymbol getSymbol() {
        return symbol;
    }

    /**
     * Setter for covered attribute
     */
    public void setCovered(boolean covered) {
        this.covered = covered;
    }
}
