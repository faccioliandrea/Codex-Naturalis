package it.polimi.ingsw.view.tui.enums;

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

    Decks(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Decks getDeck(int value) {
        for (Decks deck : Decks.values()) {
            if (deck.getValue() == value) {
                return deck;
            }
        }
        return null;
    }

    public boolean isResourceDeck() {
        return this.getValue()/10 == 1;
    }

    public int getCard(){
        return this.getValue()%10;
    }

}
