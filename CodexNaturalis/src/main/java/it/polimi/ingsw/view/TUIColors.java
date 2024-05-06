package it.polimi.ingsw.view;

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

    public String toString() {
        return this.colorcode;
    }

    public int getValue() {
        return this.value;
    }

    public static String reset() {
        return "\u001B[0m";
    }
}
