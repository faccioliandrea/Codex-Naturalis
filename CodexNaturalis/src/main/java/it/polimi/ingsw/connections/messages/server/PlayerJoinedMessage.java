package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that a player has joined the lobby
 */
public class PlayerJoinedMessage extends ServerToClientMessage {
    final private String username;

    /**
     * Constructor
     * @param username the username of the player that has joined the lobby
     */
    public PlayerJoinedMessage(String username) {
        this.username = username;
    }

    /**
     * Execute a method on the client to notify that a player has joined the lobby
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.playerJoinedLobby(this.username);
    }
}
