package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.*;
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
        connections = new HashMap<String, ClientConnection>();
    }

    /**
     * Add a connection to the list of connections
     * @param connection the connection to add
     * @param username the username of the connection
     */

    public void addConnection(ClientConnection connection, String username){
        if(connections.containsKey(username) && connections.get(username).getStatus() == ConnectionStatus.OFFLINE) {
            connections.replace(username, connection);
            System.out.printf("%s Reconnected%n", username);
            controller.playerReconnected(username);
            // TODO: handle reconnection
        }
        else if(connections.containsKey(username)) {
            invalidUsername(connection);
        }
        else{
            connections.put(username, connection);
            System.out.printf("%s Connected%n", username);
            validUsername(connection);
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
                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
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
        return connections.get(username).getStatus() == ConnectionStatus.ONLINE;
    }


    // Client -> Server methods

    public Object getLobbies(String username) {
        ArrayList<String> idList = controller.getLobbies(username);

        if (connections.get(username) instanceof SocketClientConnection) {

            if (!idList.isEmpty()) {
                try {
                    ((SocketClientConnection) connections.get(username)).lobbyExists(idList);

                } catch (IOException e) {
                    connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                }
            } else {
                try {
                    ((SocketClientConnection) connections.get(username)).lobbyDoesNotExist();
                } catch (IOException e) {
                    connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                }
            }
            return null;
        } else {
            // TODO: handle RMI
            return null;
        }
    }

    public int addPlayerToLobby(String username, String lobbyId) {
        int result = controller.addPlayerToLobby(username, lobbyId);
        if (connections.get(username) instanceof SocketClientConnection ) {
            switch (result) {
                case 0:
                    try {
                        ((SocketClientConnection) connections.get(username)).lobbyDoesNotExist();
                    } catch (IOException e) {
                        connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;

                    }
                    break;

                case 1:
                    for (String u : controller.getUserToLobby().keySet()) {
                        if (controller.getUserToLobby().get(u).equals(lobbyId) && u.equals(username) ) {
                            try {
                                ((SocketClientConnection) connections.get(u)).joinLobbySuccess(controller.getLobbyController().getLobbies().get(lobbyId).isFull());
                            } catch (IOException e) {
                                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                            }
                        } else if (controller.getUserToLobby().get(u).equals(lobbyId) && !u.equals(username)) {
                            try {
                                ((SocketClientConnection) connections.get(u)).playerJoined(username);
                            } catch (IOException e) {
                                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                            }
                        }
                    }
                    break;
                case 2:

                    try {
                        ((SocketClientConnection) connections.get(username)).lobbyFull();
                    } catch (IOException e) {
                        connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                    }
                    break;

            }
            return result;
        } else {
            // TODO: handle RMI
            return result;
        }
    }

    public int choosePrivateGoal(String username, int index) {
        int result = controller.choosePrivateGoal(username, index);
        if (connections.get(username) instanceof SocketClientConnection) {
            if (result == 1) {
                try {
                    ((SocketClientConnection) connections.get(username)).privateGoalChosen();
                } catch (IOException e) {
                    connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                }
            }
            return result;
        } else {
            return result;
            // TODO: handle RMI
        }
    }

    public int chooseStarterCardSide(String username, boolean flipped) {
        int result = controller.chooseStarterCardSide(username, flipped);
        if (connections.get(username) instanceof SocketClientConnection ) {
            if (result == 0) {
                try {
                    ((SocketClientConnection) connections.get(username)).waitingOthersStartingChoice();
                } catch (IOException e) {
                    connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                }
            } else if (result==1){
                String currentPlayer = controller.getGameController().getCurrentPlayer(controller.getUserToGame().get(username));
                controller.initTurn(currentPlayer);
            }
            return result;
        } else {
            return result;
            // TODO: handle RMI
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
                        connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                    }
                }
            } else {
                return placeCardSuccessInfo;
                // TODO: handle RMI
            }

        } catch (RequirementsNotSatisfied | InvalidPositionException e) {
            if (connections.get(username) instanceof SocketClientConnection){
                try {
                    ((SocketClientConnection) connections.get(username)).placeCardFailure();
                } catch (IOException ex) {
                    connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                }
            } else {
                return null;
                // TODO: handle RMI
            }
        }
        return null;
    }

    public ArrayList<CardInfo> drawResource(String username, int index) {
        ArrayList<CardInfo> result = controller.drawResource(username, index);
        if (connections.get(username) instanceof SocketClientConnection ) {
            if (result != null) {
                try {
                    ((SocketClientConnection) connections.get(username)).drawSuccess(result);
                } catch (IOException e) {
                    connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                }
            }
        } else {
            return result;
            // TODO: handle RMI
        }
        return null;
    }

    public ArrayList<CardInfo> drawGold(String username, int index) {
        ArrayList<CardInfo> result = controller.drawGold(username, index);
        if (connections.get(username) instanceof SocketClientConnection ) {
            if (result != null) {
                try {
                    ((SocketClientConnection) connections.get(username)).drawSuccess(result);
                } catch (IOException e) {
                    connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
                }
            }
        } else {
            return result;
            // TODO: handle RMI
        }
        return null;
    }



    public String createLobby(String username, int numPlayers) {
        String lobbyId = controller.createLobby(username, numPlayers);
        if (connections.get(username) instanceof SocketClientConnection ){
            if (lobbyId!=null) {
                addPlayerToLobby(username, lobbyId);
            }
        }
        else {
            // TODO: handle RMI
            return lobbyId;
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
                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void otherPlayerTurn(String username, String currentPlayer){
        if (connections.get(username) instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connections.get(username)).otherPlayerTurnMessage(currentPlayer);
            } catch (IOException e) {
                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void endGame(String username, HashMap<String, Integer> leaderboard) {
        if (connections.get(username) instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connections.get(username)).gameEnded(leaderboard);
            } catch (IOException e) {
                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void gameCreated(String username, StarterData starterData) {

        if ((connections.get(username) instanceof SocketClientConnection) ) {
            try {
                ((SocketClientConnection) connections.get(username)).gameStarted(starterData);
            } catch (IOException e) {
                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
            }
        } else {
            // TODO handle RMI
        }
    }

    public void validUsername(ClientConnection connection) {
        if (connection instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connection).validUsername();
            } catch (IOException e) {
                connection.setStatus(ConnectionStatus.OFFLINE) ;
            }
        } else {
            // TODO handle RMI
        }
    }

    public void invalidUsername(ClientConnection connection) {
        if (connection instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connection).invalidUsername();
            } catch (IOException e) {
                connection.setStatus(ConnectionStatus.OFFLINE) ;
            }
        } else {
            // TODO handle RMI
        }
    }



    public void gameState(String username, GameStateInfo gameStateInfo) {
        if (connections.get(username) instanceof SocketClientConnection ){
            try {
                ((SocketClientConnection) connections.get(username)).sendStatus(gameStateInfo);
            } catch (IOException e) {
                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
            }
        } else {

            //TODO handle RMI
        }
    }


    // TODO

    public void onClientDisconnect(ClientConnection c){
        System.out.println(String.format("Client %s disconnected", c.getRemoteAddr()));
        String username = connections.keySet().stream().filter(u -> connections.get(u).equals(c)).findFirst().orElse(null);
        controller.playerDisconnected(username);
    }

    public void playerDisconnected(String username, String receiver, boolean gameStarted){
        if (connections.get(receiver) instanceof SocketClientConnection ) {
            try {
                ((SocketClientConnection) connections.get(receiver)).playerDisconnected(username, gameStarted);
            } catch (IOException e) {
                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
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
                connections.get(username).setStatus(ConnectionStatus.OFFLINE) ;
            }
        } else {

            //TODO handle RMI
        }
    }

    public HashMap<String, ClientConnection> getConnections() {
        return connections;
    }
}

