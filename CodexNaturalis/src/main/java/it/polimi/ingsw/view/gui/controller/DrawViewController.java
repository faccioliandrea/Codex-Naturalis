package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.TurnInfo;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

public class DrawViewController implements Initializable {

    private TurnInfo turnInfo;
    @FXML
    private HBox resourceHBox;
    @FXML
    private  HBox goldHBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupResourceHBox();
        setupGoldHBox();
    }

    public DrawViewController(TurnInfo turnInfo) {
        this.turnInfo = turnInfo;
    }

    private void setupResourceHBox() {
        resourceHBox.getChildren().clear();
        for (CardInfo card : this.turnInfo.getResourceDeck()) {
            ImageView imageView = GUIUtility.createImageView(GUI.getCardPath(card), 250, 250);
            imageView.setOnMouseClicked(e -> {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    try {
                        disableDraw();
                        imageView.setEffect(new Glow(0.5));
                        GUI.getQueue().put(10 + this.turnInfo.getResourceDeck().indexOf(card));
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            resourceHBox.getChildren().add(imageView);
        }
    }

    private void setupGoldHBox() {
        goldHBox.getChildren().clear();
        for(CardInfo card: this.turnInfo.getGoldDeck()) {
            ImageView imageView = GUIUtility.createImageView(GUI.getCardPath(card), 250, 250);
            imageView.setOnMouseClicked(e -> {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    try {
                        disableDraw();
                        imageView.setEffect(new Glow(0.5));
                        GUI.getQueue().put(20 + this.turnInfo.getGoldDeck().indexOf(card));
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            goldHBox.getChildren().add(imageView);
        }
    }

    private void disableDraw() {
        resourceHBox.getChildren().forEach(x -> x.setDisable(true));
        goldHBox.getChildren().forEach(x -> x.setDisable(true));
    }

}
