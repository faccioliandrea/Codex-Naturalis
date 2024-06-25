package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the username of the player that ended his turn
 */
public class EndTurnMessage extends ClientToServerMessage {
    /**
     * Constructor
     * @param username the player's username
     */
    public EndTurnMessage(String username) {
        this.username = username;
    }
    
    /**
     * Execute a method on the server to end the player's turn
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
       bridge.endTurn(this.username);
    }
}
