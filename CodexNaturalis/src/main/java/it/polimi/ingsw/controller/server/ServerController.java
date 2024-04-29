package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.controller.CardInfo;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.exceptions.DeckInitializationException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that manages the server
 */
public class ServerController {
    private GameController gameController;
    private LobbyController lobbyController;
    private HashMap<String, ClientConnection> connections;
    private HashMap<String, String> userToLobby;

    private HashMap<String, String> userToGame;

    /**
     * Default constructor
     */
    public ServerController() {
        connections = new HashMap<String,ClientConnection>();
        userToLobby = new HashMap<String,String>();
        userToGame = new HashMap<String,String>();
        lobbyController = new LobbyController();
        gameController = new GameController();
    }

    /**
     * Add a connection to the list of connections
     * @param connection the connection to add
     * @param username the username of the connection
     */
    public void addConnection(ClientConnection connection, String username){
        if(connections.containsKey(username) && !connections.get(username).isAlive())
            connections.replace(username, connection);
        else if(connections.containsKey(username))
            connection.invalidUsername();
        else{
            connections.put(username, connection);
            connection.validUsername();
        }
    }


    /**
     * Create a new game with the specified users
     * @param users the list of users
     */
    private void createGame(ArrayList<String> users) {
        String gameId = "" + (int) (Math.random() * 1000);
        try {
            gameController.createGame(gameId,users);
            for (String username : users) {
                userToGame.put(username, gameId);
                userToLobby.remove(username);
                connections.get(username).gameStarted();
            }
            gameController.startGame(gameId);
            //TODO:aggiunger il ritorno della hand iniziale e la starter card e il privateGoal

        } catch (DeckInitializationException e) {
            //for(String username : users)
                //connections.get(username).sendNotification("Deck initialization error!");
        } catch (InvalidNumberOfPlayersException e) {
            //for (String username : users)
                //connections.get(username).sendNotification("Invalid number of players!");
        }
    }

    /**
     * Initialize the turn of the player and send the status to the client
     * @param user the username of the player
     */
    public void initTurn(String user){
        if(checkUserCurrentPlayer(user)) {
            ArrayList<CardInfo> hand = gameController.getHand(userToGame.get(user));
            ArrayList<CardInfo> rd = gameController.getResourceDeck(userToGame.get(user));
            ArrayList<CardInfo> gd = gameController.getGoldDeck(userToGame.get(user));
            ArrayList<Point> availablePositions = gameController.getAvailablePositions(userToGame.get(user));
            int currTurn = gameController.getCurrentTurn(userToGame.get(user));
            boolean isLastTurn = gameController.isLast(userToGame.get(user));
            ArrayList<CardInfo> board = gameController.getUserBoard(userToGame.get(user));
            connections.get(user).initTurn(hand, rd, gd, availablePositions, currTurn, isLastTurn, board);
        }

    }

    public void initTurnAck(String user){
        //TODO: add the timer to reset when this ack is received, bat there is the ping (to manage)
    }

    /**
     * let the player choose its private goal
     * @param user the username of the player
     * @param index the index of the private goal to choose
     */
    public void choosePrivateGoal(String user,int index){
        if(checkUserCurrentPlayer(user)) {
            gameController.choosePrivateGoal(userToGame.get(user), index);
            connections.get(user).privateGoalChosen();
        }
    }

    /**
     * Place a card on the player's board
     * @param user the username of the player
     * @param cardId the id of the card to place
     * @param position the position to place the card
     */
    public void placeCard(String user, String cardId, Point position, boolean flipped){
        if(checkUserCurrentPlayer(user)) {
            try {
                Card card = gameController.getCard(userToGame.get(user), cardId);
                card.setFlipped(flipped);
                gameController.placeCard(userToGame.get(user), card, position);
                int cardsPoints = gameController.getUserCardsPoints(userToGame.get(user));
                int GoalsPoints = gameController.getUserGoalsPoints(userToGame.get(user));
                connections.get(user).placeCardSuccess(cardsPoints, GoalsPoints);
            } catch (RequirementsNotSatisfied | InvalidPositionException e) {
                connections.get(user).placeCardFailure();
            }
        }
    }

    /**
     * Draw a resource from the deck and return the new hand
     * @param user the username of the player
     * @param index the index of the resource to draw
     */
    public void drawResource(String user, int index){
        if(checkUserCurrentPlayer(user)) {
            gameController.drawResource(userToGame.get(user), index);
            connections.get(user).drawSuccess(gameController.getHand(userToGame.get(user)));
        }
    }

