package it.polimi.ingsw.model.exceptions;

/**
 * Class RequirementsNotSatisfied is thrown when a player tries to place a Card without satisfying requirements.

 */
public class RequirementsNotSatisfied extends Exception {
    /**
     * Method getMessage.
     *
     * @return the message of the RequirementsNotSatisfied object.
     */
    @Override
    public String getMessage() {
        return ("Error: you don't satisfy the requirements!");
    }
}
