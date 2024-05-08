package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.connections.server.ConnectionBridge;

public class CreateGameMessage extends ClientToServerMessage {
    public CreateGameMessage(String username) {
        this.username = username;
    }
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.createGame(username);
    }
}
