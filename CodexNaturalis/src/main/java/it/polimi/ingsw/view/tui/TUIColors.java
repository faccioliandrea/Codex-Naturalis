package it.polimi.ingsw.view.tui;

/**
 * Enum that defines colors for the TUI
 */
public enum TUIColors {
    BLACK(0),
    RED(1),
    GREEN(2),
    YELLOW(3),
    BLUE(4),
    PURPLE(5),
    CYAN(6),
    WHITE(7);

    private final String colorcode;
    private final int value;

    TUIColors(int code) {
        String ansi =  "\u001B[%d%dm";
        this.value = code;
        this.colorcode = String.format(ansi, 3, code);
    }

    /**
     * Getter for the color code
     * @return the color code
     */
    public String toString() {
        return this.colorcode;
    }

    /**
     * Getter for the value
     * @return the value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Method to reset the color
     * @return the reset color code
     */
    public static String reset() {
        return "\u001B[0m";
    }
}
