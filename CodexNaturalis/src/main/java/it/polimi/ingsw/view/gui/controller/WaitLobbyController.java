package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.view.gui.components.FadingLabel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitLobbyController implements Initializable {
    @FXML
    private VBox notificationVBox;
    @FXML
    private Label lobbyIdLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void addNotification(String username) {
        FadingLabel label = new FadingLabel(username + " joined the lobby");
        notificationVBox.getChildren().add(label);
    }

    public void setLobbyId(String id) {
        lobbyIdLabel.setText("Lobby ID: " + id);
    }
}
