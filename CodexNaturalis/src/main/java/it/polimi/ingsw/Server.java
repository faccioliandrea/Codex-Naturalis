package it.polimi.ingsw;

import it.polimi.ingsw.connections.server.RMIServerConnection;
import it.polimi.ingsw.connections.server.RMIServerConnectionInterface;
import it.polimi.ingsw.connections.server.SocketClientConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server
{
    private static final int localPort = 5555;
    private static final int localPortRMI = 1099;

    private static final String name = "Codex";


    public static void main( String[] args )
    {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
            System.out.println("Server IP Address: " + inetAddress.getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        // MARK: listen for connections from client and create thread for each one
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(localPort)) {
                System.out.println("Socket Server listening on port " + localPort);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Received connection from " + clientSocket.getRemoteSocketAddress());

                    SocketClientConnection clientConnection = new SocketClientConnection(serverSocket, clientSocket);
                    new Thread(clientConnection).start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            System.setProperty("java.rmi.server.hostname", InetAddress.getLocalHost().getHostAddress());
            RMIServerConnectionInterface obj = new RMIServerConnection();
            Registry registry = LocateRegistry.createRegistry(localPortRMI);
            registry.rebind(name, obj);

            System.out.println("RMI Server bound in registry");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
