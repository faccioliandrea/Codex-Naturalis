package it.polimi.ingsw.connections.messages.server;

import java.awt.*;
import java.util.ArrayList;

import it.polimi.ingsw.controller.CardInfo;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class InitTurnMessage extends ServerToClientMessage {
    private ArrayList<CardInfo> hand;
    private ArrayList<CardInfo> goldDeck;
    private ArrayList<CardInfo> resourceDeck;
    private ArrayList<Point> availablePositions;
    private int currentTurn;
    private boolean isLastTurn;
    private ArrayList<CardInfo> board;

    public InitTurnMessage(ArrayList<CardInfo> hand, ArrayList<CardInfo> goldDeck, ArrayList<CardInfo> resourceDeck, ArrayList<Point> availablePositions, int currentTurn, boolean isLastTurn, ArrayList<CardInfo> board) {
        this.hand = hand;
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.availablePositions = availablePositions;
        this.currentTurn = currentTurn;
        this.isLastTurn = isLastTurn;
        this.board = board;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        //change name of the method
        //bridge.initTurn(this.hand, this.resourceDeck, this.goldDeck, this.availablePositions, this.currentTurn, this.isLastTurn, this.board);
    }
}
