package it.polimi.ingsw.connections.server;

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

public class ConnectionBridge {

    private final ServerController controller;
    private HashMap<String, ClientConnection> connections;

    public ConnectionBridge(ServerController controller) {

        this.controller = controller;
        connections = new HashMap<>();
    }

    /**
     * Add a connection to the list of connections
     * @param connection the connection to add
     * @param username the username of the connection
     */

    public LogInResponse addConnection(ClientConnection connection, String username) throws IOException {
        if(connections.containsKey(username) && connections.get(username).getStatus() == ConnectionStatus.OFFLINE){
            connections.replace(username, connection);

            System.out.printf("%s Reconnected%n", username);
            return controller.playerReconnected(username);

        }
        else if(connections.containsKey(username)) {
            if (connection instanceof SocketClientConnection)
                invalidUsername(connection);
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
        ArrayList<String> idList = controller.getLobbies(username);

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

    // TODO: return the lobbyId

    public AddPlayerToLobbyresponse addPlayerToLobby(String username, String lobbyId) {
        AddPlayerToLobbyresponse result = controller.addPlayerToLobby(username, lobbyId);
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
                    for (String u : controller.getUserToLobby().keySet()) {
                        if (controller.getUserToLobby().get(u).equals(lobbyId) && u.equals(username) ) {
                            try {
                                ((SocketClientConnection) connections.get(u)).joinLobbySuccess(controller.getLobbyController().getLobbies().get(lobbyId).isFull());
                            } catch (IOException e) {
                                connections.get(username).setOffline();
                            }
                        } else if (controller.getUserToLobby().get(u).equals(lobbyId)) {
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
                if (controller.getLobbyController().getLobbies().get(lobbyId).isFull())
                    result = AddPlayerToLobbyresponse.PlAYER_ADDED_LAST;
                for (String u : controller.getUserToLobby().keySet()) {
                    if (controller.getUserToLobby().get(u).equals(lobbyId) && !u.equals(username)) {
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
        controller.choosePrivateGoal(username, index);
        if (connections.get(username) instanceof SocketClientConnection) {
            try {
                ((SocketClientConnection) connections.get(username)).privateGoalChosen();
            } catch (IOException e) {
                connections.get(username).setOffline();
            }
        }
    }

    public ChooseStarterCardSideResponse chooseStarterCardSide(String username, boolean flipped) {
        ChooseStarterCardSideResponse result = controller.chooseStarterCardSide(username, flipped);
        if (connections.get(username) instanceof SocketClientConnection  ) {
            if (result.equals(ChooseStarterCardSideResponse.WAIT_FOR_OTHER_PLAYER)) {
                try {
                    ((SocketClientConnection) connections.get(username)).waitingOthersStartingChoice();
                } catch (IOException e) {
                    connections.get(username).setOffline();
                }
            } else if (result.equals(ChooseStarterCardSideResponse.SUCCESS)){
                String currentPlayer = controller.getGameController().getCurrentPlayer(controller.getUserToGame().get(username));
                controller.initTurn(currentPlayer);
            }
            return result;
        } else {
            if (result.equals(ChooseStarterCardSideResponse.SUCCESS)) {
                String currentPlayer = controller.getGameController().getCurrentPlayer(controller.getUserToGame().get(username));
                controller.initTurn(currentPlayer);
            }
            return result;
        }
    }


    public PlaceCardSuccessInfo placeCard(String username, String cardId, Point position, boolean flipped) {
        try {
            PlaceCardSuccessInfo placeCardSuccessInfo = controller.placeCard(username, cardId, position, flipped);

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
        ArrayList<CardInfo> result = controller.drawResource(username, index);
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
        ArrayList<CardInfo> result = controller.drawGold(username, index);
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
        String lobbyId = controller.createLobby(username, numPlayers);
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
        controller.endTurn(username);

    }

    public void createGame(String username) {
        controller.createGame(controller.getLobbyController().getLobbies().get(controller.getUserToLobby().get(username)).getUsers());
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

    public void invalidUsername(ClientConnection connection) {
        try {
            ((SocketClientConnection) connection).invalidUsername();
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
        System.out.println(String.format("Client %s disconnected", c.getRemoteAddr()));
        String username = connections.keySet().stream().filter(u -> connections.get(u).equals(c)).findFirst().orElse(null);
        controller.playerDisconnected(username);
    }

    public void playerDisconnected(String username, String receiver, boolean gameStarted){
        try {
            connections.get(receiver).playerDisconnected(username, gameStarted);
        } catch (IOException e) {
            connections.get(username).setOffline();
        }
    }

    public void playerReconnected(String username, String receiver) {
        try {
            connections.get(receiver).playerReconnected(username);
        } catch (IOException e) {
            connections.get(username).setOffline();
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
}

