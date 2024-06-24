package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.enumeration.CardSymbol;

/**
 * Class representing a requirement of the Goal
 */
public class GoalRequirement {
    final private CardSymbol requiredSymbol;
    final private int quantity;
    /**
     * Default constructor
     * @param requiredSymbol Symbol required
     * @param quantity Quantity of the symbol required
     */
    public GoalRequirement(CardSymbol requiredSymbol, int quantity){
        this.requiredSymbol = requiredSymbol;
        this.quantity = quantity;
    }

    /**
     * Getter for requiredSymbol attribute
     * @return requiredSymbol
     */
    public CardSymbol getRequiredSymbol() {
        return requiredSymbol;
    }

    /**
     * Getter for quantity attribute
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

}
