package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.chat.ClientChatHandler;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChatVBox extends VBox {
    @FXML
    private Button sendButton;
    @FXML
    private TextField messageTextField;
    @FXML
    private VBox chatVBox;
    @FXML
    private ScrollPane chatScrollPane;

    public ChatVBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/components/chat.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            setupEvents();
            chatScrollPane.vvalueProperty().bind(chatVBox.heightProperty());
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
        messageTextField.textProperty().addListener((observable, oldValue, newValue) -> sendButton.setDisable(newValue.trim().isEmpty()));
        sendButton.setOnAction(e -> sendMessage(messageTextField.getText()));
    }

    public void updateMessages() {
        chatVBox.getChildren().clear();
        GUI.getInstance().getData().getLastMessages().forEach(message -> chatVBox.getChildren().add(new ChatMessage(message, GUI.getInstance().getData().getUsername())));
    }

    private void sendMessage(String message) {
        ClientChatHandler.sendChatMessage(message);
        messageTextField.clear();
        if (ClientChatHandler.isSameUserMessage(message)) {
            return;
        }
        updateMessages();
    }
}
