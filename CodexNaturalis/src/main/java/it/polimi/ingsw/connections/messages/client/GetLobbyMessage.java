package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;


public class GetLobbyMessage extends ClientToServerMessage {
    public GetLobbyMessage(String username) {
        this.username = username;
    }
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.getLobbies(this.username);
    }
}
