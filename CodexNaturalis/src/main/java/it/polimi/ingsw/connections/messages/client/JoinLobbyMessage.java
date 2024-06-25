package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the username of the player that wants to join a lobby
 */
public class JoinLobbyMessage extends ClientToServerMessage {
    final private String lobbyID;

    /**
     * Constructor
     * @param username the player's username
     * @param lobbyID the ID of the lobby the player wants to join
     */
    public JoinLobbyMessage(String username, String lobbyID) {
        this.username = username;
        this.lobbyID = lobbyID;
    }

    /**
     * Execute a method on the server to join a lobby
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.addPlayerToLobby(this.username, this.lobbyID);
    }
}
