package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the username of the player that wants to log in
 */
public class LoginRequestMessage extends ClientToServerMessage {
    final private String username;

    /**
     * Getter
     * @return the player's username
     */
    public String getUsername(){
        return username;
    }

    /**
     * Constructor
     * @param username the player's username
     */
    public LoginRequestMessage(String username) {
        this.username = username;
    }

    /**
     * Execute a method on the server to log in user
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {}
}
