package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

public class InitTurnAckMessage extends ClientToServerMessage {
    public InitTurnAckMessage(String username) {
        this.username = username;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.initTurnAck(this.username);
    }
}
