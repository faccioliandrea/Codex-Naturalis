package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.chat.ClientChatHandler;
import it.polimi.ingsw.connections.data.*;
import it.polimi.ingsw.connections.enums.LogInResponse;
import it.polimi.ingsw.model.enumeration.CardSymbol;
import it.polimi.ingsw.view.UIManager;
import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.view.UIMessagesConstants;
import it.polimi.ingsw.view.tui.enums.*;

import java.awt.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class TUI extends UIManager {
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private final Object lock = new Object();

    private static final int CHAT_HISTORY_LENGTH = 10;

    /**
     * Constructor
     */
    public TUI() {
        instance = this;
        BlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();
        new Thread(new InputHandler(commandQueue, inputQueue)).start();
        new Thread(new CommandHandler(commandQueue)).start();
    }


    @Override
    public String askForServerAddr(String defaultAddr) {
        synchronized (lock) {
            boolean valid = false;
            String input = "";
            this.printColorDebug(TUIColors.PURPLE, String.format("Please enter the server address (leave blank for %s): ", defaultAddr));
            while (!valid) {
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
    }

    @Override
    public String askForUsername() {
        synchronized (lock) {
            this.printColorDebug(TUIColors.PURPLE, "Username: ");
            try {
                return inputQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int askForPlayerNum() {
        synchronized (lock) {
            int playerNum = 0;
            do {
                inputQueue.clear();
                this.printColorDebug(TUIColors.PURPLE, "Insert number of players: 2, 3 or 4");
                try {
                    playerNum = Integer.parseInt(inputQueue.take());
                    if (playerNum != 2 && playerNum != 3 && playerNum != 4) {
                        System.out.println("Please, type 2, 3 or 4");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NumberFormatException e) {
                    System.out.println("Oops, you inserted a string!");
                }

            } while (playerNum != 2 && playerNum != 3 && playerNum != 4);
            return playerNum;
        }
    }

    @Override
    public String askForLobbyId(ArrayList<String> lobbies) {
        synchronized (lock) {
            this.printColorDebug(TUIColors.CYAN, "Available lobbies:");
            printDebug(lobbies.toString());
            String lobbyId;
            do {
                inputQueue.clear();
                this.printColorDebug(TUIColors.PURPLE, "Type the lobby id you want to join (leave blank to create a new lobby):");
                try {
                    lobbyId = inputQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!lobbies.contains(lobbyId) && !lobbyId.isEmpty()) {
                    System.out.println("Please, type a valid lobby id");
                }
            } while (!lobbies.contains(lobbyId) && !lobbyId.isEmpty());
            return lobbyId;
        }
    }

    @Override
    public int askForPrivateGoal() {
        synchronized (lock) {
            int choice = -1;
            do {
                inputQueue.clear();
                this.printColorDebug(TUIColors.PURPLE, "Choose your private Goal: [1] [2]");
                try {
                    choice = Integer.parseInt(inputQueue.take()) - 1;
                    if (choice != 1 && choice != 0) {
                        System.out.println("Please, type 1 or 2");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NumberFormatException e) {
                    System.out.println("Oops, you inserted a string!");
                }
            } while (choice != 1 && choice != 0);
            return choice;
        }
    }

    @Override
    public boolean askForStarterCardSide() {
        synchronized (lock) {
            int choice = -1;
            do {
                inputQueue.clear();
                this.printColorDebug(TUIColors.PURPLE, "Choose your Starter card side: [1] Front [2] Back");
                try {
                    choice = Integer.parseInt(inputQueue.take());
                    if (choice != 1 && choice != 2) {
                        System.out.println("Please, type 1 or 2");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NumberFormatException e) {
                    System.out.println("Oops, you inserted a string!");
                }

            } while (choice != 1 && choice != 2);
            return choice == 2;
        }

    }

    /**
     * Displays the board of a specific player
     * @param username the username of the player
     */
    protected void displayBoard(String username) {
        if (data.getBoard().isEmpty()) {
            this.printColorDebug(TUIColors.RED, "Board not available");
        } else {
            if (username == null) {
                this.displayBoard(data.getBoard(), data.getAvailablePositions(), data.getSymbols());
            } else if (!data.getBoards().isEmpty()) {
                if (data.getBoards().get(username) == null) {
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

    @Override
    public CardInfo askForPlayCard() {
        synchronized (lock) {
            int choice = -1;
            do {
                inputQueue.clear();
                this.printColorDebug(TUIColors.PURPLE, "Choose which card do you want to play:");
                this.data.getHand().forEach(x -> printColorDebug(TUIColors.PURPLE, String.format("[%s] %s", this.data.getHand().indexOf(x) + 1, x.getId())));
                try {
                    choice = Integer.parseInt(inputQueue.take()) - 1;
                    if (choice < 0 || choice > this.data.getHand().size() - 1) {
                        System.out.println("Please, choose a valid card");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NumberFormatException e) {
                    System.out.println("Oops, you inserted a string!");
                }

            } while (choice < 0 || choice > this.data.getHand().size() - 1);

            int choiceSide = -1;
            do {
                inputQueue.clear();
                this.printColorDebug(TUIColors.PURPLE, "Choose the card side: [1] Front [2] Back");
                try {
                    choiceSide = Integer.parseInt(inputQueue.take());
                    if (choiceSide != 1 && choiceSide != 2) {
                        System.out.println("Please, type 1 or 2");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NumberFormatException e) {
                    System.out.println("Oops, you inserted a string!");
                }
            } while (choiceSide != 1 && choiceSide != 2);
            this.data.getHand().get(choice).setFlipped(choiceSide == 2);

            int choicePos = -1;
            do {
                inputQueue.clear();
                this.printColorDebug(TUIColors.PURPLE, "Choose the position:");
                String availablePos = "";
                for (Point p : this.data.getAvailablePositions()) {
                    availablePos = availablePos.concat("[" + this.data.getAvailablePositions().indexOf(p) + "] ");
                }
                printColorDebug(TUIColors.PURPLE, availablePos);
                try {
                    choicePos = Integer.parseInt(inputQueue.take());
                    if (this.data.getAvailablePositions().size() - 1 < choicePos || choicePos < 0) {
                        System.out.println("Please, type a valid position");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NumberFormatException e) {
                    System.out.println("Oops, you inserted a string!");
                }
            } while (this.data.getAvailablePositions().size() - 1 < choicePos || choicePos < 0);
            this.data.getHand().get(choice).setCoord(this.data.getAvailablePositions().get(choicePos));
            return this.data.getHand().get(choice);
        }
    }

    @Override
    public Decks askForDrawCard() {
        synchronized (lock) {
            int choice = -1;
            Decks deck;
            do {
                inputQueue.clear();
                this.printColorDebug(TUIColors.PURPLE, "Choose which deck do you want to draw from: [1] Resource [2] Gold");
                try {
                    choice = Integer.parseInt(inputQueue.take());
                    if (choice != 1 && choice != 2) {
                        System.out.println("Please, type 1 or 2");
                    } else if (choice == 1 && this.data.getResourceDeck().isEmpty()) {
                        this.printColorDebug(TUIColors.RED, "Deck is empty, you can't draw from it. Choose the gold deck.");
                    } else if (choice == 2 && this.data.getGoldDeck().isEmpty()) {
                        this.printColorDebug(TUIColors.RED, "Deck is empty, you can't draw from it. Choose the resource deck.");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NumberFormatException e) {
                    System.out.println("Oops, you inserted a string!");
                }
            } while (choice != 1 && choice != 2);
            deck = choice == 1 ? Decks.RESOURCE_DECK : Decks.GOLD_DECK;

            this.printColorDebug(TUIColors.PURPLE, String.format("Choose which card do you want to draw: [1] %s [2] %s [3] Covered", deck == Decks.RESOURCE_DECK ? this.data.getResourceDeck().get(0).getId() : this.data.getGoldDeck().get(0).getId(), deck == Decks.RESOURCE_DECK ? this.data.getResourceDeck().get(1).getId() : this.data.getGoldDeck().get(1).getId()));

            int choiceCard = -1;
            do {
                inputQueue.clear();
                try {
                    choiceCard = Integer.parseInt(inputQueue.take());
                    if (choiceCard != 1 && choiceCard != 2 && choiceCard != 3) {
                        System.out.println("Please, type 1, 2 or 3");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (NumberFormatException e) {
                    System.out.println("Oops, you inserted a string!");
                }
            } while (choiceCard != 1 && choiceCard != 2 && choiceCard != 3);

            deck = Decks.getDeck(deck.getValue() + choiceCard - 1);
            return deck;
        }
    }

    @Override
    public boolean askForNewGame() {
        synchronized (lock) {
            inputQueue.clear();
            printColorDebug(TUIColors.PURPLE, "Would you like to play again? (y/n)");
            try {
                String choice = inputQueue.take();
                return choice.equals("y");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void invalidUsername(String username, LogInResponse status) {
        synchronized (lock) {
            if (status.equals(LogInResponse.USERNAME_TAKEN))
                this.printColorDebug(TUIColors.RED, username + UIMessagesConstants.usernameTaken);
            if (status.equals(LogInResponse.INVALID_USERNAME))
                this.printColorDebug(TUIColors.RED, UIMessagesConstants.invalidUsername);
        }
    }

    @Override
    public void welcome(String username) {
        synchronized (lock) {
            this.printColorDebug(TUIColors.GREEN, "Welcome " + username + "!");
        }
    }

    @Override
    public void showCommands() {
        synchronized (lock) {
            printColorDebug(TUIColors.YELLOW, "Type :help to see all the available commands.");
        }
    }

    @Override
    public void noLobbies() {
        synchronized (lock) {
            this.printDebug("There are no lobbies available. Let's create one!");
        }
    }

    @Override
    public void joinedLobby() {
        synchronized (lock) {
            printDebug("You joined the lobby. Waiting for other players to join...");
        }
    }

    @Override
    public void joinedLobby(String username) {
        synchronized (lock) {
            printDebug(String.format("%s joined lobby", username));
        }
    }

    @Override
    public void lobbyCreated(String lobbyId){
        synchronized (lock) {
            printDebug(String.format("Lobby %s created", lobbyId));
        }
    }

    @Override
    public void playerDisconnected(String username, boolean gameStarted) {
        printColorDebug(TUIColors.RED, String.format("%s disconnected. %s", username, gameStarted ? "The game will continue skipping his turns." : ""));
    }

    @Override
    public void playerReconnected(String username) {
        printColorDebug(TUIColors.GREEN, String.format("%s reconnected.", username));
    }

    @Override
    public void reconnectionState() {
        synchronized (lock) {
            printColorDebug(TUIColors.GREEN, String.format("Welcome back %s!", data.getUsername()));
            this.printColorDebug(TUIColors.CYAN, "This is your board:");
            this.displayBoard(data.getBoard(), data.getAvailablePositions(), data.getSymbols());
            this.printColorDebug(TUIColors.CYAN, "Your current hand:");
            data.getHand().forEach(x -> this.printDebug(String.format("Card id: %s", x.getId())));
            printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description or :hand to see all three");
        }
    }

    @Override
    public void joinedLobbyLast() {
        synchronized (lock) {
            printDebug("You joined the lobby. The game will start soon!");
        }
    }

    @Override
    public void lobbyFull() {
        synchronized (lock) {
            printDebug("lobby full");
        }
    }

    @Override
    public void gameStarted(StarterData starterData) {
        synchronized (lock) {
            printDebug("Game started");
            printColorDebug(TUIColors.CYAN, "Your current hand:");
            starterData.getHand().forEach(x -> this.printDebug(String.format("Card id: %s", x.getId())));
            printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description or :hand to see all three");
            printColorDebug(TUIColors.CYAN, "Game shared goals:");
            starterData.getSharedGoals().forEach(this::printGoalInfo);
            printColorDebug(TUIColors.CYAN, "You have to choose one of the following private goals:");
            starterData.getPrivateGoals().forEach(this::printGoalInfo);
            printColorDebug(TUIColors.CYAN, "You have to choose your starter card side:");
            printCardInfo(starterData.getStarterCard());
        }
    }

    @Override
    public void waitingOthersStartingChoice() {
        synchronized (lock) {
            printDebug("Awesome! Now wait for the other players to choose their private goals and starter cards");
        }
    }

    @Override
    public void otherPlayerTurn(String currentPlayer) {
        synchronized (lock) {
            printDebug("It's " + currentPlayer + "'s turn! Please wait for your turn!");
        }
    }

    @Override
    public void yourTurn(boolean isLastTurn) {
        synchronized (lock) {
            printDebug("It's your turn!");
            if (isLastTurn) {
                printColorDebug(TUIColors.RED, "This is your last turn.");
            }
            this.printColorDebug(TUIColors.CYAN, "This is your board:");
            this.displayBoard(data.getBoard(), data.getAvailablePositions(), data.getSymbols());
            this.printColorDebug(TUIColors.CYAN, "Your current hand:");
            data.getHand().forEach(x -> this.printDebug(String.format("Card id: %s", x.getId())));
            printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description or :hand to see all three");
        }
    }

    @Override
    public void placeCardSuccess() {
        synchronized (lock) {
            printDebug("Card placed!");
            printDebug("You currently have " + data.getCardPoints() + " points and you will score " + data.getGoalPoints() + " points from the goals!");
            printColorDebug(TUIColors.CYAN, "This is your board:");
            displayBoard(data.getBoard(), null, data.getSymbols());
            printColorDebug(TUIColors.CYAN, "Resource deck:");
            data.getResourceDeck().stream().filter(x -> !x.isFlipped()).forEach(x -> printDebug(String.format("Card id: %s", x.getId())));
            printDebug("Covered: " + data.getResourceDeck().get(2).getColor());
            printColorDebug(TUIColors.CYAN, "Gold deck:");
            data.getGoldDeck().stream().filter(x -> !x.isFlipped()).forEach(x -> printDebug(String.format("Card id: %s", x.getId())));
            printDebug("Covered: " + data.getGoldDeck().get(2).getColor());
            printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
        }
    }

    @Override
    public void placeCardFailure() {
        synchronized (lock) {
            printColorDebug(TUIColors.RED, UIMessagesConstants.placeCardFailure);
        }
    }

    @Override
    public void drawCardSuccess() {
        synchronized (lock) {
            printDebug("Card drawn!");
            printColorDebug(TUIColors.CYAN, "This is your updated hand:");
            data.getHand().forEach(x -> this.printDebug(String.format("Card id: %s", x.getId())));
            printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description or :hand to see all three");
        }
    }

    @Override
    public void turnEnded(GameStateInfo gameStateInfo) {
        synchronized (lock) {
            printDebug(gameStateInfo.getLastPlayer() + " has ended his turn.");
            printColorDebug(TUIColors.CYAN, "This is his current board:");
            displayBoard(gameStateInfo.getBoards().get(gameStateInfo.getLastPlayer()), null, null);
            printDebug("He currently has " + gameStateInfo.getLeaderboard().get(gameStateInfo.getLastPlayer()) + " points");
            printColorDebug(TUIColors.CYAN, "These are the first two cards of the resource deck:");
            gameStateInfo.getResourceDeck().stream().filter(x -> !x.isFlipped()).forEach(x -> printDebug(String.format("Card id: %s", x.getId())));
            printDebug("Covered: " + gameStateInfo.getResourceDeck().get(2).getColor());
            printColorDebug(TUIColors.CYAN, "These are the first two cards of the gold deck:");
            gameStateInfo.getGoldDeck().stream().filter(x -> !x.isFlipped()).forEach(x -> printDebug(String.format("Card id: %s", x.getId())));
            printDebug("Covered: " + gameStateInfo.getGoldDeck().get(2).getColor());
            printColorDebug(TUIColors.YELLOW, "Type :card [cardId] to see the card description");
            vRule();
            printLeaderboard(data.getSortedLeaderboard());
            vRule();
        }
    }

    @Override
    public void gameEnded() {
        synchronized (lock) {
            printDebug("Game ended!");
            String[] messages = {
                    "You won!",
                    "You won 2nd place!",
                    "You won 3rd place!",
                    "You arrived last, better luck next time!"
            };
            ArrayList<String> orderedUsernames = new ArrayList<>(data.getSortedLeaderboard().keySet());
            printColorDebug(TUIColors.YELLOW, messages[orderedUsernames.indexOf(data.getUsername())]);
            printLeaderboard(data.getSortedLeaderboard());
        }
    }

    @Override
    public void goodbye() {
        synchronized (lock) {
            printColorDebug(TUIColors.BLUE, "See you next time!");
            System.exit(0);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        printColorDebug(TUIColors.RED, message);
        System.exit(0);
    }

    @Override
    public void connectingToServer() {
        printColorDebug(TUIColors.RED, "Could not connect to the server. Retrying...");
    }

    @Override
    public void noOtherPlayerConnected() {
        printColorDebug(TUIColors.RED, UIMessagesConstants.noOtherPlayerConnected);
    }
    @Override
    public void serverOfflineMessage() {
        printColorDebug(TUIColors.RED, UIMessagesConstants.serverOfflineMessage);
        System.exit(0);
    }

    /**
     * Displays the board
     * @param board board to display
     * @param availablePos array of available positions
     * @param symbols map of symbols present on the game table
     */
    public void displayBoard(ArrayList<CardInfo> board, ArrayList<Point> availablePos, Map<CardSymbol, Integer> symbols) {
        boolean padding = availablePos != null && !availablePos.isEmpty();

        int rows = boardGridRows(board, padding);
        int cols = boardGridColumns(board, padding);
        String[][] grid = new String[rows][cols];
        for (CardInfo card: board) {
            Point matrCoord = toMatrixCoord(card.getCoord(), padding);

            grid[matrCoord.y][matrCoord.x] = String.format("%s %s %s", CardColors.valueOf(card.getColor()), card.getId(), TUIColors.reset());

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


    /**
     * Prints a generic object
     * @param o object to print
     */
    public void printDebug(Object o) {
        System.out.println(o.toString());
    }


    /**
     * Prints a colored message
     * @param color TUIColour to use
     * @param o object to print
     */
    public void printColorDebug(TUIColors color, Object o) {
        System.out.println(color.toString() + o.toString() + TUIColors.reset());
    }

    /**
     * Prints a card data
     * @param cardId card id
     */
    protected void printCardInfo(String cardId) {
        if (cardId == null || cardId.isEmpty()) {
            printColorDebug(TUIColors.RED, "Please specify a card (type :card [cardId])");
            return;
        }
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
        String description;
        if (card.getCoord() != null) {
            description = card.isFlipped() ? card.getFrontDescription() : card.getBackDescription();
        } else {
            description = card.getDescription();
        }
        printDebug(applyColors(description));
        vRule();
    }

    /**
     * Prints the goal
     */
    protected void printGoalsInfo() {
        try {
            ArrayList<GoalInfo> goalsPub = data.getPublicGoals();
            GoalInfo goalsPriv = data.getPrivateGoal();
            if (goalsPub.isEmpty() || goalsPriv == null) {
                printColorDebug(TUIColors.RED, "Goals not available, wait for the game to start");
            } else {
                printColorDebug(TUIColors.CYAN, "Public goals:");
                goalsPub.forEach(this::printGoalInfo);
                printColorDebug(TUIColors.CYAN, "Private goal:");
                printGoalInfo(goalsPriv);
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

    /**
     * Prints the leaderboard
     */
    protected void printLeaderboard() {
        try {
            printLeaderboard(data.getSortedLeaderboard());
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
        for (Map.Entry<String, Integer> e: leaderboard.entrySet()) {
            int points = e.getValue();
            this.printDebug(String.format("\t%d^  %s - %d %s", pos, colorPlayerName(e.getKey()), points, points == 1 ? "point" : "points"));
            pos++;
        }
    }

    @Override
    public void showChat() {
        if (!ClientChatHandler.isRunning()) {
            printColorDebug(TUIColors.RED, "Chat not available, you need to join a lobby first!");
        } else {
            vRule();
            data.getLastMessages(CHAT_HISTORY_LENGTH).forEach(this::printChatMessage);
            vRule();
        }
    }

    @Override
    public void sendMessage(String raw) {
        try {
            ClientChatHandler.sendChatMessage(raw);
            printColorDebug(TUIColors.YELLOW, "Message sent!");
        } catch (NullPointerException e) {
            printColorDebug(TUIColors.RED, "Chat not available, you need to join a lobby first!");
        }
    }

    @Override
    public void messageReceived() {
        printColorDebug(TUIColors.YELLOW, "New message received! Type :chat to open the chat.");
    }

    /**
     * Prints the cards in the player's hand
     */
    protected void printHand() {
        try {
            data.getHand().forEach(this::printCardInfo);
        } catch (NullPointerException e) {
            printColorDebug(TUIColors.RED, "Hand not available, wait for the game to start");
        }
    }

    /**
     * Prints the current points
     */
    protected void printPoints() {
        if (!data.getBoard().isEmpty()) {
            printDebug(
                    String.format(
                            "You currently have %s%d%s points from Cards and %s%d%s points from Goals",
                            TUIColors.YELLOW,
                            data.getCardPoints(),
                            TUIColors.reset(),
                            TUIColors.YELLOW,
                            data.getGoalPoints(),
                            TUIColors.reset()
                    )
            );
        } else {
            printColorDebug(TUIColors.RED, "Points not available, wait for the game to start");
        }
    }

    private String applyColors(String s) {
        for (CardTextColors cc: CardTextColors.values()) {
            s = s.replace(cc.name(), cc + cc.name() + TUIColors.reset());
        }
        return s;
    }

    private void vRule() {
        printDebug("-------------------------------------------------------------");
    }

    private String colorPlayerName(String username) {
        try {
            return TUIColors.valueOf(data.getPlayerColors().get(username).toString()) + username + TUIColors.reset();
        } catch (NullPointerException e) { // not in game, still in lobby
            return username;
        }
    }

    private void printChatMessage(ChatMessageData msg) {
        if (msg.getRecipient() == null) {
            printDebug(String.format("[%s@everyone]: %s", colorPlayerName(msg.getSender()), msg.getContent()));
        } else {
            printDebug(String.format("[%s@%s]: %s", colorPlayerName(msg.getSender()), colorPlayerName(msg.getRecipient()), msg.getContent()));
        }
    }
}
