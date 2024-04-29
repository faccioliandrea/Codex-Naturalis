package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;

public class ChoosePrivateGoalMessage extends ClientToServerMessage {
    private int index;

    public ChoosePrivateGoalMessage(String username, int index) {
        this.username = username;
        this.index = index;
    }

    @Override
    public void execute(ServerController controller) {
        controller.choosePrivateGoal(this.username, this.index);
    }
}
