package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.view.gui.components.BoardStackPane;
import it.polimi.ingsw.view.gui.utility.GUIConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OpponentBoardController implements Initializable {
    @FXML
    private Group boardGroup;
    @FXML
    private Label titleLabel;

    private final String opponentName;
    private final ArrayList<CardInfo> board;

    /**
     * Constructor for the opponent board controller
     *
     * @param opponentName name of the opponent
     * @param board board of the opponent
     */
    public OpponentBoardController(String opponentName, ArrayList<CardInfo> board) {
        this.opponentName = opponentName;
        this.board = board;
    }

    /**
     * Initializes the controller
     *
     * @param url URL
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BoardStackPane boardGridPane = new BoardStackPane(GUIConstants.opponentBoardWidthPercentage);
        boardGroup.getChildren().add(boardGridPane);
        titleLabel.setText(opponentName + "'s board");
        boardGridPane.setupBoard(board, false);
    }
}
