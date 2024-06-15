package it.polimi.ingsw.view.tui;

/**
 * Enum that defines the colors of the cards
 */
public enum CardTextColors {
    MUSHROOM(TUIColors.RED),
    LEAF(TUIColors.GREEN),
    WOLF(TUIColors.BLUE),
    BUTTERFLY(TUIColors.PURPLE),
    SCROLL(TUIColors.YELLOW),
    FEATHER(TUIColors.YELLOW),
    POTION(TUIColors.YELLOW),
    STARTER(TUIColors.YELLOW);

    private final String colorcode;

    CardTextColors(TUIColors bg) {
        String ansi =  "\u001B[3%dm";
        this.colorcode = String.format(ansi, bg.getValue());
    }

    /**
     * Getter for the color code
     * @return the color code
     */
    public String toString() {
        return this.colorcode;
    }
}
