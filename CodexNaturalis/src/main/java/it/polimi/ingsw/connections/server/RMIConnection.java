package it.polimi.ingsw.connections.server;

import java.io.IOException;

public class RMIConnection implements ClientConnection{

    private boolean isAlive;

        @Override
        public boolean getStatus() {
            return isAlive;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

}
