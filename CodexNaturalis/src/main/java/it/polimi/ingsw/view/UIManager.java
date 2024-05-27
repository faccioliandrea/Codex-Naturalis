package it.polimi.ingsw.view;

import it.polimi.ingsw.chat.ClientChatHandler;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.view.data.UIData;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UIManager {
    protected UIData data = new UIData();
    protected ClientChatHandler chatHandler;

    private static int boardMinX = 0;
    private static int boardMaxY = 0;

    public abstract String askForServerAddr(String defaultAddr);
    public abstract String askForUsername();
    public abstract int askForPlayerNum();
    public abstract String askForLobbyId(ArrayList<String> lobbies);
    public abstract int askForPrivateGoal();
    public abstract boolean askForStarterCardSide();
    public abstract CardInfo askForPlayCard();
    public abstract int askForDrawCard();
    public abstract boolean askForNewGame();
    public abstract void invalidUsername(String username);
    public abstract void welcome(String username);
    public abstract void showCommands();
    public abstract void noLobbies();
    public abstract void joinedLobby();
    public abstract void joinedLobby(String username);
    public abstract void lobbyCreated(String lobbyId);
    public abstract void playerDisconnected(String username, boolean gameStarted);
    public abstract void playerReconnected(String username) ;
    public abstract void reconnectionState() ;
    public abstract void joinedLobbyLast();
    public abstract void lobbyFull();
    public abstract void gameStarted(StarterData starterData);
    public abstract void waitingOthersStartingChoice();
    public abstract void otherPlayerTurn(String currentPlayer);
    public abstract void yourTurn(boolean isLastTurn);
    public abstract void placeCardSuccess();
    public abstract void placeCardFailure();
    public abstract void drawCardSuccess();
    public abstract void turnEnded(GameStateInfo gameStateInfo);
    public abstract void gameEnded();
    public abstract void goodbye();
    public abstract void showErrorMessage(String message);
    public abstract void showChat();
    public abstract void sendMessage(String raw);
    public abstract void noOtherPlayerConnected() ;
    public abstract void connectingToServer();



    public static boolean isValidIP(String ipAddr, String defaultAddr) {
        Pattern ip_addr_pattern = Pattern.compile(String.format("(^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$)|(%s)", defaultAddr), Pattern.CASE_INSENSITIVE);
        Matcher matcher = ip_addr_pattern.matcher(ipAddr);
        return matcher.matches() || ipAddr.isEmpty();
    }

    public static int boardGridColumns(ArrayList<CardInfo> board, boolean padding) {
        OptionalInt x_max = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.x).max();
        OptionalInt x_min = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.x).min();
        if (!x_min.isPresent() || !x_max.isPresent()) {
            return 0;
        }
        int width = x_max.getAsInt() - x_min.getAsInt() + 1;
        boardMinX = x_min.getAsInt();

        return width + (padding ? 2 : 0);
    }

    public static int boardGridRows(ArrayList<CardInfo> board, boolean padding) {
        OptionalInt y_max = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.y).max();
        OptionalInt y_min = board.stream().map(CardInfo::getCoord).mapToInt(p -> p.y).min();
        if (!y_min.isPresent() || !y_max.isPresent()) {
            return 0;
        }
        int height = y_max.getAsInt() - y_min.getAsInt() + 1;
        boardMaxY = y_max.getAsInt();

        return height + (padding ? 2 : 0);
    }

    public static Point toMatrixCoord(Point p, boolean padding) {
        int i = (boardMaxY + (padding ? 1 : 0)) - p.y;
        int j = p.x - (boardMinX - (padding ? 1 : 0));

        return new Point(j, i);
    }

    public UIData getData() {
        return this.data;
    }


    public void setData(UIData data) {
        this.data = data;
    }

    public void setChatHandler(ClientChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }
}
