package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.InputStreamRunnable;
import it.polimi.ingsw.connections.OutputStreamRunnable;
import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.connections.messages.client.ClientToServerMessage;
import it.polimi.ingsw.connections.messages.client.LoginRequestMessage;
import it.polimi.ingsw.connections.messages.server.*;
import it.polimi.ingsw.controller.CardInfo;
import it.polimi.ingsw.controller.server.ServerController;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClientConnection implements ClientConnection {
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
                    msg.execute(controller);
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

    @Override
    public void threadExceptionCallback(Exception e) {
        this.isAlive = false;
        this.controller.onClientDisconnect(this);
    }

    // messages
    @Override
    public void validUsername() throws IOException {
        this.outputStream.sendMessage(new ValidUsernameMessage());
    }

    @Override
    public void invalidUsername() throws IOException {
        this.outputStream.sendMessage(new InvalidUsernameMessage());
    }

    @Override
    public void lobbyExists(ArrayList<String> idList) throws IOException {
        this.outputStream.sendMessage(new LobbyExistsMessage(idList));
    }

    @Override
    public void joinLobbySuccess() throws IOException {
        this.outputStream.sendMessage(new JoinLobbySuccessMessage());
    }

    @Override
    public void lobbyFull() throws IOException {
        this.outputStream.sendMessage(new LobbyFullMessage());
    }

    @Override
    public void lobbyDoesNotExist() throws IOException {
        this.outputStream.sendMessage(new LobbyDoesNotExistMessage());
    }

    @Override
    public void playerJoined(String username) throws IOException {
        this.outputStream.sendMessage(new PlayerJoinedMessage(username));
    }

    @Override
    public void gameStarted() throws IOException {
        this.outputStream.sendMessage(new GameStartedMessage());
    }

    @Override
    public void privateGoalChosen() throws IOException {
        this.outputStream.sendMessage(new PrivateGoalChosenMessage());
    }

    @Override
    public void initTurn(ArrayList<CardInfo> hand, ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<Point> availablePositions, int currTurn, boolean isLastTurn, ArrayList<CardInfo> board) throws IOException {
        this.outputStream.sendMessage(new InitTurnMessage(hand, resourceDeck, goldDeck, availablePositions, currTurn, isLastTurn, board));
    }

    @Override
    public void placeCardSuccess(int cardsPoints, int goalsPoints) throws IOException {
        this.outputStream.sendMessage(new PlaceCardSuccessMessage(cardsPoints, goalsPoints));
    }

    @Override
    public void placeCardFailure() throws IOException {
        this.outputStream.sendMessage(new PlaceCardFailureMessage());
    }

    @Override
    public void drawSuccess(ArrayList<CardInfo> hand) throws IOException {
        this.outputStream.sendMessage(new DrawSuccessMessage(hand));
    }

    @Override
    public void sendStatus(ArrayList<CardInfo> resourceDeck, ArrayList<CardInfo> goldDeck, ArrayList<CardInfo> board, int cardsPoints) throws IOException {
        this.outputStream.sendMessage(new GameStateMessage(resourceDeck, goldDeck, board, cardsPoints));
    }

    @Override
    public void gameEnded(HashMap<String, Integer> leaderboard) throws IOException {
        this.outputStream.sendMessage(new GameEndMessage(leaderboard));
    }
}
