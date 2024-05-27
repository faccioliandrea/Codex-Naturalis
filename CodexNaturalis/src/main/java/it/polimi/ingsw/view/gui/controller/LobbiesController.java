package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LobbiesController implements Initializable {
    @FXML
    private TextField usernameTextField;
    @FXML
    private Button usernameButton;
    @FXML
    private Button joinLobbyButton;
    @FXML
    private Button createLobbyButton;
    @FXML
    private ListView<String> lobbiesListView;

    private ArrayList<String> lobbies;
    private String selectedLobby;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameButton.setOnMouseClicked(e -> onUsernameButtonClicked());
        joinLobbyButton.setOnMouseClicked(e -> onJoinButtonClicked());
        createLobbyButton.setOnMouseClicked(e -> onCreateLobbyButtonClicked());
        usernameTextField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onUsernameButtonClicked();
            }});
        lobbiesListView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onJoinButtonClicked();
            }});
    }

    private void usernameEntered(String value) {
        usernameButton.setDisable(value.isEmpty());
    }

    private void onUsernameButtonClicked() {
        try {
            GUI.getQueue().put(usernameTextField.getText());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void onJoinButtonClicked() {
        try {
            GUI.getQueue().put(selectedLobby);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void onCreateLobbyButtonClicked() {
        try {
            GUI.getQueue().put("");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void lobbySelected(String lobbyId) {
        selectedLobby = lobbyId;
        joinLobbyButton.setDisable(lobbyId == null);
    }

    public void showLobbies(ArrayList<String> lobbies) {
        this.lobbies = lobbies;
        lobbiesListView.getItems().clear();
        if (!lobbies.isEmpty()) {
            lobbiesListView.getItems().addAll(lobbies);
            lobbiesListView.setDisable(false);
        }
        lobbiesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> lobbySelected(newValue));
        usernameButton.setDisable(true);
        createLobbyButton.setDisable(false);
    }

    public void askForUsername() {
        usernameTextField.clear();
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> usernameEntered(newValue));
    }
}
