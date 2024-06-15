package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.InputStreamRunnable;
import it.polimi.ingsw.connections.OutputStreamRunnable;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.connections.messages.client.ClientToServerMessage;
import it.polimi.ingsw.connections.messages.client.LoginRequestMessage;
import it.polimi.ingsw.connections.messages.server.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is the implementation of the ClientConnection interface for the Socket connection type.
 */
public class SocketClientConnection implements ClientConnection, Runnable {
    private SocketAddress remoteAddr;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private InputStreamRunnable inputStream;
    private OutputStreamRunnable outputStream;

    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    private ConnectionStatus connectionStatus;

    /**
     * Constructor for the SocketClientConnection class.
     * @param serverSocket The server socket.
     * @param clientSocket The client socket to connect to.
     */
    public SocketClientConnection(ServerSocket serverSocket, Socket clientSocket) {
        synchronized (this) {
            this.connectionStatus = ConnectionStatus.INITIALIZING;
            this.serverSocket = serverSocket;
            this.clientSocket = clientSocket;

            this.remoteAddr = this.clientSocket.getRemoteSocketAddress();
        }
    }

    /**
     * Getter for the IP address of the client
     * @return the IP address of the client
     */
    public String getRemoteAddr() {
        return this.remoteAddr.toString();
    }

    @Override
    public void setOffline() {
        this.connectionStatus = ConnectionStatus.OFFLINE;
    }

    @Override
    public ConnectionStatus getStatus() {
        return this.connectionStatus;
    }

    /**
     * Getter for the InputStreamRunnable
     * @return the InputStreamRunnable
     */
    public InputStreamRunnable getInputStream() {
        return inputStream;
    }

    /**
     * Getter for the OutputStreamRunnable
     * @return the OutputStreamRunnable
     */
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
            this.connectionStatus = ConnectionStatus.ONLINE;



            while (this.connectionStatus == ConnectionStatus.ONLINE) {
                ClientToServerMessage msg = (ClientToServerMessage) queue.take();
                if (msg instanceof LoginRequestMessage) {
                    ConnectionBridge.getInstance().addConnection(this, ((LoginRequestMessage) msg).getUsername());
                } else {
                    msg.execute(ConnectionBridge.getInstance());
                }
            }
        } catch (IOException | InterruptedException e) {
            this.connectionStatus = ConnectionStatus.OFFLINE;
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
            this.connectionStatus = ConnectionStatus.CLOSED;
        }
    }


    public void threadExceptionCallback(String e) {
        if (this.connectionStatus == ConnectionStatus.OFFLINE) {
            return;
        }
        this.connectionStatus = ConnectionStatus.OFFLINE;
        ConnectionBridge.getInstance().onClientDisconnect(this);
    }

    // messages

    /**
     * Notify the client that the username is valid.
     * @throws IOException if an error occurs while sending the message
     */
    public void validUsername() throws IOException {
        this.outputStream.sendMessage(new ValidUsernameMessage());
    }

    /**
     * Notify the client that the username is invalid.
     * @throws IOException if an error occurs while sending the message
     */
    public void invalidUsername() throws IOException {
        this.outputStream.sendMessage(new InvalidUsernameMessage());
    }

    /**
     * Notify the client that the lobby exists.
     * @param idList the list of lobby IDs
     * @throws IOException if an error occurs while sending the message
     */
    public void lobbyExists(ArrayList<String> idList) throws IOException {
        this.outputStream.sendMessage(new LobbyExistsMessage(idList));
    }

    /**
     * Notify the client that the lobby has been joined successfully.
     * @param isLastPlayer true if the player is the last one to join the lobby, false otherwise
     * @throws IOException if an error occurs while sending the message
     */
    public void joinLobbySuccess(boolean isLastPlayer) throws IOException {
        this.outputStream.sendMessage(new JoinLobbySuccessMessage(isLastPlayer));
    }

    /**
     * Notify the client that the lobby is full.
     * @throws IOException if an error occurs while sending the message
     */
    public void lobbyFull() throws IOException {
        this.outputStream.sendMessage(new LobbyFullMessage());
    }

    /**
     * Notify the client that the lobby does not exist.
     * @throws IOException if an error occurs while sending the message
     */
    public void lobbyDoesNotExist() throws IOException {
        this.outputStream.sendMessage(new LobbyDoesNotExistMessage());
    }

    /**
     * Notify the client that the lobby has been created.
     * @param lobbyId the ID of the lobby
     * @throws IOException if an error occurs while sending the message
     */
    public void lobbyCreated(String lobbyId) throws IOException{
        this.outputStream.sendMessage(new LobbyCreatedMessage(lobbyId));
    }

    @Override
    public void playerJoined(String username) throws IOException {
        this.outputStream.sendMessage(new PlayerJoinedMessage(username));
    }

    @Override
    public void gameStarted(StarterData starterData) throws IOException {
        this.outputStream.sendMessage(new GameStartedMessage(starterData));
    }

    public void privateGoalChosen() throws IOException {
        this.outputStream.sendMessage(new PrivateGoalChosenMessage());
    }

    public void waitingOthersStartingChoice() throws IOException {
        this.outputStream.sendMessage(new WaitingOthersStartingChoiceMessage());
    }

    @Override
    public void otherPlayerTurnMessage(String currentPlayer) throws IOException {
        this.outputStream.sendMessage(new OtherPlayerTurnMessage(currentPlayer));
    }

    @Override
    public void initTurn(TurnInfo turnInfo) throws IOException {
        this.outputStream.sendMessage(new InitTurnMessage(turnInfo));
    }

    public void placeCardSuccess(PlaceCardSuccessInfo placeCardSuccessInfo) throws IOException {
        this.outputStream.sendMessage(new PlaceCardSuccessMessage(placeCardSuccessInfo));
    }

    public void placeCardFailure() throws IOException {
        this.outputStream.sendMessage(new PlaceCardFailureMessage());
    }

    public void drawSuccess(ArrayList<CardInfo> hand) throws IOException {
        this.outputStream.sendMessage(new DrawSuccessMessage(hand));
    }

    @Override
    public void sendStatus(GameStateInfo gameState) throws IOException {
        this.outputStream.sendMessage(new GameStateMessage(gameState));
    }

    @Override
    public void gameEnded(HashMap<String, Integer> leaderboard) throws IOException {
        this.outputStream.sendMessage(new GameEndMessage(leaderboard));
    }

    @Override
    public void playerDisconnected(String username, boolean gameStarted) throws IOException {
        this.outputStream.sendMessage(new PlayerDisconnectedMessage(username, gameStarted));
    }

    @Override
    public void playerReconnected(String username) throws IOException {
        this.outputStream.sendMessage(new PlayerReconnectedMessage(username));
    }

    @Override
    public void reconnectionState(GameStateInfo gameStateInfo) throws IOException{
        this.outputStream.sendMessage(new ReconnectionStateMessage(gameStateInfo));
    }

    @Override
    public void noOtherPlayerConnected() throws IOException {
        this.outputStream.sendMessage(new NoOtherPlayerConnectedMessage());
    }

    @Override
    public void sendChatMessage(ChatMessageData msg) throws IOException {
        this.outputStream.sendMessage(new ServerToClientChatMessage(msg));
    }
}