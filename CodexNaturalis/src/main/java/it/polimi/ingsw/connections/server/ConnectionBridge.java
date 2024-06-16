package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.connections.enums.AddPlayerToLobbyresponse;
import it.polimi.ingsw.connections.enums.ChooseStarterCardSideResponse;
import it.polimi.ingsw.connections.enums.LogInResponse;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public AddPlayerToLobbyresponse addPlayerToLobby(String username, String lobbyId) {
        AddPlayerToLobbyresponse result = ServerController.getInstance().addPlayerToLobby(username, lobbyId);
        if (connections.get(username) instanceof SocketClientConnection) {
            switch (result) {
                case LOBBY_NOT_FOUND:
                    try {
                        ((SocketClientConnection) connections.get(username)).lobbyDoesNotExist();
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
                case LOBBY_FULL:
                    try {
                        ((SocketClientConnection) connections.get(username)).lobbyFull();
                    } catch (IOException e) {
                        connections.get(username).setOffline();
                    }
                    break;
            }
            return AddPlayerToLobbyresponse.PLAYER_ADDED;
        } else {
            if(result.equals(AddPlayerToLobbyresponse.PLAYER_ADDED)) {
                if (ServerController.getInstance().getLobbyController().getLobbies().get(lobbyId).isFull())
                    result = AddPlayerToLobbyresponse.PlAYER_ADDED_LAST;
                for (String u : ServerController.getInstance().getUserToLobby().keySet()) {
                    if (ServerController.getInstance().getUserToLobby().get(u).equals(lobbyId) && !u.equals(username)) {
                        playerJoinedLobby(connections.get(u), username);
                    }
                }
            }
            return result;
        }
    }

    private void playerJoinedLobby(ClientConnection connection, String username) {
        try {
            connection.playerJoined(username);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }

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

    public ChooseStarterCardSideResponse chooseStarterCardSide(String username, boolean flipped) {
        ChooseStarterCardSideResponse result = ServerController.getInstance().chooseStarterCardSide(username, flipped);
        if (connections.get(username) instanceof SocketClientConnection  ) {
            if (result.equals(ChooseStarterCardSideResponse.WAIT_FOR_OTHER_PLAYER)) {
                try {
                    ((SocketClientConnection) connections.get(username)).waitingOthersStartingChoice();
                } catch (IOException e) {
                    connections.get(username).setOffline();
                }
            } else if (result.equals(ChooseStarterCardSideResponse.SUCCESS)){
                String currentPlayer = ServerController.getInstance().getGameController().getCurrentPlayer(ServerController.getInstance().getUserToGame().get(username));
                ServerController.getInstance().initTurn(currentPlayer);
            }
            return result;
        } else {
            if (result.equals(ChooseStarterCardSideResponse.SUCCESS)) {
                String currentPlayer = ServerController.getInstance().getGameController().getCurrentPlayer(ServerController.getInstance().getUserToGame().get(username));
                ServerController.getInstance().initTurn(currentPlayer);
            }
            return result;
        }
    }


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


    public void endTurn(String username) {
        ServerController.getInstance().endTurn(username);

    }

    public void createGame(String username) {
        ServerController.getInstance().createGame(ServerController.getInstance().getLobbyController().getLobbies().get(ServerController.getInstance().getUserToLobby().get(username)));
    }

    // Server -> Client communications

    public void initTurn(String username, TurnInfo turnInfo){
        try {
            connections.get(username).initTurn(turnInfo);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }

    public void otherPlayerTurn(String username, String currentPlayer){
        try {
            connections.get(username).otherPlayerTurnMessage(currentPlayer);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }

    public void endGame(String username, HashMap<String, Integer> leaderboard) {
        try {
            connections.get(username).gameEnded(leaderboard);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }

    public void gameCreated(String username, StarterData starterData) {
        try {
                connections.get(username).gameStarted(starterData);
            } catch (IOException e) {
                connections.get(username).setOffline();
            }
    }

    public void validUsername(ClientConnection connection) {
        try {
            if (connection instanceof SocketClientConnection)
                ((SocketClientConnection) connection).validUsername();
        } catch (IOException e) {
            connection.setOffline();
        }
    }

    public void invalidUsername(ClientConnection connection, LogInResponse status) {
        try {
            ((SocketClientConnection) connection).invalidUsername(status);
        } catch (IOException e) {
            connection.setOffline();
        }
    }



    public void gameState(String username, GameStateInfo gameStateInfo) {
        try {
            connections.get(username).sendStatus(gameStateInfo);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }


    public void onClientDisconnect(ClientConnection c){
        c.setOffline();
        System.out.printf("Client %s disconnected%n", c.getRemoteAddr());
        String username = connections.keySet().stream().filter(u -> connections.get(u).equals(c)).findFirst().orElse(null);
        ServerController.getInstance().playerDisconnected(username);
    }

    public void playerDisconnected(String username, String receiver, boolean gameStarted){
        try {
            connections.get(receiver).playerDisconnected(username, gameStarted);
        } catch (IOException e) {
            connections.get(receiver).setOffline();
        }
    }

    public void playerReconnected(String username, String receiver) {
        try {
            connections.get(receiver).playerReconnected(username);
        } catch (IOException e) {
            connections.get(receiver).setOffline();
        }
    }

    public void reconnectionState(GameStateInfo gameStateInfo){
        try {
            connections.get(gameStateInfo.getUsername()).reconnectionState(gameStateInfo);
        } catch (IOException e) {
            connections.get(gameStateInfo.getUsername()).setOffline();
        }
    }

    public void noOtherPlayerConnected(String user) {
        try {
            connections.get(user).noOtherPlayerConnected();
        } catch (IOException e) {
            connections.get(user).setOffline();
        }

    }

    public HashMap<String, ClientConnection> getConnections() {
        return connections;
    }

    public void recvChatMessage(ChatMessageData msg) {
        ServerController.getInstance().distributeMessage(msg);
    }

    public void sendChatMessage(ChatMessageData msg, String receiver) {
        try {
            connections.get(receiver).sendChatMessage(msg);
        } catch (IOException e) {
            connections.get(receiver).setOffline();
        }
    }
}

