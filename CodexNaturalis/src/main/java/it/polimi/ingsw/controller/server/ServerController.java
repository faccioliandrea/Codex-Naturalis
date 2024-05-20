package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.connections.enums.AddPlayerToLobbyresponse;
import it.polimi.ingsw.connections.enums.ChooseStarterCardSideResponse;
import it.polimi.ingsw.connections.server.ConnectionBridge;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.model.exceptions.DeckInitializationException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.exceptions.RequirementsNotSatisfied;
import it.polimi.ingsw.model.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Class that manages the server
 */
public class ServerController {

    private GameController gameController;
    private LobbyController lobbyController;
    private HashMap<String, String> userToLobby;
    private HashMap<String, String> userToGame;
    private ConnectionBridge connectionBridge;
    private ExecutorService executorService;

    /**
     * Default constructor
     */
    public ServerController() {
        userToLobby = new HashMap<String,String>();
        userToGame = new HashMap<String,String>();
        lobbyController = new LobbyController();
        gameController = new GameController();
        connectionBridge = new ConnectionBridge(this);
    }


    /**
     * Create a new game with the specified users
     * @param users the list of users
     */
    public void createGame(ArrayList<String> users) {
        String gameId = "" + (int) (Math.random() * 1000);
        String lobbyId = userToLobby.get(users.get(0));
        try {
            gameController.createGame(gameId,users);
            for (String username : users) {
                userToGame.put(username, gameId);
                userToLobby.remove(username);
            }
            lobbyController.removeLobby(lobbyId);
            gameController.startGame(gameId);
            executorService = Executors.newFixedThreadPool(users.size());
            for (String username : users) {
                ArrayList<CardInfo> hand = gameController.getHand(gameId, username);
                ArrayList<GoalInfo> privateGoals = gameController.getPrivateGoals(gameId, username);
                ArrayList<GoalInfo> sharedGoals = gameController.getSharedGoals(gameId);
                Map<String, PlayerColor> playerColors = gameController.getGamePlayers(gameId).stream().collect(Collectors.toMap(Player::getUsername, Player::getPlayerColor, (x, y) -> x, HashMap::new));
                CardInfo starterCard = gameController.getStarterCard(gameId, username);
                executorService.submit(() -> connectionBridge.gameCreated(username, new StarterData(hand, privateGoals, sharedGoals, starterCard, users, playerColors)));
            }


        } catch (DeckInitializationException e) {
            // for(String username : users)
                // connections.get(username).sendNotification("Deck initialization error!");
        } catch (InvalidNumberOfPlayersException e) {
            // for (String username : users)
                // connections.get(username).sendNotification("Invalid number of players!");
        }
    }

    /**
     * Initialize the turn of the player and send the status to the client
     * @param user the username of the player
     */
    public void initTurn(String user){
        if(checkUserCurrentPlayer(user)) {
            if(connectionBridge.checkUserConnected(user)){
                ArrayList<CardInfo> hand = gameController.getHand(userToGame.get(user), user);
                ArrayList<CardInfo> rd = gameController.getResourceDeck(userToGame.get(user));
                ArrayList<CardInfo> gd = gameController.getGoldDeck(userToGame.get(user));
                ArrayList<Point> availablePositions = gameController.getAvailablePositions(userToGame.get(user), user);
                Map<CardSymbol, Integer> symbols = gameController.getUserSymbols(userToGame.get(user));
                int currTurn = gameController.getCurrentTurn(userToGame.get(user));
                boolean isLastTurn = gameController.isLast(userToGame.get(user));
                ArrayList<CardInfo> board = gameController.getUserBoard(userToGame.get(user));
                TurnInfo turnInfo = new TurnInfo(hand, rd, gd, availablePositions, currTurn, (HashMap<CardSymbol, Integer>) symbols, isLastTurn, board);
                for (Player username : gameController.getGames().get(userToGame.get(user)).getPlayers()) {
                    if (!username.getUsername().equals(user)) {
                        connectionBridge.otherPlayerTurn(username.getUsername(), user);
                    }
                }
                connectionBridge.initTurn(user, turnInfo);
            } else {
                gameController.endTurn(userToGame.get(user));
                gameController.getGames().get(userToGame.get(user)).getGameModel().setTotalTurns(gameController.getGames().get(userToGame.get(user)).getGameModel().getTotalTurns()-1);
                initTurn(gameController.getCurrentPlayer(userToGame.get(user)));
            }
        }
    }

