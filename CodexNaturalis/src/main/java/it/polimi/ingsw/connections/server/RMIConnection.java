package it.polimi.ingsw.connections.server;

import it.polimi.ingsw.connections.ConnectionStatus;

import java.io.IOException;

public class RMIConnection implements ClientConnection{

    private ConnectionStatus connectionStatus;


        @Override
        public void close() throws IOException {

        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

}
