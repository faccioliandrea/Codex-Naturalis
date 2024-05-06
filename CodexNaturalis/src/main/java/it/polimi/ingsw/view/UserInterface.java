package it.polimi.ingsw.view;

import it.polimi.ingsw.connections.data.CardInfo;

import java.awt.*;
import java.util.*;

public class UserInterface { /* MARK: just for testing */

//    public ClientController controller;

    public UserInterface(/* ClientController controller*/) { }

    public String askForUsername() {
        this.printColorDebug(TUIColors.PURPLE, "Username: " );
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().trim();
    }

    public int askForPlayerNum() {
        this.printColorDebug(TUIColors.PURPLE, "Lobby player number: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    public String askForLobbyId() {
        this.printColorDebug(TUIColors.PURPLE, "Lobby Id: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public int askForPrivateGoal() {
        this.printColorDebug(TUIColors.PURPLE, "Choose your private Goal: [1] [2]");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt()-1;
    }

    public boolean askForStarterCardSide() {
        this.printColorDebug(TUIColors.PURPLE, "Choose your Starter card side: [1] Front [2] Back");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt() == 2;
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
//        System.out.printf("%d x %d%n", height, width);
        for (CardInfo card: board) {

            int i = (y_max.getAsInt() + 1)  - card.getCoord().y;
            int j = card.getCoord().x - (x_min.getAsInt() - 1);

//            System.out.printf("xy:(%d, %d) -> ij:(%d,%d)%n", card.getCoord().x, card.getCoord().y, i, j);

//            System.err.println(CardColors.valueOf(card.getColor()) + card.getColor() + ": " + CardColors.valueOf(card.getColor()).toString().replace("\u001B", "") + TUIColors.reset());
            try {
                grid[i][j] = String.format("%s %s %s", CardColors.valueOf(card.getColor()), card.getId(), TUIColors.reset());
            } catch (IndexOutOfBoundsException e) {
                System.err.printf("i: %d, j: %d%n", i , j);
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
    }

}
