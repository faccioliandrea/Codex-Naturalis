package it.polimi.ingsw.view.tui;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

/**
 * Class that handles the input from the user
 */
public class InputHandler implements Runnable {
    private static final String commandChar = ":";
    private final Scanner scanner = new Scanner(System.in);
    private final BlockingQueue<String> commandQueue;
    private final BlockingQueue<String> inputQueue;

    private boolean interrupted = false;

    /**
     * Constructor
     * @param commandQueue Blocking queue for the commands
     * @param inputQueue Blocking queue for the input
     */
    InputHandler(BlockingQueue<String> commandQueue, BlockingQueue<String> inputQueue) {
        this.commandQueue = commandQueue;
        this.inputQueue = inputQueue;
    }

    @Override
    public void run() {
        while (!interrupted) {
            try {
                String in = scanner.nextLine().trim();
                if (in.startsWith(commandChar)) {
                    commandQueue.put(in.substring(1));
                } else {
                    inputQueue.put(in);
                }
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
    }
}
