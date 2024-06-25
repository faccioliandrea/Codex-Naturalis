package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the username of the player that wants to get the lobbies
 */
public class GetLobbyMessage extends ClientToServerMessage {
    /**
     * Constructor
     * @param username the player's username
     */
    public GetLobbyMessage(String username) {
        this.username = username;
    }

    /**
     * Execute a method on the server to get the lobbies
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.getLobbies(this.username);
    }
}
