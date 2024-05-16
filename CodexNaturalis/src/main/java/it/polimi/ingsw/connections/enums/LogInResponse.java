package it.polimi.ingsw.connections.enums;

/**
 * This enum represents the response of the login request
 * LOGGED_IN if the login is successful
 * INVALID_USERNAME if the username is already connected
 * RECONNECTED if the user was already connected and is reconnecting
 */
public enum LogInResponse {
    LOGGED_IN,
    INVALID_USERNAME,
    RECONNECT
}
