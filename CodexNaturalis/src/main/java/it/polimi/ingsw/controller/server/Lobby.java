package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.chat.ServerChatHandler;
import it.polimi.ingsw.connections.server.ConnectionBridge;

import java.util.ArrayList;

/**
 * Class that represents a lobby
 */
public class Lobby {
    private final String id;
    private final int numPlayers;
    private final ArrayList<String> users;
    private final ServerChatHandler chatHandler;

    /**
     * Default constructor
     * @param numPlayers the number of players in the lobby
     */
    public Lobby(String id, int numPlayers) {
        this.id = id;
        this.numPlayers = numPlayers;
        users = new ArrayList<>();
        chatHandler = new ServerChatHandler(users);
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

    /**
     * Remove a player from the lobby
     * @param username the username of the player to remove
     */
    public void removePlayer(String username) {
        users.remove(username);
    }

    /**
     * Get the chat handler of the lobby
     * @return the chat handler of the lobby
     */
    public ServerChatHandler getChatHandler() {
        return chatHandler;
    }

    /**
     * Get the id of the lobby
     * @return the id of the lobby
     */
    public String getId() {
        return id;
    }
}
