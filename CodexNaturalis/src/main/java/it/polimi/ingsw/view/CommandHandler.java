package it.polimi.ingsw.view;


import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class CommandHandler implements Runnable {

    private boolean interrupted = false;

    private final BlockingQueue<String> commandQueue;
    private final UserInterface ui;

    private final HashMap<String, Consumer<String>> commands = new HashMap<>();
    private final HashMap<String, String> manPages = new HashMap<>();

    CommandHandler(BlockingQueue<String> commandQueue, UserInterface ui) {
        this.commandQueue = commandQueue;
        this.ui = ui;
        this.initCommands();
    }

    private void initCommands() {
        this.commands.put("help", this::showHelp);
        this.manPages.put("help", this.buildManPage("help", "Shows this page", "command", "shows manual page specific to the specified command"));

        this.commands.put("board", this::showBoard);
        this.manPages.put("board", this.buildManPage("board", "Shows the current state of the board", "player", "shows the specified player's board"));

        this.commands.put("chat", this::showChat);
        this.manPages.put("chat", this.buildManPage("chat", "Opens chat", null, null));

    }

    private void showChat(String arg) {
        ui.printColorDebug(TUIColors.YELLOW, "CHAT");
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
                String[] in = commandQueue.take().split(" ");
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
}
