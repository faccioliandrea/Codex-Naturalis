package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.CardInfo;

import java.util.Collection;

public class DrawSuccessMessage extends ServerToClientMessage {
    private Collection<CardInfo> updatedHand;

    public DrawSuccessMessage(Collection<CardInfo> updatedHand) {
        this.updatedHand = updatedHand;
    }

    @Override
    public void execute(ClientController controller) {
        controller.drawSuccess(this.updatedHand);
    }
}
