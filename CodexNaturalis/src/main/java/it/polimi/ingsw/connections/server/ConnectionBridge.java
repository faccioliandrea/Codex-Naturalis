package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.connections.enums.AddPlayerToLobbyResponse;
import it.polimi.ingsw.connections.enums.ChooseStarterCardSideResponse;
import it.polimi.ingsw.connections.enums.LogInResponse;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class handles the connections between the server and the clients
 */
public class ConnectionBridge {
    private static ConnectionBridge instance;

    private final HashMap<String, ClientConnection> connections = new HashMap<>();

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
     * Validates the username
     * @param username the username
     * @return true if the username is valid, false otherwise
     */
    private static boolean isValidUsername(String username) {
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]{1,15}$");
        Matcher matcher = usernamePattern.matcher(username);
        return matcher.matches();
    }

    /**
     * Add a connection to the list of connections
     * @param connection the connection to add
     * @param username the username of the connection
     * @return the response to the login request
     */
    public LogInResponse addConnection(ClientConnection connection, String username) throws IOException {
        if(connections.containsKey(username) && connections.get(username).getStatus() == ConnectionStatus.OFFLINE){
            connections.replace(username, connection);
            System.out.printf("%s Reconnected%n", username);
            return ServerController.getInstance().playerReconnected(username);

        }
        else if(connections.containsKey(username)){
            if (connection instanceof SocketClientConnection)
                invalidUsername(connection, LogInResponse.USERNAME_TAKEN);
            return LogInResponse.USERNAME_TAKEN;
        }
        else if (!isValidUsername(username)){
            if (connection instanceof SocketClientConnection)
                invalidUsername(connection, LogInResponse.INVALID_USERNAME);
            return LogInResponse.INVALID_USERNAME;
        }
        else{
            connections.put(username, connection);
            System.out.printf("%s Connected%n", username);
            if (connection instanceof SocketClientConnection)
                validUsername(connection);
            return LogInResponse.LOGGED_IN;

        }
    }

    /**
     * @param username the username of the player
     * @return true if the player is registered in the server, false otherwise
     */
    public boolean checkUserConnected(String username){
        return connections.get(username).getStatus() == ConnectionStatus.ONLINE;
    }



    // Client -> Server methods
    /**
     * @param username the username of the player
     * @return the list of lobbies
     */
    public ArrayList<String> getLobbies(String username) {
        ArrayList<String> idList = ServerController.getInstance().getLobbies(username);

        if (connections.get(username) instanceof SocketClientConnection) {

            if (!idList.isEmpty()) {
                try {
                    ((SocketClientConnection) connections.get(username)).lobbyExists(idList);
                } catch (IOException e) {
                    connections.get(username).setOffline();
                }
            } else {
                try {
                    ((SocketClientConnection) connections.get(username)).lobbyDoesNotExist();
                } catch (IOException e) {
                    connections.get(username).setOffline();
                }
            }
            return idList;
        } else {
            return idList;
        }
    }

    /**
     * @param username the username of the player
     * @param lobbyId the id of the lobby
     * @return the response of addPlayerToLobby
     */
    public AddPlayerToLobbyResponse addPlayerToLobby(String username, String lobbyId) {
        AddPlayerToLobbyResponse result = ServerController.getInstance().addPlayerToLobby(username, lobbyId);
        if (connections.get(username) instanceof SocketClientConnection) {
            switch (result) {
                case LOBBY_NOT_FOUND:
                    try {
                        ((SocketClientConnection) connections.get(username)).lobbyFull();
                    } catch (IOException e) {
                        connections.get(username).setOffline();
                    }
                    break;
                case REFRESH_LOBBIES:
                    try {
                        ((SocketClientConnection) connections.get(username)).lobbyExists(ServerController.getInstance().getLobbies(username));
                    } catch (IOException e) {
                        connections.get(username).setOffline();
                    }
                    break;
                case PLAYER_ADDED:
                    for (String u : ServerController.getInstance().getUserToLobby().keySet()) {
                        if (ServerController.getInstance().getUserToLobby().get(u).equals(lobbyId) && u.equals(username) ) {
                            try {
                                ((SocketClientConnection) connections.get(u)).joinLobbySuccess(ServerController.getInstance().getLobbyController().getLobbies().get(lobbyId).isFull());
                            } catch (IOException e) {
                                connections.get(username).setOffline();
                            }
                        } else if (ServerController.getInstance().getUserToLobby().get(u).equals(lobbyId)) {
                            playerJoinedLobby(connections.get(u), username);
                        }
                    }
                    break;
                case NO_LOBBIES:
                    try {
                        ((SocketClientConnection) connections.get(username)).lobbyDoesNotExist();
                    } catch (IOException e) {
                        connections.get(username).setOffline();
                    }
                    break;
            }
            return AddPlayerToLobbyResponse.PLAYER_ADDED;
        } else {
            if(result.equals(AddPlayerToLobbyResponse.PLAYER_ADDED)) {
                if (ServerController.getInstance().getLobbyController().getLobbies().get(lobbyId).isFull())
                    result = AddPlayerToLobbyResponse.PLAYER_ADDED_LAST;
                for (String u : ServerController.getInstance().getUserToLobby().keySet()) {
                    if (ServerController.getInstance().getUserToLobby().get(u).equals(lobbyId) && !u.equals(username)) {
                        playerJoinedLobby(connections.get(u), username);
                    }
                }
            }
            return result;
        }
    }

    /**
     * Receive the private goal choice from the player
     * @param connection the connection of the player
     * @param username the username of the player who joined the lobby
     */
    private void playerJoinedLobby(ClientConnection connection, String username) {
        try {
            connection.playerJoined(username);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }

    /**
     * Receive the private goal choice from the player
     * @param username the username of the player
     * @param index the index of the private goal
     */
    public void choosePrivateGoal(String username, int index) {
        ServerController.getInstance().choosePrivateGoal(username, index);
        if (connections.get(username) instanceof SocketClientConnection) {
            try {
                ((SocketClientConnection) connections.get(username)).privateGoalChosen();
            } catch (IOException e) {
                connections.get(username).setOffline();
            }
        }
    }

    /**
     * Receive the starter card side choice from the player
     * @param username the username of the player
     * @param flipped the side of the starter card
     * @return the response of chooseStarterCardSide
     */
    public ChooseStarterCardSideResponse chooseStarterCardSide(String username, boolean flipped) {
        ChooseStarterCardSideResponse result = ServerController.getInstance().chooseStarterCardSide(username, flipped);
        if (connections.get(username) instanceof SocketClientConnection  ) {
            if (result.equals(ChooseStarterCardSideResponse.WAIT_FOR_OTHER_PLAYER)) {
                try {
                    ((SocketClientConnection) connections.get(username)).waitingOthersStartingChoice();
                } catch (IOException e) {
                    connections.get(username).setOffline();
                }
            }
            return result;
        } else {
            return result;
        }
    }

    /**
     * Place a card on the board
     * @param username the username of the player
     * @param cardId the id of the card
     * @param position the position of the card
     * @param flipped the side of the card
     * @return the response of placeCard
     */
    public PlaceCardSuccessInfo placeCard(String username, String cardId, Point position, boolean flipped) {
        try {
            PlaceCardSuccessInfo placeCardSuccessInfo = ServerController.getInstance().placeCard(username, cardId, position, flipped);

            if (connections.get(username) instanceof SocketClientConnection ) {
                if (placeCardSuccessInfo != null) {
                    try {
                        ((SocketClientConnection) connections.get(username)).placeCardSuccess(placeCardSuccessInfo);
                    } catch (IOException e) {
                        connections.get(username).setOffline();
                    }
                }
            }
            return placeCardSuccessInfo;

        } catch (RequirementsNotSatisfied | InvalidPositionException e) {
            if (connections.get(username) instanceof SocketClientConnection){
                try {
                    ((SocketClientConnection) connections.get(username)).placeCardFailure();
                } catch (IOException ex) {
                    connections.get(username).setOffline();
                }
            }
            return null;
        }
    }

    /**
     * Draw a  resource card from the hand
     * @param username the username of the player
     * @param index the index of the card
     * @return the updated hand
     */
    public ArrayList<CardInfo> drawResource(String username, int index) {
        ArrayList<CardInfo> result = ServerController.getInstance().drawResource(username, index);
        if (connections.get(username) instanceof SocketClientConnection ) {
            if (result != null) {
                try {
                    ((SocketClientConnection) connections.get(username)).drawSuccess(result);
                } catch (IOException e) {
                    connections.get(username).setOffline();
                }
            }
        }
        return result;
    }

    /**
     * Draw a gold card from the hand
     * @param username the username of the player
     * @param index the index of the card
     * @return updated hand
     */
    public ArrayList<CardInfo> drawGold(String username, int index) {
        ArrayList<CardInfo> result = ServerController.getInstance().drawGold(username, index);
        if (connections.get(username) instanceof SocketClientConnection ) {
            if (result != null) {
                try {
                    ((SocketClientConnection) connections.get(username)).drawSuccess(result);
                } catch (IOException e) {
                    connections.get(username).setOffline();
                }
            }
        }
        return result;
    }


    /**
     * Create a lobby
     * @param username the username of the player
     * @param numPlayers the number of players in the lobby
     * @return the id of the lobby
     */
    public String createLobby(String username, int numPlayers) {
        String lobbyId = ServerController.getInstance().createLobby(username, numPlayers);
        if (connections.get(username) instanceof SocketClientConnection ){
            if (lobbyId!=null) {
                try {
                    ((SocketClientConnection) connections.get(username)).lobbyCreated(lobbyId);
                } catch (IOException e) {
                    connections.get(username).setOffline();
                }
                addPlayerToLobby(username, lobbyId);
            }
        }
        return lobbyId;
    }

    /**
     * End the turn of the player
     * @param username the username of the player
     */
    public void endTurn(String username) {
        ServerController.getInstance().endTurn(username);

    }

    /**
     * Create a game
     * @param username the username of the player
     */
    public void createGame(String username) {
        ServerController.getInstance().createGame(ServerController.getInstance().getLobbyController().getLobbies().get(ServerController.getInstance().getUserToLobby().get(username)));
    }


    // Server -> Client communications
    /**
     * Notify the player that it's his turn
     * @param username the username of the player to notify
     * @param turnInfo the information of the turn
     */
    public void initTurn(String username, TurnInfo turnInfo){
        try {
            connections.get(username).initTurn(turnInfo);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }

    /**
     * Notify the player the current player
     * @param username the username of the player to notify
     * @param currentPlayer the username of the current player
     */
    public void otherPlayerTurn(String username, String currentPlayer){
        try {
            connections.get(username).otherPlayerTurnMessage(currentPlayer);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }

    /**
     * Notify the player that the game has ended
     * @param username the username of the player to notify
     * @param leaderboard the leaderboard of the game
     */
    public void endGame(String username, Map<String, Integer> leaderboard) {
        try {
            connections.get(username).gameEnded(leaderboard);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }

    /**
     * Notify the player that the game has been created
     * @param username the username of the player to notify
     * @param starterData the data of the game start
     */
    public void gameCreated(String username, StarterData starterData) {
        try {
                connections.get(username).gameStarted(starterData);
            } catch (IOException e) {
                connections.get(username).setOffline();
            }
    }

    /**
     * Notify the player that the username is valid
     * @param connection the connection of the player to notify
     */
    public void validUsername(ClientConnection connection) {
        try {
            if (connection instanceof SocketClientConnection)
                ((SocketClientConnection) connection).validUsername();
        } catch (IOException e) {
            connection.setOffline();
        }
    }

    /**
     * Notify the player that the username is invalid
     * @param connection the connection of the player to notify
     * @param status the response of the login request
     */
    public void invalidUsername(ClientConnection connection, LogInResponse status) {
        try {
            ((SocketClientConnection) connection).invalidUsername(status);
        } catch (IOException e) {
            connection.setOffline();
        }
    }

    /**
     * Send to the player the updated game state
     * @param username the username of the player to notify
     * @param gameStateInfo the updated game state
     */
    public void gameState(String username, GameStateInfo gameStateInfo) {
        try {
            connections.get(username).sendStatus(gameStateInfo);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }


    /**
     * Handle the disconnection of a player
     * @param c the connection of the player that disconnected
     */
    public void onClientDisconnect(ClientConnection c){
        c.setOffline();
        System.out.printf("Client %s disconnected%n", c.getRemoteAddr());
        String username = connections.keySet().stream().filter(u -> connections.get(u).equals(c)).findFirst().orElse(null);
        ServerController.getInstance().playerDisconnected(username);
    }

    /**
     * Notify the player that another player disconnected
     * @param username the username of the player that disconnected
     * @param receiver the username of the player to notify
     * @param gameStarted true if the game has started, false otherwise
     */
    public void playerDisconnected(String username, String receiver, boolean gameStarted){
        try {
            connections.get(receiver).playerDisconnected(username, gameStarted);
        } catch (IOException e) {
            connections.get(receiver).setOffline();
        }
    }

    /**
     * Notify the player that another player reconnected
     * @param username the username of the player that reconnected
     * @param receiver the username of the player to notify
     */
    public void playerReconnected(String username, String receiver) {
        try {
            connections.get(receiver).playerReconnected(username);
        } catch (IOException e) {
            connections.get(receiver).setOffline();
        }
    }

    /**
     * Send the updated game state to the reconnected player
     * @param gameStateInfo the game state
     */
    public void reconnectionState(GameStateInfo gameStateInfo){
        try {
            connections.get(gameStateInfo.getUsername()).reconnectionState(gameStateInfo);
        } catch (IOException e) {
            connections.get(gameStateInfo.getUsername()).setOffline();
        }
    }

    /**
     * Notify the player that no other player is connected
     * @param username the username of the player to notify
     */
    public void noOtherPlayerConnected(String username) {
        try {
            connections.get(username).noOtherPlayerConnected();
        } catch (IOException e) {
            connections.get(username).setOffline();
        }

    }

    /**
     * Get the hash map of connections
     * @return the hash map of connections
     */
    public HashMap<String, ClientConnection> getConnections() {
        return connections;
    }

    /**
     * Handles a received message from the chat
     * @param msg the chat message
     */
    public void recvChatMessage(ChatMessageData msg) {
        ServerController.getInstance().distributeMessage(msg);
    }

    /**
     * Send a chat message to a player
     * @param msg the chat message
     * @param receiver the username of the player to notify
     */
    public void sendChatMessage(ChatMessageData msg, String receiver) {
        try {
            connections.get(receiver).sendChatMessage(msg);
        } catch (IOException e) {
            connections.get(receiver).setOffline();
        }
    }
}

