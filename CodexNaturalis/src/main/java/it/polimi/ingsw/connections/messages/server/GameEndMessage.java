package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

import java.util.Map;

/**
 * Message that contains the leaderboard of the game
 */
public class GameEndMessage extends ServerToClientMessage {
    final private Map<String, Integer> leaderBoard;

    /**
     * Constructor
     * @param leaderBoard the leaderboard of the game
     */
    public GameEndMessage(Map<String, Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    /**
     * Execute a method on the client to end the game
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameEnd(this.leaderBoard);
    }
}
