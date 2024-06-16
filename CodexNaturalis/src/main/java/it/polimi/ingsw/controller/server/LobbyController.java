package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.connections.server.ConnectionBridge;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is the controller for the lobby
 */
public class LobbyController {
    private static LobbyController instance;
    private final ArrayList<String> users = new ArrayList<>();

    private final HashMap<String, Lobby> lobbies = new HashMap<>();

    /**
     * Default constructor
     */
    private LobbyController() { }

    /**
     * Singleton instance getter
     * @return the instance of the LobbyController
     */
    public static synchronized LobbyController getInstance() {
        if (instance == null) {
            instance = new LobbyController();
        }
        return instance;
    }

    /**
     * Add a player to a lobby
     * @param username the username of the player
     * @param lobbyId the id of the lobby
     */
    public void addPlayer(String username, String lobbyId) {
        users.add(username);
        lobbies.get(lobbyId).addPlayer(username);
    }


    /**
     * Get the lobbies
     * @return the lobbies
     */
    public HashMap<String, Lobby> getLobbies() {
        return lobbies;
    }

    /**
     * Create a new lobby
     * @param lobbyId the id of the lobby
     * @param numPlayers the number of players in the lobby
     */
    public void createNewLobby(String lobbyId, int numPlayers) {

        lobbies.put(lobbyId, new Lobby(lobbyId, numPlayers));
    }

    public void removeLobby(String lobbyId){lobbies.remove(lobbyId);}

    public void removePlayer(String username, String s) {
        users.remove(username);
        lobbies.get(s).removePlayer(username);
    }
}
