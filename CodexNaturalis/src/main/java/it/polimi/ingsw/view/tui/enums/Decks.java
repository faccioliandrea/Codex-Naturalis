package it.polimi.ingsw.view.tui.enums;

/**
 * Enum that defines the decks
 */
public enum Decks {
    RESOURCE_DECK(10),
    GOLD_DECK(20),
    RESOURCE_DECK_1(11),
    RESOURCE_DECK_2(12),
    RESOURCE_DECK_3(13),
    GOLD_DECK_1(21),
    GOLD_DECK_2(22),
    GOLD_DECK_3(23);

    private final int value;

    /**
     * Constructor
     * @param value Value of the deck
     */
    Decks(int value) {
        this.value = value;
    }

    /**
     * Getter for the value
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Method to get the deck type from the value
     * @param value Value of the deck
     * @return the deck type
     */
    public static Decks getDeck(int value) {
        for (Decks deck : Decks.values()) {
            if (deck.getValue() == value) {
                return deck;
            }
        }
        return null;
    }

    /**
     * Method to check if the card is from a resource deck
     * @return true if the deck is a resource deck, false otherwise
     */
    public boolean isResourceDeck() {
        return this.getValue()/10 == 1;
    }

    /**
     * Method to get the card deck index
     * @return the index of the card in the deck
     */
    public int getCard(){
        return this.getValue()%10;
    }

}
