package it.polimi.ingsw.view.gui.utility;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.GoalInfo;
import it.polimi.ingsw.model.enumeration.PlayerColor;
import it.polimi.ingsw.view.gui.GUI;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.Objects;

public final class GUIUtility {

    /**
     * Creates an ImageView for a card from the specified path
     *
     * @param path       path of the image
     * @param cellHeight height of the cell
     * @param cellWidth  width of the cell
     * @return the ImageView
     */
    public static ImageView createCardImageView(String path, double cellHeight, double cellWidth) {
        Image image = new Image(Objects.requireNonNull(GUIUtility.class.getResourceAsStream(path)));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(cellHeight);
        imageView.setFitWidth(cellWidth);
        imageView.setPreserveRatio(true);
        imageView.setId("card");
        return imageView;
    }

    /** Creates the path of the card image
     *
     * @param card the card
     * @return the path of the card image
     */
    public static String getCardPath(CardInfo card) {
        return GUIConstants.imgPath + "cards/" + (card.isFlipped() ? "back/" : "front/") + card.getId() + ".png";
    }

    /** Creates the path of the goal image
     *
     * @param goal the goal
     * @return the path of the goal image
     */
    public static String getGoalPath(GoalInfo goal) {
        return GUIConstants.imgPath + "cards/front/" + goal.getId() + ".png";
    }

    /**
     * Creates an Image from the specified path
     *
     * @param path path of the image
     * @return the Image
     */
    public static Image createImage(String path) {
        return new Image(Objects.requireNonNull(GUIUtility.class.getResourceAsStream(path)));
    }

    /** Creates a DropShadow effect with a cyan glow and a cyan inner shadow
     *
     * @return the DropShadow effect
     */
    public static DropShadow highlightShadow() {
        DropShadow glow = new DropShadow(BlurType.ONE_PASS_BOX, Color.CYAN, 50, 0 , 0, 0);
        glow.setInput(new InnerShadow(BlurType.THREE_PASS_BOX, Color.CYAN, 20, 0,0,0));

        return glow;
    }

    /** Casts player color to JavaFX color
     *
     * @param username username of player
     * @return color of player
     */
    public static Color playerColor(String username) {
        PlayerColor color = GUI.getInstance().getData().getPlayerColors().get(username);
        switch (color) {
            case YELLOW:
                return GUIConstants.yellowPlayerColor;
            case RED:
                return GUIConstants.redPlayerColor;
            case BLUE:
                return GUIConstants.bluePlayerColor;
            case GREEN:
                return GUIConstants.greenPlayerColor;
            default:
                return Color.BLACK;
        }
    }

    /** Creates an ImageView for an icon from the specified path
     *
     * @param iconName name of the icon
     * @return the ImageView
     */
    public static ImageView createIcon(String iconName) {
        ImageView view = new ImageView(new Image(Objects.requireNonNull(GUIUtility.class.getResourceAsStream(GUIConstants.iconsPath + iconName))));
        view.setPreserveRatio(true);
        return view;
    }
}
