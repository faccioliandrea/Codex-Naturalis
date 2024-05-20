package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.utility.GUIConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Objects;
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
        ImageView view = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(GUIConstants.iconsPath + iconName))));
        view.setPreserveRatio(true);
        view.setFitHeight(50.0);
        view.setFitWidth(50.0);
        return view;
    }
}
