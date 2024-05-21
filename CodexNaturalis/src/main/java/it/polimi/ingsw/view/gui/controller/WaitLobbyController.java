package it.polimi.ingsw.view.gui.controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitLobbyController implements Initializable {
    @FXML
    private VBox notificationVBox;
    @FXML
    private Label lobbyIdLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void userJoined(String username) {
        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        FadeTransition fadeIn = new FadeTransition(
                Duration.seconds(5)
        );
        Label label = new Label(username + " joined the lobby");
        fadeIn.setNode(label);
        fadeIn.setFromValue(1.0);
        fadeIn.setToValue(0.0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);
        pause.setOnFinished(e -> fadeIn.playFromStart());
        notificationVBox.getChildren().add(label);
        fadeIn.play();
    }

    public void setLobbyId(String id) {
        lobbyIdLabel.setText("Lobby ID: " + id);
    }
}
