package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

public class EndTurnMessage extends ClientToServerMessage {
    public EndTurnMessage(String username) {
        this.username = username;
    }
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.endTurn(this.username);
    }
}
