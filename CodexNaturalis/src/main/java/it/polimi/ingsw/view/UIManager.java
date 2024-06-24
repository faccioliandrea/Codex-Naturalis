package it.polimi.ingsw.view;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.enums.LogInResponse;
import it.polimi.ingsw.view.data.UIData;
import it.polimi.ingsw.view.tui.enums.Decks;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class that defines the methods that the UI must implement
 */
public abstract class UIManager {
    protected static UIManager instance;
    protected UIData data = new UIData();

    private static int boardMinX = 0;
    private static int boardMaxY = 0;

    /**
     * Asks the user for the server address
     * @param defaultAddr the default address
     * @return the server address
     */
    public abstract String askForServerAddr(String defaultAddr);

    /**
     * Asks the user for username
     * @return the username
     */
    public abstract String askForUsername();

    /**
     * Asks the user for the number of players
     * @return the number of players
     */
    public abstract int askForPlayerNum();

    /**
     * Asks the user for the lobby id
     * @param lobbies the list of lobbies
     * @return the lobby id
     */
    public abstract String askForLobbyId(ArrayList<String> lobbies);

    /**
     * Asks the user for the private goal
     * @return the index of the private goal
     */
    public abstract int askForPrivateGoal();

    /**
     * Asks the user for the starter card side
     * @return the side of the starter card
     */
    public abstract boolean askForStarterCardSide();

    /**
     * Asks the user for the card to play
     * @return the card to play
     */
    public abstract CardInfo askForPlayCard();

    /**
     * Asks the user for the card to draw
     *
     * @return the index of the card to draw
     */
    public abstract Decks askForDrawCard();

    /**
     * Asks the user if they want to play another game
     * @return true if the user wants to play another game, false otherwise
     */
    public abstract boolean askForNewGame();

    /**
     * Notifies the user that the username is invalid
     * @param username the username
     */
    public abstract void invalidUsername(String username, LogInResponse status);

    /**
     * Shows a welcome message
     * @param username the username
     */
    public abstract void welcome(String username);

    /**
     * Shows the commands
     */
    public abstract void showCommands();

    /**
     * Shows the available lobbies
     */
    public abstract void noLobbies();

    /**
     * Notifies the user th lobby has been joined successfully
     */
    public abstract void joinedLobby();

    /**
     * Notifies the user that a player has joined the lobby
     * @param username the username of the player
     */
    public abstract void joinedLobby(String username);

    /**
     * Notifies the user that the lobby has been created
     * @param lobbyId the id of the lobby
     */
    public abstract void lobbyCreated(String lobbyId);

    /**
     * Notifies the user that another player has disconnected
     * @param username the username of the player
     * @param gameStarted true if the game has started, false otherwise
     */
    public abstract void playerDisconnected(String username, boolean gameStarted);

    /**
     * Notifies the user that another player has reconnected
     * @param username the username of the player
     */
    public abstract void playerReconnected(String username);

    /**
     * Displays the current state of the game when the user reconnects
     */
    public abstract void reconnectionState();

    /**
     * Notifies the user that the lobby has been joined successfully
     */
    public abstract void joinedLobbyLast();

    /**
     * Notifies the user that the lobby is full
     */
    public abstract void lobbyFull();

    /**
     * Notifies the user that the game has started
     */
    public abstract void gameStarted(StarterData starterData);

    /**
     * Notifies the user to wait for the other players to choose the private goal
     */
    public abstract void waitingOthersStartingChoice();

    /**
     * Notifies the user that it's another player's turn
     * @param currentPlayer the username of the current player
     */
    public abstract void otherPlayerTurn(String currentPlayer);

    /**
     * Notifies the user that it's their turn
     * @param isLastTurn true if it's the last turn, false otherwise
     */
    public abstract void yourTurn(boolean isLastTurn);

    /**
     * Notifies the user that the card has been placed successfully
     */
    public abstract void placeCardSuccess();

    /**
     * Notifies the user that the card has not been placed successfully
     */
    public abstract void placeCardFailure();

    /**
     * Notifies the user that the card has been drawn successfully
     */
    public abstract void drawCardSuccess();

    /**
     * Notifies the user another player's turn has ended
     * @param gameStateInfo the current game state
     */
    public abstract void turnEnded(GameStateInfo gameStateInfo);

    /**
     * Notifies the user that the game has ended
     */
    public abstract void gameEnded();

    /**
     * Displays a goodbye message
     */
    public abstract void goodbye();

    /**
     * Shows an error message
     * @param message the message
     */
    public abstract void showErrorMessage(String message);

    /**
     * Updates and shows the chat
     */
    public abstract void showChat();

    /**
     * Asks the chat handler to send a message
     * @param raw the raw message from user input
     */
    public abstract void sendMessage(String raw);

    /**
     * Notifies the user that a new message has been received
     */
    public abstract void messageReceived();

    /**
     * Notifies the user that they are the only player connected
     */
    public abstract void noOtherPlayerConnected();

    /**
     * Connecting to the server
     */
    public abstract void connectingToServer();

    /**
     * Displays a message that the server is offline
     */
    public abstract void serverOfflineMessage();


    /**
     * Validates the server address
     * @param ipAddr the server address
     * @param defaultAddr the default address
     * @return true if the address is valid, false otherwise
     */
    public static boolean isValidIP(String ipAddr, String defaultAddr) {
        Pattern ip_addr_pattern = Pattern.compile(String.format("(^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$)|(%s)", defaultAddr), Pattern.CASE_INSENSITIVE);
        Matcher matcher = ip_addr_pattern.matcher(ipAddr);
        return matcher.matches() || ipAddr.isEmpty();
    }

    /**
     * Calculates the number of columns of the board
     * @param board list of cards on the board
     * @param padding true if padding is needed, false otherwise
     * @return the number of columns
     */
    public static int boardGridColumns(ArrayList<CardInfo> board, boolean padding) {
        OptionalInt x_max = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.x).max();
        OptionalInt x_min = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.x).min();
        if (!x_min.isPresent()) {
            return 0;
        }
        int width = x_max.getAsInt() - x_min.getAsInt() + 1;
        boardMinX = x_min.getAsInt();

        return width + (padding ? 2 : 0);
    }

    /**
     * Calculates the number of rows of the board
     * @param board list of cards on the board
     * @param padding true if padding is needed, false otherwise
     * @return the number of rows
     */
    public static int boardGridRows(ArrayList<CardInfo> board, boolean padding) {
        OptionalInt y_max = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.y).max();
        OptionalInt y_min = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.y).min();
        if (!y_min.isPresent()) {
            return 0;
        }
        int height = y_max.getAsInt() - y_min.getAsInt() + 1;
        boardMaxY = y_max.getAsInt();

        return height + (padding ? 2 : 0);
    }

    /**
     * Converts point to matrix coordinates
     * @param p the point
     * @param padding true if padding is needed, false otherwise
     * @return point (j, i) in the matrix
     */
    public static Point toMatrixCoord(Point p, boolean padding) {
        int i = (boardMaxY + (padding ? 1 : 0)) - p.y;
        int j = p.x - (boardMinX - (padding ? 1 : 0));

        return new Point(j, i);
    }

    /**
     * Getter for the data
     * @return the data
     */
    public UIData getData() {
        return this.data;
    }

    /**
     * Setter for the data
     * @param data the data
     */
    public void setData(UIData data) {
        this.data = data;
    }

    /**
     * Singleton instance getter
     * @return the instance of the UIManager
     */
    public static synchronized UIManager getInstance() {
        return instance;
    }

    /**
     * Server not found
     */
    public void serverNotFound() {
        serverOfflineMessage();
    }
}
