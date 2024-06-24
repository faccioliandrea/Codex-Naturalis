package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameEndMessage extends ServerToClientMessage {
    final private Map<String, Integer> leaderBoard;

    public GameEndMessage(Map<String, Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameEnd(this.leaderBoard);
    }
}
