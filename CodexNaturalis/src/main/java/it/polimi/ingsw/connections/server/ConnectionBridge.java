package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.client.RMIClientConnection;
import it.polimi.ingsw.connections.client.RMIClientConnectionInterface;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.connections.enums.AddPlayerToLobbyresponse;
import it.polimi.ingsw.connections.enums.ChooseStarterCardSideResponse;
import it.polimi.ingsw.connections.enums.LogInResponse;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionBridge {

    private final ServerController controller;
    private HashMap<String, ClientConnection> connections;

    private HashMap<ClientConnection, ConnectionStatus> connectionsStatus;

    public ConnectionBridge(ServerController controller) {

        this.controller = controller;
        connections = new HashMap<String, ClientConnection>();
        connectionsStatus = new HashMap<ClientConnection, ConnectionStatus>();
    }

    /**
     * Add a connection to the list of connections
     * @param connection the connection to add
     * @param username the username of the connection
     */

    public LogInResponse addConnection(ClientConnection connection, String username) throws IOException {
        if(connections.containsKey(username) && connectionsStatus.get(connections.get(username)).equals(ConnectionStatus.OFFLINE)) {
            connectionsStatus.remove(connections.get(username));
            connections.replace(username, connection);
            connectionsStatus.put(connection, ConnectionStatus.ONLINE);

            System.out.printf("%s Reconnected%n", username);
            controller.playerReconnected(username);
            return LogInResponse.RECONNECT;
            // TODO: handle reconnection
        }
        else if(connections.containsKey(username)) {
            if (connection instanceof SocketClientConnection)
                invalidUsername(connection);
            return LogInResponse.INVALID_USERNAME;
        }
        else{
            connections.put(username, connection);
            connectionsStatus.put(connection, ConnectionStatus.ONLINE);
            System.out.printf("%s Connected%n", username);
            if (connection instanceof SocketClientConnection)
                validUsername(connection);
            return LogInResponse.LOGGED_IN;

        }
    }

    /**
     * disconnect the player from the server
     * @param username the username of the player
     */
    public void disconnect(String username) {
        if (checkUserConnected(username)) {
            try {
                connections.get(username).close();
            } catch (IOException e) {
//                try {
//                    //connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
            }
            connections.remove(username);
            controller.removeUser(username);
        }
    }

    /**
     * @param username the username of the player
     * @return true if the player is registered in the server, false otherwise
     */
    public boolean checkUserConnected(String username){
        return connectionsStatus.get(connections.get(username)).equals(ConnectionStatus.ONLINE);
    }



    // Client -> Server methods
    public ArrayList<String> getLobbies(String username) {
        ArrayList<String> idList = controller.getLobbies(username);

        if (connections.get(username) instanceof SocketClientConnection) {

            if (!idList.isEmpty()) {
                try {
                    ((SocketClientConnection) connections.get(username)).lobbyExists(idList);

                } catch (IOException e) {

                    connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
                }
            } else {
                try {
                    ((SocketClientConnection) connections.get(username)).lobbyDoesNotExist();
                } catch (IOException e) {
                    connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
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
                        connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);

                    }
                    break;
                case PLAYER_ADDED:
                    for (String u : controller.getUserToLobby().keySet()) {
                        if (controller.getUserToLobby().get(u).equals(lobbyId) && u.equals(username) ) {
                            try {
                                ((SocketClientConnection) connections.get(u)).joinLobbySuccess(controller.getLobbyController().getLobbies().get(lobbyId).isFull());
                            } catch (IOException e) {
                                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
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
                        connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
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
        if (connection instanceof SocketClientConnection) {
            try {
                ((SocketClientConnection) connection).playerJoined(username);
            } catch (IOException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        } else {
            try {
                ((RMIClientConnectionInterface) connection).playerJoined(username);
            } catch (RemoteException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        }
    }

    public int choosePrivateGoal(String username, int index) {
        int result = controller.choosePrivateGoal(username, index);
        if (connections.get(username) instanceof SocketClientConnection) {
            if (result == 1) {
                try {
                    ((SocketClientConnection) connections.get(username)).privateGoalChosen();
                } catch (IOException e) {
                    connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
                }
            }
            return result;
        } else {
            return result;
        }
    }

    public ChooseStarterCardSideResponse chooseStarterCardSide(String username, boolean flipped) {
        ChooseStarterCardSideResponse result = controller.chooseStarterCardSide(username, flipped);
        if (connections.get(username) instanceof SocketClientConnection  ) {
            if (result.equals(ChooseStarterCardSideResponse.WAIT_FOR_OTHER_PLAYER)) {
                try {
                    ((SocketClientConnection) connections.get(username)).waitingOthersStartingChoice();
                } catch (IOException e) {
                    connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
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
                        connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
                    }
                }
            }
            return placeCardSuccessInfo;

        } catch (RequirementsNotSatisfied | InvalidPositionException e) {
            if (connections.get(username) instanceof SocketClientConnection){
                try {
                    ((SocketClientConnection) connections.get(username)).placeCardFailure();
                } catch (IOException ex) {
                    connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
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
                    connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
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
                    connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
                }
            }
        }
        return result;
    }



    public String createLobby(String username, int numPlayers) {
        String lobbyId = controller.createLobby(username, numPlayers);
        if (connections.get(username) instanceof SocketClientConnection ){
            if (lobbyId!=null) {
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

    public void initTurnAck(String username) {
    }


    // Server -> Client communications

    public void initTurn(String username, TurnInfo turnInfo){
        if (connections.get(username) instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connections.get(username)).initTurn(turnInfo);
            } catch (IOException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        } else {
            try {
                ((RMIClientConnectionInterface) connections.get(username)).initTurn(turnInfo);
            } catch (RemoteException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        }
    }

    public void otherPlayerTurn(String username, String currentPlayer){
        if (connections.get(username) instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connections.get(username)).otherPlayerTurnMessage(currentPlayer);
            } catch (IOException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        } else {
            try {
                ((RMIClientConnectionInterface) connections.get(username)).otherPlayerTurn(currentPlayer);
            } catch (RemoteException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        }
    }

    public void endGame(String username, HashMap<String, Integer> leaderboard) {
        if (connections.get(username) instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connections.get(username)).gameEnded(leaderboard);
            } catch (IOException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        } else {
            try {
                ((RMIClientConnectionInterface) connections.get(username)).gameEnded(leaderboard);
            } catch (RemoteException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        }
    }

    public void gameCreated(String username, StarterData starterData) {

        if ((connections.get(username) instanceof SocketClientConnection) ) {
            try {
                ((SocketClientConnection) connections.get(username)).gameStarted(starterData);
            } catch (IOException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        } else {
            try {
                ((RMIClientConnectionInterface) connections.get(username)).gameStarted(starterData);
            } catch (RemoteException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        }
    }

    public void validUsername(ClientConnection connection) {
        if (connection instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connection).validUsername();
            } catch (IOException e) {
                connectionsStatus.replace(connection, ConnectionStatus.OFFLINE);
            }
        }
    }

    public void invalidUsername(ClientConnection connection) {
        if (connection instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connection).invalidUsername();
            } catch (IOException e) {
                connectionsStatus.replace(connection, ConnectionStatus.OFFLINE);
            }
        }
    }



    public void gameState(String username, GameStateInfo gameStateInfo) {
        if (connections.get(username) instanceof SocketClientConnection ){
            try {
                ((SocketClientConnection) connections.get(username)).sendStatus(gameStateInfo);
            } catch (IOException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        } else{
            try {
                ((RMIClientConnectionInterface) connections.get(username)).sendStatus(gameStateInfo);
            } catch (RemoteException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        }
    }



    // TODO
    public void onClientDisconnect(ClientConnection c){
        connectionsStatus.replace(c, ConnectionStatus.OFFLINE);
        try {
            System.out.println(String.format("Client %s disconnected", c.getRemoteAddr()));
        } catch (IOException e) {
            connectionsStatus.replace(c, ConnectionStatus.OFFLINE);
        }
        String username = connections.keySet().stream().filter(u -> connections.get(u).equals(c)).findFirst().orElse(null);
        controller.playerDisconnected(username);
    }

    public void playerDisconnected(String username, String receiver, boolean gameStarted){
        if (connections.get(receiver) instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connections.get(receiver)).playerDisconnected(username, gameStarted);
            } catch (IOException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        } else {

            //TODO handle RMI
        }
    }

    public void playerReconnected(String username, String receiver) {
        if (connections.get(receiver) instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connections.get(receiver)).playerReconnected(username);
            } catch (IOException e) {
                connectionsStatus.replace(connections.get(username), ConnectionStatus.OFFLINE);
            }
        } else {

            //TODO handle RMI
        }
    }

    public void reconnectionState(GameStateInfo gameStateInfo){
        if (connections.get(gameStateInfo.getUsername()) instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connections.get(gameStateInfo.getUsername())).reconnectionState(gameStateInfo);
            } catch (IOException e) {
                connectionsStatus.replace(connections.get(gameStateInfo.getUsername()), ConnectionStatus.OFFLINE);
            }
        } else {

            //TODO handle RMI
        }

    }

    public HashMap<String, ClientConnection> getConnections() {
        return connections;
    }

    public HashMap<ClientConnection, ConnectionStatus> getConnectionsStatus() {
        return connectionsStatus;
    }
}

