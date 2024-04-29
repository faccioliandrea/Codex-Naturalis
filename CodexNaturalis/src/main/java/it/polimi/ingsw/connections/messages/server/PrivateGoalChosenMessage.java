package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

public class PrivateGoalChosenMessage extends ServerToClientMessage {
    public PrivateGoalChosenMessage() {}

    @Override
    public void execute(ClientController controller) {
        controller.privateGoalChosen();
    }
}
