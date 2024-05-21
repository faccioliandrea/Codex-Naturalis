package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.utility.GUIConstants;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameSetupController implements Initializable {
    @FXML
    private VBox privateGoalsVBox;
    @FXML
    private VBox starterCardVBox;

    private ArrayList<GoalInfo> privateGoals;
    private CardInfo starterCard;

    public void setPrivateGoals(ArrayList<GoalInfo> privateGoals) {
        this.privateGoals = privateGoals;
    }

    public void setStarterCard(CardInfo starterCard) {
        this.starterCard = starterCard;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void askPublicGoals() {
        privateGoalsVBox.setVisible(true);
        HBox goalHBox = new HBox();
        goalHBox.setAlignment(Pos.CENTER);
        goalHBox.setSpacing(20.0);
        for (GoalInfo goal : privateGoals) {
            ImageView imageView = GUIUtility.createImageView(GUIUtility.getGoalPath(goal), GUIConstants.handCardDimension, GUIConstants.handCardDimension);
            imageView.setOnMouseClicked(e -> {
                GUI.getQueue().add(privateGoals.indexOf(goal));
                privateGoalsVBox.setVisible(false);
            });
            goalHBox.getChildren().add(imageView);
        }
        privateGoalsVBox.getChildren().add(goalHBox);
    }

    public void askStarterCardSide() {
        starterCardVBox.setVisible(true);
        HBox starterHBox = new HBox();
        starterHBox.setAlignment(Pos.CENTER);
        starterHBox.setSpacing(20.0);
        CardInfo starterCardTop = new CardInfo(starterCard.getId(), starterCard.getCoord(), starterCard.isFlipped(), starterCard.getDescription(), starterCard.getFrontDescription(), starterCard.getBackDescription(), null);
        CardInfo starterCardBottom = new CardInfo(starterCard.getId(), starterCard.getCoord(), starterCard.isFlipped(), starterCard.getDescription(), starterCard.getFrontDescription(), starterCard.getBackDescription(), null);
        starterCardBottom.setFlipped(true);
        starterCardTop.setFlipped(false);
        ImageView imageViewFront = GUIUtility.createImageView(GUIUtility.getCardPath(starterCardTop), GUIConstants.handCardDimension, GUIConstants.handCardDimension);
        ImageView imageViewBack = GUIUtility.createImageView(GUIUtility.getCardPath(starterCardBottom), GUIConstants.handCardDimension, GUIConstants.handCardDimension);
        imageViewFront.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                GUI.getQueue().add(starterCardTop.isFlipped());
                starterHBox.setDisable(true);
            }
        });
        imageViewBack.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                GUI.getQueue().add(starterCardBottom.isFlipped());
                starterHBox.setDisable(true);
            }
        });
        starterHBox.getChildren().addAll(imageViewFront, imageViewBack);
        starterCardVBox.getChildren().add(starterHBox);
    }
}
