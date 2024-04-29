package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

import java.util.HashMap;

public class GameEndMessage extends ServerToClientMessage {
    private HashMap<String, Integer> leaderBoard;

    public GameEndMessage(HashMap<String, Integer> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    @Override
    public void execute(ClientController controller) {
        controller.gameEnd(this.leaderBoard);
    }
}
