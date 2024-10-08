package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class NewLobbyController implements Initializable {
    @FXML
    private HBox playerButtons;
    @FXML
    private Button twoPlayerButton;
    @FXML
    private Button threePlayerButton;
    @FXML
    private Button fourPlayerButton;

    /**
     * Initializes the controller
     *
     * @param url URL
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        twoPlayerButton.setGraphic(createIcon("2_players.png"));
        threePlayerButton.setGraphic(createIcon("3_players.png"));
        fourPlayerButton.setGraphic(createIcon("4_players.png"));
        playerButtons.getChildren().forEach(node -> {
            Button button = (Button) node;
            button.setOnAction(e ->
                    GUI.getQueue().add(Integer.parseInt(button.getText()))
            );
        });
    }

    private ImageView createIcon(String iconName) {
        ImageView view = GUIUtility.createIcon(iconName);
        view.setFitHeight(50.0);
        view.setFitWidth(50.0);
        return view;
    }
}
