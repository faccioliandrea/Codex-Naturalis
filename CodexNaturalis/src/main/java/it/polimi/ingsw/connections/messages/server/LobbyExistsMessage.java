package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

import java.util.Collection;

public class LobbyExistsMessage extends ServerToClientMessage {
    private Collection<String> lobbiesID;

    public LobbyExistsMessage(Collection<String> lobbiesID) {
        this.lobbiesID = lobbiesID;
    }

    @Override
    public void execute(ClientController controller) {
        controller.lobbyExists(this.lobbiesID);
    }
}
