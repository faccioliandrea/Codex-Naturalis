package it.polimi.ingsw.view.gui.utility;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public final class GUIUtility {
    public static ImageView createImageView(String path, double cellHeight, double cellWidth) {
        Image image = new Image(Objects.requireNonNull(GUIUtility.class.getResourceAsStream("/img/cards/" + path)));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(cellHeight);
        imageView.setFitWidth(cellWidth);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static Image createImage(String path) {
        return new Image(Objects.requireNonNull(GUIUtility.class.getResourceAsStream("/img/cards/" + path)));
    }
}
