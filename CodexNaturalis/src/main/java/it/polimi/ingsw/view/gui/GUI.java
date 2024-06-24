package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.enums.LogInResponse;
import it.polimi.ingsw.view.UIManager;
import it.polimi.ingsw.view.UIMessagesConstants;
import it.polimi.ingsw.view.gui.controller.*;
import it.polimi.ingsw.view.tui.enums.Decks;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GUI extends UIManager {
    final static private BlockingQueue<Object> queue = new LinkedBlockingQueue<>();

    private final LobbiesController lobbiesController = new LobbiesController();
    private final WaitLobbyController waitLobbyController = new WaitLobbyController();
    private final MainController mainController = new MainController();
    private final GameEndController gameEndController = new GameEndController();
    private final GameSetupController gameSetupController = new GameSetupController();

    public GUI() {
        instance = this;
        new Thread(() ->
            Application.launch(GUIApp.class)
        ).start();
    }

    public static BlockingQueue<Object> getQueue() {
        return queue;
    }

    /**
     * Asks the user for the server address
     * @param defaultAddr the default address
     * @return the server address
     */
    @Override
    public String askForServerAddr(String defaultAddr) {
        try {
            queue.clear();
            return queue.take().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the user for username
     * @return the username
     */
    @Override
    public String askForUsername() {
        GUIApp.changeScene("lobbies", lobbiesController);
        lobbiesController.askForUsername();
        try {
            queue.clear();
            return queue.take().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the user for the number of players
     * @return the number of players
     */
    @Override
    public int askForPlayerNum() {
        GUIApp.changeScene("new-lobby", new NewLobbyController());
        try {
            queue.clear();
            return (int) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the user for the lobby id
     * @param lobbies the list of lobbies
     * @return the lobby id
     */
    @Override
    public String askForLobbyId(ArrayList<String> lobbies) {
        GUIApp.changeScene("lobbies", lobbiesController);
        lobbiesController.showLobbies(lobbies);
        try {
            queue.clear();
            return queue.take().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the user for the private goal
     * @return the index of the private goal
     */
    @Override
    public int askForPrivateGoal() {
        Platform.runLater(gameSetupController::askPublicGoals);
        try {
            queue.clear();
            return (int) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the user for the starter card side
     * @return the side of the starter card
     */
    @Override
    public boolean askForStarterCardSide() {
        Platform.runLater(gameSetupController::askStarterCardSide);
        try {
            queue.clear();
            return (boolean) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the user for the card to play
     * @return the card to play
     */
    @Override
    public CardInfo askForPlayCard() {
        Platform.runLater(() -> {
            mainController.updateData();
            mainController.askForPlayCard();
            mainController.setTitle("Select a card to play and a position to place it in");
        });
        CardInfo selected;
        try {
            queue.clear();
            Point coord = (Point) queue.take();
            selected = mainController.cardSelected();
            selected.setCoord(coord);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(mainController::endPickCard);
        return selected;
    }

    /**
     * Asks the user for the card to draw
     *
     * @return the index of the card to draw
     */
    @Override
    public Decks askForDrawCard() {
        Platform.runLater(() -> {
            mainController.updateData();
            mainController.askForDrawCard();
            mainController.setTitle("Select a card to draw from the resource or gold deck");
        });
        try {
            queue.clear();
            return Decks.getDeck((int) queue.take());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the user if they want to play another game
     * @return true if the user wants to play another game, false otherwise
     */
    @Override
    public boolean askForNewGame() {
        Platform.runLater(gameEndController::askNewGame);
        try {
            queue.clear();
            return (boolean) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Notifies the user that the username is invalid
     * @param username the username
     * @param status the status of the login
     */
    @Override
    public void invalidUsername(String username, LogInResponse status) {
        if (status.equals(LogInResponse.USERNAME_TAKEN))
            GUIApp.showAlert(username + UIMessagesConstants.usernameTaken, Alert.AlertType.WARNING);
        if (status.equals(LogInResponse.INVALID_USERNAME))
            GUIApp.showAlert(UIMessagesConstants.invalidUsername, Alert.AlertType.WARNING);
    }

    @Override
    public void welcome(String username) {
    }

    @Override
    public void showCommands() {
    }

    @Override
    public void noLobbies() {
    }

    /**
     * Notifies the user lobby has been joined successfully
     */
    @Override
    public void joinedLobby() {
        GUIApp.changeScene("wait-lobby", waitLobbyController);
    }

    /**
     * Notifies the user that a player has joined the lobby
     * @param username the username of the player
     */
    @Override
    public void joinedLobby(String username) {
        Platform.runLater(() -> waitLobbyController.addNotification(username));
    }

    /**
     * Notifies the user that a lobby has been created
     * @param lobbyId the id of the lobby
     */
    @Override
    public void lobbyCreated(String lobbyId) {
        GUIApp.changeScene("wait-lobby", waitLobbyController);
        Platform.runLater(() -> waitLobbyController.setLobbyId(lobbyId));
    }

    /**
     * Notifies the user that another player has disconnected
     * @param username the username of the player
     * @param gameStarted true if the game has started, false otherwise
     */
    @Override
    public void playerDisconnected(String username, boolean gameStarted) {
        Platform.runLater(() -> {
            String title = username + " disconnected";
            if (gameStarted) {
                mainController.setInfoTitle(title);
            } else {
                waitLobbyController.addNotification(title);
            }
        });
    }

    /**
     * Notifies the user that another player has reconnected
     * @param username the username of the player
     */
    @Override
    public void playerReconnected(String username) {
        Platform.runLater(() ->
            mainController.setInfoTitle(username + " reconnected")
        );
    }

    /**
     * Displays the current state of the game when the user reconnects
     */
    @Override
    public void reconnectionState() {
        GUIApp.changeScene("main", mainController);
        Platform.runLater(() -> {
            mainController.updateData();
            mainController.updateChat();
            mainController.setTitle(data.getCurrentPlayer() + "'s turn");
        });
    }

    @Override
    public void joinedLobbyLast() {
    }

    /**
     * Notifies the user that the lobby is full
     */
    @Override
    public void lobbyFull() {
        GUIApp.showAlert("Lobby is full", Alert.AlertType.ERROR);
    }

    /**
     * Notifies the user that the game has started
     */
    @Override
    public void gameStarted(StarterData starterData) {
        GUIApp.changeScene("game-setup", gameSetupController);
        gameSetupController.setPrivateGoals(starterData.getPrivateGoals());
        gameSetupController.setStarterCard(starterData.getStarterCard());
    }

    /**
     * Notifies the user to wait for the other players to choose the private goal and starter card side
     */
    @Override
    public void waitingOthersStartingChoice() {
        GUIApp.changeScene("wait-setup", null);
    }

    /**
     * Notifies the user that it's another player's turn
     * @param currentPlayer the username of the current player
     */
    @Override
    public void otherPlayerTurn(String currentPlayer) {
        GUIApp.changeScene("main", mainController);
        Platform.runLater(() -> {
            mainController.updateData();
            mainController.setTitle("It's " + currentPlayer + "'s turn!");
        });
    }

    /**
     * Notifies the user that it's their turn
     * @param isLastTurn true if it's the last turn, false otherwise
     */
    @Override
    public void yourTurn(boolean isLastTurn) {
        GUIApp.changeScene("main", mainController);
        if (isLastTurn) {
            GUIApp.showAlert("This is your last turn", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Update GUI when the card has been placed successfully
     */
    @Override
    public void placeCardSuccess() {
        Platform.runLater(mainController::updateData
        );
    }

    /**
     * Notifies user when the card has not been placed successfully
     */
    @Override
    public void placeCardFailure() {
        GUIApp.showAlert(UIMessagesConstants.placeCardFailure, Alert.AlertType.WARNING);
    }

    /**
     * Update GUI when the card has been drawn successfully
     */
    @Override
    public void drawCardSuccess() {
        Platform.runLater(mainController::updateData
        );
    }

    /**
     * Notifies the user that another player's turn has ended
     * @param gameStateInfo the current game state
     */
    @Override
    public void turnEnded(GameStateInfo gameStateInfo) {
        Platform.runLater(() -> {
            mainController.updateData();
            mainController.setTitle("Turn ended. Now wait for your opponent(s) to finish their turn");
        });
    }

    /**
     * Notifies the user that the game has ended
     */
    @Override
    public void gameEnded() {
        GUIApp.changeScene("game-end", gameEndController);
        Platform.runLater(gameEndController::setLeaderboard
        );
    }

    /**
     * Close the application
     */
    @Override
    public void goodbye() {
        closeGame();
    }

    private void closeGame() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Shows an error message
     * @param message the message
     */
    @Override
    public void showErrorMessage(String message) {
        GUIApp.notInteractableAlert(message, Alert.AlertType.ERROR);
        closeGame();
    }

    /**
     * Notifies the user that is joining the server
     */
    @Override
    public void connectingToServer() {
        GUIApp.changeScene("connecting-server", null);
    }

    /**
     * Notifies the user that no other player is connected
     */
    @Override
    public void noOtherPlayerConnected() {
        GUIApp.showAlert(UIMessagesConstants.noOtherPlayerConnected, Alert.AlertType.WARNING);
    }

    /**
     * Notifies the user that the server is offline
     */
    @Override
    public void serverOfflineMessage() {
        GUIApp.showAlert(UIMessagesConstants.serverOfflineMessage, Alert.AlertType.ERROR, event -> closeGame());
    }

    @Override
    public void showChat() {
    }

    @Override
    public void sendMessage(String raw) {
    }

    /**
     * Notifies the user that a new message has been received
     */
    @Override
    public void messageReceived() {
        Platform.runLater(mainController::updateChat);
    }
}
