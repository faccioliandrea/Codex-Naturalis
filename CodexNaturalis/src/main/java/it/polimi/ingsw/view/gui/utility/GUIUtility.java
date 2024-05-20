package it.polimi.ingsw.view.gui.utility;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
        Image image = new Image(Objects.requireNonNull(GUIUtility.class.getResourceAsStream( GUIConstants.imgPath + "cards/" + path)));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(cellHeight);
        imageView.setFitWidth(cellWidth);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Creates an Image from the specified path
     * @param path path of the image
     * @return the Image
     */
    public static Image createImage(String path) {
        return new Image(Objects.requireNonNull(GUIUtility.class.getResourceAsStream("/img/cards/" + path)));
    }
}
