package it.polimi.ingsw.model.cards.gold;
import it.polimi.ingsw.model.enumeration.CardSymbolKingdom;

/**
 * Class representing a requirement to place a gold card
 */
public class GoldCardRequirement {
    private CardSymbolKingdom requiredSymbol;
    private int quantity;

    /**
     * Default constructor
     * @param requiredSymbol Symbol required
     * @param quantity Quantity of the symbol required
     */
    public GoldCardRequirement(CardSymbolKingdom requiredSymbol, int quantity) {
        this.requiredSymbol = requiredSymbol;
        this.quantity = quantity;
    }

    /**
     * Getter for requiredSymbol
     * @return requiredSymbol
     */
    public CardSymbolKingdom getRequiredSymbol() {
        return requiredSymbol;
    }

    /**
     * Getter for quantity
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }
}
