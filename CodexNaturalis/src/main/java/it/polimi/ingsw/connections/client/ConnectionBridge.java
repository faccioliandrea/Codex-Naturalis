package it.polimi.ingsw.connections.client;

import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.connections.enums.AddPlayerToLobbyresponse;
import it.polimi.ingsw.connections.enums.ChooseStarterCardSideResponse;
import it.polimi.ingsw.connections.enums.LogInResponse;
import it.polimi.ingsw.connections.server.RMIServerConnectionInterface;
import it.polimi.ingsw.controller.client.ClientController;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionBridge {
    private final ClientController controller;

    private ServerConnection serverConnection;

    private RMIClientConnectionInterface clientConnection;

    public ConnectionBridge(ClientController controller) {
        this.controller = controller;
    }

    public void loginRequest() {
        String username = controller.loginRequest();

        if (serverConnection instanceof SocketServerConnection) {

            try {
                ((SocketServerConnection) serverConnection).loginRequest(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            rmiLoginRequest(username);
        }
    }

    private void rmiLoginRequest(String username) {
        try {
            LogInResponse login = ((RMIServerConnectionInterface) serverConnection).loginRequest(username, clientConnection);
            if(login.equals(LogInResponse.LOGGED_IN)) {
                validUsername();
            } else if (login.equals(LogInResponse.INVALID_USERNAME)) {
                invalidUsername();
            } else {
                System.out.println(LogInResponse.RECONNECT);
                //TODO: handle reconnect
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            try {
                ArrayList<String> lobbies = ((RMIServerConnectionInterface) serverConnection).getLobby(controller.getUsername());
                if(lobbies.isEmpty())
                    lobbyDoesNotExists();
                else
                    lobbyExists(lobbies);

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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
            try {
                AddPlayerToLobbyresponse result = ((RMIServerConnectionInterface) serverConnection).addPlayerToLobby(controller.getUsername(), lobbyId);
                if(result.equals(AddPlayerToLobbyresponse.LOBBY_NOT_FOUND))
                    lobbyDoesNotExists();
                else if(result.equals(AddPlayerToLobbyresponse.PLAYER_ADDED))
                    joinLobbySuccess(false);
                else if(result.equals(AddPlayerToLobbyresponse.PlAYER_ADDED_LAST))
                    joinLobbySuccess(true);
                else
                    lobbyFull();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

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
            try {
                String lobbyid = ((RMIServerConnectionInterface) serverConnection).createLobbyAndJoin(controller.getUsername(), numPlayers);
                lobbyCreated(lobbyid);
                joinLobbyRequest(lobbyid);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void lobbyCreated(String lobbyId) {
        this.controller.lobbyCreated(lobbyId);
    }

    public void gameStarted(StarterData starterData) {
        controller.gameStarted(starterData);
    }

    public void initTurn(TurnInfo turnInfo) {
        controller.initTurn(turnInfo);
    }

    public void createGame() {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).createGame(controller.getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ((RMIServerConnectionInterface) serverConnection).createGame(controller.getUsername());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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
            try {
                PlaceCardSuccessInfo cardSuccessInfo =  ((RMIServerConnectionInterface) serverConnection).placeCard(controller.getUsername(), card.getId(), card.getCoord(), card.isFlipped());
                if(cardSuccessInfo != null)
                    placeCardSuccess(cardSuccessInfo);
                else
                    placeCardFailure();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void placeCardSuccess(PlaceCardSuccessInfo placeCardSuccessInfo) {
        controller.placeCardSuccess(placeCardSuccessInfo);
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
            try {
                ArrayList<CardInfo> cardInfos =  ((RMIServerConnectionInterface) serverConnection).drawResource(controller.getUsername(), index);
                controller.drawSuccess(cardInfos);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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
            try {
                ArrayList<CardInfo> cardInfos =  ((RMIServerConnectionInterface) serverConnection).drawGold(controller.getUsername(), index);
                controller.drawSuccess(cardInfos);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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
            try {
                ((RMIServerConnectionInterface) serverConnection).endTurn(controller.getUsername());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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
            try {
                ((RMIServerConnectionInterface) serverConnection).choosePrivateGoal(controller.getUsername(), index);
                privateGoalChosen();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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
            try{
                ChooseStarterCardSideResponse result = ((RMIServerConnectionInterface) serverConnection).chooseStarterCardSide(controller.getUsername(), flipped);
                if(result.equals(ChooseStarterCardSideResponse.WAIT_FOR_OTHER_PLAYER))
                    WaitingOthersStartingChoiceMessage();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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

    public void setRmiClientConnectionInterface(RMIClientConnectionInterface client) {
        this.clientConnection = client;
    }

    public void playerDisconnected(String username, boolean gameStarted){
        controller.playerDisconnected(username, gameStarted);}

    public void playerReconnected(String username) {controller.playerReconnected(username);}

    public void reconnectionState(GameStateInfo gameStateInfo) {
        controller.reconnectionState(gameStateInfo);
    }

    public void noOtherPlayerConnected() {
        controller.noOtherPlayerConnected();
    }
}
