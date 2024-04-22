package it.polimi.ingsw.model.exceptions;

/**
 * Thrown when the {@code GameModel} fails to initialize one of the decks
 * from the relative json file
 */
public class DeckInitializationException extends Exception {
    public DeckInitializationException() {
        super();
    }

    public DeckInitializationException(String message) {
        super(message);
    }

    public DeckInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
    public DeckInitializationException(Throwable cause) {
        super(cause);
    }
}
