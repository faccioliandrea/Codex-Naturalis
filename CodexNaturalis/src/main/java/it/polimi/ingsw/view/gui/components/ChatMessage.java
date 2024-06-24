package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.view.gui.utility.GUIUtility;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
        Text senderText = new Text(sender);
        senderText.setStyle("-fx-font-weight: bold");
        senderText.setFill(GUIUtility.playerColor(message.getSender()));
        Label contentText = new Label(message.getContent());
        contentText.setTextFill(Color.WHITE);
        contentText.setWrapText(true);
        this.setAlignment(Pos.TOP_LEFT);
        this.setSpacing(2);
        this.getChildren().addAll(senderText, contentText);
    }
}
