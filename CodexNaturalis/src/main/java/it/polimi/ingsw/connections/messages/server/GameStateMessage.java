package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.CardInfo;
import it.polimi.ingsw.controller.client.ClientController;

import java.util.Collection;

public class GameStateMessage extends ServerToClientMessage {
    private Collection<CardInfo> goldDeck;
    private Collection<CardInfo> resourceDeck;
    private Collection<CardInfo> board;
    private int publicPoints;

    public GameStateMessage(Collection<CardInfo> goldDeck, Collection<CardInfo> resourceDeck, Collection<CardInfo> board, int publicPoints) {
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.board = board;
        this.publicPoints = publicPoints;
    }

    @Override
    public void execute(ClientController controller) {
        controller.gameState(this.resourceDeck, this.goldDeck, this.board, this.publicPoints);
    }
}
