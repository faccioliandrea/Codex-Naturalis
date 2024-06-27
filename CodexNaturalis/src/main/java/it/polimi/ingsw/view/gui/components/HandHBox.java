package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.utility.GUIConstants;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.io.IOException;

public class HandHBox extends HBox {
    @FXML
    private HBox handHBox;

    private Pair<CardInfo, ImageView> selectedCard;
    private final double screenWidth;

    public HandHBox(double screenWidth) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/components/hand.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            this.screenWidth = screenWidth;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setupHand(BoardStackPane board, boolean selectable) {
        handHBox.getChildren().clear();
        for (CardInfo card : GUI.getInstance().getData().getHand()) {
            ImageView imageView = GUIUtility.createCardImageView(GUIUtility.getCardPath(card), 0, screenWidth * GUIConstants.BigCardToScreenWidthRatio);
                imageView.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY) && selectable) {
                        if (selectedCard != null && selectedCard.getKey() != card) {
                            selectedCard.getValue().setEffect(null);
                        }
                        if (board != null) {
                            board.setAvailablePositionDisabled(false); // when card selected, enable selection of available position
                        }
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

    public void setHandEffect(Effect effect) {
        handHBox.setEffect(effect);
    }

    /** Get current selected card
     * @return the selected card
     */
    public CardInfo cardSelected() {
        if (selectedCard == null) return null;
        CardInfo selected = selectedCard.getKey();
        selectedCard = null;
        return selected;
    }
}
