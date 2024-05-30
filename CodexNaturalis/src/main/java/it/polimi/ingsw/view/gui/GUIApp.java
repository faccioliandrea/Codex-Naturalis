package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.controller.JoinMenuController;
import it.polimi.ingsw.view.gui.controller.OpponentBoardController;
import it.polimi.ingsw.view.gui.utility.GUIConstants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUIApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Stage stage;
    private static Stage opponentStage;
    private static String lastFXML = "";

    /**
     * Starts the GUI application
     *
     * @param stage stage to start
     */
    @Override
    public void start(final Stage stage) {
        System.setProperty("prism.lcdtext", "false");
        GUIApp.stage = stage;
        GUIApp.opponentStage = new Stage();
        opponentStage.initModality(Modality.APPLICATION_MODAL);
        opponentStage.initOwner(stage);
        opponentStage.initStyle(StageStyle.UNDECORATED);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setTitle("Codex Naturalis");
        stage.setMaximized(true);
        stage.setResizable(false);
        Font font = Font.loadFont(GUIApp.class.getResourceAsStream("/fonts/UnifrakturMaguntia-Regular.ttf"), 20);
        Font font2 = Font.loadFont(GUIApp.class.getResourceAsStream("/fonts/Exo2-Regular.ttf"), 20);

        changeScene("join-menu", new JoinMenuController());
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

    /**
     * Changes the scene to the one specified by the fxml file and controller
     *
     * @param fxml       FXML name
     * @param controller controller for the FXML
     */
    public static void changeScene(String fxml, Initializable controller) {
        try {
            if (lastFXML.equals(fxml)) return;
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApp.class.getResource("/fxml/" + fxml + ".fxml"));
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            String paddingStyle = "-fx-padding: " + GUIConstants.scenePadding;
            root.setStyle(paddingStyle);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/css/styles.css");
            lastFXML = fxml;
            Platform.runLater(() -> stage.setScene(scene));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows an alert with the specified message and type
     *
     * @param errorMessage message to show
     * @param type         type of the alert
     */
    public static void showAlert(String errorMessage, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert errorAlert = new Alert(type);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle(type.toString());
            errorAlert.setContentText(errorMessage);
            errorAlert.show();
        });
    }

    /**
     * Shows a not interactable alert with the specified message and type
     *
     * @param errorMessage message to show
     * @param type         type of the alert
     */
    public static void notInteractableAlert(String errorMessage, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert errorAlert = new Alert(type);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle(type.toString());
            errorAlert.setContentText(errorMessage);
            errorAlert.getDialogPane().getButtonTypes().clear();
            errorAlert.show();
        });
    }

    public static void showOpponentBoard(OpponentBoardController controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApp.class.getResource("/fxml/opponent-board.fxml"));
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            root.setStyle("-fx-effect: innershadow(gaussian, #000000, 3, 3.0, 0, 0);");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/css/styles.css");
            opponentStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
                if (KeyCode.ESCAPE == event.getCode()) {
                    opponentStage.close();
                }
            });
            opponentStage.setScene(scene);
            opponentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
