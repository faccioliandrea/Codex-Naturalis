package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;


public class DrawGoldCardMessage extends ClientToServerMessage {
    private int index;

    public DrawGoldCardMessage(String username, int index) {
        this.username = username;
        this.index = index;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.drawGold(this.username, this.index);
    }
}
