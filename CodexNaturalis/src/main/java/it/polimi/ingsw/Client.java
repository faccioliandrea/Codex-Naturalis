package it.polimi.ingsw;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.connections.client.ServerConnection;
import it.polimi.ingsw.connections.client.SocketServerConnection;
import it.polimi.ingsw.view.UserInterface;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int DEFAULT_PORT = 5555;
    private static final String DEFAULT_ADDRESS = "localhost";

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
                System.out.println("Please, type [1] GUI or [2] CLI");
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

        UserInterface ui = new UserInterface();


        ClientController matchController;
        matchController = new ClientController(ui);

        if(chosenConnection.equals("Socket")) {
            Socket socket = null;
            boolean connected = false;
            while (!connected) {
                try {
                    socket = new Socket(address, port);
                    connected = true;
                    SocketServerConnection conn = new SocketServerConnection(matchController, socket);
                    matchController.getConnectionBridge().setServerConnection(conn);
                    new Thread(conn).start();
                    matchController.getConnectionBridge().loginRequest();
                } catch (IOException  e) {
                    ui.printDebug("Could not connect to the server. Retrying...");
                }
            }
        } else {
            //RMI
            return;
        }




    }
}