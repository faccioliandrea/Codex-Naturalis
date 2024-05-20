package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.view.UIManager;
import it.polimi.ingsw.view.gui.controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GUI extends UIManager {
    // TODO: Pass UIData instead of connections data structures
    final static private BlockingQueue<Object> queue = new LinkedBlockingQueue<>();

    private final LobbiesController lobbiesController = new LobbiesController();
    private final WaitLobbyController waitLobbyController = new WaitLobbyController();
    private final MainController mainController = new MainController();

    private boolean ipErrorShown = false;

    public GUI() {
        new Thread(() ->
            Application.launch(GUIApp.class)
        ).start();
    }

    public static BlockingQueue<Object> getQueue() {
        return queue;
    }

    @Override
    public String askForServerAddr(String defaultAddr) {
        try {
            queue.clear();
            return queue.take().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

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

    @Override
    public String askForLobbyId(ArrayList<String> lobbies) {
        lobbiesController.showLobbies(lobbies);
        try {
            queue.clear();
            return queue.take().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int askForPrivateGoal() {
        // TODO: Implement
        return 0;
    }

    @Override
    public boolean askForStarterCardSide() {
        // TODO: Implement
        return false;
    }

    @Override
    public CardInfo askForPlayCard(ArrayList<CardInfo> hand, ArrayList<Point> availablePositions) {
        //GUIApp.changeScene("new-main", newMainController);
        Platform.runLater(() -> {
            mainController.updateData(data);
            mainController.askForPlayCard(data);
            mainController.setTitle("Select a card to play and a position to place it in");
        });
        try {
            queue.clear();
            Point point = (Point) queue.take();
            CardInfo selected = mainController.cardSelected();
            selected.setCoord(point);
            return selected;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int askForDrawCard(TurnInfo turnInfo) {
        Platform.runLater(() -> {
            mainController.updateData(data);
            mainController.askForDrawCard(data);
            mainController.setTitle("Select a card to draw from the resource or gold deck");
        });
        try {
            queue.clear();
            return (int) queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean askForNewGame() {
        // TODO: Implement
        return false;
    }

    @Override
    public void invalidUsername(String username) {
        GUIApp.showAlert("Invalid username", Alert.AlertType.WARNING);
    }

    @Override
    public void welcome(String username) {}

    @Override
    public void showCommands() {}

    @Override
    public void noLobbies() {
        // TODO: Implement
        // GUIApp.showAlert("No lobbies available. Please create one", Alert.AlertType.WARNING);
    }

    @Override
    public void joinedLobby() {
        GUIApp.changeScene("wait-lobby", waitLobbyController);
    }

    @Override
    public void joinedLobby(String username) {
        Platform.runLater(() -> waitLobbyController.userJoined(username));
    }

    @Override
    public void lobbyCreated(String lobbyId) {

    }


    @Override
    public void playerDisconnected(String username, boolean gameStarted) {
        // TODO: Implement
    }

    @Override
    public void playerReconnected(String username) {
        // TODO: Implement
    }

    @Override
    public void reconnectionState() {
        // TODO: Implement
    }

    @Override
    public void joinedLobbyLast() {}

    @Override
    public void lobbyFull() {
        // TODO: Implement
        GUIApp.showAlert("Lobby is full", Alert.AlertType.ERROR);
    }

    @Override
    public void gameStarted(StarterData starterData) {
        GUIApp.changeScene("main", mainController);
    }

    @Override
    public void waitingOthersStartingChoice() {
        // TODO: Implement
        GUIApp.showAlert("Awesome! Now wait for the other players to choose their private goals and starter cards", Alert.AlertType.INFORMATION);
    }

    @Override
    public void otherPlayerTurn(String currentPlayer) {
        // TODO: Implement
        Platform.runLater(() -> {
            mainController.updateData(data);
            mainController.setTitle("It's " + currentPlayer + "'s turn!");
        });
    }

    @Override
    public void yourTurn(boolean isLastTurn) {
        // TODO: Implement
        GUIApp.showAlert("It's your turn!" + (isLastTurn ? " This is your last turn." : ""), Alert.AlertType.INFORMATION);
    }

    @Override
    public void placeCardSuccess() {
        Platform.runLater(() ->
            mainController.updateData(data)
        );
    }

    @Override
    public void placeCardFailure() {
        GUIApp.showAlert("You don't have the requirements to place the card!", Alert.AlertType.WARNING);
    }

    @Override
    public void drawCardSuccess() {
        Platform.runLater(() ->
            mainController.updateData(data)
        );
    }

    @Override
    public void turnEnded(GameStateInfo gameStateInfo) {
        // TODO: Implement
        Platform.runLater(() -> {
            mainController.updateData(data);
            mainController.setTitle("Turn ended. Now wait for your opponent(s) to finish their turn");
        });
    }

    @Override
    public void gameEnded() {
        // TODO: Implement
        GUIApp.showAlert("Game ended!", Alert.AlertType.INFORMATION);
    }

    @Override
    public void goodbye() {
        // TODO: Implement
    }

    @Override
    public void showErrorMessage(String message) {
        // TODO: Display once
        if (ipErrorShown) return;
        GUIApp.notInteractableAlert(message, Alert.AlertType.ERROR);
        ipErrorShown = true;
    }

    public static String getCardPath(CardInfo card) {
        return "/" + (card.isFlipped() ? "back/" : "front/") + card.getId() + ".png";
    }

    /*public void showOpponentBoard(String opponent) {
        GUIApp.changeScene("opponent-board", new OpponentBoardController(opponent, data.getBoards().get(opponent)));
    }*/
}
