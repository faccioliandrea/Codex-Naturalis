package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.view.gui.controller.JoinMenuController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.*;

public class GUIApp extends Application {

    public static void main(String[] args) { launch(args); }
    private static Stage stage;
    private static String lastFXML = "";

    @Override public void start(final Stage stage) {
        GUIApp.stage = stage;
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setTitle("Codex Naturalis");
        stage.setMaximized(true);
        stage.setResizable(false);
        changeScene("join-menu", new JoinMenuController());
        stage.show();
    }

    public static void changeScene(String fxml, Initializable controller) {
        try {
            if (lastFXML.equals(fxml)) return;
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApp.class.getResource("/fxml/" + fxml + ".fxml"));
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            lastFXML = fxml;
            Platform.runLater(() -> stage.setScene(scene));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlert(String errorMessage, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert errorAlert = new Alert(type);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle(type.toString());
            errorAlert.setContentText(errorMessage);
            errorAlert.show();
        });
    }
}
