package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;

public class InitTurnAckMessage extends ClientToServerMessage {
    public InitTurnAckMessage(String username) {
        this.username = username;
    }

    @Override
    public void execute(ServerController controller) {
        controller.initTurnAck(this.username);
    }
}
