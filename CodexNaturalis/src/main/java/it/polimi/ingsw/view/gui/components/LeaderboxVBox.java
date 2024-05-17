package it.polimi.ingsw.view.gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LeaderboxVBox extends VBox {
    @FXML
    private VBox leaderboardVBox;

    public LeaderboxVBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/leaderboard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void leaderboardSetup(LinkedHashMap<String, Integer> leaderboard) {
        leaderboardVBox.getChildren().clear();
        int pos = 1;
        for (Map.Entry<String, Integer> e: leaderboard.entrySet()) {
            int points = e.getValue();
            HBox leaderboardHBox = new HBox();
            leaderboardHBox.setSpacing(2);
            leaderboardHBox.getChildren().add(new Label(String.format("%d.", pos)));
            leaderboardHBox.getChildren().add(new Label(e.getKey()));
            Region spacing = new Region();
            HBox.setHgrow(spacing, Priority.ALWAYS);
            leaderboardHBox.getChildren().add(spacing);
            leaderboardHBox.getChildren().add(new Label(String.valueOf(points)));
            leaderboardVBox.getChildren().add(leaderboardHBox);
            pos++;
        }
    }
}
