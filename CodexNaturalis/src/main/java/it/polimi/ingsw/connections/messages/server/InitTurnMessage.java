package it.polimi.ingsw.connections.messages.server;

import java.awt.*;
import java.util.Collection;

import it.polimi.ingsw.controller.CardInfo;
import it.polimi.ingsw.controller.client.ClientController;

public class InitTurnMessage extends ServerToClientMessage {
    private Collection<CardInfo> hand;
    private Collection<CardInfo> goldDeck;
    private Collection<CardInfo> resourceDeck;
    private Collection<Point> availablePositions;
    private int currentTurn;
    private boolean isLastTurn;
    private Collection<CardInfo> board;

    public InitTurnMessage(Collection<CardInfo> hand, Collection<CardInfo> goldDeck, Collection<CardInfo> resourceDeck, Collection<Point> availablePositions, int currentTurn, boolean isLastTurn, Collection<CardInfo> board) {
        this.hand = hand;
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.availablePositions = availablePositions;
        this.currentTurn = currentTurn;
        this.isLastTurn = isLastTurn;
        this.board = board;
    }

    @Override
    public void execute(ClientController controller) {
        controller.initTurn(this.hand, this.resourceDeck, this.goldDeck, this.availablePositions, this.currentTurn, this.isLastTurn, this.board);
    }
}
