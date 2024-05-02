package it.polimi.ingsw;

import it.polimi.ingsw.connections.server.SocketClientConnection;
import it.polimi.ingsw.controller.server.ServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private static final int localPort = 5555;

    private static final ServerController serverController = new ServerController();

    public static void main( String[] args )
    {
        // MARK: listen for connections from client and create thread for each one
        try (ServerSocket serverSocket = new ServerSocket(localPort);) {
            System.out.println("Server listening on port " + localPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Received connection from " + clientSocket.getRemoteSocketAddress());

                SocketClientConnection clientConnection = new SocketClientConnection(serverSocket, clientSocket, serverController.getConnectionBridge());
                new Thread(clientConnection).start();
            }

        } catch (IOException /*| InterruptedException | ClassNotFoundException*/ e) {
            e.printStackTrace();
        }

        // TODO: allocate remoteobject for RMI
    }
}
