package it.polimi.ingsw.controller.server;

import java.util.ArrayList;

/**
 * Class that represents a lobby
 */
public class Lobby {
    private int numPlayers;
    private ArrayList<String> users;

    /**
     * Default constructor
     * @param numPlayers the number of players in the lobby
     */
    public Lobby(int numPlayers) {
        this.numPlayers = numPlayers;
        users = new ArrayList<String >();
    }

    /**
     * Get the number of players in the lobby
     * @return the number of players in the lobby
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * @return the list of users in the lobby
     */
    public ArrayList<String> getUsers() {
        return users;
    }

    /**
     * @return true if the lobby is full, false otherwise
     */
    public boolean isFull() {
        return users.size() == numPlayers;
    }

    /**
     * Add a player to the lobby
     * @param username the username of the player to add
     */
    public void addPlayer(String username) {
        users.add(username);
    }

    public void removePlayer(String username) {
        users.remove(username);
    }
}