    public void initTurnAck(String user){
        //TODO: add the timer to reset when this ack is received, but there is the ping (to manage)
    }

    /**
     * let the player choose its private goal
     * @param username the username of the player
     * @param index the index of the private goal to choose
     */
    public int choosePrivateGoal(String username,int index){
        gameController.choosePrivateGoal(userToGame.get(username), username,  index);
        return 1;

    }

    /**
     * Handle the choice of the starter card side
     * @param username the username of the player
     * @param flipped side of the starter card
     */
    public ChooseStarterCardSideResponse chooseStarterCardSide(String username, boolean flipped) {
        gameController.chooseStarterCardSide(userToGame.get(username), username, flipped);
        if(gameController.getGames().get(userToGame.get(username)).getPlayers().stream().anyMatch(x->x.getBoard().getPlayedCards().isEmpty())){
            return ChooseStarterCardSideResponse.WAIT_FOR_OTHER_PLAYER;
        } else if (gameController.getGames().get(userToGame.get(username)).getPlayers().stream().allMatch(x->x.getBoard().getPlayedCards().size()==1)){
            return ChooseStarterCardSideResponse.SUCCESS;
        }
        return ChooseStarterCardSideResponse.FAILURE;

    }

    /**
     * Place a card on the player's board
     * @param user the username of the player
     * @param cardId the id of the card to place
     * @param position the position to place the card
     */
    public PlaceCardSuccessInfo placeCard(String user, String cardId, Point position, boolean flipped) throws RequirementsNotSatisfied, InvalidPositionException{

        if(checkUserCurrentPlayer(user)) {
            Card card = gameController.getCardFromHand(userToGame.get(user), cardId);
            card.setFlipped(flipped);
            gameController.placeCard(userToGame.get(user), card, position);
            int cardsPoints = gameController.getUserCardsPoints(userToGame.get(user));
            int GoalsPoints = gameController.getUserGoalsPoints(userToGame.get(user));
            ArrayList<Point> availablePositions = gameController.getAvailablePositions(userToGame.get(user), user);
            Map<CardSymbol, Integer> symbols = gameController.getUserSymbols(userToGame.get(user));
            return new PlaceCardSuccessInfo(cardsPoints, GoalsPoints, new CardInfo(card), availablePositions, symbols);
        }
        return null;
    }

    /**
     * Draw a resource from the deck and return the new hand
     * @param user the username of the player
     * @param index the index of the resource to draw
     */
    public ArrayList<CardInfo> drawResource(String user, int index){

        //TODO check if deck is empty
        if(checkUserCurrentPlayer(user)) {
            gameController.drawResource(userToGame.get(user), index);
            return gameController.getHand(userToGame.get(user), user);
        }
        return null;
    }

    /**
     * Draw a gold from the deck and return the new hand
     * @param user the username of the player
     * @param index the index of the gold to draw
     */
    public ArrayList<CardInfo> drawGold(String user, int index){
        //TODO check if deck is empty
        if(checkUserCurrentPlayer(user)) {
            gameController.drawGold(userToGame.get(user), index);
            return gameController.getHand(userToGame.get(user), user);
        }
        return null;
    }

