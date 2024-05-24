package it.polimi.ingsw;

import it.polimi.ingsw.connections.server.RMIServerConnection;
import it.polimi.ingsw.connections.server.RMIServerConnectionInterface;
import it.polimi.ingsw.connections.server.SocketClientConnection;
import it.polimi.ingsw.controller.server.ServerController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server
{
    private static final int localPort = 5555;
    private static final int localPortRMI = 1099;

    private static final String name = "Codex";

    private static final ServerController serverController = new ServerController();

    public static void main( String[] args )
    {
        // MARK: listen for connections from client and create thread for each one
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(localPort);) {
                System.out.println("Server listening on port " + localPort);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Received connection from " + clientSocket.getRemoteSocketAddress());

                    SocketClientConnection clientConnection = new SocketClientConnection(serverSocket, clientSocket, serverController.getConnectionBridge());
                    new Thread(clientConnection).start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            System.setProperty("java.rmi.server.hostname", InetAddress.getLocalHost().getHostAddress());
            RMIServerConnectionInterface obj = new RMIServerConnection(serverController.getConnectionBridge());
            Registry registry = LocateRegistry.createRegistry(localPortRMI);
            registry.rebind(name, obj);

            System.out.println("RMI Server bound in registry");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
