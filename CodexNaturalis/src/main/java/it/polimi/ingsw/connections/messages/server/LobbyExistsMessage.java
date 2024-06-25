package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

import java.util.ArrayList;

/**
 * Message that contains the list of lobbies
 */
public class LobbyExistsMessage extends ServerToClientMessage {
    final private ArrayList<String> lobbiesID;

    /**
     * Constructor
     * @param lobbiesID the list of lobbies
     */
    public LobbyExistsMessage(ArrayList<String> lobbiesID) {
        this.lobbiesID = lobbiesID;
    }

    /**
     * Execute a method on the client to check if the lobby exists
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.lobbyExists(this.lobbiesID);
    }
}
