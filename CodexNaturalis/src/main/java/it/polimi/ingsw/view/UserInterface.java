package it.polimi.ingsw.view;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.controller.client.ClientController;

import java.awt.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UserInterface {
    private ClientController controller;
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();

    public UserInterface() {
        new Thread(new InputHandler(commandQueue, inputQueue)).start();
        new Thread(new CommandHandler(commandQueue, this)).start();
    }

    public void setController(ClientController controller) {
        this.controller = controller;
    }

    public String askForUsername() {
        this.printColorDebug(TUIColors.PURPLE, "Username: " );
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int askForPlayerNum() {
        this.printColorDebug(TUIColors.PURPLE, "Lobby player number: ");
        try {
            return Integer.parseInt(inputQueue.take());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String askForLobbyId() {
        this.printColorDebug(TUIColors.PURPLE, "Lobby Id: ");
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int askForPrivateGoal() {
        this.printColorDebug(TUIColors.PURPLE, "Choose your private Goal: [1] [2]");
        try {
            return Integer.parseInt(inputQueue.take()) - 1;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean askForStarterCardSide() {
        this.printColorDebug(TUIColors.PURPLE, "Choose your Starter card side: [1] Front [2] Back");
        try {
            return inputQueue.take().equals("2");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayBoard(String username) {
        if (controller != null) {
            if (username==null && controller.getCurrentTurnInfo() != null) {
                this.displayBoard(controller.getCurrentTurnInfo().getBoard(), controller.getCurrentTurnInfo().getAvailablePositions());
            } else if (username != null) {
                // TODO: add checks on user existance
                // TODO: get selected user's board (print with no available positions)
                // MARK: temporary
                this.printColorDebug(TUIColors.RED, String.format("%s's board not available", username));
            } else {
                this.printColorDebug(TUIColors.RED, "Board not available");
            }
        } else {
            this.printColorDebug(TUIColors.RED, "Board not available");
        }
    }

    public void displayBoard(ArrayList<CardInfo> board,ArrayList<Point> availablePos) {
        OptionalInt x_max = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.x).max();
        OptionalInt x_min = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.x).min();
        if (!x_min.isPresent() || !x_max.isPresent()){
            return;
        }
        int width = x_max.getAsInt() - x_min.getAsInt() + 1;
        OptionalInt y_max = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.y).max();
        OptionalInt y_min = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.y).min();
        if (!y_min.isPresent() || !y_max.isPresent()){
            return;
        }
        int height = y_max.getAsInt() - y_min.getAsInt() + 1;

        int rows = height + (availablePos != null ? 3 : 1); // +1 for matrix indexes going from 0 to n-1, +2 for table padding (available positions)
        int cols = width  + (availablePos != null ? 3 : 1); // +1 for matrix indexes going from 0 to n-1, +2 for table padding (available positions)

        String[][] grid = new String[rows][cols];
        for (CardInfo card: board) {

            int i = (y_max.getAsInt() + 1)  - card.getCoord().y;
            int j = card.getCoord().x - (x_min.getAsInt() - 1);

            try {
                grid[i][j] = String.format("%s %s %s", CardColors.valueOf(card.getColor()), card.getId(), TUIColors.reset());
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        if (availablePos != null) {
            for (Point p: availablePos) {

                int i = (y_max.getAsInt() + 1) - p.y;
                int j = p.x - (x_min.getAsInt() - 1);

                grid[i][j] = String.format("%s %2d  %s", CardColors.AVAILABLE, availablePos.indexOf(p), TUIColors.reset());
            }
        }

        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++) {
                System.out.print(grid[i][j] != null ? grid[i][j] : "     ");
            }
            System.out.println();
        }
    }


    public void printDebug(Object o) {
        System.out.println(o.toString());
    }

    public void printColorDebug(TUIColors color, Object o) {
        System.out.println(color.toString() + o.toString() + TUIColors.reset());
    }

    public void printCardInfo(CardInfo card) {
        String descr = card.getDescription();
        for (CardTextColors cc: CardTextColors.values()) {
            descr = descr.replace(cc.name(), cc.toString() + cc.name() + TUIColors.reset());
        }
        printDebug(descr);
        printDebug("-------------------------------------------------------------");
    }

}
