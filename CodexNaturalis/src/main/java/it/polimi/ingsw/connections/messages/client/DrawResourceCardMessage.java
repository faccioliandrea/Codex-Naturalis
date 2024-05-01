package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

public class DrawResourceCardMessage extends ClientToServerMessage {
    private int index;

    public DrawResourceCardMessage(String username, int cardIndex) {
        this.username = username;
        this.index = cardIndex;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.drawResource(this.username, this.index);
    }
}
