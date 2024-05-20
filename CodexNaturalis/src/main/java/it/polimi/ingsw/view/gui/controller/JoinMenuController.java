package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUIApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;

public class JoinMenuController implements Initializable {
    @FXML
    Screen screen;
    @FXML
    ImageView logoImageView;
    @FXML
    Button joinButton;
    @FXML
    TextField ipTextField;

    private Boolean isIpValid = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoImageView.setFitHeight(screen.getVisualBounds().getHeight() * 0.5);
        ipTextField.textProperty().addListener((observable, oldValue, newValue) -> ipEntered(newValue));
    }

    @FXML
    public void submitIp() {
        String ipAddress = ipTextField.getText();
        boolean isIpValid = GUI.isValidIP(ipAddress, "localhost");
        if (!isIpValid) {
            GUIApp.showAlert("Invalid IP address", javafx.scene.control.Alert.AlertType.ERROR);
            return;
        }
        try {
            GUI.getQueue().put(ipTextField.getText());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void ipEntered(String value) {
        isIpValid = !value.isEmpty();
        joinButton.setDisable(!isIpValid);
    }
}
