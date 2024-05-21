package it.polimi.ingsw.view.gui.utility;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.Objects;

public final class GUIUtility {

    /**
     * Creates an ImageView from the specified path
     * @param path path of the image
     * @param cellHeight height of the cell
     * @param cellWidth width of the cell
     * @return the ImageView
     */
    public static ImageView createImageView(String path, double cellHeight, double cellWidth) {
        Image image = new Image(Objects.requireNonNull(GUIUtility.class.getResourceAsStream(path)));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(cellHeight);
        imageView.setFitWidth(cellWidth);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static String getCardPath(CardInfo card) {
        return GUIConstants.imgPath + "cards/" + (card.isFlipped() ? "back/" : "front/") + card.getId() + ".png";
    }

    public static String getGoalPath(GoalInfo goal) {
        return GUIConstants.imgPath + "cards/front/" + goal.getId() + ".png";
    }

    /**
     * Creates an Image from the specified path
     * @param path path of the image
     * @return the Image
     */
    public static Image createImage(String path) {
        return new Image(Objects.requireNonNull(GUIUtility.class.getResourceAsStream(path)));
    }

    public static DropShadow highlightShadow() {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#ffff00"));
        shadow.setSpread(8.0);
        return shadow;
    }
}
