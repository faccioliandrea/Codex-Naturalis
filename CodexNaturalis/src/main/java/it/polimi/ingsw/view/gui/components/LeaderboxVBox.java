package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIApp;
import it.polimi.ingsw.view.gui.controller.OpponentBoardController;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Class that represents the leaderbox
 */
public class LeaderboxVBox extends VBox {
    @FXML
    private GridPane leaderboardGridPane;

    /**
     * Constructor for the LeaderboxVBox
     *
     */
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
        leaderboardGridPane.getRowConstraints().clear();
        leaderboardGridPane.getChildren().clear();
        for (int i = 0; i < GUI.getInstance().getData().getSortedLeaderboard().entrySet().size(); i++) {
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPrefHeight(25);
            rowConstraint.setVgrow(Priority.NEVER);
            rowConstraint.setValignment(VPos.CENTER);
            leaderboardGridPane.getRowConstraints().add(rowConstraint);
        }
        int pos = 1;
        for (Map.Entry<String, Integer> entry : GUI.getInstance().getData().getSortedLeaderboard().entrySet()) {
            int points = entry.getValue();
            String username = entry.getKey();
            Label usernameLabel = new Label(username + (username.equals(GUI.getInstance().getData().getUsername()) ? " (You)" : ""));
            Circle playerColor = new Circle(5);
            playerColor.setFill(GUIUtility.playerColor(username));
            if (!Objects.equals(username, GUI.getInstance().getData().getUsername()) && !GUI.getInstance().getData().getBoards().get(username).isEmpty() && interactable) {
                ImageView leaderboardBtnGraphics = GUIUtility.createIcon("toggle_board.png");
                leaderboardBtnGraphics.setFitWidth(18);
                leaderboardBtnGraphics.getStyleClass().add("iconButton");
                leaderboardBtnGraphics.setOnMouseClicked(event -> {
                    OpponentBoardController controller = new OpponentBoardController(username, GUI.getInstance().getData().getBoards().get(username));
                    GUIApp.showOpponentBoard(controller);
                });
                leaderboardGridPane.addRow(pos - 1, new Label(String.format("%d.", pos)), playerColor, usernameLabel, new Label(String.valueOf(points)), leaderboardBtnGraphics);
            } else {
                Button leaderboardBtn = new Button();
                leaderboardBtn.setBackground(null);
                leaderboardBtn.setPrefWidth(18);
                leaderboardBtn.setDisable(true);
                leaderboardGridPane.addRow(pos - 1, new Label(String.format("%d.", pos)), playerColor, usernameLabel, new Label(String.valueOf(points)));
            }
            pos++;
        }
    }
}
