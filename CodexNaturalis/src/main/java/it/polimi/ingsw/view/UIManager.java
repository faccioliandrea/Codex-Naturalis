package it.polimi.ingsw.view;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GameStateInfo;
import it.polimi.ingsw.connections.data.StarterData;
import it.polimi.ingsw.connections.data.TurnInfo;
import it.polimi.ingsw.view.data.UIData;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UIManager {
    protected UIData data = new UIData();

    public abstract String askForServerAddr(String defaultAddr);
    public abstract String askForUsername();
    public abstract int askForPlayerNum();
    public abstract String askForLobbyId(ArrayList<String> lobbies);
    public abstract int askForPrivateGoal();
    public abstract boolean askForStarterCardSide();
    public abstract CardInfo askForPlayCard(ArrayList<CardInfo> hand, ArrayList<Point> availablePositions);
    public abstract int askForDrawCard(TurnInfo turnInfo);
    public abstract boolean askForNewGame();

    public abstract void invalidUsername(String username);
    public abstract void welcome(String username);
    public abstract void showCommands();
    public abstract void noLobbies();
    public abstract void joinedLobby();
    public abstract void joinedLobby(String username);
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


    protected boolean isValidIP(String ipAddr, String defaultAddr) {
        Pattern ip_addr_pattern = Pattern.compile(String.format("(^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$)|(%s)", defaultAddr), Pattern.CASE_INSENSITIVE);
        Matcher matcher = ip_addr_pattern.matcher(ipAddr);
        matcher = ip_addr_pattern.matcher(ipAddr);
        return matcher.matches() || ipAddr.isEmpty();
    }

    public UIData getData() {
        return this.data;
    }
}
