package it.polimi.ingsw.model.exceptions;

public class InvalidNumberOfPlayersException extends Exception {
    private static final String msg = "Invalid number of players";

    public InvalidNumberOfPlayersException() {
        super(msg);
    }
}
