package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.view.gui.components.BoardGridPane;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.LeaderboxVBox;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private BoardGridPane boardGridPane;
    private LeaderboxVBox leaderboxVBox;

    @FXML
    private Group boardGroup;
    @FXML
    private HBox handHBox;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private VBox goalsVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardGridPane = new BoardGridPane(0.7);
        leaderboxVBox = new LeaderboxVBox();
        boardGroup.getChildren().add(boardGridPane);
        mainAnchorPane.getChildren().add(leaderboxVBox);
        AnchorPane.setRightAnchor(leaderboxVBox,20d);
        AnchorPane.setTopAnchor(leaderboxVBox,10d);
    }

    public void setupHand(ArrayList<CardInfo> hand) {
        handHBox.getChildren().clear();
        for (CardInfo card : hand) {
            ImageView imageView = GUIUtility.createImageView(GUI.getCardPath(card), 250, 250);
            imageView.setOnMouseClicked(e -> {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    try {
                        imageView.setEffect(new Glow(0.5));
                        GUI.getQueue().put(card);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                    card.setFlipped(!card.isFlipped());
                    imageView.setImage(GUIUtility.createImage(GUI.getCardPath(card)));
                }
            });
            imageView.setDisable(true);
            handHBox.getChildren().add(imageView);
        }
    }

    public void setupGoals(ArrayList<GoalInfo> goals) {
        goalsVBox.getChildren().clear();
        goalsVBox.getChildren().add(new Label("Your goals"));
        for(GoalInfo goal: goals) {
            ImageView imageView = GUIUtility.createImageView("/front/" + goal.getId() + ".png", 250, 250);
            goalsVBox.getChildren().add(imageView);
        }
    }

    public void setupAvailablePositions(ArrayList<Point> positions) {
        boardGridPane.addAvailablePositionsToBoard(positions);
    }

    public void setupBoard(ArrayList<CardInfo> board) {
        boardGridPane.setupBoard(board, true);
    }

    public void setHandSelection(boolean notDisabled) {
        handHBox.getChildren().forEach(node -> node.setDisable(!notDisabled));
    }

    public void setAvailablePositionSelection(boolean notDisabled) {
        boardGridPane.setAvailablePositionSelection(!notDisabled);
    }

    public void updateLeaderboard(LinkedHashMap<String, Integer> leaderboard) {
        leaderboxVBox.leaderboardSetup(leaderboard);
    }
}
