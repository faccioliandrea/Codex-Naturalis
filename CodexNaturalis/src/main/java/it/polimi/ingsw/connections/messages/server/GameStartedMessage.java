package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.StarterData;

public class GameStartedMessage extends ServerToClientMessage {
    private StarterData starterData;

    public GameStartedMessage(StarterData starterData) {
        this.starterData = starterData;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameStarted(starterData);
    }
}
