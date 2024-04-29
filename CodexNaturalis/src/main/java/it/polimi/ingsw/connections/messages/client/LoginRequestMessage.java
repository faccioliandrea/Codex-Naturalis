package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;

public class LoginRequestMessage extends ClientToServerMessage {
    private String username;

    public LoginRequestMessage(String username) {
        this.username = username;
    }

    @Override
    public void execute(ServerController controller) {}
}
