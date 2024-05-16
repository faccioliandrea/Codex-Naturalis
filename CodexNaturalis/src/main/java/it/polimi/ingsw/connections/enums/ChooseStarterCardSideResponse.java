package it.polimi.ingsw.connections.enums;

/**
 * This enum represents the response of the choose starter card side request
 * WAIT_FOR_OTHER_PLAYER if the server is waiting for the other player to choose the side
 * SUCCESS if the choice is successful and all the players have chosen the side
 * FAILURE if some error occurred
 */
public enum ChooseStarterCardSideResponse {
    WAIT_FOR_OTHER_PLAYER,
    SUCCESS,
    FAILURE,
}
