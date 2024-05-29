package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIApp;
import it.polimi.ingsw.view.gui.controller.OpponentBoardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class LeaderboxVBox extends VBox {
    @FXML
    private VBox leaderboardVBox;

    public LeaderboxVBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/components/leaderboard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Sets up the leaderboard
     *
     */
    public void leaderboardSetup(boolean interactable) {
        leaderboardVBox.getChildren().clear();
        int pos = 1;
        for (Map.Entry<String, Integer> e : GUI.getInstance().getData().getSortedLeaderboard().entrySet()) {
            int points = e.getValue();
            HBox leaderboardHBox = new HBox();
            leaderboardHBox.setSpacing(2);
            leaderboardHBox.getChildren().add(new Label(String.format("%d.", pos)));
            String username = e.getKey();
            Label usernameLabel = new Label(username);
            // TODO: Add colors
            // Color.valueOf("BLACK");
            if (!Objects.equals(username, GUI.getInstance().getData().getUsername()) && !GUI.getInstance().getData().getBoards().get(username).isEmpty() && interactable) {
                usernameLabel.hoverProperty().addListener((obs, oldVal, newValue) -> {
                    if (newValue) {
                        OpponentBoardController controller = new OpponentBoardController(username, GUI.getInstance().getData().getBoards().get(username));
                        GUIApp.showOpponentBoard(controller);
                    }
                });
            }
            leaderboardHBox.getChildren().add(usernameLabel);
            Region spacing = new Region();
            HBox.setHgrow(spacing, Priority.ALWAYS);
            leaderboardHBox.getChildren().add(spacing);
            leaderboardHBox.getChildren().add(new Label(String.valueOf(points)));
            leaderboardVBox.getChildren().add(leaderboardHBox);
            pos++;
        }
    }
}
