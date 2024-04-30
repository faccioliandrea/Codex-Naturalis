package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.client.ClientController;

import java.util.HashMap;
import java.util.Scanner;

public class UserInterface { /* MARK: just for testing */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

//    public ClientController controller;

    private final HashMap<String, String> colorMap = new HashMap<>();
    public UserInterface(/* ClientController controller*/) {
        colorMap.put("BUTTERFLY", ANSI_PURPLE);
        colorMap.put("MUSHROOM", ANSI_RED);
        colorMap.put("LEAF", ANSI_GREEN);
        colorMap.put("WOLF", ANSI_CYAN);
        colorMap.put("STARTER", ANSI_YELLOW);

//        this.controller = controller;
    }

    public String askForUsername() {
        this.printDebug("Username: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    public int askForPlayerNum() {
        this.printDebug("Lobby player number: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    public String askForLobbyId() {
        this.printDebug("Lobby Id: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

//    public void setController(ClientController controller) {
//        this.controller = controller;
//    }

    public void printDebug(Object o) {
        System.out.println(o.toString());
    }


}
