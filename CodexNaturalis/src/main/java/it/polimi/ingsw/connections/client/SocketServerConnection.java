package it.polimi.ingsw.connections.client;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.InputStreamRunnable;
import it.polimi.ingsw.connections.OutputStreamRunnable;
import it.polimi.ingsw.connections.messages.*;
import it.polimi.ingsw.connections.messages.client.*;
import it.polimi.ingsw.connections.messages.server.*;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketServerConnection implements ServerConnection, Runnable {
    private final ConnectionBridge connectionBridge;
    private InputStreamRunnable inputStream;
    private OutputStreamRunnable outputStream;
    private Socket socket;
    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    private ConnectionStatus connectionStatus;

    public SocketServerConnection(ConnectionBridge connectionBridge, Socket socket) {
        synchronized (this) {
            this.connectionBridge = connectionBridge;
            this.socket = socket;

            this.connectionStatus = ConnectionStatus.INITIALIZING;
        }
    }

    public void run() {
        try {
            outputStream = new OutputStreamRunnable(socket.getOutputStream(), this::threadExceptionCallback);
            inputStream = new InputStreamRunnable(new ObjectInputStream(socket.getInputStream()), queue, this::threadExceptionCallback);
            Thread outputThread = new Thread(outputStream);
            Thread inputThread = new Thread(inputStream);

            outputThread.start();
            inputThread.start();
            this.connectionStatus = ConnectionStatus.ONLINE;


            while (this.connectionStatus == ConnectionStatus.ONLINE) {
                ServerToClientMessage msg = (ServerToClientMessage) queue.take();
                msg.execute(connectionBridge);
            }
        } catch (IOException | InterruptedException e ) {
            this.connectionStatus = ConnectionStatus.OFFLINE;
            return;
        }
    }

    @Override
    public void close() throws IOException{
        synchronized (this) {
            inputStream.close();
            outputStream.close();
            socket.close();
            this.connectionStatus = ConnectionStatus.CLOSED;
        }
    }

    @Override
    public ConnectionStatus getStatus() {
        return this.connectionStatus;
    }

    @Override
    public void threadExceptionCallback(Exception e) {
        this.connectionStatus = ConnectionStatus.OFFLINE;
        try {
            this.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex); // TODO: Server offline (close application)
        }
    }

    public void loginRequest(String username) throws IOException {
        this.outputStream.sendMessage(new LoginRequestMessage(username));
    }

    public void getLobby(String username) throws IOException{
        this.outputStream.sendMessage(new GetLobbyMessage(username));
    }

    public void joinLobby(String username, String id) throws IOException {
        this.outputStream.sendMessage(new JoinLobbyMessage(username, id));
    }

    public void createLobbyAndJoin(String username, int playerNum) throws IOException {
        this.outputStream.sendMessage(new CreateLobbyAndJoinMessage(username, playerNum));
    }

    public void choosePrivateGoal(String username, int index) throws IOException {
        this.outputStream.sendMessage(new ChoosePrivateGoalMessage(username, index));
    }

    public void chooseStarterCardSide(String username, boolean flipped) throws IOException {
        this.outputStream.sendMessage(new ChooseStarterCardSideMessage(username, flipped));
    }

    public void initTurnAck(String username) throws IOException {
        this.outputStream.sendMessage(new InitTurnAckMessage(username));
    }

    public void placeCard(String username, String cardId, Point pos, boolean flipped) throws IOException {
        this.outputStream.sendMessage(new PlaceCardMessage(username, cardId, pos, flipped));
    }

    public void drawResource(String username, int index) throws IOException {
        this.outputStream.sendMessage(new DrawResourceCardMessage(username, index));
    }

    public void drawGold(String username, int index) throws IOException {
        this.outputStream.sendMessage(new DrawGoldCardMessage(username, index));
    }

    public void endTurn(String username) throws IOException {
        this.outputStream.sendMessage(new EndTurnMessage(username));
    }
}
