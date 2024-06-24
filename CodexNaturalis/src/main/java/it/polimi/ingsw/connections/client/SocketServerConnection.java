package it.polimi.ingsw.connections.client;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.InputStreamRunnable;
import it.polimi.ingsw.connections.OutputStreamRunnable;
import it.polimi.ingsw.connections.messages.*;
import it.polimi.ingsw.connections.messages.client.*;
import it.polimi.ingsw.connections.messages.server.*;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is the implementation of the ServerConnection interface for the Socket connection type.
 */
public class SocketServerConnection implements ServerConnection, Runnable {
    private InputStreamRunnable inputStream;
    private OutputStreamRunnable outputStream;
    private final Socket socket;
    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    private ConnectionStatus connectionStatus;

    /**
     * Constructor for the SocketServerConnection class.
     * @param socket The socket to connect to the server.
     */
    public SocketServerConnection(Socket socket) {
        synchronized (this) {
            this.socket = socket;

            this.connectionStatus = ConnectionStatus.INITIALIZING;
        }
    }

    public void run() {
        try {
            outputStream = new OutputStreamRunnable(socket.getOutputStream(), this::threadExceptionCallback);
            inputStream = new InputStreamRunnable(socket.getInputStream(), queue, this::threadExceptionCallback);
            Thread outputThread = new Thread(outputStream);
            Thread inputThread = new Thread(inputStream);

            outputThread.start();
            inputThread.start();
            this.connectionStatus = ConnectionStatus.ONLINE;


            while (this.connectionStatus == ConnectionStatus.ONLINE) {
                ServerToClientMessage msg = (ServerToClientMessage) queue.take();
                new Thread(() -> msg.execute(ConnectionBridge.getInstance())).start();
            }
        } catch (IOException | InterruptedException e ) {
            this.connectionStatus = ConnectionStatus.OFFLINE;
        }
    }

    /**
     * Close the connection
     * @throws IOException If an error occurs during the communication with the server.
     */
    @Override
    public void close() throws IOException{
        synchronized (this) {
            inputStream.close();
            outputStream.close();
            socket.close();
            this.connectionStatus = ConnectionStatus.CLOSED;
        }
    }

    /**
     * Get the status of the connection
     * @return ConnectionStatus
     */
    @Override
    public ConnectionStatus getStatus() {
        return this.connectionStatus;
    }

    /**
     * Called from child threads when an exception is thrown
     * @param e: Exception message
     */
    @Override
    public void threadExceptionCallback(String e) {
        if (this.connectionStatus == ConnectionStatus.OFFLINE) {
            return;
        }
        this.connectionStatus = ConnectionStatus.OFFLINE;
        try {
            this.close();
        } catch (IOException ex) {

            ConnectionBridge.getInstance().serverNotFound();
            //throw new RuntimeException(ex); // FIXME: Server offline (close application)
        }
    }

    /**
     * Sends a login request to the server.
     * @param username The username to use for the login.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void loginRequest(String username) throws IOException {
        this.outputStream.sendMessage(new LoginRequestMessage(username));
    }

    /**
     * Requests available lobbies from the servere.
     * @param username The username used by the server to identify the sender.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void getLobby(String username) throws IOException{
        this.outputStream.sendMessage(new GetLobbyMessage(username));
    }
    
    /**
     * Joins a lobby with a given id.
     * @param username The username used by the server to identify the sender.
     * @param id The id of the lobby to join.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void joinLobby(String username, String id) throws IOException {
        this.outputStream.sendMessage(new JoinLobbyMessage(username, id));
    }
    
    /**
     * Creates a new lobby on the server and joins it.
     * @param username The username used by the server to identify the sender.
    * @param playerNum The number of players in the lobby.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void createLobbyAndJoin(String username, int playerNum) throws IOException {
        this.outputStream.sendMessage(new CreateLobbyAndJoinMessage(username, playerNum));
    }

    /**
     * Creates a new game on the server.
     * @param username The username used by the server to identify the sender.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void createGame(String username) throws IOException {
        this.outputStream.sendMessage(new CreateGameMessage(username));
    }

    /**
     * Chooses the private goal.
     * @param username The username used by the server to identify the sender.
     * @param index The index of the goal to choose.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void choosePrivateGoal(String username, int index) throws IOException {
        this.outputStream.sendMessage(new ChoosePrivateGoalMessage(username, index));
    }
    
    /**
     * Chooses the starter card side.
     * @param username The username used by the server to identify the sender.
     * @param flipped Whether the card should be flipped or not.
     */
    public void chooseStarterCardSide(String username, boolean flipped) throws IOException {
        this.outputStream.sendMessage(new ChooseStarterCardSideMessage(username, flipped));
    }
  
    /**
     * Asks the server to place a card on the board.
     * @param username The username used by the server to identify the sender.
     * @param cardId The id of the card to place.
     * @param pos The position where to place the card.
     * @param flipped Whether the card should be flipped or not.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void placeCard(String username, String cardId, Point pos, boolean flipped) throws IOException {
        this.outputStream.sendMessage(new PlaceCardMessage(username, cardId, pos, flipped));
    }

    /**
     * Asks the server to draw a resource card.
     * @param username The username used by the server to identify the sender.
     * @param index The index of the card to choose.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void drawResource(String username, int index) throws IOException {
        this.outputStream.sendMessage(new DrawResourceCardMessage(username, index));
    }

    /**
     * Asks the server to draw a gold card.
     * @param username The username used by the server to identify the sender.
     * @param index The index of the gold card to draw.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void drawGold(String username, int index) throws IOException {
        this.outputStream.sendMessage(new DrawGoldCardMessage(username, index));
    }

    /**
     * Notifies the server that the turn has ended.
     * @param username The username used by the server to identify the sender.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void endTurn(String username) throws IOException {
        this.outputStream.sendMessage(new EndTurnMessage(username));
    }

    /**
     * Sends a chat message.
     * @param msg The message to send.
     * @throws IOException If an error occurs during the communication with the server.
     */
    public void sendChatMessage(ChatMessageData msg) throws IOException {
        this.outputStream.sendMessage(new ClientToServerChatMessage(msg));
    }
}
