package it.polimi.ingsw.model.exceptions;

/**
 * Thrown when the {@code GameModel} fails to initialize one of the decks
 * from the relative json file
 */
public class DeckInitializationException extends Exception {
    private static final String msg = "Error while initializing the decks from the json files";
    public DeckInitializationException() { super(msg); }
}
