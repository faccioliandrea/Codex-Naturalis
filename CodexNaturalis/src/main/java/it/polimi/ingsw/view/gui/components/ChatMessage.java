package it.polimi.ingsw.view.gui.components;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.view.gui.utility.GUIConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ChatMessage extends VBox {
    public ChatMessage(ChatMessageData message, String currentUser) {
        Label sender = new Label(message.getSender() + (message.getRecipient() == null ? "" : " to you"));
        sender.setStyle("-fx-font-weight: bold");
        sender.setWrapText(true);
        Label text = new Label(message.getContent());
        text.setWrapText(true);
        boolean isCurrentUser = message.getSender().equals(currentUser);
        this.setAlignment(isCurrentUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        if (isCurrentUser)
            text.setTextFill(Color.WHITE);
        else
            this.getChildren().add(sender);
        this.getChildren().add(text);
        this.setBackground(new Background(new BackgroundFill(isCurrentUser ? GUIConstants.sentMessageColor : GUIConstants.receivedMessageColor, new CornerRadii(5), new Insets(-5))));
    }
}
