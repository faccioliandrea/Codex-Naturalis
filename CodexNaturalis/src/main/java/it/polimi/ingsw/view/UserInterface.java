package it.polimi.ingsw.view;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.connections.data.TurnInfo;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.awt.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

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
        String username;
        do {
            inputQueue.clear();
            this.printColorDebug(TUIColors.PURPLE, "Username: " );
            try {
                username =  inputQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (username.isEmpty()) {
                System.out.println("Please, type a valid username");
            }
        } while(username.isEmpty());
        return username;
    }

    public int askForPlayerNum() {
        int playerNum=0;
        do {
            inputQueue.clear();
            this.printColorDebug(TUIColors.PURPLE, "Insert number of players: 2, 3 or 4");
            try {
                playerNum =  Integer.parseInt(inputQueue.take());
                if (playerNum != 2 && playerNum != 3 && playerNum != 4) {
                    System.out.println("Please, type 2, 3 or 4");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }

        } while(playerNum != 2 && playerNum != 3 && playerNum != 4);
        return playerNum;
    }

    public String askForLobbyId(ArrayList<String> lobbies) {
        this.printColorDebug(TUIColors.CYAN, "Available lobbies:");
        printDebug(lobbies.toString());
        String lobbyId;
        do {
            inputQueue.clear();
            this.printColorDebug(TUIColors.PURPLE, "Type the lobby id you want to join: ");
            try {
                lobbyId =  inputQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!lobbies.contains(lobbyId)) {
                System.out.println("Please, type a valid lobby id");
            }
        } while(!lobbies.contains(lobbyId));
        return lobbyId;
    }


    public int askForPrivateGoal() {
        int choice = -1;
        do {
            inputQueue.clear();
            this.printColorDebug(TUIColors.PURPLE, "Choose your private Goal: [1] [2]");
            try {
                choice =  Integer.parseInt(inputQueue.take()) - 1;
                if (choice != 1 && choice != 0) {
                    System.out.println("Please, type 1 or 2");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }
        } while(choice != 1 && choice != 0);
        return choice;
    }

    public boolean askForStarterCardSide() {
        int choice = -1;
        do {
            inputQueue.clear();
            this.printColorDebug(TUIColors.PURPLE, "Choose your Starter card side: [1] Front [2] Back");
            try {
                choice =  Integer.parseInt(inputQueue.take());
                if (choice != 1 && choice != 2) {
                    System.out.println("Please, type 1 or 2");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }

        } while(choice != 1 && choice != 2);
        return choice == 2;

    }

    public void displayBoard(String username) {
        if (controller == null) {
            this.printColorDebug(TUIColors.RED, "Board not available");
        } else {
            if (username == null && controller.getCurrentTurnInfo() != null) {
                this.displayBoard(controller.getCurrentTurnInfo().getBoard(), controller.getCurrentTurnInfo().getAvailablePositions());
            } else if (username != null && controller.getBoards()!=null) {
                if(controller.getBoards().get(username)==null) {
                    this.printColorDebug(TUIColors.RED, String.format("User %s not found", username));
                } else if (controller.getBoards().get(username).isEmpty()) {
                    this.printColorDebug(TUIColors.RED, String.format("%s's board not available", username));
                } else {
                    this.displayBoard(controller.getBoards().get(username), null);
                }
            } else {
                this.printColorDebug(TUIColors.RED, "Board not available");
            }
        }
    }


    public CardInfo askForPlayCard(ArrayList<CardInfo> hand, ArrayList<Point> availablePositions) {
        int choice = -1;
        do {
            inputQueue.clear();
            this.printColorDebug(TUIColors.PURPLE, "Choose which card do you want to play:");
            hand.forEach(x->printColorDebug(TUIColors.PURPLE, String.format("[%s] %s", hand.indexOf(x)+1, x.getId())));
            try {
                choice =  Integer.parseInt(inputQueue.take()) - 1;
                if (choice != 1 && choice != 0) {
                    System.out.println("Please, choose a valid card");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }

        } while(choice != 1 && choice != 0);

        int choiceSide=-1;
        do {
            inputQueue.clear();
            this.printColorDebug(TUIColors.PURPLE, "Choose the card side: [1] Front [2] Back");
            try {
                choiceSide =  Integer.parseInt(inputQueue.take());
                if (choiceSide != 1 && choiceSide != 2) {
                    System.out.println("Please, type 1 or 2");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }
        } while(choiceSide != 1 && choiceSide != 2);
        hand.get(choice).setFlipped(choiceSide==2);

        int choicePos = -1;
        do {
            inputQueue.clear();
            this.printColorDebug(TUIColors.PURPLE, "Choose the position:");
            String availablePos = "";
            for (Point p: availablePositions) {
                availablePos = availablePos.concat("[" + availablePositions.indexOf(p) + "] ");
            }
            printColorDebug(TUIColors.PURPLE, availablePos);
            try {
                choicePos =  Integer.parseInt(inputQueue.take());
                if (availablePositions.size()-1<choicePos || choicePos < 0) {
                    System.out.println("Please, type a valid position");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }
        } while(availablePositions.size()-1<choicePos || choicePos < 0);
        hand.get(choice).setCoord(availablePositions.get(choicePos));
        return hand.get(choice);
    }

    public int askForDrawCard(TurnInfo turnInfo){
        int choice = -1;
        int deck;
        do {
            inputQueue.clear();
            this.printColorDebug(TUIColors.PURPLE, "Choose which deck do you want to draw from: [1] Resource [2] Gold");
            try {
                choice =  Integer.parseInt(inputQueue.take());
                if (choice != 1 && choice != 2) {
                    System.out.println("Please, type 1 or 2");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }
        } while(choice != 1 && choice != 2);
        deck = choice == 1 ? 10 : 20;

        this.printColorDebug(TUIColors.PURPLE, String.format("Choose which card do you want to draw: [1] %s [2] %s [3] Covered", deck == 10 ? turnInfo.getResourceDeck().get(0).getId() : turnInfo.getGoldDeck().get(0).getId(), deck == 10 ? turnInfo.getResourceDeck().get(1).getId() : turnInfo.getGoldDeck().get(1).getId() ));

        int choiceCard = -1;
        do {
            inputQueue.clear();
            try {
                choiceCard =  Integer.parseInt(inputQueue.take());
                if (choiceCard != 1 && choiceCard != 2 && choiceCard != 3) {
                    System.out.println("Please, type 1, 2 or 3");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }
        } while(choiceCard != 1 && choiceCard != 2 && choiceCard != 3);

        deck = deck + choiceCard;
        return deck;
    }

    // TODO: print symbols hashmap
    public void displayBoard(ArrayList<CardInfo> board, ArrayList<Point> availablePos) {
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

        // TODO: fix table padding
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

    public void printCardInfo(String cardId) {
        controller.getCurrentTurnInfo().getHand().stream()
                .filter(x -> x.getId().equals(cardId))
                .findFirst()
                .ifPresent(this::printCardInfo);
        controller.getCurrentTurnInfo().getResourceDeck().stream()
                .limit(2)
                .filter(x -> x.getId().equals(cardId))
                .findFirst()
                .ifPresent(this::printCardInfo);
        controller.getCurrentTurnInfo().getGoldDeck().stream()
                .limit(2)
                .filter(x -> x.getId().equals(cardId))
                .findFirst()
                .ifPresent(this::printCardInfo);
    }


    public void printCardInfo(CardInfo card) {
        String descr = card.getDescription();
        for (CardTextColors cc: CardTextColors.values()) {
            descr = descr.replace(cc.name(), cc.toString() + cc.name() + TUIColors.reset());
        }
        printDebug(descr);
        printDebug("-------------------------------------------------------------");
    }

    public void printLeaderboard() {
        HashMap<String, Integer> leaderboard = controller.getLeaderboard();
        if (leaderboard == null) {
            this.printColorDebug(TUIColors.RED, "Leaderboard not available");
        } else {
            printLeaderboard(leaderboard);
        }
    }

    public void printLeaderboard(HashMap<String, Integer> leaderboard) {
        printColorDebug(TUIColors.BLUE, "Leaderboard:");
        int pos = 1;
        for (Map.Entry<String, Integer> e:
                leaderboard
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new)) // LinkedHashMap needet to keep the order
                .entrySet()
        ) {
            int points = e.getValue();
            this.printDebug(String.format("\t%d^  %s - %d %s", pos, e.getKey(), points, points == 1 ? "point" : "points"));
            pos++;
        }
    }


}
