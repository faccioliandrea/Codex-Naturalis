package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the number of players is invalid
 */
public class InvalidNumberOfPlayersException extends Exception {
    private static final String msg = "Invalid number of players";

    /**
     * Class constructor
     */
    public InvalidNumberOfPlayersException() {
        super(msg);
    }
}
