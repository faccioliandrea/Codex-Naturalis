package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

import java.util.HashMap;

public class GameEndMessage extends ServerToClientMessage {
    final private HashMap<String, Integer> leaderBoard;

    public GameEndMessage(HashMap<String, Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameEnd(this.leaderBoard);
    }
}
