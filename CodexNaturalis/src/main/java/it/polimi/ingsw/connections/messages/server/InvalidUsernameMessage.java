package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.enums.LogInResponse;

/**
 * Message that contains the response to a login request with an invalid username
 */
public class InvalidUsernameMessage extends ServerToClientMessage {
    private final LogInResponse status;

    /**
     * Constructor
     * @param status the response to a login request with an invalid username
     */
    public InvalidUsernameMessage(LogInResponse status) {
        this.status = status;
    }

    /**
     * Execute a method on the client to handle the response to a login request with an invalid username
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.invalidUsername(status);
    }
}
