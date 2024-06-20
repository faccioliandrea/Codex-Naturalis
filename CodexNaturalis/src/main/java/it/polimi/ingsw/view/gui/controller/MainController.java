package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.components.BoardStackPane;
import it.polimi.ingsw.view.gui.components.ChatVBox;
import it.polimi.ingsw.view.gui.components.FadingLabel;
import it.polimi.ingsw.view.gui.components.LeaderboxVBox;
import it.polimi.ingsw.view.gui.utility.GUIConstants;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
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
    private HBox privateGoalHBox;
    @FXML
    private VBox resourcesVBox;
    @FXML
    private VBox goldVBox;
    @FXML
    private VBox leaderboardAndChatVBox;
    @FXML
    private Label turnInfoLabel;
    @FXML
    private VBox turnInfoVBox;
    @FXML
    private VBox infoVBox;
    @FXML
    private HBox decksHBox;
    @FXML
    private Screen screen;

    private Pair<CardInfo, ImageView> selectedCard;

    private BoardStackPane boardGridPane;
    private LeaderboxVBox leaderboxVBox;
    private ChatVBox chatVBox;

    private double screenWidth;

    /**
     * Initializes the controller
     *
     * @param url URL
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boardGridPane = new BoardStackPane(GUIConstants.mainBoardWidthPercentage);
        leaderboxVBox = new LeaderboxVBox();
        leaderboxVBox.setBackground(new Background(new BackgroundFill(Color.web("#ffffffB0"), new CornerRadii(18), new Insets(-7))));
        chatVBox = new ChatVBox();
        chatVBox.setBackground(new Background(new BackgroundFill(Color.web("#ffffffB0"), new CornerRadii(18), new Insets(-7))));
        leaderboardAndChatVBox.getChildren().clear();
        leaderboardAndChatVBox.getChildren().addAll(leaderboxVBox, chatVBox);
        boardGroup.getChildren().clear();
        boardGroup.getChildren().add(boardGridPane);

        screenWidth = screen.getVisualBounds().getWidth();
        leaderboardAndChatVBox.setPrefWidth(screenWidth * GUIConstants.SmallCardToScreenWidthRatio * 2 + 60);
    }

    private void setupHand() {
        handHBox.getChildren().clear();
        for (CardInfo card : GUI.getInstance().getData().getHand()) {
            ImageView imageView = GUIUtility.createCardImageView(GUIUtility.getCardPath(card), 0, screenWidth * GUIConstants.BigCardToScreenWidthRatio);
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

    private void setupGoals() {
        goalsHBox.getChildren().clear();
        for (GoalInfo goal : GUI.getInstance().getData().getGoals().subList(0,2)) {
            ImageView imageView = GUIUtility.createCardImageView(GUIUtility.getGoalPath(goal), 0, screenWidth * GUIConstants.SmallCardToScreenWidthRatio);
            goalsHBox.getChildren().add(imageView);
        }
        privateGoalHBox.getChildren().clear();
        ImageView imageView = GUIUtility.createCardImageView(GUIUtility.getGoalPath(GUI.getInstance().getData().getGoals().get(2)), 0, screenWidth * GUIConstants.SmallCardToScreenWidthRatio);
        privateGoalHBox.getChildren().add(imageView);
    }

    private void setupResources(boolean selectable) {
        resourcesVBox.getChildren().clear();
        for (CardInfo card : GUI.getInstance().getData().getResourceDeck()) {
            ImageView imageView = GUIUtility.createCardImageView(GUIUtility.getCardPath(card), 0, screenWidth * GUIConstants.SmallCardToScreenWidthRatio);
            if (selectable) {
                imageView.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY)) {
                        try {
                            GUI.getQueue().put(10 + GUI.getInstance().getData().getResourceDeck().indexOf(card));
                            drawEnded();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
            resourcesVBox.getChildren().add(imageView);
        }
    }

    private void setupGold(boolean selectable) {
        goldVBox.getChildren().clear();
        for (CardInfo card : GUI.getInstance().getData().getGoldDeck()) {
            ImageView imageView = GUIUtility.createCardImageView(GUIUtility.getCardPath(card), 0, screenWidth * GUIConstants.SmallCardToScreenWidthRatio);
            if (selectable) {
                imageView.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY)) {
                        try {
                            GUI.getQueue().put(20 + GUI.getInstance().getData().getGoldDeck().indexOf(card));
                            drawEnded();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
            goldVBox.getChildren().add(imageView);
        }
    }

    private void setupBoard(boolean padding) {
        boardGridPane.setupBoard(GUI.getInstance().getData().getBoard(), padding);
    }

    /**
     * Updates the leaderboard
     */
    public void updateLeaderboard() {
        leaderboxVBox.leaderboardSetup(true);
    }

    /**
     * Asks the user to play a card
     */
    public void askForPlayCard() {
        setupHand();
        setupBoard(true);
        handHBox.setEffect(GUIUtility.highlightShadow());
        boardGridPane.addAvailablePositionsToBoard(GUI.getInstance().getData().getAvailablePositions());
        if (selectedCard != null) {
            boardGridPane.setAvailablePositionDisabled(false);
        }
    }

    /** Asks the user to draw a card
     */
    public void askForDrawCard() {
        setupResources(true);
        setupGold(true);
        decksHBox.setEffect(GUIUtility.highlightShadow());
    }

    private void drawEnded() {
        resourcesVBox.getChildren().forEach(x -> x.setDisable(true));
        goldVBox.getChildren().forEach(x -> x.setDisable(true));
        decksHBox.setEffect(null);
    }

    /**
     * Updates the GUI (hand, goals, board, leaderboard, resource deck, gold deck)
     */
    public void updateData() {
        setupHand();
        setupGoals();
        setupBoard(false);
        updateLeaderboard();
        setupResources(false);
        setupGold(false);
    }

    /** Get current selected card
     * @return the selected card
     */
    public CardInfo cardSelected() {
        CardInfo selected = selectedCard.getKey();
        selectedCard = null;
        return selected;
    }

    /**
     * Sets the title of the turn info
     * @param title the title
     */
    public void setTitle(String title) {
        turnInfoLabel.setText(title);
    }

    /**
     * Remove hand highlight effect at end of picking the card
     */
    public void endPickCard() {
        handHBox.setEffect(null);
    }

    /**
     * Sets the info title
     * @param title the title
     */
    public void setInfoTitle(String title) {
        FadingLabel label = new FadingLabel(title);
        infoVBox.getChildren().add(label);
    }

    /** Updates the chat GUI
     */
    public void updateChat() {
        chatVBox.updateMessages();
    }
}
