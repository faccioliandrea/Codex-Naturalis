package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;

public class DrawResourceCardMessage extends ClientToServerMessage {
    private int index;

    public DrawResourceCardMessage(String username, int cardIndex) {
        this.username = username;
        this.index = cardIndex;
    }

    @Override
    public void execute(ServerController controller) {
        controller.drawResource(this.username, this.index);
    }
}
