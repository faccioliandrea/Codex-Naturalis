package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.view.data.UIData;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.BoardStackPane;
import it.polimi.ingsw.view.gui.components.FadingLabel;
import it.polimi.ingsw.view.gui.components.LeaderboxVBox;
import it.polimi.ingsw.view.gui.utility.GUIConstants;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Group boardGroup;
    @FXML
    private HBox handHBox;
    @FXML
    private HBox goalsHBox;
    @FXML
    private HBox resourcesHBox;
    @FXML
    private HBox goldHBox;
    @FXML
    private Group leaderboardGroup;
    @FXML
    private Label turnInfoLabel;
    @FXML
    private VBox infoVBox;

    private Pair<CardInfo, ImageView> selectedCard;

    private BoardStackPane boardGridPane;
    private LeaderboxVBox leaderboxVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardGridPane = new BoardStackPane(GUIConstants.mainBoardWidthPercentage);
        leaderboxVBox = new LeaderboxVBox();
        leaderboardGroup.getChildren().clear();
        leaderboardGroup.getChildren().add(leaderboxVBox);
        boardGroup.getChildren().clear();
        boardGroup.getChildren().add(boardGridPane);
    }

    private void setupHand(UIData data) {
        handHBox.getChildren().clear();
        for (CardInfo card : data.getHand()) {
            ImageView imageView = GUIUtility.createImageView(GUIUtility.getCardPath(card), GUIConstants.handCardDimension, GUIConstants.handCardDimension);
            imageView.setOnMouseClicked(e -> {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    if (selectedCard != null && selectedCard.getKey() != card) {
                        selectedCard.getValue().setEffect(null);
                    }
                    boardGridPane.setAvailablePositionDisabled(false); // when card selected, enable selection of available position
                    selectedCard = new Pair<>(card, imageView);
                    imageView.setEffect(new Glow(0.5));
                } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                    card.setFlipped(!card.isFlipped());
                    imageView.setImage(GUIUtility.createImage(GUIUtility.getCardPath(card)));
                }
            });
            handHBox.getChildren().add(imageView);
        }
    }

    private void setupGoals(UIData data) {
        goalsHBox.getChildren().clear();
        for (GoalInfo goal : data.getGoals()) {
            ImageView imageView = GUIUtility.createImageView(GUIUtility.getGoalPath(goal), GUIConstants.defaultCardDimension, GUIConstants.defaultCardDimension);
            goalsHBox.getChildren().add(imageView);
        }
    }

    private void setupResources(UIData data, boolean selectable) {
        resourcesHBox.getChildren().clear();
        for (CardInfo card : data.getResourceDeck()) {
            ImageView imageView = GUIUtility.createImageView(GUIUtility.getCardPath(card), GUIConstants.defaultCardDimension, GUIConstants.defaultCardDimension);
            if (selectable) {
                imageView.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY)) {
                        try {
                            GUI.getQueue().put(10 + data.getResourceDeck().indexOf(card));
                            drawEnded();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
            resourcesHBox.getChildren().add(imageView);
        }
    }

    private void setupGold(UIData data, boolean selectable) {
        goldHBox.getChildren().clear();
        for (CardInfo card : data.getGoldDeck()) {
            ImageView imageView = GUIUtility.createImageView(GUIUtility.getCardPath(card), GUIConstants.defaultCardDimension, GUIConstants.defaultCardDimension);
            if (selectable) {
                imageView.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY)) {
                        try {
                            GUI.getQueue().put(20 + data.getGoldDeck().indexOf(card));
                            drawEnded();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
            goldHBox.getChildren().add(imageView);
        }
    }

    private void setupBoard(UIData data, boolean padding) {
        boardGridPane.setupBoard(data.getBoard(), padding);
    }

    public void updateLeaderboard(UIData data) {
        leaderboxVBox.leaderboardSetup(data, true);
    }

    public void askForPlayCard(UIData data) {
        setupHand(data);
        setupBoard(data, true);
        handHBox.setEffect(GUIUtility.highlightShadow());
        boardGridPane.addAvailablePositionsToBoard(data.getAvailablePositions());
        if (selectedCard != null) {
            boardGridPane.setAvailablePositionDisabled(false);
        }
    }

    public void askForDrawCard(UIData data) {
        setupResources(data, true);
        setupGold(data, true);
        resourcesHBox.setEffect(GUIUtility.highlightShadow());
        goldHBox.setEffect(GUIUtility.highlightShadow());
    }

    private void drawEnded() {
        resourcesHBox.getChildren().forEach(x -> x.setDisable(true));
        goldHBox.getChildren().forEach(x -> x.setDisable(true));
        resourcesHBox.setEffect(null);
        goldHBox.setEffect(null);
    }

    public void updateData(UIData data) {
        setupHand(data);
        setupGoals(data);
        setupBoard(data, false);
        updateLeaderboard(data);
        setupResources(data, false);
        setupGold(data, false);
    }

    public CardInfo cardSelected() {
        CardInfo selected = selectedCard.getKey();
        selectedCard = null;
        return selected;
    }

    public void setTitle(String title) {
        turnInfoLabel.setText(title);
    }

    public void endPickCard() {
        handHBox.setEffect(null);
    }

    public void setInfoTitle(String title) {
        FadingLabel label = new FadingLabel(title);
        infoVBox.getChildren().add(label);
    }
}
