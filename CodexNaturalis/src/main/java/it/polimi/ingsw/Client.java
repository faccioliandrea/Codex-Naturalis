package it.polimi.ingsw;

import it.polimi.ingsw.connections.client.RMIClientConnection;
import it.polimi.ingsw.connections.client.RMIClientConnectionInterface;
import it.polimi.ingsw.connections.server.RMIServerConnectionInterface;
import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.connections.client.SocketServerConnection;
import it.polimi.ingsw.view.UIManager;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

import java.io.IOException;
import java.net.Socket;
import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

    private static final int DEFAULT_PORT = 5555;
    private static final String DEFAULT_ADDRESS = "localhost";
    private static final String REMOTE_NAME = "Codex";

    private static int port = DEFAULT_PORT;
    private static String address = DEFAULT_ADDRESS;

    public static void main(String[] args) {

        Scanner sc;
        sc = new Scanner(System.in);
        int choice = 0;
        String chosenString;
        String chosenMode;
        String chosenConnection;
        System.out.println("Choose your UI mode: ");
        System.out.println("[1] TUI");
        System.out.println("[2] GUI");
        do {
            System.out.print("Your Choice: ");
            chosenString = sc.nextLine().trim();
            try {
                choice = Integer.parseInt(chosenString);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }
            if (!(choice == 1 || choice == 2)) {
                System.out.println("Please, type [1] TUI or [2] GUI");
            }
        } while(!(choice == 1 || choice == 2));
        chosenMode = (choice == 1) ? "TUI" : "GUI";


        choice = 0;
        System.out.println("Now please choose between Socket technology and RMI technology");
        System.out.println("[1] Socket");
        System.out.println("[2] RMI");
        do {
            System.out.print("Your Choice: ");
            chosenString = sc.nextLine().trim();
            try {
                choice = Integer.parseInt(chosenString);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }
            if (!(choice == 1 || choice == 2)) {
                System.out.println("Please, type [1] Socket or [2] RMI");
            }
        } while(!(choice == 1 || choice == 2));
        chosenConnection = (choice == 1) ? "Socket" : "RMI";

        UIManager ui;
        if (chosenMode.equals("GUI")){
            ui = new GUI();
        } else {
            ui = new TUI();
        }

        ClientController matchController;
        matchController = new ClientController(ui);

        if(chosenConnection.equals("Socket")) {
            address = ui.askForServerAddr(DEFAULT_ADDRESS);
            Socket socket = null;
            boolean connected = false;
            try {
                while (!connected) {
                    try {
                        socket = new Socket(address, port);
                        connected = true;
                        SocketServerConnection conn = new SocketServerConnection(matchController.getConnectionBridge(), socket);
                        matchController.getConnectionBridge().setServerConnection(conn);
                        new Thread(conn).start();
                        matchController.getConnectionBridge().loginRequest();
                    } catch (IOException e) {
                        ui.connectingToServer();
                    }
                }
            } catch (Exception e) {
                // FIXME: Handle server crash (on its thread)
                ui.showErrorMessage("An error occurred.");
            }
        } else {
            address = ui.askForServerAddr(DEFAULT_ADDRESS);
            try {
                Registry registry = LocateRegistry.getRegistry(address);
                RMIServerConnectionInterface obj = (RMIServerConnectionInterface) registry.lookup(REMOTE_NAME);
                RMIClientConnectionInterface client = new RMIClientConnection();
                client.setBridge(matchController.getConnectionBridge());
                matchController.getConnectionBridge().setRmiClientConnectionInterface(client);
                matchController.getConnectionBridge().setServerConnection(obj);
                matchController.getConnectionBridge().loginRequest();
            } catch (ConnectException e) {
                ui.connectingToServer();
            } catch (Exception e) {
                // FIXME: Handle server crash (on its thread)
                ui.showErrorMessage("An error occurred.");
            }
        }




    }
}