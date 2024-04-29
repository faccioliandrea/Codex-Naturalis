package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;

public class EndTurnMessage extends ClientToServerMessage {
    public EndTurnMessage(String username) {
        this.username = username;
    }
    @Override
    public void execute(ServerController controller) {
        controller.endTurn(this.username);
    }
}