    /**
     * End the turn of the player and send the new status to all the players in the lobby
     * @param user the username of the player
     */
    public void endTurn(String user){
        if(checkUserCurrentPlayer(user)) {
            ArrayList<CardInfo> rd = gameController.getResourceDeck(userToGame.get(user));
            ArrayList<CardInfo> gd = gameController.getGoldDeck(userToGame.get(user));
            HashMap<String, Integer>  leaderboard= gameController.getLeaderboard(userToGame.get(user));
            Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
            for (Player player : gameController.getGames().get(userToGame.get(user)).getPlayers()) {
                connectionStatus.put(player.getUsername(), connectionBridge.getConnectionsStatus().get(connectionBridge.getConnections().get(player.getUsername())));

                GameStateInfo gameState = new GameStateInfo(
                        player.getUsername(),
                        gameController.getCurrentPlayer(userToGame.get(user)),
                        user,
                        gameController.getGamePlayers(userToGame.get(user)).stream().collect(Collectors.toMap(Player::getUsername, Player::getPlayerColor, (x, y) -> x, HashMap::new)),
                        gameController.getHand(userToGame.get(player.getUsername()), user),
                        rd,
                        gd,
                        gameController.getAvailablePositions(userToGame.get(user), user),
                        gameController.getCurrentTurn(userToGame.get(user)),
                        gameController.isLast(userToGame.get(user)),
                        gameController.getUserBoardByUsername(userToGame.get(user), player.getUsername()),
                        gameController.getUserSymbolsByUsername(userToGame.get(user), player.getUsername()),
                        leaderboard,
                        gameController.getBoards(userToGame.get(user)),
                        connectionStatus,
                        gameController.getSharedGoals(userToGame.get(user)),
                        gameController.getPrivateGoal(userToGame.get(user), player.getUsername()),
                        gameController.getUserCardsPointsByUsername(userToGame.get(user), player.getUsername()),
                        gameController.getUserGoalsPointsByUsername(userToGame.get(user), player.getUsername()),
                        gameController.isGameFinished(userToGame.get(user))
                );

                connectionBridge.gameState(player.getUsername(), gameState);

            }

            if (gameController.isGameFinished(userToGame.get(user)))
                endGame(userToGame.get(user));
            else {
                gameController.endTurn(userToGame.get(user));
                initTurn(gameController.getCurrentPlayer(userToGame.get(user)));
            }


        }

    }


    /**
     * End the game and send a notification to all the players in the lobby
     * @param gameId the id of the game
     */
    private void endGame(String gameId){
        HashMap<String, Integer> leaderboard = gameController.getFullLeaderboard(gameId);
        for(String username : userToGame.keySet()){
            if(userToGame.get(username).equals(userToGame.get(username))) {
                executorService.submit(() -> connectionBridge.endGame(username, leaderboard));
                //connectionBridge.endGame(username, leaderboard);
            }
        }
        destroyGame(gameId);
    }

    private void destroyGame(String gameId){
        List<String> names = userToGame.keySet().stream().filter(username -> userToGame.get(username).equals(gameId)).collect(Collectors.toList());
        names.forEach(username -> userToGame.remove(username));
        gameController.getGames().remove(gameId);
    }


    /**
     * Create a new lobby with the specified number of players and add the player to it
     * @param numPlayers the number of players in the lobby
     * @param username the username of the player
     */
    public String createLobby(String username, int numPlayers){
        if (connectionBridge.checkUserConnected(username)) {
            String lobbyId = "" + (int) (Math.random() * 1000);
            lobbyController.createNewLobby(lobbyId, numPlayers);
            return lobbyId;

        }
        return null;
    }


    /**
     * Handle player disconnection
     * @param username the username of the player
     */
    public void playerDisconnected(String username){
        if(userToGame.containsKey(username)) {
            for (String u : userToGame.keySet()) {
                if (userToGame.get(u).equals(userToGame.get(username)) && !u.equals(username)) {
                    connectionBridge.playerDisconnected(username, u, true);
                }
            }
            if(gameController.getUserBoardByUsername(userToGame.get(username), username).isEmpty() && !gameController.getHand(userToGame.get(username), username).isEmpty()){
                // TODO: disconnection before private goals and starter card choice -> end game and remove all players!
                endGame(userToGame.get(username));

            } else if( gameController.getCurrentPlayer(userToGame.get(username)).equals(username)){
                if(gameController.getHand(userToGame.get(username), username).size()!=3){
                    //disconnection during the turn before draw -> draw a resource and end the turn
                    gameController.drawResource(userToGame.get(username), 0);
                }
                endTurn(username);
            }

        } else if (userToLobby.containsKey(username)) {
            for (String u : userToLobby.keySet()) {
                if (userToLobby.get(u).equals(userToLobby.get(username)) && !u.equals(username)) {
                    connectionBridge.playerDisconnected(username, u, false);
                }
            }
            lobbyController.removePlayer(username, userToLobby.get(username));
            userToLobby.remove(username);
        }
    }

