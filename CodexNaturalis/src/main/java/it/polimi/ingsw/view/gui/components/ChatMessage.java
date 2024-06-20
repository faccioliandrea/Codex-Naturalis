package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Class for the chat message
 */
public class ChatMessage extends HBox {
    /**
     * Constructor for the chat message
     *
     * @param message chat message
     * @param currentUser current user
     */
    public ChatMessage(ChatMessageData message, String currentUser) {
        String sender = "[" + message.getSender() + (message.getRecipient() != null ? (" to " + (message.getRecipient().equals(currentUser) ? "you" : message.getRecipient())) : "") + "]:";
        Label senderLabel = new Label(sender);
        senderLabel.setStyle("-fx-font-weight: bold");
        senderLabel.setTextFill(GUIUtility.playerColor(message.getSender()));
        senderLabel.setWrapText(true);
        Label text = new Label(message.getContent());
        text.setWrapText(true);
        text.setTextFill(Color.WHITE);
        this.setAlignment(Pos.TOP_LEFT);
        this.setSpacing(2);
        this.getChildren().addAll(senderLabel, text);
    }
}
