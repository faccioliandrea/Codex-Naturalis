package it.polimi.ingsw.model.exceptions;

/**
 * Class InvalidPositionException is thrown when a player tries to place a Card in an unavailable position on the board.

 */
public class InvalidPositionException extends Exception {
    /**
     * Method getMessage.
     *
     * @return the message of the InvalidPositionException object.
     */
    @Override
    public String getMessage() {
        return ("Error: you can't place a card here!");
    }
}
