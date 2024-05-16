package it.polimi.ingsw.connections.enums;

/**
 * This enum represents the response of the add player to lobby request
 * PLAYER_ADDED if the player is added to the lobby
 * LOBBY_FULL if the lobby is full
 * LOBBY_NOT_FOUND if the lobby is not found
 * PLAYER_NOT_CONNECTED if the player is not connected
 * PlAYER_ADDED_LAST if the player is added and is the last player
 */
public enum AddPlayerToLobbyresponse {
    PLAYER_ADDED,
    LOBBY_FULL,
    LOBBY_NOT_FOUND,
    PLAYER_NOT_CONNECTED,
    PlAYER_ADDED_LAST
}