    /**
     * Draw a gold from the deck and return the new hand
     * @param user the username of the player
     * @param index the index of the gold to draw
     */
    public void drawGold(String user, int index){
        if(checkUserCurrentPlayer(user)) {
            gameController.drawGold(userToGame.get(user), index);
            connections.get(user).drawSuccess(gameController.getHand(userToGame.get(user)));
        }
    }

    /**
     * End the turn of the player and send the new status to all the players in the lobby
     * @param user the username of the player
     */
    public void endTurn(String user){
        if(checkUserCurrentPlayer(user)) {
            ArrayList<CardInfo> rd = gameController.getResourceDeck(userToGame.get(user));
            ArrayList<CardInfo> gd = gameController.getGoldDeck(userToGame.get(user));
            ArrayList<CardInfo> board = gameController.getUserBoard(userToGame.get(user));
            int points = gameController.getUserCardsPoints(userToGame.get(user));
            gameController.endTurn(userToGame.get(user));
            for (String username : connections.keySet()) {
                if (userToGame.get(user).equals(userToGame.get(username)))
                    connections.get(username).sendStatus(rd, gd, board, points);
            }
            if (gameController.isGameFinished(userToGame.get(user)))
                endGame(userToGame.get(user));
        }
    }


    /**
     * End the game and send a notification to all the players in the lobby
     * @param gameId the id of the game
     */
    private void endGame(String gameId){
        HashMap<String, Integer> leaderboard = gameController.endGame(gameId);
        for(String username : userToGame.keySet()){
            if(userToLobby.get(username).equals(userToLobby.get(username)))
                connections.get(username).gameEnded(leaderboard);
        }
    }

    /**
     * disconnect the player from the server
     * @param username the username of the player
     */
    public void disconnect(String username) {
        if (checkUserConnected(username)) {
            connections.get(username).close();
            connections.remove(username);
            userToLobby.remove(username);
        }

    }

    /**
     * Create a new lobby with the specified number of players and add the player to it
     * @param numPlayers the number of players in the lobby
     * @param username the username of the player
     */
    public void createLobby(String username, int numPlayers){
        if (checkUserConnected(username)) {
            //TODO: If the number of players is not between 2 and 4 return the error
            String lobbyId = "" + (int) (Math.random() * 1000);
            lobbyController.createNewLobby(lobbyId, numPlayers);
            addPlayerToLobby(username, lobbyId);
        }
    }


    /**
     * Add a player to a lobby
     * @param username the username of the player
     * @param lobbyId the id of the lobby
     */
    public void addPlayerToLobby(String username, String lobbyId) {
        if (checkUserConnected(username)) {
            if(!lobbyController.getLobbies().containsKey(lobbyId))
                connections.get(username).lobbyDoesNotExist();
            else if(lobbyController.getLobbies().get(lobbyId).isFull())
                connections.get(username).lobbyFull();
            else {
                lobbyController.addPlayer(username, lobbyId);
                userToLobby.put(username, lobbyId);
                for (String u : userToLobby.values()) {
                    if (userToLobby.get(u).equals(lobbyId) && u.equals(username))
                        connections.get(u).joinLobbySuccess();
                    else if (userToLobby.get(u).equals(lobbyId) && !u.equals(username))
                        connections.get(u).playerJoined(username);
                }
                if (lobbyController.getLobbies().get(lobbyId).isFull())
                    createGame(lobbyController.getLobbies().get(lobbyId).getUsers());
            }
        }
    }

    /**
     * send the lobbies to the client
     */
    public void getLobbies(String username) {
        if (checkUserConnected(username)){
            if (lobbyController.getLobbies().isEmpty())
                connections.get(username).lobbyDoesNotExist();
            else
                connections.get(username).sendLobbies(lobbyController.getLobbies().keySet());
            //the lobbies can't be full, when a lobby is full a game is created and the lobby is removed
        }
    }


    /**
     * @param username the username of the player
     * @return true if the player is the current player of the game, false otherwise
     */
    private boolean checkUserCurrentPlayer(String username){
        return username.equals(gameController.getCurrentPlayer(userToGame.get(username)));
    }

    /**
     * @param username the username of the player
     * @return true if the player is registered in the server, false otherwise
     */
    private boolean checkUserConnected(String username){
        return connections.containsKey(username);
    }

    //TODO
//  public void onClientDisconnect(){
//
//  }
}
