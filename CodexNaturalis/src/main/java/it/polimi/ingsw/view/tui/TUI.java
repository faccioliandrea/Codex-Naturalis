package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.view.UIManager;

import java.awt.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TUI extends UIManager {
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();

    public TUI() {
        new Thread(new InputHandler(commandQueue, inputQueue)).start();
        new Thread(new CommandHandler(commandQueue, this)).start();
    }

    public String askForServerAddr(String defaultAddr) {
        boolean valid = false;
        String input = "";
        this.printColorDebug(TUIColors.PURPLE, String.format("Please enter the server address (leave blank for %s): ", defaultAddr));
        while(!valid) {
            try {
                input = inputQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            valid = isValidIP(input, defaultAddr);
            if (!valid) {
                this.printColorDebug(TUIColors.RED, "Please enter a valid IP address: ");
            }
        }
        if (input.isEmpty()) {
            return defaultAddr;
        } else {
            return input;
        }
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

    protected void displayBoard(String username) {
        if (data.getBoard().isEmpty()) {
            this.printColorDebug(TUIColors.RED, "Board not available");
        } else {
            if (username == null) {
                this.displayBoard(data.getBoard(), data.getAvailablePositions(), data.getSymbols());
            } else if (!data.getBoards().isEmpty()) {
                if(data.getBoards().get(username) == null) {
                    this.printColorDebug(TUIColors.RED, String.format("User %s not found", username));
                } else if (data.getBoards().get(username).isEmpty()) {
                    this.printColorDebug(TUIColors.RED, String.format("%s's board not available", username));
                } else {
                    this.displayBoard(data.getBoards().get(username), null, null);
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
                if (choice < 0 || choice > hand.size()-1) {
                    System.out.println("Please, choose a valid card");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("Oops, you inserted a string!");
            }

        } while(choice < 0 || choice > hand.size()-1);

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

    public int askForDrawCard(TurnInfo turnInfo) {
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

        deck = deck + choiceCard - 1;
        return deck;
    }

    public boolean askForNewGame() {
        inputQueue.clear();
        printColorDebug(TUIColors.PURPLE, "Would you like to play again? (y/n)");
        try {
            String choice = inputQueue.take();
            return choice.equals("y");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void invalidUsername(String username) {
        this.printColorDebug(TUIColors.RED, username + " is already taken. Please select a new one");
    }

    @Override
    public void welcome(String username) {
        this.printColorDebug(TUIColors.GREEN, "Welcome " + username + "!");
    }

    @Override
    public void showCommands() {
        printColorDebug(TUIColors.YELLOW, "Type :help to see all the available commands.");
    }

    @Override
    public void noLobbies() {
        this.printDebug("There are no lobbies available. Let's create one!");
    }

    @Override
    public void joinedLobby() {
        printDebug("You joined the lobby. Waiting for other players to join...");
    }

    @Override
    public void joinedLobby(String username) {
        printDebug(String.format("%s joined lobby", username));
    }

    @Override
    public void joinedLobbyLast() {
        printDebug("You joined the lobby. The game will start soon!");
    }

    @Override
    public void lobbyFull() {
        printDebug("lobby full");
    }

    @Override
    public void gameStarted(StarterData starterData) {
        printDebug("Game started");
        printColorDebug(TUIColors.CYAN, "Your current hand:");
        starterData.getHand().forEach(x->this.printDebug(String.format("Card id: %s", x.getId())));
        printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description or :hand to see all three");
        printColorDebug(TUIColors.CYAN, "Game shared goals:");
        starterData.getSharedGoals().forEach(this::printGoalInfo);
        printColorDebug(TUIColors.CYAN,"You have to choose one of the following private goals:");
        starterData.getPrivateGoals().forEach(this::printGoalInfo);
        printColorDebug(TUIColors.CYAN,"You have to choose your starter card side:");
        printCardInfo(starterData.getStarterCard());
    }

    @Override
    public void waitingOthersStartingChoice() {
        printDebug("Awesome! Now wait for the other players to choose their private goals and starter cards");
    }

    @Override
    public void otherPlayerTurn(String currentPlayer) {
        printDebug("It's " + currentPlayer + "'s turn! Please wait for your turn!");
    }

    @Override
    public void yourTurn(boolean isLastTurn) {
        printDebug("It's your turn!");
        if(isLastTurn){
            printColorDebug(TUIColors.RED, "This is your last turn.");
        }
        this.printColorDebug(TUIColors.CYAN,"This is your board:");
        this.displayBoard(data.getBoard(), data.getAvailablePositions(), data.getSymbols());
        this.printColorDebug(TUIColors.CYAN, "Your current hand:");
        data.getHand().forEach(x->this.printDebug(String.format("Card id: %s", x.getId())));
        printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description or :hand to see all three");
    }

    @Override
    public void placeCardSuccess() {
        printDebug("Card placed!");
        printDebug("You currently have " + data.getCardPoints() + " points and you will score " + data.getGoalPoints() + " points from the goals!");
        printColorDebug(TUIColors.CYAN,"This is your board:");
        displayBoard(data.getBoard(), null, data.getSymbols());
        printColorDebug(TUIColors.CYAN,"Resource deck:");
        data.getResourceDeck().stream().filter(x -> !x.isFlipped()).forEach(x->printDebug(String.format("Card id: %s", x.getId())));
        printDebug("Covered: " + data.getResourceDeck().get(2).getColor());
        printColorDebug(TUIColors.CYAN,"Gold deck:");
        data.getGoldDeck().stream().filter(x -> !x.isFlipped()).forEach(x->printDebug(String.format("Card id: %s", x.getId())));
        printDebug("Covered: " + data.getGoldDeck().get(2).getColor());
        printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
    }

    @Override
    public void placeCardFailure() {
        printColorDebug(TUIColors.RED, "You don't have the requirements to place the card!");
    }

    @Override
    public void drawCardSuccess() {
        printDebug("Card drawn!");
        printColorDebug(TUIColors.CYAN, "This is your updated hand:");
        data.getHand().forEach(x->this.printDebug(String.format("Card id: %s", x.getId())));
        printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description or :hand to see all three");
    }

    @Override
    public void turnEnded(GameStateInfo gameStateInfo) {
        printDebug(gameStateInfo.getUsername() + " has ended his turn.");
        printColorDebug(TUIColors.CYAN, "This is his current board:");
        displayBoard(gameStateInfo.getBoard(), null, gameStateInfo.getSymbols());
        printDebug("He currently has " + gameStateInfo.getCardsPoints() + " points");
        printColorDebug(TUIColors.CYAN,"These are the first two cards of the resource deck:");
        gameStateInfo.getResourceDeck().stream().filter(x -> !x.isFlipped()).forEach(x->printDebug(String.format("Card id: %s", x.getId())));
        printDebug("Covered: " + gameStateInfo.getResourceDeck().get(2).getColor());
        printColorDebug(TUIColors.CYAN,"These are the first two cards of the gold deck:");
        gameStateInfo.getGoldDeck().stream().filter(x -> !x.isFlipped()).forEach(x->printDebug(String.format("Card id: %s", x.getId())));
        printDebug("Covered: " + gameStateInfo.getGoldDeck().get(2).getColor());
        printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
        vRule();
        printLeaderboard(gameStateInfo.getLeaderboard());
        vRule();
    }

    @Override
    public void gameEnded() {
        printDebug("Game ended!");
        printLeaderboard(data.getLeaderboard());
    }

    @Override
    public void goodbye() {
        printColorDebug(TUIColors.BLUE, "See you next time!");
    }

    // TODO: print symbols hashmap
    public void displayBoard(ArrayList<CardInfo> board, ArrayList<Point> availablePos, Map<CardSymbol, Integer> symbols) {
        boolean padding = availablePos != null && !availablePos.isEmpty();

        int rows = boardGridRows(board, padding);
        int cols = boardGridColumns(board, padding);
        String[][] grid = new String[rows][cols];
        for (CardInfo card: board) {
            Point matrCoord = toMatrixCoord(card.getCoord(), padding);

            try {
                grid[matrCoord.y][matrCoord.x] = String.format("%s %s %s", CardColors.valueOf(card.getColor()), card.getId(), TUIColors.reset());
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        if (availablePos != null) {
            for (Point p: availablePos) {
                Point matrCoord = toMatrixCoord(p, true);

                grid[matrCoord.y][matrCoord.x] = String.format("%s %2d  %s", CardColors.AVAILABLE, availablePos.indexOf(p), TUIColors.reset());
            }
        }

        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++) {
                System.out.print(grid[i][j] != null ? grid[i][j] : "     ");
            }
            System.out.println();
        }

        if (board == data.getBoard()) { // only print this for current player
            printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
        }
        if(symbols!=null){
            printSymbols(symbols);
        }
    }


    public void printDebug(Object o) {
        System.out.println(o.toString());
    }

    public void printColorDebug(TUIColors color, Object o) {
        System.out.println(color.toString() + o.toString() + TUIColors.reset());
    }

    protected void printCardInfo(String cardId) {
        try {
            Optional<CardInfo> card = Stream.of(
                            data.getHand(),
                            data.getBoard(),
                            data.getResourceDeck(),
                            data.getGoldDeck()
                    )
                    .flatMap(Collection::stream)
                    .filter(x -> x.getId().equals(cardId))
                    .findFirst();
            if (card.isPresent()) {
                this.printCardInfo(card.get());
            } else {
                this.printColorDebug(TUIColors.RED, "Card not found");
            }
        } catch (NullPointerException e) {
            printColorDebug(TUIColors.RED, "Cannot get cards now, wait for the game to start");
        }
    }

    private void printCardInfo(CardInfo card) {
        String descr;
        if (card.getCoord() != null) {
            descr = card.isFlipped() ? card.getFrontDescription() : card.getBackDescription();
        } else {
            descr = card.getDescription();
        }
        printDebug(applyColors(descr));
        vRule();
    }

    protected void printGoalsInfo() {
        try {
            ArrayList<GoalInfo> goals = data.getGoals();
            if (goals.isEmpty()) {
                printColorDebug(TUIColors.RED, "Goals not available, wait for the game to start");
            } else {
                goals.forEach(this::printGoalInfo);
            }
        } catch (NullPointerException e) {
            printColorDebug(TUIColors.RED, "Goals not available, wait for the game to start");
        }
    }

    private void printGoalInfo(GoalInfo goal) {
        printDebug(applyColors(goal.getDescription()));
        vRule();
    }

    private void printSymbols(Map<CardSymbol, Integer> symbols){
        symbols.forEach((k, v) -> printDebug(String.format("%s: %d", applyColors(k.toString()), v)));
    }

    protected void printLeaderboard() {
        try {
            printLeaderboard(data.getLeaderboard());
        } catch (NullPointerException e) {
            this.printColorDebug(TUIColors.RED, "Leaderboard not available, wait for the game to start");
        }
    }

    private void printLeaderboard(Map<String, Integer> leaderboard) {
        if (leaderboard == null || leaderboard.isEmpty()) {
            this.printColorDebug(TUIColors.RED, "Leaderboard not available, wait for the game to start");
            return;
        }
        printColorDebug(TUIColors.BLUE, "Leaderboard:");
        int pos = 1;
        for (Map.Entry<String, Integer> e:
                leaderboard.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new)) // LinkedHashMap needed to keep the order
                .entrySet()
        ) {
            int points = e.getValue();
            this.printDebug(String.format("\t%d^  %s - %d %s", pos, e.getKey(), points, points == 1 ? "point" : "points"));
            pos++;
        }
    }

    protected void printHand() {
        try {
            data.getHand().forEach(this::printCardInfo);
        } catch (NullPointerException e) {
            printColorDebug(TUIColors.RED, "Hand not available, wait for the game to start");
        }
    }

    private String applyColors(String s) {
        for (CardTextColors cc: CardTextColors.values()) {
            s = s.replace(cc.name(), cc.toString() + cc.name() + TUIColors.reset());
        }
        return s;
    }

    private void vRule() {
        printDebug("-------------------------------------------------------------");
    }
}
