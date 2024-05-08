package it.polimi.ingsw.connections.client;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;
import it.polimi.ingsw.controller.client.ClientController;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionBridge {
    private final ClientController controller;

    private ServerConnection serverConnection;

    public ConnectionBridge(ClientController controller) {
        this.controller = controller;
    }

    public String loginRequest() {
        String username = controller.loginRequest();

        if (serverConnection instanceof SocketServerConnection) {

            try {
                ((SocketServerConnection) serverConnection).loginRequest(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return username;
        } else {
            // TODO: handle RMI
            return username;
        }
    }

    public void invalidUsername() {

        controller.invalidUsername();
    }

    public void validUsername() {
        controller.validUsername();
    }

    public void lobbyRequest() {

        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).getLobby(controller.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void joinLobbyRequest(String lobbyId) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).joinLobby(controller.getUsername(), lobbyId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void lobbyDoesNotExists() {
        controller.lobbyDoesNotExist();
    }

    public void lobbyExists(ArrayList<String> lobbies) {
        controller.lobbyExists(lobbies);
    }

    public void joinLobbySuccess(boolean isLastPlayer) {
        controller.joinLobbySuccess(isLastPlayer);
    }

    public void lobbyFull() {
        controller.lobbyFull();
    }

    public void playerJoinedLobby(String username) {
        controller.playerJoinedLobby(username);
    }

    public void createLobbyRequest(int numPlayers) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).createLobbyAndJoin(controller.getUsername(), numPlayers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void gameStarted(StarterData starterData) {
        controller.gameStarted(starterData);
    }

    public void initTurn(TurnInfo turnInfo) {
        controller.initTurn(turnInfo);
    }

    public void initTurnAck() {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).initTurnAck(controller.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void createGame() {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).createGame(controller.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }

    }

    public void placeCardRequest(CardInfo card) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).placeCard(controller.getUsername(), card.getId(), card.getCoord(), card.isFlipped());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void placeCardSuccess(int cardsPoints, int goalPoints, CardInfo placedCard, ArrayList<Point> newAvailable) {
        controller.placeCardSuccess(cardsPoints, goalPoints, placedCard, newAvailable);
    }

    public void placeCardFailure() {
        controller.placeCardFailure();
    }

    public void drawResourceRequest(int index) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).drawResource(controller.getUsername(), index);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void drawGoldRequest(int index) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).drawGold(controller.getUsername(), index);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void drawSuccess(ArrayList<CardInfo> hand) {
        controller.drawSuccess(hand);
    }

    public void endTurn() {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).endTurn(controller.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void gameState(GameStateInfo gameStateInfo) {
        controller.gameState(gameStateInfo);
    }

    public void choosePrivateGoalRequest(int index){
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).choosePrivateGoal(controller.getUsername(), index);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void chooseStarterCardSideRequest(boolean flipped){
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).chooseStarterCardSide(controller.getUsername(), flipped);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void WaitingOthersStartingChoiceMessage() {
        controller.waitingOthersStartingChoice();

    }

    public void OtherPlayerTurnMessage(String currentPlayer) {
        controller.otherPlayerTurnMessage(currentPlayer);

    }

    public void privateGoalChosen(){
        controller.privateGoalChosen();
    }

    public void gameEnd(HashMap<String, Integer> leaderboard){
        controller.gameEnd(leaderboard);
    }

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }


    public void lobbyIsReady() {
        controller.lobbyIsReady();
    }
}
