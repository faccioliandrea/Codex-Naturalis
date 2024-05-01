package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class GameStartedMessage extends ServerToClientMessage {
    public GameStartedMessage() {}

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameStarted();
    }
}
