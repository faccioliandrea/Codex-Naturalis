package it.polimi.ingsw.connections.client;

import it.polimi.ingsw.controller.CardInfo;
import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.model.goals.Goal;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionBridge {
    private final ClientController controller;

    public ConnectionBridge(ClientController controller) {
        this.controller = controller;
    }

    public String loginRequest() {
        String username = controller.loginRequest();

        if (controller.getServerConnection() instanceof SocketServerConnection) {

            try {
                ((SocketServerConnection) controller.getServerConnection()).loginRequest(username);
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

        if (controller.getServerConnection() instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) controller.getServerConnection()).getLobby(controller.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void joinLobbyRequest(String lobbyId) {
        if (controller.getServerConnection() instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) controller.getServerConnection()).joinLobby(controller.getUsername(), lobbyId);
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

    public void joinLobbySuccess() {
        controller.joinLobbySuccess();
    }

    public void lobbyFull() {
        controller.lobbyFull();
    }

    public void playerJoinedLobby(String username) {
        controller.playerJoinedLobby(username);
    }

    public void createLobbyRequest(int numPlayers) {
        if (controller.getServerConnection() instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) controller.getServerConnection()).createLobbyAndJoin(controller.getUsername(), numPlayers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void gameStarted() {
        controller.gameStarted();
    }

    public void initTurn(ArrayList<CardInfo> hand, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<Point> availablePosition, int currTurn, boolean isLastTurn, ArrayList<CardInfo> board) {
        controller.initTurn(hand, resourceDeck, goldDeck, availablePosition, currTurn, isLastTurn, board);
    }

    public void initTurnAck() {
        if (controller.getServerConnection() instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) controller.getServerConnection()).initTurnAck(controller.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void placeCardRequest(String cardId, Point position, boolean side) {
        if (controller.getServerConnection() instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) controller.getServerConnection()).placeCard(controller.getUsername(), cardId, position, side);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void placeCardSuccess(int cardsPoints, int goalPoints) {
        controller.placeCardSuccess(cardsPoints, goalPoints);
    }

    public void placeCardFailure() {
        controller.placeCardFailure();
    }

    public void drawResourceRequest(int index) {
        if (controller.getServerConnection() instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) controller.getServerConnection()).drawResource(controller.getUsername(), index);
                ;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void drawGoldRequest(int index) {
        if (controller.getServerConnection() instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) controller.getServerConnection()).drawGold(controller.getUsername(), index);
                ;
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
        if (controller.getServerConnection() instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) controller.getServerConnection()).endTurn(controller.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void gameState(ArrayList<CardInfo> resaourceDeck, ArrayList<CardInfo> golddeck, ArrayList<CardInfo> board, int points) {
        controller.gameState(resaourceDeck, golddeck, board, points);
    }

    public void choosePrivateGoalRequest(int index){
        if (controller.getServerConnection() instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) controller.getServerConnection()).choosePrivateGoal(controller.getUsername(), index);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void privateGoalChosen(){
        controller.privateGoalChosen();
    }

    public void gameEnd(HashMap<String, Integer> leaderboard){
        controller.gameEnd(leaderboard);
    }
}
