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

/**
 * This class handles the connections between the client and the server
 */
public class ConnectionBridge {
    private static ConnectionBridge instance;
    private ServerConnection serverConnection;
    private RMIClientConnectionInterface clientConnection;

    /**
     * Default constructor
     */
    private ConnectionBridge() { }

    /**
     * Singleton instance getter
     * @return the instance of the ConnectionBridge
     */
    public static synchronized ConnectionBridge getInstance() {
        if (instance == null) {
            instance = new ConnectionBridge();
        }
        return instance;
    }

    /**
     * Handles a login request from the server
     */
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
            try {
                clientConnection.pingServer();
            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Handles a ping request
     * @throws RemoteException if the connection is lost
     */
    public void  rmiPing() throws RemoteException {
        ((RMIServerConnectionInterface) serverConnection).ping();
    }

    /**
     * Send a login request to the server with RMI
     * @param username the username to login with
     */
    private void rmiLoginRequest(String username) {
        try {
            LogInResponse login = ((RMIServerConnectionInterface) serverConnection).loginRequest(username, clientConnection);
            if(login.equals(LogInResponse.LOGGED_IN)) {
                validUsername();
            } else if (login.equals(LogInResponse.USERNAME_TAKEN) || login.equals(LogInResponse.INVALID_USERNAME)) {
                invalidUsername(login);
            } else {
                System.out.println(LogInResponse.RECONNECT);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles a login response from the server with invalid username
     * @param status the status of the login
     */
    public void invalidUsername(LogInResponse status) {
        ClientController.getInstance().invalidUsername(status);
    }

    /**
     * Handles a login response from the server with valid username
     */
    public void validUsername() {
        ClientController.getInstance().validUsername();
    }

    /**
     * Handles a lobby request
     */
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

            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Asks the server to join a lobby
     * @param lobbyId the id of the lobby to join
     */
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
            } catch (RemoteException ignored) {
                //handled by the ping
            }

        }
    }

    /**
     * Handles a lobby does not exists response from the server
     */
    public void lobbyDoesNotExists() {
        ClientController.getInstance().lobbyDoesNotExist();
    }

    /**
     * Handles a lobby exists response from the server
     * @param lobbies the list of available lobbies
     */
    public void lobbyExists(ArrayList<String> lobbies) {
        ClientController.getInstance().lobbyExists(lobbies);
    }

    /**
     * Handles a join lobby success response from the server
     * @param isLastPlayer true if the player is the last one to join the lobby
     */
    public void joinLobbySuccess(boolean isLastPlayer) {
        ClientController.getInstance().joinLobbySuccess(isLastPlayer);
    }

    /**
     * Handles a lobby full response from the server
     */
    public void lobbyFull() {
        ClientController.getInstance().lobbyFull();
    }

    /** Handles a player joined lobby message from the server
     * @param username the username of the player that joined the lobby
     */
    public void playerJoinedLobby(String username) {
        ClientController.getInstance().playerJoinedLobby(username);
    }

    /**
     * Asks the server to create a lobby
     * @param numPlayers the number of players in the lobby
     */
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
            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Handles a lobby created message from the server
     * @param lobbyId the id of the created lobby
     */
    public void lobbyCreated(String lobbyId) {
        ClientController.getInstance().lobbyCreated(lobbyId);
    }

    /**
     * Handles the game started message
     * @param starterData the StartedData
     */
    public void gameStarted(StarterData starterData) {
        ClientController.getInstance().gameStarted(starterData);
    }

    /**
     * Handles init turn message
     * @param turnInfo the TurnInfo
     */
    public void initTurn(TurnInfo turnInfo) {
        ClientController.getInstance().initTurn(turnInfo);
    }

    /**
     * Asks the server to create the game
     */
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
                //handled by the ping
            }
        }

    }

    /**
     * Asks the server to place a card
     * @param card the CardInfo of the card to be placed
     */
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
            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Handles a place card success message from the server
     * @param placeCardSuccessInfo the PlaceCardSuccessInfo
     */
    public void placeCardSuccess(PlaceCardSuccessInfo placeCardSuccessInfo) {
        ClientController.getInstance().placeCardSuccess(placeCardSuccessInfo);
    }

    /**
     * Handles a place card failure message from the server
     */
    public void placeCardFailure() {
        ClientController.getInstance().placeCardFailure();
    }

    /**
     * Asks the server to draw a resource card
     * @param index the index of the card to draw
     */
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
            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Asks the server to draw a gold card
     * @param index the index of the card to draw
     */
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
            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Handles a draw success message from the server
     * @param hand the updated hand of the player
     */
    public void drawSuccess(ArrayList<CardInfo> hand) {
        ClientController.getInstance().drawSuccess(hand);
    }

    /**
     * Asks the server to end the turn
     */
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
            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Handles a game state message from the server
     */
    public void gameState(GameStateInfo gameStateInfo) {
        ClientController.getInstance().gameState(gameStateInfo);
    }

    /**
     * Asks the server to choose a private goal
     * @param index the index of the private goal to choose
     */
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
            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Asks the server to choose a starter card side
     * @param flipped true if the card is flipped
     */
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
            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Sends a chat message to the server
     * @param msg the ChatMessageData
     */
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
            } catch (RemoteException ignored) {
                //handled by the ping
            }
        }
    }

    /**
     * Handles a chat message received from the server
     * @param msg the ChatMessageData
     */
    public void recvChatMessage(ChatMessageData msg) {
        ClientController.getInstance().recvChatMessage(msg);
    }

    /**
     * Handles the waiting others starting choice message
     */
    public void WaitingOthersStartingChoiceMessage() {
        ClientController.getInstance().waitingOthersStartingChoice();
    }

    /**
     * Handles the other player turn message
     * @param currentPlayer the username of the current player
     */
    public void OtherPlayerTurnMessage(String currentPlayer) {
        ClientController.getInstance().otherPlayerTurnMessage(currentPlayer);
    }

    /**
     * Handles the private goal chosen message
     */
    public void privateGoalChosen(){
        ClientController.getInstance().privateGoalChosen();
    }

    /**
     * Handles the game end message
     * @param leaderboard the final leaderboard of the game
     */
    public void gameEnd(HashMap<String, Integer> leaderboard){
        ClientController.getInstance().gameEnd(leaderboard);
    }

    /**
     * Setter for the server connection
     * @param serverConnection the ServerConnection
     */
    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * Setter for the client connection
     * @param client the RMIClientConnectionInterface
     */
    public void setRmiClientConnectionInterface(RMIClientConnectionInterface client) {
        this.clientConnection = client;
    }

    /**
     * Handles a player disconnected message
     * @param username the username of the disconnected player
     * @param gameStarted true if the game has already started
     */
    public void playerDisconnected(String username, boolean gameStarted){
        ClientController.getInstance().playerDisconnected(username, gameStarted);}

    /**
     * Handles a player reconnected message
     * @param username the username of the reconnected player
     */
    public void playerReconnected(String username) {ClientController.getInstance().playerReconnected(username);}

    /**
     * Handles a reconnection state message
     * @param gameStateInfo the GameStateInfo
     */
    public void reconnectionState(GameStateInfo gameStateInfo) {
        ClientController.getInstance().reconnectionState(gameStateInfo);
    }

    /**
     * Handles a no other player connected message
     */
    public void noOtherPlayerConnected() {
        ClientController.getInstance().noOtherPlayerConnected();
    }

    /**
     * Handles a server not found message
     */
    public void serverNotFound() {
        ClientController.getInstance().serverNotFound();
    }
}
