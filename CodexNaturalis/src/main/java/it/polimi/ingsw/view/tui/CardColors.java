package it.polimi.ingsw.view.tui;

/**
 * Enum that defines the colors of the cards
 */
public enum CardColors {
    MUSHROOM(TUIColors.RED, TUIColors.BLACK),
    LEAF(TUIColors.GREEN, TUIColors.BLACK),
    WOLF(TUIColors.BLUE, TUIColors.BLACK),
    BUTTERFLY(TUIColors.PURPLE, TUIColors.BLACK),
    STARTER(TUIColors.YELLOW, TUIColors.BLACK),
    AVAILABLE(TUIColors.WHITE, TUIColors.BLACK);

    private final String colorCode;

    CardColors(TUIColors bg, TUIColors fg) {
        String ansi =  "\u001B[4%d;3%dm";
        this.colorCode = String.format(ansi, bg.getValue(), fg.getValue());
    }

    /**
     * Getter for the color code
     * @return the color code
     */
    public String toString() {
        return this.colorCode;
    }
}
