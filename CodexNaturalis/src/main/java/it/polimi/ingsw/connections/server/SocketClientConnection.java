package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.InputStreamRunnable;
import it.polimi.ingsw.connections.OutputStreamRunnable;
import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.connections.messages.client.ClientToServerMessage;
import it.polimi.ingsw.connections.messages.client.LoginRequestMessage;
import it.polimi.ingsw.connections.messages.server.*;
import it.polimi.ingsw.controller.CardInfo;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.goals.Goal;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClientConnection implements ClientConnection, Runnable {
    private final ServerController controller;
    private SocketAddress remoteAddr;

    private Socket clientSocket;
    private ServerSocket serverSocket;

    private InputStreamRunnable inputStream;
    private OutputStreamRunnable outputStream;

    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    private boolean isAlive;

    public SocketClientConnection(ServerSocket serverSocket, Socket clientSocket, ServerController controller) {
        synchronized (this) {
            this.controller = controller;
            this.serverSocket = serverSocket;
            this.clientSocket = clientSocket;

            this.remoteAddr = this.clientSocket.getRemoteSocketAddress();

            this.isAlive = false;
        }
    }

    @Override
    public boolean getStatus() {
        return this.isAlive;
    }

    public String getRemoteAddr() {
        return this.remoteAddr.toString();
    }

    public synchronized boolean isAlive() {
        return isAlive;
    }

    public synchronized void setAlive(boolean alive) {
        isAlive = alive;
    }

    public InputStreamRunnable getInputStream() {
        return inputStream;
    }

    public OutputStreamRunnable getOutputStream() {
        return outputStream;
    }

    public void run() {
        try {
            outputStream = new OutputStreamRunnable(clientSocket.getOutputStream(), this::threadExceptionCallback);
            inputStream = new InputStreamRunnable(clientSocket.getInputStream(), queue, this::threadExceptionCallback);
            Thread outputThread = new Thread(outputStream);
            Thread inputThread = new Thread(inputStream);

            outputThread.start();
            inputThread.start();
            this.isAlive = true;



            while (this.isAlive) {
                ClientToServerMessage msg = (ClientToServerMessage) queue.take();
                if (msg instanceof LoginRequestMessage) {
                    controller.addConnection(this, ((LoginRequestMessage) msg).getUsername());
                } else {
                    msg.execute(controller.getConnectionBridge());
                }
            }

            System.err.println("all thread started");
        } catch (IOException | InterruptedException e) {
            this.isAlive = false;
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
            this.isAlive = false;
        }
    }


    public void threadExceptionCallback(Exception e) {
        this.isAlive = false;
        this.controller.onClientDisconnect(this);
    }

    // messages

    public void validUsername() throws IOException {
        this.outputStream.sendMessage(new ValidUsernameMessage());
    }


    public void invalidUsername() throws IOException {
        this.outputStream.sendMessage(new InvalidUsernameMessage());
    }

    public void lobbyExists(ArrayList<String> idList) throws IOException {
        this.outputStream.sendMessage(new LobbyExistsMessage(idList));
    }

    public void joinLobbySuccess() throws IOException {
        this.outputStream.sendMessage(new JoinLobbySuccessMessage());
    }


    public void lobbyFull() throws IOException {
        this.outputStream.sendMessage(new LobbyFullMessage());
    }


    public void lobbyDoesNotExist() throws IOException {
        this.outputStream.sendMessage(new LobbyDoesNotExistMessage());
    }

    public void playerJoined(String username) throws IOException {
        this.outputStream.sendMessage(new PlayerJoinedMessage(username));
    }

    public void gameStarted() throws IOException {
        this.outputStream.sendMessage(new GameStartedMessage());
    }

    public void privateGoalChosen() throws IOException {
        this.outputStream.sendMessage(new PrivateGoalChosenMessage());
    }

    public void initTurn(ArrayList<CardInfo> hand, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<Point> availablePositions, int currTurn, boolean isLastTurn, ArrayList<CardInfo> board) throws IOException {
        this.outputStream.sendMessage(new InitTurnMessage(hand, resourceDeck, goldDeck, availablePositions, currTurn, isLastTurn, board));
    }

    public void placeCardSuccess(int cardsPoints, int goalsPoints) throws IOException {
        this.outputStream.sendMessage(new PlaceCardSuccessMessage(cardsPoints, goalsPoints));
    }

    public void placeCardFailure() throws IOException {
        this.outputStream.sendMessage(new PlaceCardFailureMessage());
    }

    public void drawSuccess(ArrayList<CardInfo> hand) throws IOException {
        this.outputStream.sendMessage(new DrawSuccessMessage(hand));
    }

    public void sendStatus(ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<CardInfo> board, int cardsPoints) throws IOException {
        this.outputStream.sendMessage(new GameStateMessage(resourceDeck, goldDeck, board, cardsPoints));
    }

    public void gameEnded(HashMap<String, Integer> leaderboard) throws IOException {
        this.outputStream.sendMessage(new GameEndMessage(leaderboard));
    }
}