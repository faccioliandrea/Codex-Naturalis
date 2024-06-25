package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the number of players for the lobby to be created
 */
public class CreateLobbyAndJoinMessage extends ClientToServerMessage {
    final private int numOfPlayers;

    /**
     * Constructor
     * @param username the player's username
     * @param numOfPlayers the number of players for the lobby to be created
     */
    public CreateLobbyAndJoinMessage(String username, int numOfPlayers) {
        this.username = username;
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Execute a method on the server to create a lobby
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.createLobby(this.username, this.numOfPlayers);
    }
}
