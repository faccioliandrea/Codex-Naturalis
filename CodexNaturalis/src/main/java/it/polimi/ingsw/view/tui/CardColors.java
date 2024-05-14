package it.polimi.ingsw.view.tui;

public enum CardColors {
    MUSHROOM(TUIColors.RED, TUIColors.BLACK),
    LEAF(TUIColors.GREEN, TUIColors.BLACK),
    WOLF(TUIColors.BLUE, TUIColors.BLACK),
    BUTTERFLY(TUIColors.PURPLE, TUIColors.BLACK),
    STARTER(TUIColors.YELLOW, TUIColors.BLACK),
    AVAILABLE(TUIColors.WHITE, TUIColors.BLACK);

    private final String colorcode;

    CardColors(TUIColors bg, TUIColors fg) {
        String ansi =  "\u001B[4%d;3%dm";
        this.colorcode = String.format(ansi, bg.getValue(), fg.getValue());
    }

    public String toString() {
        return this.colorcode;
    }
}
