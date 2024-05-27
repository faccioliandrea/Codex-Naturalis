package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.view.UIManager;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.utility.GUIConstants;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class BoardStackPane extends StackPane {
    @FXML
    private GridPane boardGridPane;
    @FXML
    private Screen screen;
    @FXML
    private StackPane stackPane;
    @FXML
    private Group innerGroup;

    private long dragStartTime;

    static final double vDelta = -0.4054;
    static final double hDelta = -0.2156;

    private int columnCount;
    private int rowCount;

    private final double widthPercentage;

    /**
     * Constructor for the BoardGridPane
     *
     * @param widthPercentage the height percentage in respect to screen size of the component
     */
    public BoardStackPane(double widthPercentage) {
        this.widthPercentage = widthPercentage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/components/board.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            createZoomPane();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void resetZoomPane() {
        innerGroup.setScaleX(1);
        innerGroup.setScaleY(1);
        innerGroup.setTranslateX(0);
        innerGroup.setTranslateY(0);
    }

    private void createZoomPane() {
        final double SCALE_DELTA = 1.01;
        stackPane.setOnScroll(event -> {
            event.consume();

            if (event.getDeltaY() == 0) {
                return;
            }

            double scaleFactor =
                    (event.getDeltaY() > 0)
                            ? SCALE_DELTA
                            : 1 / SCALE_DELTA;

            innerGroup.setScaleX(innerGroup.getScaleX() * scaleFactor);
            innerGroup.setScaleY(innerGroup.getScaleY() * scaleFactor);
        });
        stackPane.setOnMousePressed(event -> dragStartTime = System.currentTimeMillis());
        stackPane.setOnMouseDragged(event -> {
            long elapsedTime = System.currentTimeMillis() - dragStartTime;
            if (elapsedTime / 1000.0  > GUIConstants.dragThreshold) {
                innerGroup.setTranslateX(event.getX() - innerGroup.getLayoutBounds().getMaxX() / 2);
                innerGroup.setTranslateY(event.getY() - innerGroup.getLayoutBounds().getMaxY() / 2);
            }
        });

        stackPane.layoutBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) -> {
            boardGridPane.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
            stackPane.setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
        });
    }

    /**
     * Sets up the board
     *
     * @param cards   the cards to set up
     * @param padding whether to add padding or not
     */
    public void setupBoard(ArrayList<CardInfo> cards, boolean padding) {
        this.columnCount = UIManager.boardGridColumns(cards, padding);
        this.rowCount = UIManager.boardGridRows(cards, padding);
        boardGridPane.getChildren().clear();
        boardGridPane.setPrefHeight(screen.getVisualBounds().getHeight() * widthPercentage);
        double cellHeight = boardGridPane.getPrefHeight() / rowCount;
        boardGridPane.setPrefWidth(cellHeight * columnCount * 3 / 2);
        double cellWidth = boardGridPane.getPrefWidth() / columnCount;
        boardGridPane.getColumnConstraints().clear();
        for (int i = 0; i < columnCount; i++) {
            ColumnConstraints columnConstraint = new ColumnConstraints();
            columnConstraint.setPrefWidth(cellWidth);
            columnConstraint.setHgrow(Priority.NEVER);
            columnConstraint.setHalignment(HPos.CENTER);
            boardGridPane.getColumnConstraints().add(columnConstraint);
        }
        boardGridPane.getRowConstraints().clear();
        for (int i = 0; i < rowCount; i++) {
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPrefHeight(cellHeight);
            rowConstraint.setVgrow(Priority.NEVER);
            rowConstraint.setValignment(VPos.CENTER);
            boardGridPane.getRowConstraints().add(rowConstraint);
        }
        boardGridPane.setVgap(cellHeight * vDelta);
        boardGridPane.setHgap(cellWidth * hDelta);
        resetZoomPane();
        addCardsToBoard(cards, padding);
    }

    private void addCardsToBoard(ArrayList<CardInfo> cards, boolean padding) {
        double cellHeight = boardGridPane.getPrefHeight() / rowCount;
        double cellWidth = boardGridPane.getPrefWidth() / columnCount;
        for (CardInfo card : cards) {
            ImageView img = GUIUtility.createImageView(GUIUtility.getCardPath(card), cellHeight, cellWidth);
            Point translatedCoord = UIManager.toMatrixCoord(card.getCoord(), padding);
            boardGridPane.add(img, translatedCoord.x, translatedCoord.y);
        }
    }

    /**
     * Sets the available positions on the board
     *
     * @param availablePositions the available positions
     */
    public void addAvailablePositionsToBoard(ArrayList<Point> availablePositions) {
        double cellHeight = boardGridPane.getPrefHeight() / rowCount;
        double cellWidth = boardGridPane.getPrefWidth() / columnCount;
        for (Point position : availablePositions) {
            Rectangle rectangle = createAvailablePositionRectangle(cellHeight, cellWidth);
            rectangle.setOnMouseClicked(e -> {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    try {
                        GUI.getQueue().put(position);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            rectangle.setDisable(true);
            Point translatedCoord = UIManager.toMatrixCoord(position, true);
            boardGridPane.add(rectangle, translatedCoord.x, translatedCoord.y);
        }
    }

    /**
     * Sets the selected enable on available positions
     *
     * @param disabled whether to disable or enable the available positions
     */
    public void setAvailablePositionDisabled(boolean disabled) {
        boardGridPane.getChildren().forEach(node -> node.setDisable(disabled));
    }

    private Rectangle createAvailablePositionRectangle(double cellHeight, double cellWidth) {
        Rectangle rectangle = new Rectangle(cellWidth, cellHeight);
        rectangle.setArcHeight(cellHeight * 0.1);
        rectangle.setArcWidth(cellHeight * 0.1);
        rectangle.setFill(Color.web("#1f93ff26"));
        return rectangle;
    }
}
