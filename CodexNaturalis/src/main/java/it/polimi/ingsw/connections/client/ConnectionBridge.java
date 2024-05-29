package it.polimi.ingsw.connections.client;

import it.polimi.ingsw.chat.ChatMessageData;
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
    private static ConnectionBridge instance;
    private ServerConnection serverConnection;
    private RMIClientConnectionInterface clientConnection;

    private ConnectionBridge() { }

    public static synchronized ConnectionBridge getInstance() {
        if (instance == null) {
            instance = new ConnectionBridge();
        }
        return instance;
    }

    public void loginRequest() {
        String username = ClientController.getInstance().loginRequest();

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

        ClientController.getInstance().invalidUsername();
    }

    public void validUsername() {
        ClientController.getInstance().validUsername();
    }

    public void lobbyRequest() {

        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).getLobby(ClientController.getInstance().getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ArrayList<String> lobbies = ((RMIServerConnectionInterface) serverConnection).getLobby(ClientController.getInstance().getUsername());
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
                ((SocketServerConnection) serverConnection).joinLobby(ClientController.getInstance().getUsername(), lobbyId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                AddPlayerToLobbyresponse result = ((RMIServerConnectionInterface) serverConnection).addPlayerToLobby(ClientController.getInstance().getUsername(), lobbyId);
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
        ClientController.getInstance().lobbyDoesNotExist();
    }

    public void lobbyExists(ArrayList<String> lobbies) {
        ClientController.getInstance().lobbyExists(lobbies);
    }

    public void joinLobbySuccess(boolean isLastPlayer) {
        ClientController.getInstance().joinLobbySuccess(isLastPlayer);
    }

    public void lobbyFull() {
        ClientController.getInstance().lobbyFull();
    }

    public void playerJoinedLobby(String username) {
        ClientController.getInstance().playerJoinedLobby(username);
    }

    public void createLobbyRequest(int numPlayers) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).createLobbyAndJoin(ClientController.getInstance().getUsername(), numPlayers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                String lobbyid = ((RMIServerConnectionInterface) serverConnection).createLobbyAndJoin(ClientController.getInstance().getUsername(), numPlayers);
                lobbyCreated(lobbyid);
                joinLobbyRequest(lobbyid);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void lobbyCreated(String lobbyId) {
        ClientController.getInstance().lobbyCreated(lobbyId);
    }

    public void gameStarted(StarterData starterData) {
        ClientController.getInstance().gameStarted(starterData);
    }

    public void initTurn(TurnInfo turnInfo) {
        ClientController.getInstance().initTurn(turnInfo);
    }

    public void createGame() {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).createGame(ClientController.getInstance().getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ((RMIServerConnectionInterface) serverConnection).createGame(ClientController.getInstance().getUsername());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void placeCardRequest(CardInfo card) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).placeCard(ClientController.getInstance().getUsername(), card.getId(), card.getCoord(), card.isFlipped());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                PlaceCardSuccessInfo cardSuccessInfo =  ((RMIServerConnectionInterface) serverConnection).placeCard(ClientController.getInstance().getUsername(), card.getId(), card.getCoord(), card.isFlipped());
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
        ClientController.getInstance().placeCardSuccess(placeCardSuccessInfo);
    }

    public void placeCardFailure() {
        ClientController.getInstance().placeCardFailure();
    }

    public void drawResourceRequest(int index) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).drawResource(ClientController.getInstance().getUsername(), index);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ArrayList<CardInfo> cardInfos =  ((RMIServerConnectionInterface) serverConnection).drawResource(ClientController.getInstance().getUsername(), index);
                ClientController.getInstance().drawSuccess(cardInfos);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void drawGoldRequest(int index) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).drawGold(ClientController.getInstance().getUsername(), index);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ArrayList<CardInfo> cardInfos =  ((RMIServerConnectionInterface) serverConnection).drawGold(ClientController.getInstance().getUsername(), index);
                ClientController.getInstance().drawSuccess(cardInfos);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void drawSuccess(ArrayList<CardInfo> hand) {
        ClientController.getInstance().drawSuccess(hand);
    }

    public void endTurn() {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).endTurn(ClientController.getInstance().getUsername());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ((RMIServerConnectionInterface) serverConnection).endTurn(ClientController.getInstance().getUsername());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void gameState(GameStateInfo gameStateInfo) {
        ClientController.getInstance().gameState(gameStateInfo);
    }

    public void choosePrivateGoalRequest(int index){
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).choosePrivateGoal(ClientController.getInstance().getUsername(), index);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                ((RMIServerConnectionInterface) serverConnection).choosePrivateGoal(ClientController.getInstance().getUsername(), index);
                privateGoalChosen();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void chooseStarterCardSideRequest(boolean flipped){
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).chooseStarterCardSide(ClientController.getInstance().getUsername(), flipped);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try{
                ChooseStarterCardSideResponse result = ((RMIServerConnectionInterface) serverConnection).chooseStarterCardSide(ClientController.getInstance().getUsername(), flipped);
                if(result.equals(ChooseStarterCardSideResponse.WAIT_FOR_OTHER_PLAYER))
                    WaitingOthersStartingChoiceMessage();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendChatMessage(ChatMessageData msg) {
        if (serverConnection instanceof SocketServerConnection) {
            try {
                ((SocketServerConnection) serverConnection).sendChatMessage(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try{
                ((RMIServerConnectionInterface) serverConnection).sendChatMessage(msg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void recvChatMessage(ChatMessageData msg) {
        ClientController.getInstance().recvChatMessage(msg);
    }

    public void WaitingOthersStartingChoiceMessage() {
        ClientController.getInstance().waitingOthersStartingChoice();
    }

    public void OtherPlayerTurnMessage(String currentPlayer) {
        ClientController.getInstance().otherPlayerTurnMessage(currentPlayer);
    }

    public void privateGoalChosen(){
        ClientController.getInstance().privateGoalChosen();
    }

    public void gameEnd(HashMap<String, Integer> leaderboard){
        ClientController.getInstance().gameEnd(leaderboard);
    }

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void setRmiClientConnectionInterface(RMIClientConnectionInterface client) {
        this.clientConnection = client;
    }

    public void playerDisconnected(String username, boolean gameStarted){
        ClientController.getInstance().playerDisconnected(username, gameStarted);}

    public void playerReconnected(String username) {ClientController.getInstance().playerReconnected(username);}

    public void reconnectionState(GameStateInfo gameStateInfo) {
        ClientController.getInstance().reconnectionState(gameStateInfo);
    }

    public void noOtherPlayerConnected() {
        ClientController.getInstance().noOtherPlayerConnected();
    }
}
