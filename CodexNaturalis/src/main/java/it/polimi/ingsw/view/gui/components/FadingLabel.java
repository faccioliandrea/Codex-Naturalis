package it.polimi.ingsw.view.gui.components;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class FadingLabel extends Label {
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
        fadeIn.setOnFinished(e -> setGraphic(null));
        pause.playFromStart();
    }
}
