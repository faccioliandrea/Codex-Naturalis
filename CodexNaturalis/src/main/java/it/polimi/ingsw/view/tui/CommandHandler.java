package it.polimi.ingsw.view.tui;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * Class that handles the commands from the user
 */
public class CommandHandler implements Runnable {

    private boolean interrupted = false;

    private final BlockingQueue<String> commandQueue;
    private final TUI ui;

    private final HashMap<String, Consumer<String>> commands = new HashMap<>();
    private final HashMap<String, String> manPages = new LinkedHashMap<>();

    CommandHandler(BlockingQueue<String> commandQueue) {
        this.commandQueue = commandQueue;
        this.ui = (TUI) TUI.getInstance();
        this.initCommands();
    }

    private void initCommands() {
        initCommand("help", "Shows this page", "command", "displays manual page specific to the specified command", this::showHelp);
        initCommand("board", "Displays the current state of the board", "player", "displays the specified player's board", this::showBoard);
        initCommand("hand", "Displays cards in the hand", null,  null, this::showHand);
        initCommand("card", "Displays the specified card info", "card id", "(not null)", this::showCardInfo);
        initCommand("goals", "Displays all 3 goals info", null,  null, this::showGoalsInfo);
        initCommand("points", "Displays card and goals points", null,  null, this::showPoints);
        initCommand("lead", "Displays the leaderboard", null, null, this::showLeaderboard);
        initCommand("chat", "Displays last 5 chat messages", null, null, this::showChat);
        initCommand("msg", "Write a new message", "message", "write a message in the chat, use @user to send it to a specific person", this::sendMessage);
    }

    private void showPoints(String arg) {
        ui.printPoints();
    }

    private void initCommand(String commandName, String commandDoc, String argName, String argDoc, Consumer<String> cmd) {
        this.commands.put(commandName, cmd);
        this.manPages.put(commandName, this.buildManPage(commandName, commandDoc, argName,  argDoc));
    }

    private void showChat(String arg) {
        ui.showChat();
    }

    private void sendMessage(String arg) {
        ui.sendMessage(arg);
    }

    private String buildManPage(String commandName, String commandDoc, String argName, String argDoc) {
        if (argName == null || argDoc == null) {
            return String.format(":%s\t\t%s", commandName, commandDoc);
        } else {
            return String.format(":%s\t\t%s%n\t\t\t[%s]: %s", commandName, commandDoc, argName, argDoc);
        }
    }

    @Override
    public void run() {
        while (!interrupted) {
            try {
                String[] in = commandQueue.take().split(" ", 2);
                Consumer<String> c = commands.get(in[0]);
                if (c == null) {
                    this.commandNotFound(in[0]);
                } else {
                    if (in.length > 1) {
                        c.accept(in[1]);
                    } else {
                        c.accept(null);
                    }
                }
            } catch (InterruptedException e) {
                this.interrupted = true;
            }
        }
    }

    private void showHelp(String arg) {
        ui.printColorDebug(TUIColors.GREEN, "MANUAL:");
        if (arg == null) {
            for (String s : manPages.values()) {
                ui.printColorDebug(TUIColors.YELLOW, s);
            }
        } else {
            String manPage = manPages.get(arg);
            if (manPage == null) {
                this.commandNotFound(arg);
            } else {
                ui.printColorDebug(TUIColors.YELLOW, manPage);
            }
        }
        ui.printColorDebug(TUIColors.GREEN, "end");
    }

    private void showBoard(String arg) {
        ui.displayBoard(arg);
    }

    private void commandNotFound(String command) {
        ui.printColorDebug(TUIColors.RED, String.format("Command \"%s\" not found", command));
    }

    private void showLeaderboard(String arg) {
        ui.printLeaderboard();
    }

    private void showCardInfo(String arg) {
        ui.printCardInfo(arg);
    }

    private void showGoalsInfo(String s) {
        ui.printGoalsInfo();
    }

    private void showHand(String s) {
        ui.printHand();
    }
}