    /**
     * Handle player reconnection
     * @param username the username of the player
     */
    public void playerReconnected(String username){
        if(userToGame.containsKey(username)) {
            for (String u : userToGame.keySet()) {
                if (userToGame.get(u).equals(userToGame.get(username)) && !u.equals(username)) {
                    connectionBridge.playerReconnected(username, u);
                } else if (userToGame.get(u).equals(userToGame.get(username)) && u.equals(username)) {
                    ArrayList<CardInfo> rd = gameController.getResourceDeck(userToGame.get(username));
                    ArrayList<CardInfo> gd = gameController.getGoldDeck(userToGame.get(username));
                    HashMap<String, Integer>  leaderboard= gameController.getLeaderboard(userToGame.get(username));
                    Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
                    for (Player player : gameController.getGames().get(userToGame.get(username)).getPlayers()) {
                        connectionStatus.put(player.getUsername(), connectionBridge.getConnectionsStatus().get(connectionBridge.getConnections().get(player.getUsername())));
                    }
                    GameStateInfo gameState = new GameStateInfo(
                            username,
                            gameController.getCurrentPlayer(userToGame.get(username)),
                            null,
                            gameController.getGamePlayers(userToGame.get(username)).stream().collect(Collectors.toMap(Player::getUsername, Player::getPlayerColor, (x, y) -> x, HashMap::new)),
                            gameController.getHand(userToGame.get(username), username),
                            rd,
                            gd,
                            gameController.getAvailablePositions(userToGame.get(username), username),
                            gameController.getCurrentTurn(userToGame.get(username)),
                            gameController.isLast(userToGame.get(username)),
                            gameController.getUserBoardByUsername(userToGame.get(username), username),
                            gameController.getUserSymbolsByUsername(userToGame.get(username), username),
                            leaderboard,
                            gameController.getBoards(userToGame.get(username)),
                            connectionStatus,
                            gameController.getSharedGoals(userToGame.get(username)),
                            gameController.getPrivateGoal(userToGame.get(username), username),
                            gameController.getUserCardsPointsByUsername(userToGame.get(username), username),
                            gameController.getUserGoalsPointsByUsername(userToGame.get(username), username),
                            gameController.isGameFinished(userToGame.get(username))
                    );
                    connectionBridge.reconnectionState(gameState);
                }
            }
        }
    }


    /**
     * Add a player to a lobby
     * @param username the username of the player
     * @param lobbyId the id of the lobby
     */
    public AddPlayerToLobbyresponse addPlayerToLobby(String username, String lobbyId) {
        if (connectionBridge.checkUserConnected(username)) {
            if(!lobbyController.getLobbies().containsKey(lobbyId)) {
                return AddPlayerToLobbyresponse.LOBBY_NOT_FOUND;
            }
            else if(lobbyController.getLobbies().get(lobbyId).isFull()) {
                return AddPlayerToLobbyresponse.LOBBY_FULL;
            }
            else {
                lobbyController.addPlayer(username, lobbyId);
                userToLobby.put(username, lobbyId);
                return AddPlayerToLobbyresponse.PLAYER_ADDED;
            }
        }
        return AddPlayerToLobbyresponse.PLAYER_NOT_CONNECTED;
    }

    /**
     * Get the lobbies available
     * @param username the username of the player
     * @return the list of lobbies available
     */
    public ArrayList<String> getLobbies(String username) {
        if (connectionBridge.checkUserConnected(username)){
            if (lobbyController.getLobbies().isEmpty()) {
                return new ArrayList<>();
            }
            else {
                return new ArrayList<String>(lobbyController.getLobbies().keySet());
            }
            //the lobbies can't be full, when a lobby is full a game is created and the lobby is removed
        }
        return new ArrayList<>();
    }


    /**
     * @param username the username of the player
     * @return true if the player is the current player of the game, false otherwise
     */
    private boolean checkUserCurrentPlayer(String username){
        return username.equals(gameController.getCurrentPlayer(userToGame.get(username)));
    }

    /**
     * Getter for the user to lobby map
     * @return the user to lobby map
     */
    public HashMap<String, String> getUserToLobby() {
        return userToLobby;
    }

    /**
     * Getter for the user to game map
     * @return the user to game map
     */
    public HashMap<String, String> getUserToGame() {
        return userToGame;
    }

    /**
     * Getter for the connection bridge
     * @return the connection bridge
     */
    public ConnectionBridge getConnectionBridge() {
        return connectionBridge;
    }

    /**
     * Remove a user from the lobby
     * @param username the username of the player
     */
    public void removeUser(String username){
        userToLobby.remove(username);
    }

    /**
     * Getter for the game controller
     * @return the game controller
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Getter for the lobby controller
     * @return the lobby controller
     */
    public LobbyController getLobbyController() {
        return lobbyController;
    }


}
