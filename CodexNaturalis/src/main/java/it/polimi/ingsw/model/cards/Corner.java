package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.enumeration.CardSymbol;

/**
 * Class representing the corner of a Card
 */
public class Corner {

    private boolean covered;
    final private CardSymbol symbol;

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
