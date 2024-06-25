package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.StarterData;

/**
 * Message that contains the data needed to start the game
 */
public class GameStartedMessage extends ServerToClientMessage {
    final private StarterData starterData;

    /**
     * Constructor
     * @param starterData the data needed to start the game
     */
    public GameStartedMessage(StarterData starterData) {
        this.starterData = starterData;
    }

    /**
     * Execute a method on the client to start the game
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameStarted(starterData);
    }
}
