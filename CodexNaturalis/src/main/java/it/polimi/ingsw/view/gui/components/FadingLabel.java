package it.polimi.ingsw.view.gui.components;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Class for a fading label
 */
public class FadingLabel extends Label {
    /**
     * Constructor for the fading label
     *
     * @param title title of the label
     */
    public FadingLabel(String title) {
        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        FadeTransition fadeIn = new FadeTransition(
                Duration.seconds(5)
        );
        Label label = new Label(title);
        fadeIn.setNode(label);
        fadeIn.setFromValue(1.0);
        fadeIn.setToValue(0.0);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);
        pause.setOnFinished(e -> fadeIn.playFromStart());
        setGraphic(label);
        fadeIn.setOnFinished(e -> {
            setGraphic(null);
            if (this.getParent() != null) {
                ((VBox) this.getParent()).getChildren().remove(this);
            }
        });
        pause.playFromStart();
    }
}
