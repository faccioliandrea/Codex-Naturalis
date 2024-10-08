package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.LeaderboxVBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class GameEndController implements Initializable {
    @FXML
    private Group leaderboardGroup;
    @FXML
    private Button exitButton;
    @FXML
    private Button newGameButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Sets the leaderboard
     */
    public void setLeaderboard() {
        LeaderboxVBox leaderboxVBox = new LeaderboxVBox();
        leaderboardGroup.getChildren().add(leaderboxVBox);
        leaderboxVBox.leaderboardSetup(false);
    }

    /**
     * Asks the user if they want to start a new game or exit
     */
    public void askNewGame() {
        newGameButton.setDisable(false);
        newGameButton.setOnAction(e -> {
            try {
                GUI.getQueue().put(true);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        exitButton.setDisable(false);
        exitButton.setOnAction(e -> {
            try {
                GUI.getQueue().put(false);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
