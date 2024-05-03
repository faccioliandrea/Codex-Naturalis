package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.client.ConnectionBridge;

import java.util.ArrayList;

public class GameStateMessage extends ServerToClientMessage {
    private ArrayList<CardInfo> goldDeck;
    private ArrayList<CardInfo> resourceDeck;
    private ArrayList<CardInfo> board;
    private int publicPoints;

    public GameStateMessage(ArrayList<CardInfo> goldDeck, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> board, int publicPoints) {
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.board = board;
        this.publicPoints = publicPoints;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameState(this.resourceDeck, this.goldDeck, this.board, this.publicPoints);
    }
}
