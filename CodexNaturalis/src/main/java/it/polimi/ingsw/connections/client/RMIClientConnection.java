package it.polimi.ingsw.connections.client;
import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

/**
 * This class is the RMI client connection class
 */
public class RMIClientConnection extends UnicastRemoteObject implements RMIClientConnectionInterface {
    final private ConnectionBridge connectionBridge = ConnectionBridge.getInstance();
    private final Object lock = new Object();

    /**
     * Constructor for the RMI client connection
     * @throws RemoteException if the connection fails
     */
    public RMIClientConnection() throws RemoteException {
    }


    /**
     * Method to handle the joining of a player in the lobby
     * @param username the username of the player
     */
    @Override
    public void playerJoined(String username) {
        connectionBridge.playerJoinedLobby(username);
    }

    /**
     * Method to ping the server
     */
    @Override
    public void pingServer() {
        new Thread(()->{

            while (true){
                try {
                    connectionBridge.rmiPing();
                    synchronized (lock){
                        lock.wait(3000);
                    }
                } catch (RemoteException | InterruptedException e) {
                    connectionBridge.serverNotFound();
                    break;
                }
            }
        }).start();
    }

    /**
     * Method to handle the start of the game
     * @param starterData the starter data: the hand, the private goals, the shared goals, the starter card and the users
     * @throws RemoteException if the connection fails
     */
    @Override
    public void gameStarted(StarterData starterData) throws RemoteException {
        connectionBridge.gameStarted(starterData);
    }

    /**
     * Method to communicate the turn of the other player
     * @param currentPlayer the current player
     * @throws RemoteException if the connection fails
     */
    @Override
    public void otherPlayerTurn(String currentPlayer) throws RemoteException {
        connectionBridge.OtherPlayerTurnMessage(currentPlayer);
    }

    /**
     * Method to initialize the turn
     * @param turnInfo the turn info: the player, the hand, the private goals, the shared goals, the round track, the draft pool, the tool cards, the round, the turn, the player's status
     * @throws RemoteException if the connection fails
     */
    @Override
    public void initTurn(TurnInfo turnInfo) throws RemoteException {
        connectionBridge.initTurn(turnInfo);
    }

    /**
     * Method to send the status of the game, received at the end of each turn
     * @param gameStateInfo the game state info: the round track, the draft pool, the tool cards, the round, the turn, the player's status
     */
    @Override
    public void sendStatus(GameStateInfo gameStateInfo) {
        connectionBridge.gameState(gameStateInfo);
    }

    /**
     * Method to handle the end of the game, showing the leaderboard
     * @param leaderboard the leaderboard of the game
     * @throws RemoteException if the connection fails
     */
    @Override
    public void gameEnded(Map<String, Integer> leaderboard) throws RemoteException {
        connectionBridge.gameEnd(leaderboard);
    }

    @Override
    public void noOtherPlayerConnected() throws RemoteException {
        connectionBridge.noOtherPlayerConnected();
    }

    /**
     * Method to handle the disconnection of a player
     * @param username the username of the player
     * @param gameStarted if the game has started
     */
    @Override
    public void playerDisconnected(String username, boolean gameStarted) {
        connectionBridge.playerDisconnected(username, gameStarted);
    }

    /**
     * Method to handle the reconnection of a different player
     * @param username the username of the player
     * @throws RemoteException if the connection fails
     */
    @Override
    public void playerReconnected(String username) throws RemoteException {
        connectionBridge.playerReconnected(username);
    }

    /**
     * Method to handle the reconnection of  this a player
     * @param gameStateInfo the game state info
     * @throws RemoteException if the connection fails
     */
    @Override
    public void reconnectionState(GameStateInfo gameStateInfo) throws RemoteException {
        connectionBridge.reconnectionState(gameStateInfo);
    }

    /**
     * Method to ping the connection
     * @throws RemoteException if the connection fails
     */
    @Override
    public void ping() throws RemoteException {
    }

    @Override
    public void recvChatMessage(ChatMessageData msg) throws RemoteException {
        connectionBridge.recvChatMessage(msg);
    }
}
