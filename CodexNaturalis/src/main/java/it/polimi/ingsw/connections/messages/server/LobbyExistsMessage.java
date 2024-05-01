package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

import java.util.ArrayList;

public class LobbyExistsMessage extends ServerToClientMessage {
    private ArrayList<String> lobbiesID;

    public LobbyExistsMessage(ArrayList<String> lobbiesID) {
        this.lobbiesID = lobbiesID;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.lobbyExists(this.lobbiesID);
    }
}
