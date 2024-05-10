package it.polimi.ingsw.view;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class InputHandler implements Runnable {
    private static final String commandChar = ":";
    private final Scanner scanner = new Scanner(System.in);
    private final BlockingQueue<String> commandQueue;
    private final BlockingQueue<String> inputQueue;

    private boolean interrupted = false;

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
