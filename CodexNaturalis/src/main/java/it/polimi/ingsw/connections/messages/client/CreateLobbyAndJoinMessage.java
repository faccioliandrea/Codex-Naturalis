package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;


public class CreateLobbyAndJoinMessage extends ClientToServerMessage {
    final private int numOfPlayers;

    public CreateLobbyAndJoinMessage(String username, int numOfPlayers) {
        this.username = username;
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.createLobby(this.username, this.numOfPlayers);
    }
}
