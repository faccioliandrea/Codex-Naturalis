package it.polimi.ingsw.connections.enums;

/**
 * This enum represents the response of the login request
 * LOGGED_IN if the login is successful
 * USERNAME_TAKEN if the username is already assigned to another user
 * INVALID_USERNAME if the username is invalid
 * RECONNECTED if the user was already connected and is reconnecting
 */
public enum LogInResponse {
    LOGGED_IN,
    USERNAME_TAKEN,
    INVALID_USERNAME,
    RECONNECT
}
