package it.polimi.ingsw.connections;

import java.io.IOException;

public interface StreamRunnable extends Runnable{
    public void close() throws IOException;

//    public IOException getException();
}
