package it.polimi.ingsw.connections.enums;

/**
 * This enum represents the response of the add player to lobby request
 * PLAYER_ADDED if the player is added to the lobby
 * LOBBY_FULL if the lobby is full
 * LOBBY_NOT_FOUND if the lobby is not found
 * PLAYER_NOT_CONNECTED if the player is not connected
 * PLAYER_ADDED_LAST if the player is added and is the last player
 */
public enum AddPlayerToLobbyResponse {
    PLAYER_ADDED,
    NO_LOBBIES,
    LOBBY_NOT_FOUND,
    PLAYER_NOT_CONNECTED,
    PLAYER_ADDED_LAST,
    REFRESH_LOBBIES
}
