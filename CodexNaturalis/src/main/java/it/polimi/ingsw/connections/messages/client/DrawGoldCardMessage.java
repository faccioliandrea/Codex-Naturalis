package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;

public class DrawGoldCardMessage extends ClientToServerMessage {
    private int index;

    public DrawGoldCardMessage(String username, int index) {
        this.username = username;
        this.index = index;
    }

    @Override
    public void execute(ServerController controller) {
        controller.drawGold(this.username, this.index);
    }
}
