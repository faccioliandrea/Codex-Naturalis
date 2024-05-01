package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

public class LoginRequestMessage extends ClientToServerMessage {
    private String username;

    public String getUsername(){
        return username;
    }

    public LoginRequestMessage(String username) {
        this.username = username;
    }

    @Override
    public void execute(ConnectionBridge bridge) {}
}
