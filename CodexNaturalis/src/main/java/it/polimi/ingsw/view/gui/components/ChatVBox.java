package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.chat.ClientChatHandler;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Class that represents the chat box
 */
public class ChatVBox extends VBox {
    @FXML
    private ImageView sendIcon;
    @FXML
    private TextField messageTextField;
    @FXML
    private VBox chatVBox;
    @FXML
    private ScrollPane chatScrollPane;

    /**
     * Constructor for the ChatVBox
     */
    public ChatVBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/components/chat.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            setupEvents();
            chatScrollPane.vvalueProperty().bind(chatVBox.heightProperty());
            sendIcon.setImage(GUIUtility.createIcon("send.png").getImage());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void setupEvents() {
        messageTextField.setOnKeyPressed(e -> {
            String msg = messageTextField.getText();
            if (e.getCode() == KeyCode.ENTER && !msg.trim().isEmpty()) {
                sendMessage(msg);
            }
        });
        messageTextField.textProperty().addListener((observable, oldValue, newValue) -> sendIcon.setDisable(newValue.trim().isEmpty()));
        sendIcon.setOnMouseClicked(e -> sendMessage(messageTextField.getText()));
    }

    /**
     * Updates the chat messages
     */
    public void updateMessages() {
        chatVBox.getChildren().clear();
        GUI.getInstance().getData().getLastMessages().forEach(message -> chatVBox.getChildren().add(new ChatMessage(message, GUI.getInstance().getData().getUsername())));
    }

    private void sendMessage(String message) {
        ClientChatHandler.sendChatMessage(message);
        messageTextField.clear();
        updateMessages();
    }
}
