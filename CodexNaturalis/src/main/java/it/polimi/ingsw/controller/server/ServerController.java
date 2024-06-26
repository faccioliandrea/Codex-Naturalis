package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.chat.ServerChatHandler;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.connections.enums.AddPlayerToLobbyResponse;
import it.polimi.ingsw.connections.enums.ChooseStarterCardSideResponse;
import it.polimi.ingsw.connections.enums.LogInResponse;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Class that manages the server
 */
public class ServerController {
    private static ServerController instance;

    private final GameController gameController = GameController.getInstance();
    private final LobbyController lobbyController = LobbyController.getInstance();
    private final ConnectionBridge connectionBridge = ConnectionBridge.getInstance();
    private final HashMap<String, String> userToLobby = new HashMap<>();
    private final HashMap<String, String> userToGame = new HashMap<>();
    private final Map<String, TimerTask> onePlayer = new HashMap<>();
    private ExecutorService executorService;

    /**
     * Singleton instance getter
     * @return the instance of the ServerController
     */
    public static synchronized ServerController getInstance() {
        if (instance == null) {
            instance = new ServerController();
        }
        return instance;
    }

    /**
     * Default constructor
     */
    private ServerController() { }


    /**
     * Create a new game with the specified users
     * @param lobby Lobby object containing the players
     */
    public void createGame(Lobby lobby) {
        String gameId = "" + (int) (Math.random() * 1000);
        try {
            gameController.createGame(gameId,lobby);
            for (String username : lobby.getUsers()) {
                userToGame.put(username, gameId);
                userToLobby.remove(username);
            }
            lobbyController.removeLobby(lobby.getId());
            gameController.startGame(gameId);
            executorService = Executors.newFixedThreadPool(lobby.getUsers().size());
            for (String username : lobby.getUsers()) {
                ArrayList<CardInfo> hand = gameController.getHand(gameId, username);
                ArrayList<GoalInfo> privateGoals = gameController.getPrivateGoals(gameId, username);
                ArrayList<GoalInfo> sharedGoals = gameController.getSharedGoals(gameId);
                Map<String, PlayerColor> playerColors = gameController.getGamePlayers(gameId).stream().collect(Collectors.toMap(Player::getUsername, Player::getPlayerColor, (x, y) -> x, HashMap::new));
                CardInfo starterCard = gameController.getStarterCard(gameId, username);
                executorService.submit(() -> connectionBridge.gameCreated(username, new StarterData(hand, privateGoals, sharedGoals, starterCard, lobby.getUsers(), playerColors)));
            }

        } catch (DeckInitializationException | InvalidNumberOfPlayersException e) {
            System.err.println(e.getMessage());
            System.exit(0);
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
                TurnInfo turnInfo = new TurnInfo(hand, rd, gd, availablePositions, currTurn, symbols, isLastTurn, board);
                Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
                for (Player usern : gameController.getGames().get(userToGame.get(user)).getPlayers())
                    connectionStatus.put(usern.getUsername(), connectionBridge.getConnections().get(usern.getUsername()).getStatus());
                for (Player username : gameController.getGames().get(userToGame.get(user)).getPlayers()) {
                    if(gameController.getCurrentTurn(userToGame.get(user))==0){
                        connectionBridge.gameState(username.getUsername(), new GameStateInfo(
                                username.getUsername(),
                                gameController.getCurrentPlayer(userToGame.get(user)),
                                null,
                                gameController.getGamePlayers(userToGame.get(user)).stream().collect(Collectors.toMap(Player::getUsername, Player::getPlayerColor, (x, y) -> x, HashMap::new)),
                                gameController.getHand(userToGame.get(username.getUsername()), username.getUsername()),
                                rd,
                                gd,
                                gameController.getAvailablePositions(userToGame.get(user), username.getUsername()),
                                gameController.getCurrentTurn(userToGame.get(user)),
                                gameController.isLast(userToGame.get(user)),
                                gameController.getUserBoardByUsername(userToGame.get(user), username.getUsername()),
                                gameController.getUserSymbols(userToGame.get(user), username.getUsername()),
                                sortLeaderboard(gameController.getLeaderboard(userToGame.get(user))),
                                gameController.getBoards(userToGame.get(user)),
                                connectionStatus,
                                gameController.getSharedGoals(userToGame.get(user)),
                                gameController.getPrivateGoal(userToGame.get(user), username.getUsername()),
                                gameController.getUserCardsPoints(userToGame.get(user), username.getUsername()),
                                gameController.getUserGoalsPoints(userToGame.get(user), username.getUsername()),
                                gameController.isGameFinished(userToGame.get(user))
                        ));
                    }
                    if (!username.getUsername().equals(user)) {
                        connectionBridge.otherPlayerTurn(username.getUsername(), user);
                    }
                }
                executorService = Executors.newFixedThreadPool(1);
                executorService.submit(()->connectionBridge.initTurn(user, turnInfo));
            } else if(gameController.getGamePlayers(userToGame.get(user)).stream().filter(x -> connectionBridge.checkUserConnected(x.getUsername())).count()>1){
                gameController.endTurn(userToGame.get(user));
                gameController.getGames().get(userToGame.get(user)).getGameModel().setTotalTurns(gameController.getGames().get(userToGame.get(user)).getGameModel().getTotalTurns()+1);
                initTurn(gameController.getCurrentPlayer(userToGame.get(user)));
            }
        }
    }

    /**
     * Let the player choose its private goal
     * @param username the username of the player
     * @param index the index of the private goal to choose
     */
    public void choosePrivateGoal(String username,int index){
        gameController.choosePrivateGoal(userToGame.get(username), username,  index);
    }

    /**
     * Handle the choice of the starter card side
     * @param username the username of the player
     * @param flipped side of the starter card
     * @return the response of the operation
     */
    public ChooseStarterCardSideResponse chooseStarterCardSide(String username, boolean flipped) {
        gameController.chooseStarterCardSide(userToGame.get(username), username, flipped);
        if(gameController.getGames().get(userToGame.get(username)).getPlayers().stream().anyMatch(x->x.getBoard().getPlayedCards().isEmpty())){
            return ChooseStarterCardSideResponse.WAIT_FOR_OTHER_PLAYER;
        } else if (gameController.getGames().get(userToGame.get(username)).getPlayers().stream().allMatch(x->x.getBoard().getPlayedCards().size()==1)){
            String currentPlayer = ServerController.getInstance().getGameController().getCurrentPlayer(ServerController.getInstance().getUserToGame().get(username));
            initTurn(currentPlayer);
            return ChooseStarterCardSideResponse.SUCCESS;
        }
        return ChooseStarterCardSideResponse.FAILURE;

    }

    /**
     * Place a card on the player's board
     * @param user the username of the player
     * @param cardId the id of the card to place
     * @param position the position to place the card
     * @return the placeCardSuccessInfo
     */
    public PlaceCardSuccessInfo placeCard(String user, String cardId, Point position, boolean flipped) throws RequirementsNotSatisfied, InvalidPositionException{

        if(gameController.getGames().get(userToGame.get(user))!=null && checkUserCurrentPlayer(user) ) {
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
     * @return the new hand
     */
    public ArrayList<CardInfo> drawResource(String user, int index){
        if(gameController.getGames().get(userToGame.get(user))!=null && checkUserCurrentPlayer(user) ) {
            gameController.drawResource(userToGame.get(user), index);
            return gameController.getHand(userToGame.get(user), user);
        }
        return null;
    }

    /**
     * Draw a gold from the deck and return the new hand
     * @param user the username of the player
     * @param index the index of the gold to draw
     * @return the new hand
     */
    public ArrayList<CardInfo> drawGold(String user, int index){
        if( gameController.getGames().get(userToGame.get(user))!=null && checkUserCurrentPlayer(user) ) {
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
        if(gameController.getGames().get(userToGame.get(user))!=null && checkUserCurrentPlayer(user) ) {
            ArrayList<CardInfo> rd = gameController.getResourceDeck(userToGame.get(user));
            ArrayList<CardInfo> gd = gameController.getGoldDeck(userToGame.get(user));
            Map<String, Integer>  leaderboard= sortLeaderboard(gameController.getLeaderboard(userToGame.get(user)));
            Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
            for (Player player : gameController.getGames().get(userToGame.get(user)).getPlayers())
                connectionStatus.put(player.getUsername(), connectionBridge.getConnections().get(player.getUsername()).getStatus());
            for (Player player : gameController.getGames().get(userToGame.get(user)).getPlayers()) {

                GameStateInfo gameState = new GameStateInfo(
                        player.getUsername(),
                        gameController.getCurrentPlayer(userToGame.get(user)),
                        user,
                        gameController.getGamePlayers(userToGame.get(user)).stream().collect(Collectors.toMap(Player::getUsername, Player::getPlayerColor, (x, y) -> x, HashMap::new)),
                        gameController.getHand(userToGame.get(player.getUsername()), player.getUsername()),
                        rd,
                        gd,
                        gameController.getAvailablePositions(userToGame.get(user), player.getUsername()),
                        gameController.getCurrentTurn(userToGame.get(user)),
                        gameController.isLast(userToGame.get(user)),
                        gameController.getUserBoardByUsername(userToGame.get(user), player.getUsername()),
                        gameController.getUserSymbols(userToGame.get(user), player.getUsername()),
                        leaderboard,
                        gameController.getBoards(userToGame.get(user)),
                        connectionStatus,
                        gameController.getSharedGoals(userToGame.get(user)),
                        gameController.getPrivateGoal(userToGame.get(user), player.getUsername()),
                        gameController.getUserCardsPoints(userToGame.get(user), player.getUsername()),
                        gameController.getUserGoalsPoints(userToGame.get(user), player.getUsername()),
                        gameController.isGameFinished(userToGame.get(user))
                );
                connectionBridge.gameState(player.getUsername(), gameState);
            }

            if (gameController.isGameFinished(userToGame.get(user)))
                endGame(userToGame.get(user), null);
            else {
                gameController.endTurn(userToGame.get(user));
                initTurn(gameController.getCurrentPlayer(userToGame.get(user)));
            }


        }

    }

    /**
     * End the game and send a notification to all the players in the lobby
     * @param gameId the id of the game
     * @param forceWinner username of the player that is selected as winner
     */
    private void endGame(String gameId, String forceWinner){
        Map<String, Integer> leaderboard = gameController.getFullLeaderboard(gameId);

        if(forceWinner!= null){
            for(String username : leaderboard.keySet()){
                if(!username.equals(forceWinner)){
                    leaderboard.replace(username, 0);
                } else {
                    leaderboard.replace(username,20);
                }
            }
        }
        leaderboard = sortLeaderboard(leaderboard);
        executorService = Executors.newFixedThreadPool(gameController.getGamePlayers(gameId).size());
        for(String username : userToGame.keySet()){
            if(userToGame.get(username).equals(gameId)) {
                Map<String, Integer> finalLeaderboard = leaderboard;
                executorService.submit(()-> connectionBridge.endGame(username, finalLeaderboard));
            }
        }
        destroyGame(gameId);
    }

    private void destroyGame(String gameId){
        List<String> names = userToGame.keySet().stream().filter(username -> userToGame.get(username).equals(gameId)).collect(Collectors.toList());
        names.forEach(userToGame::remove);
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
            if(gameController.getGamePlayers(userToGame.get(username)).stream().noneMatch(x -> connectionBridge.checkUserConnected(x.getUsername()))){
                System.out.println("0 player connected, game destroyed.");
                if(onePlayer.containsKey(userToGame.get(username))){
                    onePlayer.get(userToGame.get(username)).cancel();
                    onePlayer.remove(userToGame.get(username));
                }
                destroyGame(userToGame.get(username));
            } else{
                if(gameController.getUserBoardByUsername(userToGame.get(username), username).isEmpty() && !gameController.getHand(userToGame.get(username), username).isEmpty()){
                    if(gameController.getPrivateGoals(userToGame.get(username), username).size()==2) {
                        choosePrivateGoal( username, 0);
                    }
                    chooseStarterCardSide(username, false);
                } else if( gameController.getCurrentPlayer(userToGame.get(username)).equals(username)){
                    if(gameController.getHand(userToGame.get(username), username).size()!=3){
                        //disconnection during the turn before draw -> draw a resource and end the turn
                        if(!gameController.getResourceDeck(userToGame.get(username)).isEmpty())
                            gameController.drawResource(userToGame.get(username), 0);
                        else if(!gameController.getGoldDeck(userToGame.get(username)).isEmpty())
                            gameController.drawGold(userToGame.get(username), 0);
                    }

                    if(gameController.getGamePlayers(userToGame.get(username)).stream().anyMatch(x -> connectionBridge.checkUserConnected(x.getUsername()))){
                        endTurn(username);
                    }
                }

                if(gameController.getGamePlayers(userToGame.get(username)).stream().noneMatch(x -> connectionBridge.checkUserConnected(x.getUsername()))){
                    System.out.println("0 player connected, game destroyed.");
                    if(onePlayer.containsKey(userToGame.get(username))){
                        onePlayer.get(userToGame.get(username)).cancel();
                        onePlayer.remove(userToGame.get(username));
                    }
                    destroyGame(userToGame.get(username));
                } else if(gameController.getGamePlayers(userToGame.get(username)).stream().filter(x-> connectionBridge.checkUserConnected(x.getUsername())).count()==1){
                    System.out.println("Only 1 player connected, game will finish in 1 minute.");
                    Optional<Player> optionalPlayer = gameController.getGamePlayers(userToGame.get(username)).stream().filter(x-> connectionBridge.checkUserConnected(x.getUsername())).findFirst();
                    if(!optionalPlayer.isPresent()){
                        throw new NullPointerException();
                    }
                    for (String u : userToGame.keySet()) {
                        if (userToGame.get(u).equals(userToGame.get(username)) && !u.equals(username)) {
                            connectionBridge.playerDisconnected(username, u, true);
                        }
                    }
                    connectionBridge.noOtherPlayerConnected(optionalPlayer.get().getUsername());
                    Timer timer = new Timer();
                    TimerTask task1 = new TimerTask() {
                        @Override
                        public void run() {
                            Optional<Player> optionalPlayer = gameController.getGamePlayers(userToGame.get(username)).stream().filter(x-> connectionBridge.checkUserConnected(x.getUsername())).findFirst();
                            if(!optionalPlayer.isPresent()){
                                throw new NullPointerException();
                            }
                            endGame(userToGame.get(username), optionalPlayer.get().getUsername());
                        }
                    };
                    onePlayer.put(userToGame.get(username), task1);
                    timer.schedule(task1, 60 * 1000);
                } else {
                    for (String u : userToGame.keySet()) {
                        if (userToGame.get(u).equals(userToGame.get(username)) && !u.equals(username)) {
                            connectionBridge.playerDisconnected(username, u, true);
                        }
                    }
                }
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
    public LogInResponse playerReconnected(String username){
        LogInResponse response;
        if(userToGame.containsKey(username)) {
            if(gameController.getGamePlayers(userToGame.get(username)).stream().filter(x->connectionBridge.checkUserConnected(x.getUsername())).count()<=2){
                if(onePlayer.containsKey(userToGame.get(username))){
                    System.out.println("Another player reconnected, game will continue.");
                    onePlayer.get(userToGame.get(username)).cancel();
                    onePlayer.remove(userToGame.get(username));
                }
            }
            for (String u : userToGame.keySet()) {
                if (userToGame.get(u).equals(userToGame.get(username)) && !u.equals(username)) {
                    connectionBridge.playerReconnected(username, u);
                } else if (userToGame.get(u).equals(userToGame.get(username)) && u.equals(username)) {
                    ArrayList<CardInfo> rd = gameController.getResourceDeck(userToGame.get(username));
                    ArrayList<CardInfo> gd = gameController.getGoldDeck(userToGame.get(username));
                    Map<String, Integer>  leaderboard= sortLeaderboard(gameController.getLeaderboard(userToGame.get(username)));
                    Map<String, ConnectionStatus> connectionStatus = new HashMap<>();
                    for (Player player : gameController.getGames().get(userToGame.get(username)).getPlayers()) {
                        connectionStatus.put(player.getUsername(), connectionBridge.getConnections().get(player.getUsername()).getStatus());
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
                            gameController.getUserSymbols(userToGame.get(username), username),
                            leaderboard,
                            gameController.getBoards(userToGame.get(username)),
                            connectionStatus,
                            gameController.getSharedGoals(userToGame.get(username)),
                            gameController.getPrivateGoal(userToGame.get(username), username),
                            gameController.getUserCardsPoints(userToGame.get(username), username),
                            gameController.getUserGoalsPoints(userToGame.get(username), username),
                            gameController.isGameFinished(userToGame.get(username))
                    );
                    if(!gameController.getCurrentPlayer(userToGame.get(username)).equals(username) && gameController.getGamePlayers(userToGame.get(username)).stream().filter(x->connectionBridge.checkUserConnected(x.getUsername())).count()==1){
                        gameController.endTurn(userToGame.get(username));
                        gameController.getGames().get(userToGame.get(username)).getGameModel().setTotalTurns(gameController.getGames().get(userToGame.get(username)).getGameModel().getTotalTurns()+1);
                        initTurn(gameController.getCurrentPlayer(userToGame.get(username)));
                    }
                    connectionBridge.reconnectionState(gameState);
                }
            }
            response = LogInResponse.RECONNECT;
        } else {
            connectionBridge.validUsername(connectionBridge.getConnections().get(username));
            response = LogInResponse.LOGGED_IN;
        }
        return response;
    }

    /**
     * Add a player to a lobby
     * @param username the username of the player
     * @param lobbyId the id of the lobby
     * @return the response of the operation
     */
    public AddPlayerToLobbyResponse addPlayerToLobby(String username, String lobbyId) {
        if (connectionBridge.checkUserConnected(username)) {
            if(lobbyId.equals("1001")) {
                return AddPlayerToLobbyResponse.REFRESH_LOBBIES;
            } else if(!lobbyController.getLobbies().containsKey(lobbyId)) {
                return AddPlayerToLobbyResponse.LOBBY_NOT_FOUND;
            }
            else if(lobbyController.getLobbies().isEmpty()) {
                return AddPlayerToLobbyResponse.NO_LOBBIES;
            }
            else {
                lobbyController.addPlayer(username, lobbyId);
                userToLobby.put(username, lobbyId);
                return AddPlayerToLobbyResponse.PLAYER_ADDED;
            }
        }
        return AddPlayerToLobbyResponse.PLAYER_NOT_CONNECTED;
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
                return new ArrayList<>(lobbyController.getLobbies().keySet());
            }
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

    /**
     * Distribute a message to the players in the same lobby
     * @param msg the message to distribute
     */
    public void distributeMessage(ChatMessageData msg) {
        try {
            String lobbyId = this.userToLobby.get(msg.getSender());
            ServerChatHandler chatHandler = lobbyController.getLobbies().get(lobbyId).getChatHandler();
            chatHandler.distributeMessage(msg);
        } catch (NullPointerException e) {
            String gameId = this.userToGame.get(msg.getSender());
            ServerChatHandler chatHandler = gameController.getGames().get(gameId).getChatHandler();
            chatHandler.distributeMessage(msg);
        }
    }

    /**
     * Sort the leaderboard
     * @param leaderboard the leaderboard to sort
     * @return the sorted leaderboard
     */
    private Map<String, Integer> sortLeaderboard(Map<String, Integer> leaderboard){
        return leaderboard.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(e -> Objects.requireNonNull(gameController.getGames().get(userToGame.get(leaderboard.keySet().stream().findFirst().orElse(null))).getPlayers().stream()
                                        .filter(x -> x.getUsername().equals(e.getKey()))
                                        .findFirst()
                                        .orElse(null))
                                .getCompletedGoals(), Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new));
    }
}
