package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.messages.server.LobbyDoesNotExistMessage;
import it.polimi.ingsw.connections.messages.server.LobbyExistsMessage;
import it.polimi.ingsw.connections.messages.server.ValidUsernameMessage;
import it.polimi.ingsw.controller.CardInfo;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;
import it.polimi.ingsw.model.goals.Goal;

import javax.print.attribute.IntegerSyntax;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionBridge {

    private final ServerController controller;

    public ConnectionBridge(ServerController controller) {
        this.controller = controller;
    }

    // Client -> Server methods

    public Object getLobbies(String username) {
        ArrayList<String> idList = controller.getLobbies(username);

        if (controller.getConnections().get(username) instanceof SocketClientConnection) {

            if (!idList.isEmpty()) {
                try {
                    ((SocketClientConnection) controller.getConnections().get(username)).lobbyExists(idList);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    ((SocketClientConnection) controller.getConnections().get(username)).lobbyDoesNotExist();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        } else {
            // TODO: handle RMI
            return null;
        }
    }

    public Object addPlayerToLobby(String username, String lobbyId) {
        int result = controller.addPlayerToLobby(username, lobbyId);
        if (controller.getConnections().get(username) instanceof SocketClientConnection) {
            switch (result) {
                case 0:
                    try {
                        ((SocketClientConnection) controller.getConnections().get(username)).lobbyDoesNotExist();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 1:
                    for (String u : controller.getUserToLobby().keySet()) {
                        if (controller.getUserToLobby().get(u).equals(lobbyId) && u.equals(username)) {
                            try {
                                ((SocketClientConnection) controller.getConnections().get(u)).joinLobbySuccess();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (controller.getUserToLobby().get(u).equals(lobbyId) && !u.equals(username)) {
                            try {
                                ((SocketClientConnection) controller.getConnections().get(u)).playerJoined(username);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                case 2:
                    try {
                        ((SocketClientConnection) controller.getConnections().get(username)).lobbyFull();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
            return null;
        } else {
            // TODO: handle RMI
            return null;
        }
    }

    public int choosePrivateGoal(String username, int index) {
        int result = controller.choosePrivateGoal(username, index);
        if (controller.getConnections().get(username) instanceof SocketClientConnection) {
            if (result == 1) {
                try {
                    ((SocketClientConnection) controller.getConnections().get(username)).privateGoalChosen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return result;
        } else {
            return result;
            // TODO: handle RMI
        }
    }

    public ArrayList<Object> initTurn(String username) {
        ArrayList<Object> result = controller.initTurn(username);
        if (controller.getConnections().get(username) instanceof SocketClientConnection) {
            if (result != null) {
                try {
                    ((SocketClientConnection) controller.getConnections().get(username)).initTurn((ArrayList<CardInfo>) result.get(0), (ArrayList<CardInfo>) result.get(1), (ArrayList<CardInfo>) result.get(2), (ArrayList<Point>) result.get(3), (Integer) result.get(4), (Boolean) result.get(5), (ArrayList<CardInfo>) result.get(5));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return result;
        } else {
            return result;
            // TODO: handle RMI
        }
    }

    public ArrayList<Integer> placeCard(String username, String cardId, Point position, boolean flipped) {
        try {
            ArrayList<Integer> result = controller.placeCard(username, cardId, position, flipped);
            if (controller.getConnections().get(username) instanceof SocketClientConnection) {
                if (!result.isEmpty()) {
                    try {
                        ((SocketClientConnection) controller.getConnections().get(username)).placeCardSuccess(result.get(0), result.get(1));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                return result;
                // TODO: handle RMI
            }

        } catch (RequirementsNotSatisfied | InvalidPositionException e) {
            if (controller.getConnections().get(username) instanceof SocketClientConnection) {
                try {
                    ((SocketClientConnection) controller.getConnections().get(username)).placeCardFailure();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
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
        if (controller.getConnections().get(username) instanceof SocketClientConnection) {
            if (result != null) {
                try {
                    ((SocketClientConnection) controller.getConnections().get(username)).drawSuccess(result);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
        if (controller.getConnections().get(username) instanceof SocketClientConnection) {
            if (result != null) {
                try {
                    ((SocketClientConnection) controller.getConnections().get(username)).drawSuccess(result);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            return result;
            // TODO: handle RMI
        }
        return null;
    }

    public ArrayList<Object> endTurn(String username) {
        ArrayList<Object> result = controller.endTurn(username);
        if (controller.getConnections().get(username) instanceof SocketClientConnection) {
            for (String user : controller.getConnections().keySet()) {
                if (controller.getUserToGame().get(user).equals(controller.getUserToGame().get(username))) {
                    try {
                        ((SocketClientConnection) controller.getConnections().get(user)).sendStatus((ArrayList<CardInfo>) result.get(0), (ArrayList<CardInfo>) result.get(1), (ArrayList<CardInfo>) result.get(2), (Integer) result.get(3));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            return result;
            // TODO: handle RMI
        }
        return result;
    }

    public String createLobby(String username, int numPlayers) {
        String lobbyId = controller.createLobby(username, numPlayers);
        if (controller.getConnections().get(username) instanceof SocketClientConnection){
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

    public void initTurnAck(String username) {
    }

    // Server -> Client communications

    public void endGame(String username, HashMap<String, Integer> leaderboard) {
        if (controller.getConnections().get(username) instanceof SocketClientConnection) {
            try {
                ((SocketClientConnection) controller.getConnections().get(username)).gameEnded(leaderboard);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO: handle RMI
        }
    }

    public void createGame(String username) {
        if (controller.getConnections().get(username) instanceof SocketClientConnection) {
            try {
                ((SocketClientConnection) controller.getConnections().get(username)).gameStarted();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO handle RMI
        }
    }

    public void validUsername(ClientConnection connection) {
        if (connection instanceof SocketClientConnection) {
            try {
                ((SocketClientConnection) connection).validUsername();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO handle RMI
        }
    }

    public void invalidUsername(ClientConnection connection) {
        if (connection instanceof SocketClientConnection) {
            try {
                ((SocketClientConnection) connection).invalidUsername();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO handle RMI
        }
    }

}


